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

import org.gity.internal.Settings;
import javax.swing.JFileChooser;
import javax.swing.UIManager;

/**
 *
 * @author Serphentas
 */
public class PreferencesFrame extends javax.swing.JFrame {

    /*private int newParallelCryptoThreadsCount;
    private boolean newIsParallelCrypto;*/
    private JFileChooser fc;
    private boolean isDlDir;

    /**
     * Creates new form PreferencesFrame
     */
    public PreferencesFrame() {
        initComponents();
        setLocationRelativeTo(null);
        loadParams();
    }

    /**
     * Sets the current settings into the Settings class
     */
    private void setParams() {
        // download directory settings
        if (isDlDir) {
            Settings.setDLDir(dlDirPath.getText());
        }
        Settings.setIsDLDIR(isDlDir);
    }

    /**
     * Load settings from the Settings class
     */
    private void loadParams() {
        // download directory settings
        if (Settings.isDLDir()) {
            dlDirPath.setText(Settings.getDLDir());
            dlDirPathButton.doClick();
        } else {
            dlDirAskButton.doClick();
        }
        isDlDir = Settings.isDLDir();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        workingDirChoice = new javax.swing.ButtonGroup();
        applyButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        OKButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        generalSettingsPanel = new javax.swing.JPanel();
        dlDirLabel = new javax.swing.JLabel();
        dlDirBrowseButton = new javax.swing.JButton();
        dlDirPath = new javax.swing.JTextField();
        dlDirPathButton = new javax.swing.JRadioButton();
        dlDirAskButton = new javax.swing.JRadioButton();
        dlDirAskLabel = new javax.swing.JLabel();
        languageLabel = new javax.swing.JLabel();
        languageChoice = new javax.swing.JComboBox<>();
        networkSettingsPanel = new javax.swing.JPanel();
        securitySettingsPannel = new javax.swing.JPanel();
        parallelCryptoChoiceLabel = new javax.swing.JLabel();
        parallelCryptoChoiceMenu = new javax.swing.JComboBox();
        parallelCryptoThreadsLabel = new javax.swing.JLabel();
        parallelCryptoThreadsMenu = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Preferences");
        setResizable(false);
        setSize(new java.awt.Dimension(570, 440));

        applyButton.setText("Apply");
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        OKButton.setText("OK");
        OKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKButtonActionPerformed(evt);
            }
        });

        dlDirLabel.setText("Download directory");

        dlDirBrowseButton.setText("Browse");
        dlDirBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlDirBrowseButtonActionPerformed(evt);
            }
        });

        workingDirChoice.add(dlDirPathButton);

        workingDirChoice.add(dlDirAskButton);
        dlDirAskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlDirAskButtonActionPerformed(evt);
            }
        });

        dlDirAskLabel.setText("Ask each time");

        languageLabel.setText("Language");

        languageChoice.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "English", "French" }));
        languageChoice.setEnabled(false);

        javax.swing.GroupLayout generalSettingsPanelLayout = new javax.swing.GroupLayout(generalSettingsPanel);
        generalSettingsPanel.setLayout(generalSettingsPanelLayout);
        generalSettingsPanelLayout.setHorizontalGroup(
            generalSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generalSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dlDirLabel)
                    .addComponent(languageLabel))
                .addGap(40, 40, 40)
                .addGroup(generalSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(languageChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(generalSettingsPanelLayout.createSequentialGroup()
                        .addComponent(dlDirPathButton)
                        .addGap(18, 18, 18)
                        .addComponent(dlDirPath, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(dlDirBrowseButton))
                    .addGroup(generalSettingsPanelLayout.createSequentialGroup()
                        .addComponent(dlDirAskButton)
                        .addGap(18, 18, 18)
                        .addComponent(dlDirAskLabel)))
                .addContainerGap())
        );
        generalSettingsPanelLayout.setVerticalGroup(
            generalSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generalSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(languageLabel)
                    .addComponent(languageChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(generalSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(generalSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dlDirPathButton, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(generalSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dlDirBrowseButton)
                            .addComponent(dlDirPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(generalSettingsPanelLayout.createSequentialGroup()
                        .addComponent(dlDirLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)))
                .addGap(6, 6, 6)
                .addGroup(generalSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dlDirAskButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dlDirAskLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(294, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("General", generalSettingsPanel);

        javax.swing.GroupLayout networkSettingsPanelLayout = new javax.swing.GroupLayout(networkSettingsPanel);
        networkSettingsPanel.setLayout(networkSettingsPanelLayout);
        networkSettingsPanelLayout.setHorizontalGroup(
            networkSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 595, Short.MAX_VALUE)
        );
        networkSettingsPanelLayout.setVerticalGroup(
            networkSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Network", networkSettingsPanel);

        parallelCryptoChoiceLabel.setText("Parallelized encryption/decryption");

        parallelCryptoChoiceMenu.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Yes", "No" }));
        parallelCryptoChoiceMenu.setSelectedIndex(1);
        parallelCryptoChoiceMenu.setToolTipText("Warning: uses a lot of memory !");
        parallelCryptoChoiceMenu.setEnabled(false);
        parallelCryptoChoiceMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parallelCryptoChoiceMenuActionPerformed(evt);
            }
        });
        parallelCryptoChoiceMenu.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                parallelCryptoChoiceMenuPropertyChange(evt);
            }
        });

        parallelCryptoThreadsLabel.setText("Parallel thread count");

        parallelCryptoThreadsMenu.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6" }));
        parallelCryptoThreadsMenu.setSelectedIndex(1);
        parallelCryptoThreadsMenu.setEnabled(false);
        parallelCryptoThreadsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parallelCryptoThreadsMenuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout securitySettingsPannelLayout = new javax.swing.GroupLayout(securitySettingsPannel);
        securitySettingsPannel.setLayout(securitySettingsPannelLayout);
        securitySettingsPannelLayout.setHorizontalGroup(
            securitySettingsPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(securitySettingsPannelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(securitySettingsPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(parallelCryptoChoiceLabel)
                    .addComponent(parallelCryptoThreadsLabel))
                .addGap(24, 24, 24)
                .addGroup(securitySettingsPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(parallelCryptoThreadsMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(parallelCryptoChoiceMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(349, Short.MAX_VALUE))
        );
        securitySettingsPannelLayout.setVerticalGroup(
            securitySettingsPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(securitySettingsPannelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(securitySettingsPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(parallelCryptoChoiceLabel)
                    .addComponent(parallelCryptoChoiceMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(securitySettingsPannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(parallelCryptoThreadsLabel)
                    .addComponent(parallelCryptoThreadsMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(334, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Security", securitySettingsPannel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(OKButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(applyButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancelButton))
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(applyButton)
                    .addComponent(cancelButton)
                    .addComponent(OKButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void parallelCryptoChoiceMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parallelCryptoChoiceMenuActionPerformed
        //newIsParallelCrypto = parallelCryptoChoiceMenu.getSelectedItem().toString().equals("Yes");
    }//GEN-LAST:event_parallelCryptoChoiceMenuActionPerformed

    private void parallelCryptoChoiceMenuPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_parallelCryptoChoiceMenuPropertyChange

    }//GEN-LAST:event_parallelCryptoChoiceMenuPropertyChange

    private void OKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKButtonActionPerformed
        setParams();
        this.dispose();
    }//GEN-LAST:event_OKButtonActionPerformed

    private void parallelCryptoThreadsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parallelCryptoThreadsMenuActionPerformed
        //newParallelCryptoThreadsCount = Integer.parseInt(parallelCryptoThreadsMenu.getSelectedItem().toString());
    }//GEN-LAST:event_parallelCryptoThreadsMenuActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
        setParams();
    }//GEN-LAST:event_applyButtonActionPerformed

    private void dlDirBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlDirBrowseButtonActionPerformed
        // open a FileChooser to select the download folder
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setMultiSelectionEnabled(false);
        int returnVal = fc.showDialog(this, "Open");

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            dlDirPath.setText(fc.getSelectedFile().getPath().replace("\\", "/"));
            dlDirPathButton.setSelected(true);
            isDlDir = true;
        }
    }//GEN-LAST:event_dlDirBrowseButtonActionPerformed

    private void dlDirAskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlDirAskButtonActionPerformed
        dlDirAskButton.setSelected(true);
        isDlDir = false;
    }//GEN-LAST:event_dlDirAskButtonActionPerformed

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
            java.util.logging.Logger.getLogger(PreferencesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PreferencesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PreferencesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PreferencesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new PreferencesFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton OKButton;
    private javax.swing.JButton applyButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JRadioButton dlDirAskButton;
    private javax.swing.JLabel dlDirAskLabel;
    private javax.swing.JButton dlDirBrowseButton;
    private javax.swing.JLabel dlDirLabel;
    private javax.swing.JTextField dlDirPath;
    private static javax.swing.JRadioButton dlDirPathButton;
    private javax.swing.JPanel generalSettingsPanel;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox<String> languageChoice;
    private javax.swing.JLabel languageLabel;
    private javax.swing.JPanel networkSettingsPanel;
    private javax.swing.JLabel parallelCryptoChoiceLabel;
    private javax.swing.JComboBox parallelCryptoChoiceMenu;
    private javax.swing.JLabel parallelCryptoThreadsLabel;
    private javax.swing.JComboBox parallelCryptoThreadsMenu;
    private javax.swing.JPanel securitySettingsPannel;
    private javax.swing.ButtonGroup workingDirChoice;
    // End of variables declaration//GEN-END:variables
}