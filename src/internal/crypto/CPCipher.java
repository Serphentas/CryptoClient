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

/**
 * General purpose class used for encryption and decryption of files, using
 * ChaCha20 w/ Poly1305 as MAC in encrypt-then-MAC scheme
 * <p>
 * Based on the ChaCha20Poly1305 class from Bouncy Castle but without the TLS
 * related code
 *
 * @author Serphentas
 */
public final class CPCipher {

    private final ChaChaEngine cipher;
    private final Poly1305 mac;
    private static int BUFFER_SIZE = 1024;

    public CPCipher() throws IOException {
        this.cipher = new ChaChaEngine();
        this.mac = new Poly1305();
    }

    public static void setBufferSize(int newSize) {
        BUFFER_SIZE = newSize;
    }

    public int getPlaintextLimit(int ciphertextLimit) {
        return ciphertextLimit - 16;
    }

    /**
     * Encrypts a given file with ChaCha20 w/ Poly1305 as MAC in
     * encrypt-then-MAC scheme.
     * <p>
     * Reads data from the InputStream and writes the encrypted data to the
     * OutputStream
     *
     * @param key
     * @param nonce
     * @param input
     * @param output
     * @throws IOException
     */
    public void encrypt(byte[] key, byte[] nonce, InputStream input, OutputStream output) throws IOException {
        this.cipher.init(true, new ParametersWithIV(new KeyParameter(key), nonce));
        byte[] ciphertextMac = new byte[16], readBuf = new byte[BUFFER_SIZE],
                chachaBuf = new byte[BUFFER_SIZE];
        initMAC(cipher);

        int r = 0;
        while ((r = input.read(readBuf)) > 0) {
            cipher.processBytes(readBuf, 0, r, chachaBuf, 0);
            output.write(chachaBuf, 0, r);
            updateMAC(chachaBuf, 0, r);
        }

        mac.doFinal(ciphertextMac, 0);
        output.write(ciphertextMac);
    }

    /**
     * Decrypts a given file with ChaCha20 w/ Poly1305 as MAC in
     * encrypt-then-MAC scheme.
     * <p>
     * Reads data from the InputStream and writes the decrypted data to the
     * OutputStream
     *
     * @param key
     * @param nonce
     * @param input
     * @param output
     * @throws IOException
     */
    public void decrypt(byte[] key, byte[] nonce, InputStream input,
            OutputStream output) throws IOException {
        this.cipher.init(false, new ParametersWithIV(new KeyParameter(key), nonce));
        byte[] computedMac = new byte[16], receivedMac = new byte[16], readBuf
                = new byte[BUFFER_SIZE], chachaBuf = new byte[BUFFER_SIZE];
        initMAC(cipher);

        int r = 0;
        while ((r = input.read(readBuf)) > 0) {
            // case when EOF has not been reached
            if (r == BUFFER_SIZE) {
                // use C in whole to update the MAC and decrypt
                updateMAC(readBuf, 0, r);
                cipher.processBytes(readBuf, 0, r, chachaBuf, 0);
                output.write(chachaBuf, 0, r);
            } else {
                // use all but the last 16 bytes from C to update the MAC and decrypt
                updateMAC(Arrays.copyOfRange(readBuf, 0, r - 16), 0, r - 16);
                cipher.processBytes(Arrays.copyOfRange(readBuf, 0, r - 16), 0,
                        r - 16, chachaBuf, 0);
                output.write(chachaBuf, 0, r - 16);

                // copy the last 16 bytes as the original MAC
                receivedMac = Arrays.copyOfRange(readBuf, r - 16, r);
            }
        }

        // check if the two MACs match
        mac.doFinal(computedMac, 0);
        if (!Arrays.constantTimeAreEqual(computedMac, receivedMac)) {
            throw new TlsFatalAlert(AlertDescription.bad_record_mac);
        }
    }

    /**
     * Initializes Poly1305 with the given instance of ChaCha20Engine
     *
     * @param cipher
     */
    private void initMAC(ChaChaEngine cipher) {
        byte[] firstBlock = new byte[64];
        cipher.processBytes(firstBlock, 0, firstBlock.length, firstBlock, 0);

        // NOTE: The BC implementation puts 'r' after 'k'
        System.arraycopy(firstBlock, 0, firstBlock, 32, 16);
        KeyParameter macKey = new KeyParameter(firstBlock, 16, 32);
        Poly1305KeyGenerator.clamp(macKey.getKey());
        mac.init(macKey);
    }

    /**
     * Updates the state of Poly1305
     *
     * @param buf array containing the input
     * @param off the index in the array the data begins at
     * @param len length of the input starting at off
     */
    private void updateMAC(byte[] buf, int off, int len) {
        mac.update(buf, off, len);

        byte[] longLen = Pack.longToLittleEndian(len & 0xFFFFFFFFL);
        mac.update(longLen, 0, longLen.length);
    }
}
