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
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.SecureRandom;
import org.bouncycastle.crypto.generators.SCrypt;

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
     * Fills a byte array with random values to prevent future retrieval of its
     * original state
     *
     * @param array byte array to sanitize
     * @param passCount number of passes to make
     */
    public static void sanitize(byte[] array, int passCount) {
        for (int i = 0; i < passCount; i++) {
            rand.nextBytes(array);
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

     * @param password
     *      password to derive key from
     * @return
     *      key derived from supplied password
     * @throws java.lang.Exception
     */
    public static byte[] hashSCrypt(String password) throws Exception{
        return SCrypt.generate(password.getBytes("UTF-8"), null, 1024, 128, 4, 256);
    }
}
