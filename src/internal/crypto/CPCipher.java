/* 
 * The MIT License
 *
 * Copyright 2016 Serphentas.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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

public class CPCipher {

    private final ChaChaEngine cipher;
    private final Poly1305 mac;
    private static final int BUFFER_SIZE = 4096;
    private static final byte[] readBuf = new byte[BUFFER_SIZE];
    private static final byte[] chachaBuf = new byte[BUFFER_SIZE];

    public CPCipher() throws IOException {
        this.cipher = new ChaChaEngine();
        this.mac = new Poly1305();
    }

    public int getPlaintextLimit(int ciphertextLimit) {
        return ciphertextLimit - 16;
    }

    public void encrypt(byte[] key, byte[] nonce, InputStream input, OutputStream output) throws IOException {
        this.cipher.init(true, new ParametersWithIV(new KeyParameter(key), nonce));
        byte[] ciphertextMac = new byte[16];
        initRecordMAC(cipher);

        int r = 0;
        while ((r = input.read(readBuf)) > 0) {
            cipher.processBytes(readBuf, 0, r, chachaBuf, 0);
            output.write(chachaBuf, 0, r);
            updateRecordMAC(chachaBuf, 0, r);
        }

        mac.doFinal(ciphertextMac, 0);
        output.write(ciphertextMac);
    }

    public void decrypt(byte[] key, byte[] nonce, InputStream input,
            OutputStream output) throws IOException {
        this.cipher.init(false, new ParametersWithIV(new KeyParameter(key), nonce));
        byte[] computedMac = new byte[16], receivedMac = new byte[16];

        initRecordMAC(cipher);

        int r = 0;
        while ((r = input.read(readBuf)) > 0) {
            // case when EOF has not been reached
            if (r == BUFFER_SIZE) {
                // use C in whole, update the MAC and decrypt
                updateRecordMAC(readBuf, 0, r);
                cipher.processBytes(readBuf, 0, r, chachaBuf, 0);
            } else {
                // use all but the last 16 bytes from C, update the MAC and decrypt
                updateRecordMAC(Arrays.copyOfRange(readBuf, 0, r - 16), 0, r - 16);
                cipher.processBytes(Arrays.copyOfRange(readBuf, 0, r - 16), 0,
                        r - 16, chachaBuf, 0);
                // copy the last 16 bytes as the original MAC
                receivedMac = Arrays.copyOfRange(readBuf, r - 16, r);
            }
            output.write(chachaBuf, 0, r - 16);
        }

        // check if the two MACs match
        mac.doFinal(computedMac, 0);
        if (!Arrays.constantTimeAreEqual(computedMac, receivedMac)) {
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
