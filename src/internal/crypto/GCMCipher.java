/* 
 * Copyright (c) 2016, Serphentas
 * All rights reserved.
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/ or send a letter
 * to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
package internal.crypto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.generators.SCrypt;

/**
 * General purpose class used for encryption and decryption of files, using
 * AES-256 in GCM mode of operation.
 * <p>
 * <b><i>This class is not thread-safe.</i></b>
 *
 * @author Serphentas
 */
public final class GCMCipher {

    private static final Logger log = Logger.getLogger(GCMCipher.class.getName());

    // cryptographic constants
    private static final String CIPHER = "AES/GCM/NoPadding",
            CRYPTO_PROVIDER = "BC";
    private static final int CIPHER_KEY_BITS = 256,
            GCM_NONCE_BYTES = 12,
            GCM_TAG_BITS = 128,
            S_BYTES = 128,
            N_BYTES = 256,
            R_BYTES = 256,
            BUFFER_SIZE = 4096,
            KDF_N_K = (int) Math.pow(2, 19),
            KDF_p_K = 8,
            KDF_r_K = 1,
            KDF_N_N = (int) Math.pow(2, 14),
            KDF_p_N = 8,
            KDF_r_N = 1,
            SANITIZATION_ITERATION = 1024,
            V1S1 = 1 + S_BYTES,
            S1N1 = V1S1 + GCM_NONCE_BYTES,
            N1R = S1N1 + R_BYTES + GCM_TAG_BITS / 8,
            RS2 = N1R + S_BYTES,
            S2N2 = RS2 + GCM_NONCE_BYTES;
    private static final byte[] buffer = new byte[BUFFER_SIZE];
    private static final byte VERSION = 0x00;
    private static String password = null;

    private final Cipher cipher;

    private final DataOutputStream dos;
    private final DataInputStream dis;

    public static void setPassword(String pass) {
        password = pass;
    }

    /**
     * Instantiates a Cipher, allowing subsequent use for encryption and
     * decryption of an arbitrary file
     *
     * @param dis
     * @param dos
     * @throws java.lang.Exception
     *
     */
    public GCMCipher(DataOutputStream dos, DataInputStream dis) throws Exception {
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

        // settings the I/O streams
        this.dos = dos;
        this.dis = dis;
    }

    /**
     * Encrypts a given file with AES-256 in GCM mode of operation
     * <p>
     * Reads data from the InputStream and writes the encrypted data to the
     * OutputStream
     *
     * @param inputFile
     * @return true if the file was successfully transferred, else false
     * @throws java.io.IOException
     * @throws java.security.InvalidKeyException
     * @throws java.security.InvalidAlgorithmParameterException
     * @throws javax.crypto.BadPaddingException
     * @throws javax.crypto.IllegalBlockSizeException
     */
    public boolean encrypt(File inputFile) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        // defining I/O streams
        InputStream input = new FileInputStream(inputFile);

        // generating Sx, Nx, R, Kx
        final byte[] S1 = GPCrypto.randomGen(S_BYTES),
                S2 = GPCrypto.randomGen(S_BYTES),
                N = SCrypt.generate(GPCrypto.randomGen(N_BYTES), GPCrypto
                        .randomGen(N_BYTES), KDF_N_N, KDF_p_N, KDF_r_N, 2 * GCM_NONCE_BYTES),
                N1 = Arrays.copyOfRange(N, 0, 12),
                N2 = Arrays.copyOfRange(N, 12, 24),
                R = GPCrypto.randomGen(R_BYTES);
        final SecretKey K1 = new SecretKeySpec(SCrypt.generate(password.getBytes(),
                S1, KDF_N_K, KDF_p_K, KDF_r_K, CIPHER_KEY_BITS / 8), 0,
                CIPHER_KEY_BITS / 8, "AES"),
                K2 = new SecretKeySpec(SCrypt.generate(R, S2, KDF_N_K, KDF_p_K,
                        KDF_r_K, CIPHER_KEY_BITS / 8), 0, CIPHER_KEY_BITS / 8, "AES");

        // writing header
        this.cipher.init(Cipher.ENCRYPT_MODE, K1, new GCMParameterSpec(
                GCM_TAG_BITS, N1, 0, GCM_NONCE_BYTES));
        dos.writeByte(VERSION);
        dos.write(S1);
        dos.write(N1);
        dos.write(cipher.doFinal(R));
        dos.write(S2);
        dos.write(N2);

        // encrypting file
        this.cipher.init(Cipher.ENCRYPT_MODE, K2, new GCMParameterSpec(
                GCM_TAG_BITS, N2, 0, GCM_NONCE_BYTES));
        OutputStream cos = new CipherOutputStream(dos, this.cipher);

        int r = 0;
        while ((r = input.read(buffer)) > 0) {
            cos.write(buffer, 0, r);
        }

        // erasing cryptographic parameters and closing streams
        eraseByteArrays(S1, S2, N, N1, N2, R);
        eraseKeys(K1, K2);
        cos.close();
        input.close();

        return dis.readBoolean();
    }

    /**
     * Decrypts a given file with AES-256 in GCM mode of operation
     *
     * @param remoteFilePath
     * @param localFile
     * @return true if the file was successfully transferred, else false
     * @throws java.io.IOException
     * @throws java.security.InvalidKeyException
     * @throws java.security.InvalidAlgorithmParameterException
     * @throws javax.crypto.BadPaddingException
     * @throws javax.crypto.IllegalBlockSizeException
     */
    public boolean decrypt(String remoteFilePath, File localFile) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        // defining I/O streams
        OutputStream output = new FileOutputStream(localFile);

        // reading header
        byte[] header = new byte[553];
        dis.read(header, 0, 553);

        // reading V, Sx, Nx and generating K1
        final byte V = header[0];
        final byte[] S1 = Arrays.copyOfRange(header, 1, V1S1),
                S2 = Arrays.copyOfRange(header, N1R, RS2),
                N1 = Arrays.copyOfRange(header, V1S1, S1N1),
                N2 = Arrays.copyOfRange(header, RS2, S2N2);
        final SecretKey K1 = new SecretKeySpec(SCrypt.generate(password.getBytes(),
                S1, KDF_N_K, KDF_p_K, KDF_r_K,
                CIPHER_KEY_BITS / 8), 0, CIPHER_KEY_BITS / 8, "AES");

        // reading E(K1, N1, R)
        this.cipher.init(Cipher.DECRYPT_MODE, K1, new GCMParameterSpec(
                GCM_TAG_BITS, N1, 0, GCM_NONCE_BYTES));
        final byte[] R = cipher.doFinal(Arrays.copyOfRange(header, S1N1, N1R));

        // generating K2
        final SecretKey K2 = new SecretKeySpec(SCrypt.generate(R, S2, KDF_N_K,
                KDF_p_K, KDF_r_K, CIPHER_KEY_BITS / 8), 0, CIPHER_KEY_BITS / 8, "AES");

        // decrypting file
        this.cipher.init(Cipher.DECRYPT_MODE, K2, new GCMParameterSpec(
                GCM_TAG_BITS, N2, 0, GCM_NONCE_BYTES));
        OutputStream cos = new CipherOutputStream(output, this.cipher);
        int r = 0;
        while ((r = dis.read(buffer)) > 0) {
            cos.write(buffer, 0, r);
        }

        // erasing cryptographic parameters and closing streams
        eraseByteArrays(S1, S2, N1, N2, R);
        eraseKeys(K1, K2);
        cos.close();
        output.close();

        return dis.readBoolean();
    }

    public String decryptFilename(String fileName) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {

        // reading V, Sx and Nx
        byte[] header = new byte[553];
        dis.read(header, 0, 553);
        final byte V = header[0];
        final byte[] S1 = Arrays.copyOfRange(header, 1, V1S1),
                S2 = Arrays.copyOfRange(header, N1R, RS2),
                N1 = Arrays.copyOfRange(header, V1S1, S1N1),
                N2 = Arrays.copyOfRange(header, RS2, S2N2);

        // generating K1 and reading E(K1, N1, R)
        final SecretKey K1 = new SecretKeySpec(SCrypt.generate(password.getBytes(),
                S1, KDF_N_K, KDF_p_K, KDF_r_K,
                CIPHER_KEY_BITS / 8), 0, CIPHER_KEY_BITS / 8, "AES");
        this.cipher.init(Cipher.DECRYPT_MODE, K1, new GCMParameterSpec(
                GCM_TAG_BITS, N1, 0, GCM_NONCE_BYTES));
        final byte[] R_enc = Arrays.copyOfRange(header, S1N1, N1R);
        final byte[] R = cipher.doFinal(R_enc);

        // generating K2 and reading filename
        final SecretKey K2 = new SecretKeySpec(SCrypt.generate(R, S2, KDF_N_K,
                KDF_p_K, KDF_r_K, CIPHER_KEY_BITS / 8), 0, CIPHER_KEY_BITS / 8, "AES");
        this.cipher.init(Cipher.DECRYPT_MODE, K2, new GCMParameterSpec(
                GCM_TAG_BITS, N2, 0, GCM_NONCE_BYTES));
        String fileNameDec = new String(cipher.doFinal(fileName.getBytes()));

        eraseByteArrays(S1, S2, N1, N2, R_enc, R);
        eraseKeys(K1, K2);
        dis.close();

        return fileNameDec;
    }

    private void eraseByteArrays(byte[]... arrays) {
        for (byte[] array : arrays) {
            GPCrypto.sanitize(array, SANITIZATION_ITERATION);
        }
    }

    private void eraseKeys(SecretKey... keys) {
        for (SecretKey key : keys) {
            GPCrypto.sanitize(key.getEncoded(), SANITIZATION_ITERATION);
        }
    }
}
