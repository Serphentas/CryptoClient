/* 
 * Copyright (c) 2016, Serphentas
 * All rights reserved.
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/ or send a letter
 * to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
package org.gity.visual;

import java.awt.TrayIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class ErrorHandler {

    public static void showError(Exception ex) {
        setUIStyle();
        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()
                + "\nPlease contact support.", "Error", TrayIcon.MessageType.ERROR.ordinal());
        ex.printStackTrace();
    }

    public static void showError(String message) {
        setUIStyle();
        JOptionPane.showMessageDialog(null, "Error: " + message,
                "Error", TrayIcon.MessageType.ERROR.ordinal());
    }

    private static void setUIStyle() {
        try {
            javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(null, "Error initializing UI style: "
                    + e.getMessage() + "\nPlease contact support.", "Error",
                    TrayIcon.MessageType.ERROR.ordinal());
        }
    }
} 
