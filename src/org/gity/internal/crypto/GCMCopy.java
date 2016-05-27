/* 
 * Copyright (c) 2016, Serphentas
 * All rights reserved.
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/ or send a letter
 * to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
package org.gity.internal.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.util.encoders.Hex;
import org.gity.internal.network.NTP;

/**
 * General purpose class used for encryption and decryption of files, using
 * AES-256 in GCM mode of operation.
 * <p>
 * <b><i>This class is not thread-safe.</i></b>
 *
 * @author Serphentas
 */
public final class GCMCopy {

    private static final String CIPHER = "AES/GCM/NoPadding",
            CRYPTO_PROVIDER = "BC";
    private static final int CIPHER_KEY_BITS = 256,
            GCM_NONCE_BYTES = 12,
            GCM_TAG_BITS = 128,
            S_BYTES = 64,
            R_BYTES = 64,
            BUFFER_SIZE = 512,
            KDF_r = 8,
            KDF_p = 1,
            VS1 = S_BYTES,
            S1N1 = VS1 + GCM_NONCE_BYTES,
            N1K1N = S1N1 + 1,
            K1NR = N1K1N + R_BYTES + GCM_TAG_BITS / 8,
            RS2 = K1NR + S_BYTES,
            S2N2 = RS2 + GCM_NONCE_BYTES,
            N2K2N = S2N2 + 1;
    private static int K1_KDF_N = 19,
            K2_KDF_N = 19;
    private static final byte[] buf = new byte[BUFFER_SIZE];

    private final Cipher cipher;

    /**
     * Sets the new CPU/RAM cost parameter for scrypt when deriving K1, read as
     * a power of two
     * <p>
     * Default value: 20
     *
     * @param N new CPU/RAM cost for scrypt, as a power of two
     */
    protected static void setK1N(int N) {
        GCMCopy.K1_KDF_N = N;
    }

    /**
     * Sets the new CPU/RAM cost parameter for scrypt when deriving K2, read as
     * a power of two
     * <p>
     * Default value: 19
     *
     * @param N new CPU/RAM cost for scrypt, as a power of two
     */
    protected static void setK2N(int N) {
        GCMCopy.K2_KDF_N = N;
    }

    /**
     * Instantiates a Cipher object using AES-256 in GCM mode of operation,
     * allowing subsequent use for file encryption and decryption
     */
    public GCMCopy() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
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
     * @param inputFile
     * @param outputFile
     * @throws java.io.IOException
     * @throws java.security.InvalidKeyException
     * @throws java.security.InvalidAlgorithmParameterException
     * @throws javax.crypto.BadPaddingException
     * @throws javax.crypto.IllegalBlockSizeException
     */
    public void encrypt_V00(File inputFile, File outputFile) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        // defining I/O streams
        InputStream input = new FileInputStream(inputFile);
        OutputStream output = new FileOutputStream(outputFile);

        // getting the encryption password
        char[] pass = DefaultCipher.getEncryptionPassword();

        long fileSize = inputFile.length();

        // generating Sx, Nx, R and Kx
        final byte[] S1 = GPCrypto.randomGen(S_BYTES),
                S2 = GPCrypto.randomGen(S_BYTES),
                epoch = DatatypeConverter.parseHexBinary(Long.toHexString(NTP.getTime() / 1000)),
                N1 = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, epoch[0], epoch[1],
                    epoch[2], epoch[3]},
                N2 = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, epoch[0], epoch[1],
                    epoch[2], (byte) (epoch[3] + 0x01)},
                R = GPCrypto.randomGen(R_BYTES);
        final SecretKey K1 = new SecretKeySpec(SCrypt.generate(GPCrypto.charToByte(pass),
                S1, (int) Math.pow(2, K1_KDF_N), KDF_r, KDF_p, CIPHER_KEY_BITS / 8), 0,
                CIPHER_KEY_BITS / 8, "AES"),
                K2 = new SecretKeySpec(SCrypt.generate(R, S2, (int) Math.pow(2, K2_KDF_N), KDF_r,
                        KDF_p, CIPHER_KEY_BITS / 8), 0, CIPHER_KEY_BITS / 8, "AES");

        // writing header
        this.cipher.init(Cipher.ENCRYPT_MODE, K1, new GCMParameterSpec(
                GCM_TAG_BITS, N1, 0, GCM_NONCE_BYTES));
        output.write((byte) 0x00);
        output.write(S1);
        output.write(N1);
        output.write(DatatypeConverter.parseHexBinary(Integer.toHexString(K1_KDF_N)));
        output.write(cipher.doFinal(R));
        output.write(S2);
        output.write(N2);
        output.write(DatatypeConverter.parseHexBinary(Integer.toHexString(K2_KDF_N)));

        // encrypting file
        this.cipher.init(Cipher.ENCRYPT_MODE, K2, new GCMParameterSpec(
                GCM_TAG_BITS, N2, 0, GCM_NONCE_BYTES));
        int r = 0,
                counter = 0;

        while ((r = input.read(buf)) != -1) {
            counter++;
            if (r == BUFFER_SIZE) {
                if (counter * BUFFER_SIZE != fileSize) {
                    output.write(this.cipher.update(buf));
                } else {
                    output.write(this.cipher.doFinal(buf));
                }
            } else {
                output.write(this.cipher.doFinal(Arrays.copyOfRange(buf, 0, r)));
            }
        }

        // erasing cryptographic parameters and closing streams
        GPCrypto.eraseByteArrays(S1, S2, epoch, N1, N2, R);
        GPCrypto.sanitize(buf, 1000);
        GPCrypto.eraseKeys(K1, K2);
        GPCrypto.sanitize(pass);
        input.close();
        output.close();
    }

    /**
     * Decrypts a given file with AES-256 in GCM mode of operation
     *
     * @param inputFile
     * @param outputFile
     * @throws java.io.IOException
     * @throws java.security.InvalidKeyException
     * @throws java.security.InvalidAlgorithmParameterException
     * @throws javax.crypto.BadPaddingException
     * @throws javax.crypto.IllegalBlockSizeException
     */
    public void decrypt_V00(File inputFile, File outputFile) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        // defining I/O streams
        OutputStream output = new FileOutputStream(outputFile);
        InputStream input = new FileInputStream(inputFile);

        // getting the encryption password
        char[] pass = DefaultCipher.getEncryptionPassword();

        long fileSize = inputFile.length(),
                iterCnt = fileSize / BUFFER_SIZE,
                percentage = fileSize / 100L,
                bytesRead = 0L;

        // skipping version byte and reading header
        input.skip(1);
        byte[] header = new byte[234];
        input.read(header, 0, 234);

        // reading Sx, Nx, scrypt factors and generating K1
        final byte[] S1 = Arrays.copyOfRange(header, 0, VS1),
                N1 = Arrays.copyOfRange(header, VS1, S1N1),
                K1_N = Arrays.copyOfRange(header, S1N1, N1K1N),
                S2 = Arrays.copyOfRange(header, K1NR, RS2),
                N2 = Arrays.copyOfRange(header, RS2, S2N2),
                K2_N = Arrays.copyOfRange(header, S2N2, N2K2N);
        final int K1_N_bak = (int) Math.pow(2, Integer.valueOf(Hex.toHexString(K1_N), 16)),
                K2_N_bak = (int) Math.pow(2, Integer.valueOf(Hex.toHexString(K2_N), 16));
        final SecretKey K1 = new SecretKeySpec(SCrypt.generate(GPCrypto.charToByte(pass),
                S1, K1_N_bak, KDF_r, KDF_p, CIPHER_KEY_BITS / 8), 0, CIPHER_KEY_BITS / 8, "AES");

        // reading E(K1, N1, R) 
        this.cipher.init(Cipher.DECRYPT_MODE, K1, new GCMParameterSpec(
                GCM_TAG_BITS, N1, 0, GCM_NONCE_BYTES));
        final byte[] R = cipher.doFinal(Arrays.copyOfRange(header, N1K1N, K1NR));

        // generating K2
        final SecretKey K2 = new SecretKeySpec(SCrypt.generate(R, S2, K2_N_bak,
                KDF_r, KDF_p, CIPHER_KEY_BITS / 8), 0, CIPHER_KEY_BITS / 8, "AES");

        // decrypting file
        this.cipher.init(Cipher.DECRYPT_MODE, K2, new GCMParameterSpec(
                GCM_TAG_BITS, N2, 0, GCM_NONCE_BYTES));
        int r = 0,
                counter = 0;

        /*while ((r = input.read(buf)) != -1) {
            counter++;
            System.out.println(r);
            if (r == BUFFER_SIZE) {
                if (counter * BUFFER_SIZE != (fileSize - 235)) {
                    output.write(this.cipher.update(buf));
                } else {
                    output.write(this.cipher.doFinal(buf));
                }
            } else if (r != 16) {
                output.write(this.cipher.update(Arrays.copyOfRange(buf, 0, r - 16)));
                output.write(this.cipher.doFinal(Arrays.copyOfRange(buf, r - 16, r)));
            } else {
                output.write(this.cipher.doFinal(Arrays.copyOfRange(buf, 0, 16)));
            }
            cos.write(buf, 0, r);
        }*/

        for (int i = 0; i < iterCnt; i++) {
            input.read(buf);
            if (i != iterCnt) {
                output.write(this.cipher.update(buf));
            } else if (fileSize % BUFFER_SIZE == 0) {
                output.write(this.cipher.doFinal(buf));
            }
        }

        if (fileSize % BUFFER_SIZE != 0) {
            r = input.read(buf, 0, ((int) fileSize % BUFFER_SIZE) + 16);
            output.write(this.cipher.update(Arrays.copyOfRange(buf, 0, r - 16)));
            output.write(this.cipher.doFinal(Arrays.copyOfRange(buf, r - 16, r)));
        }

        // erasing cryptographic parameters and closing streams
        GPCrypto.eraseByteArrays(header, S1, S2, N1, N2, R);
        GPCrypto.sanitize(buf, 1000);
        GPCrypto.eraseKeys(K1, K2);
        GPCrypto.sanitize(pass);
        output.close();
    }
}
