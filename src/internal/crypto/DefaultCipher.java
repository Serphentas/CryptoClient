package internal.crypto;

import internal.Settings;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Contains placeholder encryption/decrypt methods, which subsequently call the
 * appropriate methods in other classes according to the encryption scheme
 * version
 * <p>
 * This allows us to maintain backward compatibility for decryption and lets the
 * user choose any encryption scheme, with the most recent being used by
 * default.
 *
 * @author xerxes
 */
public abstract class DefaultCipher {

    private static char[] encPass;
    private static DataOutputStream dos;
    private static DataInputStream dis;

    /**
     * Sets the encryption password
     *
     * @param pass encryption password
     */
    public static void setEncryptionPassword(char[] pass) {
        encPass = pass;
    }

    /**
     * Returns the encryption password
     * <p>
     * Strictly used by *Cipher classes in the internal.crypto package
     *
     * @return
     */
    protected static char[] getEncryptionPassword() {
        return encPass.clone();
    }

    public static void init(DataOutputStream dos, DataInputStream dis) {
        DefaultCipher.dos = dos;
        DefaultCipher.dis = dis;
    }

    /**
     * Encrypt a given file
     *
     * @param input file to encrypt
     * @return
     * @throws java.io.IOException if an I/O error occurs
     * @throws java.security.InvalidKeyException if the given key is
     * inappropriate for initializing the cipher
     * @throws java.security.InvalidAlgorithmParameterException if the given
     * algorithm parameters are inappropriate for the cipher
     * @throws javax.crypto.BadPaddingException if the cipher is in decryption
     * mode, and (un)padding has been requested, but the decrypted data is not
     * bounded by the appropriate padding bytes
     * @throws javax.crypto.IllegalBlockSizeException if the cipher is a block
     * cipher, no padding has been requested (only in encryption mode), and the
     * total input length of the data processed by this cipher is not a multiple
     * of block size; or if this encryption algorithm is unable to process the
     * input data provided.
     * @throws java.lang.ClassNotFoundException if the javax.crypto.JceSecurity
     * class cannot be located while removing the 128 bits key size limit
     * @throws java.lang.NoSuchFieldException if the field "isRestricted" in the
     * class javax.crypto.JceSecurity is not found while removing the 128 bits
     * key size limit
     * @throws java.lang.IllegalAccessException if the "isRestricted" Field
     * object from the javax.crypto.JceSecurity class is enforcing Java language
     * access control and the underlying field is either inaccessible or final,
     * while removing the 128 bits key size limit
     * @throws java.security.NoSuchAlgorithmException if the specified cipher is
     * not available
     * @throws java.security.NoSuchProviderException if Bouncy Castle cannot be
     * used
     * @throws javax.crypto.NoSuchPaddingException if the padding is invalid
     */
    public static boolean encrypt(File input) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, ClassNotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
        boolean reply = false;
        dos.writeLong(input.length());

        switch (Settings.getVersion()) {
            case 0x00:
                GCMCipher gcm = new GCMCipher(dos, dis);
                GCMCipher.setK1N(Settings.getK1_N());
                GCMCipher.setK2N(Settings.getK2_N());
                reply = gcm.encrypt_V00(input);
                break;
        }

        return reply;
    }

    /**
     * Decrypts a given file rypt
     *
     * @param output output file
     * @return
     *
     * @throws java.io.IOException if an I/O error occurs
     * @throws java.security.InvalidKeyException if the given key is
     * inappropriate for initializing the cipher
     * @throws java.security.InvalidAlgorithmParameterException if the given
     * algorithm parameters are inappropriate for the cipher
     * @throws javax.crypto.BadPaddingException if the cipher is in decryption
     * mode, and (un)padding has been requested, but the decrypted data is not
     * bounded by the appropriate padding bytes
     * @throws javax.crypto.IllegalBlockSizeException if the cipher is a block
     * cipher, no padding has been requested (only in encryption mode), and the
     * total input length of the data processed by this cipher is not a multiple
     * of block size; or if this encryption algorithm is unable to process the
     * input data provided.
     * @throws java.lang.ClassNotFoundException if the javax.crypto.JceSecurity
     * class cannot be located while removing the 128 bits key size limit
     * @throws java.lang.NoSuchFieldException if the field "isRestricted" in the
     * class javax.crypto.JceSecurity is not found while removing the 128 bits
     * key size limit
     * @throws java.lang.IllegalAccessException if the "isRestricted" Field
     * object from the javax.crypto.JceSecurity class is enforcing Java language
     * access control and the underlying field is either inaccessible or final,
     * while removing the 128 bits key size limit
     * @throws java.security.NoSuchAlgorithmException if the specified cipher is
     * not available
     * @throws java.security.NoSuchProviderException if Bouncy Castle cannot be
     * used
     * @throws javax.crypto.NoSuchPaddingException if the padding is invalid
     */
    public static boolean decrypt(File output) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, ClassNotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
        final byte VERSION = (byte) dis.read();
        boolean reply = false;

        switch (VERSION) {
            case 0x00:
                GCMCipher gcm = new GCMCipher(dos, dis);
                reply = gcm.decrypt_V00(output);
        }

        return reply;
    }

}
