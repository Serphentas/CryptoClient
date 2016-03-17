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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

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
    private static final int GCM_NONCE_BYTES = 16;
    private static final int GCM_TAG_BITS = 128;
    private static final int BUFFER_SIZE = 1024;

    private final byte[] buffer = new byte[BUFFER_SIZE];
    private boolean isModeEncrypt;
    private final Cipher cipher;
    private byte[] nonce = null;
    private SecretKey key = null;
    private File input = null;
    private File output = null;
    private CipherInputStream cis;
    private CipherOutputStream cos;
    private InputStream is;
    private OutputStream os;

    public long time;

    /**
     * Instantiates a Cipher, allowing subsequent use for encryption and
     * decryption of an arbitrary file
     *
     * @throws Exception
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
     *
     * @param input file to be encrypted
     * @throws Exception
     */
    public void encrypt(File input) throws Exception {
        /*
        setting the input file so that the finishJob method will be able to
        encrypt the current file (and not one that has been set by a
        previous operation)
         */
        this.input = input;

        // generating nonce & key and storing them
        this.nonce = GPCrypto.randomGen(GCM_NONCE_BYTES);
        this.key = keyGen();
        storeNonceAndKey();

        // cipher initialization
        this.isModeEncrypt = true;
        this.cipher.init(Cipher.ENCRYPT_MODE, this.key, new GCMParameterSpec(
                GCM_TAG_BITS, this.nonce, 0, GCM_NONCE_BYTES));

        // output file creation
        this.output = new File(input.getPath() + ".encrypted");

        // finishing the encryption job
        finishJob();
    }

    /**
     * Decrypts a given file with AES-256 in GCM mode of operation
     *
     * @param input file to be decrypted
     * @throws Exception
     */
    public void decrypt(File input) throws Exception {
        /*
        setting the input file so that the finishJob method will be able to
        decrypt the current file (and not one that has been set by a
        previous operation)
         */
        this.input = input;

        // read key and nonce
        this.nonce = Files.readAllBytes(new File(input.getPath().substring(0,
                input.getPath().length() - 10) + ".nonce").toPath());
        final File keyFile = new File(input.getPath().substring(0,
                input.getPath().length() - 10) + ".key");
        this.key = new SecretKeySpec(Files.readAllBytes(keyFile.toPath()), 0,
                Files.readAllBytes(keyFile.toPath()).length, "AES");

        // GCMCipher initialization
        this.isModeEncrypt = false;
        this.cipher.init(Cipher.DECRYPT_MODE, this.key, new GCMParameterSpec(
                GCM_TAG_BITS, this.nonce, 0, GCM_NONCE_BYTES));

        // defining output file
        this.output = new File(input.getPath().substring(0, input.getPath().
                length() - 10) + ".decrypted");

        // finishing the decryption job
        finishJob();
    }

    /**
     * Performs the actual encryption/decryption based on the state of the
     * Cipher
     * <p>
     * If the file is bigger than 1MiB, it is streamed into the Cipher using a
     * buffer so as to not waste memory unnecessarily. If the file is smaller or
     * equal to 1MiB, it is loaded into memory in whole for faster processing
     * (generally 10x).
     *
     * @return encrypted/decrypted version of the input file
     * @throws Exception
     */
    private void finishJob() throws Exception {
        this.time = System.nanoTime();

        // creating the output file and defining its related OS
        this.output.createNewFile();
        this.os = new FileOutputStream(this.output.getPath());

        if (this.input.length() > Math.pow(2, 20)) {
            int r;

            if (this.isModeEncrypt) {
                // defining the IS to read from and the COS to write to
                this.is = new FileInputStream(this.input);
                this.cos = new CipherOutputStream(this.os, this.cipher);

                // performing the actual encryption
                while ((r = this.is.read(buffer)) > 0) {
                    this.cos.write(buffer, 0, r);
                }

                this.cos.close();
            } else {
                // defining the IS to read from and the CIS to write to
                this.is = new FileInputStream(this.input);
                this.cis = new CipherInputStream(this.is, this.cipher);

                // performing the actual decryption
                while ((r = this.cis.read(buffer)) > 0) {
                    this.os.write(buffer, 0, r);
                }

                this.cis.close();
            }
        } else {
            this.os.write(this.cipher.doFinal(Files.readAllBytes(this.input.toPath())));
        }

        // sanitizing the nonce to prevent extraction from RAM
        GPCrypto.sanitize(this.nonce, 1000);

        this.os.close();
        this.is.close();

        System.out.println("Done with " + input.getName());
        //this.key.destroy();
    }

    /**
     * Generates a random key to be used by the cipher
     *
     * @return random SecretKey, ready for use by the cipher
     * @throws Exception
     */
    private SecretKey keyGen() throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance("AES", "BC");
        keygen.init(CIPHER_KEY_BITS, new SecureRandom());
        return keygen.generateKey();
    }

    /**
     * Stores the nonce and the key used by the cipher into a database, so that
     * later decryption is possible due to the random nature of these two
     * components.
     *
     * @throws Exception
     */
    private void storeNonceAndKey() throws Exception {
        File noncef = new File(this.input.getPath() + ".nonce");
        File keyf = new File(this.input.getPath() + ".key");
        noncef.createNewFile();
        keyf.createNewFile();
        this.os = new FileOutputStream(noncef.getPath());
        this.os.write(this.nonce);
        this.os = new FileOutputStream(keyf.getPath());
        this.os.write(this.key.getEncoded());
    }
}
