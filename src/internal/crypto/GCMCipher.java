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

import internal.network.NTP;
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
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.util.encoders.Hex;

/**
 * General purpose class used for encryption and decryption of files, using
 * AES-256 in GCM mode of operation.
 * <p>
 * <b><i>This class is not thread-safe.</i></b>
 *
 * @author Serphentas
 */
public final class GCMCipher {

    private static final String CIPHER = "AES/GCM/NoPadding",
            CRYPTO_PROVIDER = "BC";
    private static final int CIPHER_KEY_BITS = 256,
            GCM_NONCE_BYTES = 12,
            GCM_TAG_BITS = 128,
            S_BYTES = 64,
            R_BYTES = 64,
            BUFFER_SIZE = 1024,
            KDF_r = 8,
            KDF_p = 1,
            VS1 = S_BYTES,
            S1N1 = VS1 + GCM_NONCE_BYTES,
            N1K1N = S1N1 + 1,
            K1NR = N1K1N + R_BYTES + GCM_TAG_BITS / 8,
            RS2 = K1NR + S_BYTES,
            S2N2 = RS2 + GCM_NONCE_BYTES,
            N2K2N = S2N2 + 1;
    private static int K1_KDF_N = 20,
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
        GCMCipher.K1_KDF_N = N;
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
        GCMCipher.K2_KDF_N = N;
    }

    private final DataOutputStream dos;
    private final DataInputStream dis;
    private final OutputStream os;

    /**
     * Instantiates a Cipher object using AES-256 in GCM mode of operation,
     * allowing subsequent use for file encryption and decryption
     *
     * @param dis
     * @param dos
     *
     */
    protected GCMCipher(DataOutputStream dos, DataInputStream dis, OutputStream os) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
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
        this.cipher = Cipher.getInstance(CIPHER);

        // settings the I/O streams
        this.dos = dos;
        this.dis = dis;
        this.os = os;
    }

    /**
     * Encrypts a given file with AES-256 in GCM mode of operation
     * <p>
     * Reads data from the InputStream and writes the encrypted data to the
     * OutputStream
     *
     * @param inputFile
     * @return
     * @throws java.io.IOException
     * @throws java.security.InvalidKeyException
     * @throws java.security.InvalidAlgorithmParameterException
     * @throws javax.crypto.BadPaddingException
     * @throws javax.crypto.IllegalBlockSizeException
     */
    protected boolean encrypt_V00(File inputFile) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        // defining I/O streams
        InputStream input = new FileInputStream(inputFile);

        // getting the encryption password
        char[] pass = DefaultCipher.getEncryptionPassword();

        // generating Sx, Nx, R, Kx
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
        os.write((byte) 0x00);
        os.write(S1);
        os.write(N1);
        os.write(DatatypeConverter.parseHexBinary(Integer.toHexString(K1_KDF_N)));
        os.write(cipher.doFinal(R));
        os.write(S2);
        os.write(N2);
        os.write(DatatypeConverter.parseHexBinary(Integer.toHexString(K2_KDF_N)));

        // encrypting file
        this.cipher.init(Cipher.ENCRYPT_MODE, K2, new GCMParameterSpec(
                GCM_TAG_BITS, N2, 0, GCM_NONCE_BYTES));
        InputStream cis = new CipherInputStream(input, this.cipher);

        int r = 0;
        while ((r = cis.read(buf)) != -1) {
            os.write(buf, 0, r);
        }

        // erasing cryptographic parameters and closing streams
        GPCrypto.eraseByteArrays(S1, S2, epoch, N1, N2, R);
        GPCrypto.eraseKeys(K1, K2);
        GPCrypto.sanitize(pass);
        input.close();

        return dis.readBoolean();
    }
    
        /**
     * Decrypts a given file with AES-256 in GCM mode of operation
     *
     * @param outputFile
     * @return 
     * @throws java.io.IOException
     * @throws java.security.InvalidKeyException
     * @throws java.security.InvalidAlgorithmParameterException
     * @throws javax.crypto.BadPaddingException
     * @throws javax.crypto.IllegalBlockSizeException
     */
    protected boolean decrypt_V00(File outputFile) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        // defining I/O streams
        OutputStream output = new FileOutputStream(outputFile);

        // getting the encryption password
        char[] pass = DefaultCipher.getEncryptionPassword();

        // skipping version byte and reading header
        dis.skip(1);
        byte[] header = new byte[234];
        dis.read(header, 0, 234);

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
        InputStream cis = new CipherInputStream(dis, this.cipher);
        int r = 0;
        while ((r = cis.read(buf)) > 0) {
            output.write(buf, 0, r);
        }

        // erasing cryptographic parameters and closing streams
        GPCrypto.eraseByteArrays(header, S1, S2, N1, N2, R);
        GPCrypto.eraseKeys(K1, K2);
        GPCrypto.sanitize(pass);
        output.close();
        
        return dis.readBoolean();
    }

    /*public String decryptFilename(String fileName) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {

        // reading V, Sx and Nx
        byte[] header = new byte[553];
        dis.read(header, 0, 553);
        final byte V = header[0];
        final byte[] S1 = Arrays.copyOfRange(header, 1, V1S1),
                S2 = Arrays.copyOfRange(header, N1R, RS2),
                N1 = Arrays.copyOfRange(header, V1S1, S1N1),
                N2 = Arrays.copyOfRange(header, RS2, S2N2);

        // generating K1 and reading E(K1, N1, R)
        final SecretKey K1 = new SecretKeySpec(SCrypt.generate(GPCrypto.
                charToByte(pass), S1, KDF_N_K, KDF_p_K, KDF_r_K, CIPHER_KEY_BITS
                / 8), 0, CIPHER_KEY_BITS / 8, "AES");
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
    }*/
}
