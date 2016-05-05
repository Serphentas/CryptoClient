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
            //GCMCipher gcmc = new GCMCipher();
            if (mode == 0) {
                //gcmc.encrypt(input);
            } else {
                //gcmc.decrypt(input);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
