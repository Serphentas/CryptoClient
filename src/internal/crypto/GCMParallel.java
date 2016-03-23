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

/**
 * Implementation of Runnable that allows parallelized encryption/decryption of
 * multiple files using GCMCipher
 * <p>
 * It actually instantiates a GCMCipher per file and then calls its
 * encrypt/decrypt method upon that.
 *
 * @author Serphentas
 */
public final class GCMParallel implements Runnable {

    private final File input;
    private final int mode;

    private GCMParallel(File input, int mode) {
        this.input = input;
        this.mode = mode;
    }

    /**
     * Encrypts each file in the array in parallel
     *
     * @param input File array to process
     */
    public static void encryptParallel(File[] input) {
        for (File f : input) {
            new Thread(new GCMParallel(f, 0)).start();
        }
    }

    /**
     * Decrypts each file in the array in parallel
     *
     * @param input File array to process
     */
    public static void decryptParallel(File[] input) {
        for (File f : input) {
            new Thread(new GCMParallel(f, 1)).start();
        }
    }

    @Override
    public void run() {
        try {
            GCMCipher gcmc = new GCMCipher();
            if (mode == 0) {
                gcmc.encrypt(input);
            } else {
                //gcmc.decrypt(input);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
