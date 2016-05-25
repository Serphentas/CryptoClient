/* 
 * Copyright (c) 2016, Serphentas
 * All rights reserved.
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/ or send a letter
 * to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
package org.gity.internal;

/**
 * Provides means for getting and setting settings, used in the GUI
 *
 * @author Serphentas
 */
public abstract class Settings {

    private static byte version = 0x00;
    private static int K1_N = 20, K2_N = 19;
    private static String DLDir = null;
    private static boolean isDLDIR = false,
            isWorking = false,
            isNew,
            TOSAgreed = false;

    public static void setVersion(byte version) {
        Settings.version = version;
    }

    public static byte getVersion() {
        return version;
    }

    public static void setK1_N(int newVal) {
        K1_N = newVal;
    }

    public static void setK2_N(int newVal) {
        K2_N = newVal;
    }

    public static int getK1_N() {
        return K1_N;
    }

    public static int getK2_N() {
        return K2_N;
    }

    public static String getDLDir() {
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

    public static boolean isWorking() {
        return isWorking;
    }

    public static void setIsNew(boolean newVal) {
        isNew = newVal;
    }

    public static boolean isNew() {
        return isNew;
    }

    public static void setTOSAgreed(boolean newVal) {
        TOSAgreed = newVal;
    }

    public static boolean isTOSAgreed() {
        return TOSAgreed;
    }

}
