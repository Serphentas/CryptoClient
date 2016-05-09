/* 
 * Copyright (c) 2016, Serphentas
 * All rights reserved.
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/ or send a letter
 * to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
package internal;

/**
 * Provides means for getting and setting settings, used in the GUI
 *
 * @author Serphentas
 */
public abstract class Settings {

    /*private static boolean parallelCrypto = false;
    private static int parallelCryptoThreads = 2;*/
    private static String DLDir = null;
    private static boolean isDLDIR = false;
    private static boolean isWorking = false;

    /*public static boolean isParallelCrypto() {
        return parallelCrypto;
    }

    public static void setParallelCrypto(boolean newParallelCrypto) {
        parallelCrypto = newParallelCrypto;
    }

    public static int getParallelCryptoThreads() {
        return parallelCryptoThreads;
    }

    public static void setParallelCryptoThreads(int newParallelCryptoThreads) {
        parallelCryptoThreads = newParallelCryptoThreads;
    }*/
    public static String getDLDIR() {
        return DLDir;
    }

    public static void setDLDir(String newDir) {
        DLDir = newDir;
        isDLDIR = true;
    }

    public static boolean isDLDir() {
        return isDLDIR;
    }

    public static void setIsDLDIR(boolean newVal) {
        isDLDIR = newVal;
    }

    public static void setIsWorking(boolean newVal) {
        isWorking = newVal;
    }
    
    public static boolean isWorking(){
        return isWorking;
    }
}
