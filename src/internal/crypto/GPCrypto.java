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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.SecretKey;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.util.encoders.Hex;

/**
 * General purpose cryptographic class
 * <p>
 * Methods which are used for cryptographic yet general purpose are defined
 * here, so that the GCMCipher class only contains encryption/decryption-related
 * code.
 *
 * @author Serphentas
 */
public abstract class GPCrypto {

    private static final SecureRandom rand = new SecureRandom();
    private static final int KDF_N = (int) Math.pow(2, 22),
            KDF_r = 8,
            KDF_p = 1,
            SANITIZATION_COUNT = 10000;

    /**
     * Generates a random array of bytes
     *
     * @param size width of the byte array
     * @return a random array of bytes, of length size
     */
    public static byte[] randomGen(int size) {
        byte[] randBytes = new byte[size];
        rand.nextBytes(randBytes);
        return randBytes;
    }

    /**
     * Fills a byte array 10'000 times with random values to prevent future
     * retrieval of its original state
     *
     * @param array byte array to sanitize
     */
    public static void sanitize(byte[] array) {
        for (int i = 0; i < SANITIZATION_COUNT; i++) {
            rand.nextBytes(array);
        }
    }

    /**
     * Fills a char array 10'000 times with random values to prevent future
     * retrieval of its original state
     *
     * @param c char array to sanitize
     */
    public static void sanitize(char[] c) {
        for (int i = 0; i < SANITIZATION_COUNT; i++) {
            Arrays.fill(c, (char) rand.nextInt());
        }
    }

    /**
     * Overwrites a file with random bytes to prevent future retrieval of its
     * original state
     *
     * @param input file to sanitize
     * @param passCount number of passes to make
     * @throws Exception
     */
    public static void sanitize(File input, int passCount) throws Exception {
        final FileOutputStream fos = new FileOutputStream(input);

        if (input.length() > Math.pow(2, 20)) {
            final FileInputStream fis = new FileInputStream(input);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, r);
            }
            fis.close();
        } else {
            for (int i = 0; i < passCount; i++) {
                rand.nextBytes(Files.readAllBytes(input.toPath()));
            }
        }
        fos.close();
    }

    /**
     * Overwrites many byte arrays 10'000 times
     *
     * @param arrays arrays to overwrite
     */
    public static void eraseByteArrays(byte[]... arrays) {
        for (byte[] array : arrays) {
            sanitize(array);
        }
    }

    /**
     * Overwrites many SecretKey objects 10'000 times
     *
     * @param keys SecretKey objects to overwrite
     */
    public static void eraseKeys(SecretKey... keys) {
        for (SecretKey key : keys) {
            GPCrypto.sanitize(key.getEncoded());
        }
    }

    /**
     * Hashes a given string with SHA384
     * <p>
     * The input is read as a byte array, using UTF-8 as character encoding.
     *
     * @param input message
     * @return message digest
     * @throws Exception
     */
    public static byte[] SHA384(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA384", "BC");
        return md.digest(input.getBytes("UTF-8"));
    }

    /**
     * Derives a given string (usually a password) with scrypt, using the
     * following default values:
     * <ul>
     * <li>N=2^19</li>
     * <li>p=8</li>
     * <li>r=4</li>
     * </ul>
     *
     * @param password secret value to derive the key from
     * @param salt salt to use for this invocation
     * @param dkLen output size, in bytes
     * @return key derived from supplied password
     * @throws java.io.UnsupportedEncodingException
     */
    public static byte[] scrypt(char[] password, byte[] salt, int dkLen) throws UnsupportedEncodingException {
        byte[] passBytes = charToByte(password),
                digest = SCrypt.generate(passBytes, salt, KDF_N, KDF_r, KDF_p, dkLen);
        sanitize(passBytes);
        return digest;
    }

    /**
     * Converts a char array into a byte array, using UTF-8 as the encoding
     * charset
     * <p>
     * Note: does not change the input char array by processing a clone of it
     *
     *
     * @param c char array to convert
     * @return byte array representation of the char array
     */
    public static byte[] charToByte(char[] c) {
        CharBuffer charBuffer = CharBuffer.wrap(c.clone());
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        sanitize(charBuffer.array());
        sanitize(byteBuffer.array());
        return bytes;
    }

    /**
     * Retrieves the SCrypt parameters from a formatted digest in the following
     * format:
     * <p>
     * N$r$p$salt$digest
     *
     * @param digest
     * @return the components of the SCrypt digest:
     * <ol>
     * <li>hash</li>
     * <li>salt</li>
     * <li>N</li>
     * <li>r</li>
     * <li>p</li>
     * <li>hash size, in bytes</li>
     * </ol>
     */
    public static Object[] decodeDigest(String digest) {
        String tmp;
        int N = Integer.parseInt(digest.substring(0, digest.indexOf("$")));
        tmp = digest.substring(digest.indexOf("$") + 1, digest.length());
        int r = Integer.parseInt(tmp.substring(0, tmp.indexOf("$")));
        tmp = tmp.substring(tmp.indexOf("$") + 1, tmp.length());
        int p = Integer.parseInt(tmp.substring(0, tmp.indexOf("$")));
        tmp = tmp.substring(tmp.indexOf("$") + 1, tmp.length());
        String salt = tmp.substring(0, tmp.indexOf("$"));
        tmp = tmp.substring(tmp.indexOf("$") + 1, tmp.length());

        return new Object[]{tmp, salt, N, r, p, tmp.length() / 2};
    }

    /**
     * Formats the hash in the following format:
     * <p>
     * N$r$p$salt$digest
     *
     * @param hash
     * @param salt
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static String encodeDigest(byte[] hash, byte[] salt) throws UnsupportedEncodingException {
        return KDF_N + "$" + KDF_r + "$" + KDF_p + "$" + Hex.toHexString(salt) + "$" + Hex.toHexString(hash);
    }

    /**
     * Derives a key, i.e. a hash, from a given password, using the following
     * scrypt parameters:
     * <ul>
     * <li>salt=random 32B</li>
     * <li>N=2^19</li>
     * <li>r=8</li>
     * <li>p=4</li>
     *
     * </ul>
     *
     * @param pass
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String deriveKey(char[] pass) throws UnsupportedEncodingException {
        byte[] salt = GPCrypto.randomGen(32);
        return encodeDigest(scrypt(pass, salt, 32), salt);
    }
}
