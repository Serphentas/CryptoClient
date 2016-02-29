/* 
 * Copyright (C) 2016 Dreadlockyx
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
package visual;

import internal.crypto.GCMCipher;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javax.swing.JFileChooser;
import internal.file.FileHandler;
import java.awt.FileDialog;
import java.io.FileNotFoundException;
import javax.swing.UIManager;

/**
 *
 * @author Dreadlockyx
 */
public class DefaultFrame extends javax.swing.JFrame {

    private FileDialog fd;
    private GCMCipher gcmc;

    /**
     * Creates new form defaultFrame
     *
     * @throws java.lang.Exception
     */
    public DefaultFrame() throws Exception {
        gcmc = new GCMCipher();
        initComponents();
        setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        errorDialog = new javax.swing.JDialog();
        errorDialogButton = new javax.swing.JButton();
        errorDialogLabel = new javax.swing.JLabel();
        actionLog = new javax.swing.JScrollPane();
        actionLogTextArea = new javax.swing.JTextArea();
        encryptButton = new javax.swing.JButton();
        decryptButton = new javax.swing.JButton();
        actionLogLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openButton = new javax.swing.JMenuItem();
        exitButton = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        preferencesButton = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        benchmarkButton = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();

        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setApproveButtonText("");
        fileChooser.setApproveButtonToolTipText("");
        fileChooser.setBackground(java.awt.Color.black);
        fileChooser.setDialogTitle("Open file");
        fileChooser.setFont(new java.awt.Font("Consolas", 0, 10)); // NOI18N
        fileChooser.setMinimumSize(new java.awt.Dimension(1024, 1024));
        fileChooser.setPreferredSize(new java.awt.Dimension(1024, 1024));

        errorDialog.setTitle("Error");
        errorDialog.setIconImage(null);
        errorDialog.setLocation(new java.awt.Point(0, 0));
        errorDialog.setResizable(false);
        errorDialog.setSize(new java.awt.Dimension(200, 130));
        errorDialog.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                errorDialogComponentShown(evt);
            }
        });

        errorDialogButton.setText("OK");
        errorDialogButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        errorDialogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                errorDialogButtonActionPerformed(evt);
            }
        });

        errorDialogLabel.setText("Error: no file selected");
        errorDialogLabel.setToolTipText("");

        javax.swing.GroupLayout errorDialogLayout = new javax.swing.GroupLayout(errorDialog.getContentPane());
        errorDialog.getContentPane().setLayout(errorDialogLayout);
        errorDialogLayout.setHorizontalGroup(
            errorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(errorDialogLayout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(errorDialogButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, errorDialogLayout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addComponent(errorDialogLabel)
                .addGap(49, 49, 49))
        );
        errorDialogLayout.setVerticalGroup(
            errorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(errorDialogLayout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(errorDialogLabel)
                .addGap(18, 18, 18)
                .addComponent(errorDialogButton)
                .addGap(29, 29, 29))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CryptoClient");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocation(new java.awt.Point(100, 100));
        setMaximumSize(new java.awt.Dimension(400, 302));
        setResizable(false);

        actionLogTextArea.setEditable(false);
        actionLogTextArea.setColumns(20);
        actionLogTextArea.setRows(5);
        actionLogTextArea.setFocusable(false);
        actionLog.setViewportView(actionLogTextArea);

        encryptButton.setText("Encrypt");
        encryptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encryptButtonActionPerformed(evt);
            }
        });

        decryptButton.setText("Decrypt");
        decryptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decryptButtonActionPerformed(evt);
            }
        });

        actionLogLabel.setText("Action log");

        fileMenu.setText("File");

        openButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openButton.setText("Open");
        openButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });
        fileMenu.add(openButton);

        exitButton.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        fileMenu.add(exitButton);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");
        editMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editMenuActionPerformed(evt);
            }
        });

        preferencesButton.setText("Preferences");
        preferencesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preferencesButtonActionPerformed(evt);
            }
        });
        editMenu.add(preferencesButton);

        menuBar.add(editMenu);

        toolsMenu.setText("Tools");

        benchmarkButton.setText("Benchmark");
        benchmarkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                benchmarkButtonActionPerformed(evt);
            }
        });
        toolsMenu.add(benchmarkButton);

        menuBar.add(toolsMenu);

        helpMenu.setText("Help");
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(actionLog)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(encryptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(decryptButton))
                            .addComponent(actionLogLabel))
                        .addGap(12, 345, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(decryptButton)
                    .addComponent(encryptButton))
                .addGap(59, 59, 59)
                .addComponent(actionLogLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(actionLog, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_exitButtonActionPerformed

    private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
        fd = new FileDialog(this, "Open file", FileDialog.LOAD);
        fd.setMultipleMode(true);
        fd.setVisible(true);
    }//GEN-LAST:event_openButtonActionPerformed


    private void encryptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encryptButtonActionPerformed
        try {
            if (fd.getFiles().length != 0) {
                for (File f : fd.getFiles()) {
                    gcmc.encrypt(f);
                    actionLogTextArea.append("Done encrypting " + f.getName() + "\n");
                }
            } else {
                errorDialogLabel.setText("No file specified");
                errorDialog.setVisible(true);
            }
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                errorDialogLabel.setText("No file specified");
            } else {
                errorDialogLabel.setText("Unexpected error:\n");
                actionLogTextArea.append(e.toString()+"\n");
            }
            errorDialog.setVisible(true);
        }

    }//GEN-LAST:event_encryptButtonActionPerformed

    private void benchmarkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_benchmarkButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_benchmarkButtonActionPerformed

    private void errorDialogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_errorDialogButtonActionPerformed
        errorDialog.setVisible(false);
    }//GEN-LAST:event_errorDialogButtonActionPerformed

    private void errorDialogComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_errorDialogComponentShown

    }//GEN-LAST:event_errorDialogComponentShown

    private void decryptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decryptButtonActionPerformed
        try {
            if (fd.getFiles().length != 0) {
                for (File f : fd.getFiles()) {
                    if(f.getName().endsWith(".encrypted")){
                        gcmc.decrypt(f);
                        actionLogTextArea.append("Done decrypting " + f.getName() + "\n");  
                    } else {
                        errorDialogLabel.setText("Wrong encrypted file");
                        errorDialog.setVisible(true);
                    }
                }
            } else {
                actionLogTextArea.setText("Error");
            }
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                errorDialogLabel.setText("No file specified");
            } else {
                actionLogTextArea.append(e.toString()+"\n");
                errorDialogLabel.setText("Unexpected error");
            }
            errorDialog.setVisible(true);
        }
    }//GEN-LAST:event_decryptButtonActionPerformed

    private void editMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editMenuActionPerformed

    }//GEN-LAST:event_editMenuActionPerformed

    private void preferencesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preferencesButtonActionPerformed

    }//GEN-LAST:event_preferencesButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DefaultFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DefaultFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DefaultFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DefaultFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new DefaultFrame().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(DefaultFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane actionLog;
    private javax.swing.JLabel actionLogLabel;
    private javax.swing.JTextArea actionLogTextArea;
    private javax.swing.JMenuItem benchmarkButton;
    private javax.swing.JButton decryptButton;
    private javax.swing.JMenu editMenu;
    private javax.swing.JButton encryptButton;
    private javax.swing.JDialog errorDialog;
    private javax.swing.JButton errorDialogButton;
    private javax.swing.JLabel errorDialogLabel;
    private javax.swing.JMenuItem exitButton;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openButton;
    private javax.swing.JMenuItem preferencesButton;
    private javax.swing.JMenu toolsMenu;
    // End of variables declaration//GEN-END:variables
}
