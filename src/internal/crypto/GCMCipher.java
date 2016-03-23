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
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
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
    private static final String CIPHER = "AES/GCM/NoPadding";
    private static final String CRYPTO_PROVIDER = "BC";
    private static final int CIPHER_KEY_BITS = 256;
    private static final int GCM_NONCE_BYTES = 12;
    private static final int GCM_TAG_BITS = 128;
    private static final int KDF_CPU_RAM_COST = 10;
    private static final int KDF_PARALLEL = 10;
    private static final int KDF_BLOCK_SIZE = 10;

    private final Cipher cipher;
    private byte[] nonce = null;
    private byte[] salt = null;
    private SecretKey key = null;

    public long time;

    /**
     * Instantiates a Cipher, allowing subsequent use for encryption and
     * decryption of an arbitrary file
     *
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

        // generating key and nonce-
        this.salt = GPCrypto.randomGen(128);
        this.key = new SecretKeySpec(SCrypt.generate(nonce, this.salt, KDF_CPU_RAM_COST, GCM_TAG_BITS, KDF_PARALLEL, GCM_TAG_BITS), 0, 12, "AES");
        this.nonce = SCrypt.generate(GPCrypto.randomGen(128), GPCrypto.randomGen(128), KDF_CPU_RAM_COST, KDF_BLOCK_SIZE, KDF_PARALLEL, GCM_NONCE_BYTES);

        // cipher initialization
        this.cipher.init(Cipher.ENCRYPT_MODE, this.key, new GCMParameterSpec(
                GCM_TAG_BITS, this.nonce, 0, GCM_NONCE_BYTES));

        output.write(salt);
        output.write(key.getEncoded(), salt.length - 1, CIPHER_KEY_BITS / 8);

        // finishing the encryption job
        //new Object[]{this.salt, this.nonce, new CipherInputStream(
        //  new FileInputStream(input), this.cipher)};
    }

    /**
     * Decrypts a given file with AES-256 in GCM mode of operation
     *
     * @param input file to be decrypted
     * @param is
     * @return
     * @throws Exception
     */
    public CipherInputStream decrypt(File input, InputStream is) throws Exception {
        // read key and nonce
        this.nonce = Files.readAllBytes(new File(input.getPath().substring(0,
                input.getPath().length() - 10) + ".nonce").toPath());
        final File keyFile = new File(input.getPath().substring(0,
                input.getPath().length() - 10) + ".key");
        this.key = new SecretKeySpec(Files.readAllBytes(keyFile.toPath()), 0,
                Files.readAllBytes(keyFile.toPath()).length, "AES");

        // GCMCipher initialization
        this.cipher.init(Cipher.DECRYPT_MODE, this.key, new GCMParameterSpec(
                GCM_TAG_BITS, this.nonce, 0, GCM_NONCE_BYTES));

        return new CipherInputStream(is, this.cipher);
    }

    /**
     * Performs the actual encryption/decryption based on the state of the
     * Cipher
     *
     * @return encrypted/decrypted version of the input file
     * @throws Exception
     */
    private void finishJob() throws Exception {
        this.time = System.nanoTime();

        // defining the CIS to write to
        //this.is.close();
        //this.key.destroy();
    }

    public static void eraseParams() {
        GPCrypto.sanitize(salt, GCM_TAG_BITS);
    }
}
