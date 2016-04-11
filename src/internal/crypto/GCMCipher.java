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
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.generators.SCrypt;

/**
 * General purpose class used for encryption and decryption of files.
 * <p>
 * <b><i>This class is not thread-safe.</i></b>
 *
 * @author Serphentas
 */
public final class GCMCipher {

    // cryptographic constants
    private static final String CIPHER = "AES/GCM/NoPadding",
            CRYPTO_PROVIDER = "BC";
    private static final int CIPHER_KEY_BITS = 256,
            GCM_NONCE_BYTES = 12,
            GCM_TAG_BITS = 128,
            S_BYTES = 128,
            N_BYTES = 256,
            R_BYTES = 256,
            KDF_N_K = (int) Math.pow(2, 19),
            KDF_p_K = 8,
            KDF_r_K = 1,
            KDF_N_N = (int) Math.pow(2, 14),
            KDF_p_N = 8,
            KDF_r_N = 1,
            SANITIZATION_ITERATION = 1024,
            S1N1 = S_BYTES + GCM_NONCE_BYTES,
            N1R = S1N1 + R_BYTES + GCM_TAG_BITS / 8,
            RS2 = N1R + S_BYTES,
            S2N2 = RS2 + GCM_NONCE_BYTES;

    private static final byte[] buffer = new byte[4096];

    private final Cipher cipher;
    private static String password = null;

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
        // generating Sx, Nx, R, Kx
        final byte[] S1 = GPCrypto.randomGen(S_BYTES), S2 = GPCrypto.randomGen(S_BYTES);
        final byte[] R = GPCrypto.randomGen(R_BYTES);
        final byte[] N = SCrypt.generate(GPCrypto.randomGen(N_BYTES), GPCrypto
                .randomGen(N_BYTES), KDF_N_N, KDF_p_N, KDF_r_N, 2 * GCM_NONCE_BYTES);
        final byte[] N1 = Arrays.copyOfRange(N, 0, 12), N2 = Arrays.copyOfRange(N, 12, 24);
        final SecretKey K1 = new SecretKeySpec(SCrypt.generate(password.getBytes(),
                S1, KDF_N_K, KDF_p_K, KDF_r_K,
                CIPHER_KEY_BITS / 8), 0, CIPHER_KEY_BITS / 8, "AES");
        final SecretKey K2 = new SecretKeySpec(SCrypt.generate(R, S2, KDF_N_K,
                KDF_p_K, KDF_r_K, CIPHER_KEY_BITS / 8), 0, CIPHER_KEY_BITS / 8, "AES");

        // writing S1, N1 and E(K1, N1, R) to C
        this.cipher.init(Cipher.ENCRYPT_MODE, K1, new GCMParameterSpec(
                GCM_TAG_BITS, N1, 0, GCM_NONCE_BYTES));
        output.write(S1);
        output.write(N1);
        output.write(cipher.doFinal(R));

        // cipher initialization
        this.cipher.init(Cipher.ENCRYPT_MODE, K2, new GCMParameterSpec(
                GCM_TAG_BITS, N2, 0, GCM_NONCE_BYTES));

        // writing S2, N2 and E(K2, N2, P) to C
        output.write(S2);
        output.write(N2);
        OutputStream cos = new CipherOutputStream(output, this.cipher);
        int r = 0;
        while ((r = input.read(buffer)) > 0) {
            cos.write(buffer, 0, r);
        }

        // erasing cryptographic parameters and closing streams
        eraseParams(S1, S2, N, N1, N2, R, K1, K2);
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
        // reading Sx & Nx and generating K1
        byte[] header = new byte[552];
        input.read(header, 0, 552);

        final byte[] S1 = Arrays.copyOfRange(header, 0, S_BYTES), S2 = Arrays
                .copyOfRange(header, N1R, RS2);
        final byte[] N1 = Arrays.copyOfRange(header, S_BYTES, S1N1), N2 = Arrays
                .copyOfRange(header, RS2, S2N2);

        final SecretKey K1 = new SecretKeySpec(SCrypt.generate(password.getBytes(),
                S1, KDF_N_K, KDF_p_K, KDF_r_K,
                CIPHER_KEY_BITS / 8), 0, CIPHER_KEY_BITS / 8, "AES");

        // reading E(K1, N1, R)
        this.cipher.init(Cipher.DECRYPT_MODE, K1, new GCMParameterSpec(
                GCM_TAG_BITS, N1, 0, GCM_NONCE_BYTES));
        final byte[] R_enc = Arrays.copyOfRange(header, S1N1, N1R);
        final byte[] R = cipher.doFinal(R_enc);

        // generating K2
        final SecretKey K2 = new SecretKeySpec(SCrypt.generate(R, S2, KDF_N_K,
                KDF_p_K, KDF_r_K, CIPHER_KEY_BITS / 8), 0, CIPHER_KEY_BITS / 8, "AES");

        // cipher initialization
        this.cipher.init(Cipher.DECRYPT_MODE, K2, new GCMParameterSpec(
                GCM_TAG_BITS, N2, 0, GCM_NONCE_BYTES));

        // reading E(K2, N2, C)
        OutputStream cos = new CipherOutputStream(output, this.cipher);
        int r = 0;
        while ((r = input.read(buffer)) > 0) {
            cos.write(buffer, 0, r);
        }

        // erasing cryptographic parameters and closing streams
        eraseParams(S1, S2, N1, N2, R_enc, R, K1, K2);
        cos.close();
        input.close();
        output.close();
    }

    private void eraseParams(byte[] S1, byte[] S2, byte[] N, byte[] N1, byte[] N2,
            byte[] R, SecretKey K1, SecretKey K2) {
        GPCrypto.sanitize(S1, SANITIZATION_ITERATION);
        GPCrypto.sanitize(S2, SANITIZATION_ITERATION);
        GPCrypto.sanitize(N, SANITIZATION_ITERATION);
        GPCrypto.sanitize(N1, SANITIZATION_ITERATION);
        GPCrypto.sanitize(N2, SANITIZATION_ITERATION);
        GPCrypto.sanitize(R, SANITIZATION_ITERATION);
        GPCrypto.sanitize(K1.getEncoded(), SANITIZATION_ITERATION);
        GPCrypto.sanitize(K2.getEncoded(), SANITIZATION_ITERATION);
    }
}
