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
    private static String dlDir = null;
    private static boolean isDlDir = false;
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
    public static String getWorkingDir() {
        return dlDir;
    }

    public static void setWorkingDir(String newDir) {
        dlDir = newDir;
        isDlDir = true;
    }

    public static boolean isDlDir() {
        return isDlDir;
    }

    public static void setIsDlDir(boolean newVal) {
        isDlDir = newVal;
    }

    public static void setIsWorking(boolean newVal) {
        isWorking = newVal;
    }
    
    public static boolean isWorking(){
        return isWorking;
    }
}
