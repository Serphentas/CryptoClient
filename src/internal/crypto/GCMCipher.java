/* 
 * Copyright (C) 2016 Serphentas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package internal.crypto;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.util.encoders.Hex;

/**
 * General purpose class used for encryption and decryption of files.
 * <p>
 * <b><i>This class is not thread-safe.</i></b>
 *
 * @author Serphentas
 */
public final class GCMCipher {

    // cryptographic constants
    private static final String CIPHER = "AES/GCM/NoPadding";
    private static final String CRYPTO_PROVIDER = "BC";
    private static final int CIPHER_KEY_BITS = 256;
    private static final int GCM_NONCE_BYTES = 12;
    private static final int GCM_TAG_BITS = 128;
    private static final int KDF_CPU_RAM_COST = (int) Math.pow(2, 19);
    private static final int KDF_BLOCK_SIZE = 8;
    private static final int KDF_PARALLEL = 1;
    private static final int SANITIZATION_ITERATION = 1024;

    private static final byte[] buffer = new byte[4096];

    private final Cipher cipher;
    private byte[] nonce = null;
    private byte[] salt = null;
    private SecretKey key = null;
    private static String password = null;

    public long time;

    public static void setPassword(String pass) {
        password = pass;
    }

    /**
     * Instantiates a Cipher, allowing subsequent use for encryption and
     * decryption of an arbitrary file
     *
     * @throws java.lang.Exception
     */
    public GCMCipher() throws Exception {
        /*
        suppresses the restriction over keys larger than 128 bits due to the
        JCE Unlimited Strength Jurisdiction Policy
        see http://v.gd/HN1qpB
         */
        Field field = Class.forName("javax.crypto.JceSecurity").
                getDeclaredField("isRestricted");
        field.setAccessible(true);
        field.set(null, java.lang.Boolean.FALSE);

        // instantiating AES-256 w/ GCM from Bouncy Castle
        this.cipher = Cipher.getInstance(CIPHER, CRYPTO_PROVIDER);
    }

    /**
     * Encrypts a given file with AES-256 in GCM mode of operation
     * <p>
     * Reads data from the InputStream and writes the encrypted data to the
     * OutputStream
     *
     * @param input InputStream to read data from
     * @param output OutputStream to write encrypted data to
     * @throws Exception
     */
    public void encrypt(InputStream input, OutputStream output) throws Exception {
        // generating key and nonce
        this.salt = GPCrypto.randomGen(128);
        time = System.nanoTime();
        this.key = new SecretKeySpec(SCrypt.generate(password.getBytes(), this.salt, KDF_CPU_RAM_COST, KDF_BLOCK_SIZE, KDF_PARALLEL, CIPHER_KEY_BITS / 8), 0, CIPHER_KEY_BITS / 8, "AES");
        System.out.println((System.nanoTime() - time) / 1e9);
        this.nonce = SCrypt.generate(GPCrypto.randomGen(128), GPCrypto.randomGen(128), KDF_CPU_RAM_COST, KDF_BLOCK_SIZE, KDF_PARALLEL, GCM_NONCE_BYTES);

        // cipher initialization
        this.cipher.init(Cipher.ENCRYPT_MODE, this.key, new GCMParameterSpec(
                GCM_TAG_BITS, this.nonce, 0, GCM_NONCE_BYTES));

        // writing K-salt and nonce to output file
        output.write(this.salt);
        System.out.println(this.salt.length + " " + Hex.toHexString(this.salt));
        output.write(this.nonce);
        System.out.println(this.nonce.length + " " + Hex.toHexString(this.nonce));

        // finishing the encryption job
        int r = 0;
        OutputStream cos = new CipherOutputStream(output, this.cipher);

        while ((r = input.read(buffer)) > 0) {
            cos.write(buffer, 0, r);
        }

        // erasing cryptographic parameters and closing streams
        eraseParams();
        cos.close();
        input.close();
        output.close();
    }

    /**
     * Decrypts a given file with AES-256 in GCM mode of operation
     *
     * @param input InputStream to read encrypted data from
     * @param output OutputStream to write decrypt data to
     * @throws Exception
     */
    public void decrypt(InputStream input, OutputStream output) throws Exception {
        // read K-salt and nonce
        byte[] header = new byte[140];
        input.read(header, 0, 140);
        this.salt = Arrays.copyOfRange(header, 0, 128);
        System.out.println(this.salt.length + " " + Hex.toHexString(this.salt));
        this.key = new SecretKeySpec(SCrypt.generate(password.getBytes(), this.salt, KDF_CPU_RAM_COST, KDF_BLOCK_SIZE, KDF_PARALLEL, CIPHER_KEY_BITS / 8), 0, CIPHER_KEY_BITS / 8, "AES");
        this.nonce = Arrays.copyOfRange(header, 128, 140);
        System.out.println(this.nonce.length + " " + Hex.toHexString(this.nonce));

        // GCMCipher initialization
        this.cipher.init(Cipher.DECRYPT_MODE, this.key, new GCMParameterSpec(
                GCM_TAG_BITS, this.nonce, 0, GCM_NONCE_BYTES));

        // finishing the decryption job
        int r = 0;
        InputStream cis = new CipherInputStream(input, this.cipher);

        while ((r = cis.read(buffer)) > 0) {
            output.write(buffer, 0, r);
        }

        eraseParams();
        cis.close();
        input.close();
        output.close();
    }

    private void eraseParams() {
        GPCrypto.sanitize(this.salt, SANITIZATION_ITERATION);
        GPCrypto.sanitize(this.nonce, SANITIZATION_ITERATION);
        GPCrypto.sanitize(this.key.getEncoded(), SANITIZATION_ITERATION);
    }
}
