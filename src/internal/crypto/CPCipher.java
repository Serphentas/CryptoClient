package internal.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.crypto.engines.ChaChaEngine;
import org.bouncycastle.crypto.generators.Poly1305KeyGenerator;
import org.bouncycastle.crypto.macs.Poly1305;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.tls.AlertDescription;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;
import org.bouncycastle.util.encoders.Hex;

public class CPCipher {

    private final ChaChaEngine cipher;
    private final Poly1305 mac;
    private static final byte[] readBuf = new byte[2048];
    private static final byte[] chachaBuf = new byte[2048];

    public CPCipher() throws IOException {
        this.cipher = new ChaChaEngine();
        this.mac = new Poly1305();
    }

    public int getPlaintextLimit(int ciphertextLimit) {
        return ciphertextLimit - 16;
    }

    public void encrypt(byte[] key, byte[] nonce, InputStream input, OutputStream output) throws IOException {
        this.cipher.init(true, new ParametersWithIV(new KeyParameter(key), nonce));
        byte[] macBuf = new byte[16];
        initRecordMAC(cipher);

        int r = 0;
        while ((r = input.read(readBuf)) > 0) {
            cipher.processBytes(readBuf, 0, readBuf.length, chachaBuf, 0);
            output.write(chachaBuf, 0, r);
            updateRecordMAC(chachaBuf, 0, readBuf.length);
            
            System.out.println("readbuf " + Hex.toHexString(readBuf));
            System.out.println("chachabuf " + Hex.toHexString(chachaBuf));
        }

        mac.doFinal(macBuf, 0);
        System.out.println("encmac " + Hex.toHexString(macBuf));
        output.write(macBuf);
    }

    public void decrypt(byte[] key, byte[] nonce, InputStream input, OutputStream output) throws IOException {
        this.cipher.init(false, new ParametersWithIV(new KeyParameter(key), nonce));
        byte[] macBuf = new byte[16];
        /*if (getPlaintextLimit(len) < 0) {
            throw new TlsFatalAlert(AlertDescription.decode_error);
        }*/

        initRecordMAC(cipher);

        int r = 0;
        while ((r = input.read(readBuf)) > 0) {
            cipher.processBytes(readBuf, 0, readBuf.length, chachaBuf, 0);
            output.write(chachaBuf, 0, r);
            updateRecordMAC(readBuf, 0, readBuf.length);
        }

        System.out.println("decbuf " + Hex.toHexString(readBuf));

        mac.doFinal(macBuf, 0);
        System.out.println("decmac " + Hex.toHexString(macBuf));

        if (!Arrays.constantTimeAreEqual(macBuf, Arrays.copyOfRange(readBuf, 0, 16))) {
            throw new TlsFatalAlert(AlertDescription.bad_record_mac);
        }
    }

    private void initRecordMAC(ChaChaEngine cipher) {
        byte[] firstBlock = new byte[64];
        cipher.processBytes(firstBlock, 0, firstBlock.length, firstBlock, 0);

        // NOTE: The BC implementation puts 'r' after 'k'
        System.arraycopy(firstBlock, 0, firstBlock, 32, 16);
        KeyParameter macKey = new KeyParameter(firstBlock, 16, 32);
        Poly1305KeyGenerator.clamp(macKey.getKey());
        mac.init(macKey);
    }

    private void updateRecordMAC(byte[] buf, int off, int len) {
        mac.update(buf, off, len);

        byte[] longLen = Pack.longToLittleEndian(len & 0xFFFFFFFFL);
        mac.update(longLen, 0, longLen.length);
    }
}
