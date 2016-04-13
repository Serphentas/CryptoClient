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
package visual;

import internal.Settings;
import internal.network.DataClient;
import java.awt.FileDialog;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author Serphentas
 */
public class DefaultFrame extends javax.swing.JFrame {

    private FileDialog fd;

    /**
     * Creates new form defaultFrame
     *
     * @throws java.io.IOException
     */
    public DefaultFrame() throws IOException {
        initComponents();
        setLocationRelativeTo(null);
        updateFileTable();
        updateLog("Connected to server");
    }

    /**
     * Updates the file table view, so that new files and folders are shown
     *
     * @throws IOException
     */
    public static void updateFileTable() throws IOException {
        FTPFile[] dirs = DataClient.listDirs(), files = DataClient.listFiles();
        DefaultTableModel dtm = (DefaultTableModel) fileTable.getModel();
        int i = 0;

        if (!DataClient.isAtRoot()) {
            dtm.setRowCount(files.length + 1);
            fileTable.setModel(dtm);
            fileTable.setValueAt("..", i, 0);
            fileTable.setValueAt("", i, 1);
            fileTable.setValueAt("Directory", i, 2);
            fileTable.setValueAt("", i, 3);
            i++;
        } else {
            dtm.setRowCount(files.length);
            fileTable.setModel(dtm);
        }

        for (FTPFile f : dirs) {
            fileTable.setValueAt(f.getName(), i, 0);
            fileTable.setValueAt(f.getTimestamp().getTime(), i, 1);
            fileTable.setValueAt("Directory", i, 2);
            fileTable.setValueAt("", i, 3);
            i++;
        }

        for (FTPFile f : files) {
            if (f.isFile()) {
                fileTable.setValueAt(f.getName(), i, 0);
                fileTable.setValueAt(f.getTimestamp().getTime(), i, 1);
                fileTable.setValueAt("File", i, 2);
                String size = null;
                if (f.getSize() / 1024 < 1024) {
                    size = f.getSize() / 1024 + " KiB";
                } else if (f.getSize() / 1048576 < 1024) {
                    size = f.getSize() / 1048576 + " MiB";
                } else if (f.getSize() / 1073741824 < 1024) {
                    size = f.getSize() / 1048576 + " GiB";
                }
                fileTable.setValueAt(size, i, 3);
                i++;
            }
        }
    }

    public static void updateLog(String message) {
        actionLogTextArea.append("[" + new Date() + "] " + message + "\n");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filePopupMenu = new javax.swing.JPopupMenu();
        download = new javax.swing.JMenuItem();
        separator0 = new javax.swing.JPopupMenu.Separator();
        move = new javax.swing.JMenuItem();
        separator1 = new javax.swing.JPopupMenu.Separator();
        delete = new javax.swing.JMenuItem();
        rename = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        actionLogTextArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        fileTable = new javax.swing.JTable();
        actionLogLabel = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        uploadMenuItem = new javax.swing.JMenuItem();
        downloadMenuItem = new javax.swing.JMenuItem();
        mkdirMenuItem = new javax.swing.JMenuItem();
        rmMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        disconnectMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        preferencesMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        refreshMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        benchmarkMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        download.setMnemonic('d');
        download.setText("Download");
        download.setToolTipText("Download file to this device");
        download.setName(""); // NOI18N
        download.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadActionPerformed(evt);
            }
        });
        filePopupMenu.add(download);
        filePopupMenu.add(separator0);

        move.setText("Move");
        move.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveActionPerformed(evt);
            }
        });
        filePopupMenu.add(move);
        filePopupMenu.add(separator1);

        delete.setMnemonic('r');
        delete.setText("Delete");
        delete.setToolTipText("Deletes file(s) from the server");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });
        filePopupMenu.add(delete);

        rename.setText("Rename");
        rename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renameActionPerformed(evt);
            }
        });
        filePopupMenu.add(rename);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CryptoClient");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocation(new java.awt.Point(100, 100));
        setMaximumSize(new java.awt.Dimension(2000, 2000));
        setMinimumSize(new java.awt.Dimension(852, 614));
        setSize(new java.awt.Dimension(852, 614));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        actionLogTextArea.setEditable(false);
        actionLogTextArea.setColumns(20);
        actionLogTextArea.setLineWrap(true);
        actionLogTextArea.setRows(5);
        jScrollPane1.setViewportView(actionLogTextArea);

        fileTable.setAutoCreateRowSorter(true);
        fileTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Name", "Date modified", "Type", "Size"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        fileTable.setToolTipText("");
        fileTable.setDragEnabled(true);
        fileTable.setIntercellSpacing(new java.awt.Dimension(1, 5));
        fileTable.setMinimumSize(new java.awt.Dimension(525, 32));
        fileTable.setRowHeight(32);
        fileTable.setShowHorizontalLines(false);
        fileTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fileTableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fileTableMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fileTableMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(fileTable);
        if (fileTable.getColumnModel().getColumnCount() > 0) {
            fileTable.getColumnModel().getColumn(1).setMinWidth(200);
            fileTable.getColumnModel().getColumn(1).setPreferredWidth(200);
            fileTable.getColumnModel().getColumn(1).setMaxWidth(200);
            fileTable.getColumnModel().getColumn(2).setMinWidth(150);
            fileTable.getColumnModel().getColumn(2).setPreferredWidth(150);
            fileTable.getColumnModel().getColumn(2).setMaxWidth(150);
            fileTable.getColumnModel().getColumn(3).setMinWidth(100);
            fileTable.getColumnModel().getColumn(3).setPreferredWidth(100);
            fileTable.getColumnModel().getColumn(3).setMaxWidth(100);
        }

        actionLogLabel.setText("Action log");

        fileMenu.setText("File");

        uploadMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        uploadMenuItem.setText("Upload");
        uploadMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(uploadMenuItem);

        downloadMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        downloadMenuItem.setText("Download");
        downloadMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(downloadMenuItem);

        mkdirMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mkdirMenuItem.setText("New folder");
        mkdirMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mkdirMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(mkdirMenuItem);

        rmMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        rmMenuItem.setText("Delete");
        rmMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rmMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(rmMenuItem);
        fileMenu.add(jSeparator1);

        disconnectMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        disconnectMenuItem.setText("Disconnect");
        disconnectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(disconnectMenuItem);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");
        editMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editMenuActionPerformed(evt);
            }
        });

        preferencesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        preferencesMenuItem.setText("Preferences");
        preferencesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preferencesMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(preferencesMenuItem);

        menuBar.add(editMenu);

        viewMenu.setText("View");

        refreshMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        refreshMenuItem.setText("Refresh");
        refreshMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(refreshMenuItem);

        menuBar.add(viewMenu);

        toolsMenu.setText("Tools");

        benchmarkMenuItem.setText("Benchmark");
        benchmarkMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                benchmarkMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(benchmarkMenuItem);

        menuBar.add(toolsMenu);

        helpMenu.setText("Help");

        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(actionLogLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator4)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(actionLogLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        if (Settings.isWorking()) {
            int reply = JOptionPane.showConfirmDialog(this, "There is a task "
                    + "running in the background.\nAre you sure you want to "
                    + "exit ?", "Exit", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                try {
                    DataClient.disconnect();
                } catch (IOException ex) {
                    ErrorHandler.showError(ex);
                }
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        }
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void uploadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadMenuItemActionPerformed
        if (Settings.isWorking()) {
            JOptionPane.showMessageDialog(this, "There is already a task running"
                    + "in the background.\nWait or cancel it and try again.",
                    "Upload file", JOptionPane.WARNING_MESSAGE);
        } else {
            fd = new FileDialog(this, "Upload file", FileDialog.LOAD);
            fd.setMultipleMode(true);
            fd.setVisible(true);
            if (fd.getFiles().length != 0) {
                try {
                    FileWorker fwul = new FileWorker(fileTable, actionLogTextArea);
                    FileWorker.setUploadParams(fd.getFiles());
                    fwul.execute();
                } catch (Exception e) {
                    visual.ErrorHandler.showError(e);
                }
            }
        }
    }//GEN-LAST:event_uploadMenuItemActionPerformed


    private void benchmarkMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_benchmarkMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_benchmarkMenuItemActionPerformed

    private void editMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editMenuActionPerformed

    }//GEN-LAST:event_editMenuActionPerformed

    private void preferencesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preferencesMenuItemActionPerformed
        PreferencesFrame.main(null);
    }//GEN-LAST:event_preferencesMenuItemActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        exitMenuItemActionPerformed(null);
    }//GEN-LAST:event_formWindowClosed

    private void disconnectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectMenuItemActionPerformed
        if (Settings.isWorking()) {
            int reply = JOptionPane.showConfirmDialog(this, "There is a task "
                    + "running in the background.\nAre you sure you want to "
                    + "disconnect ?", "Disconnect", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                try {
                    DataClient.disconnect();
                } catch (IOException ex) {
                    ErrorHandler.showError(ex);
                }
                this.dispose();
                LoginForm.main(null);
            }
        }
    }//GEN-LAST:event_disconnectMenuItemActionPerformed

    private void fileTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileTableMouseReleased
        if (evt.isPopupTrigger()) {
            filePopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_fileTableMouseReleased

    private void fileTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileTableMousePressed

    }//GEN-LAST:event_fileTableMousePressed

    private void downloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadActionPerformed
        if (Settings.isWorking()) {
            JOptionPane.showMessageDialog(this, "There is already a task running"
                    + " in the background.\nWait or cancel it and try again.",
                    "Download file", JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {
                FileWorker fwdl = new FileWorker(fileTable, actionLogTextArea);
                FileWorker.setDownloadParams(fileTable.getSelectedRows());
                fwdl.execute();
            } catch (Exception e) {
                visual.ErrorHandler.showError(e);
            }
        }
    }//GEN-LAST:event_downloadActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        if (Settings.isWorking()) {
            JOptionPane.showMessageDialog(this, "There is already a task running"
                    + " in the background.\nWait or cancel it and try again.",
                    "Delete file", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int reply = JOptionPane.showConfirmDialog(null,
                    "Delete selected item(s) ?", "Delete file", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                try {
                    for (int i : fileTable.getSelectedRows()) {
                        String s = (String) fileTable.getValueAt(i, 0);
                        if (fileTable.getValueAt(i, 2).equals("File")) {
                            DataClient.rm(s, 0);
                            updateLog("Deleted file " + s);
                        } else {
                            DataClient.rm(s, 1);
                            updateLog("Deleted folder " + s);
                        }
                    }
                    updateFileTable();
                } catch (Exception ex) {
                    visual.ErrorHandler.showError(ex);
                }
            }
        }
    }//GEN-LAST:event_deleteActionPerformed

    private void fileTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileTableMouseClicked
        if (evt.getClickCount() == 2) {
            try {
                if (!fileTable.getValueAt(fileTable.getSelectedRow(), 2).equals("File")) {
                    DataClient.cd((String) fileTable.getValueAt(fileTable.getSelectedRow(), 0));
                    updateFileTable();
                } else {
                    downloadActionPerformed(null);
                }
            } catch (IOException ex) {
                visual.ErrorHandler.showError(ex);
            }
        }
    }//GEN-LAST:event_fileTableMouseClicked

    private void refreshMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshMenuItemActionPerformed
        try {
            updateFileTable();
            updateLog("Refreshed table");
        } catch (IOException ex) {
            visual.ErrorHandler.showError(ex);
        }
    }//GEN-LAST:event_refreshMenuItemActionPerformed

    private void downloadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadMenuItemActionPerformed
        downloadActionPerformed(evt);
    }//GEN-LAST:event_downloadMenuItemActionPerformed

    private void mkdirMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mkdirMenuItemActionPerformed
        String reply = JOptionPane.showInputDialog(this, "Name:", "New folder",
                JOptionPane.QUESTION_MESSAGE);
        try {
            DataClient.mkdir(reply);
            updateFileTable();
            updateLog("Created folder " + reply);
        } catch (IOException ex) {
            visual.ErrorHandler.showError(ex);
        }
    }//GEN-LAST:event_mkdirMenuItemActionPerformed

    private void rmMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rmMenuItemActionPerformed
        deleteActionPerformed(evt);
    }//GEN-LAST:event_rmMenuItemActionPerformed

    private void renameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renameActionPerformed
        if (fileTable.getSelectedRows().length == 0) {
            JOptionPane.showMessageDialog(this, "Please select a file or directory.",
                    "Rename file", JOptionPane.INFORMATION_MESSAGE);
        } else if (fileTable.getSelectedRows().length > 1) {
            JOptionPane.showMessageDialog(this, "Please select only one item.",
                    "Rename file", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String newName = JOptionPane.showInputDialog(this, "New name", "Rename "
                    + "file", JOptionPane.QUESTION_MESSAGE);
            boolean nameTaken = false;

            try {
                for (FTPFile file : DataClient.listFiles()) {
                    if (file.getName().equals(newName)) {
                        nameTaken = true;
                    }
                }

                if (nameTaken) {
                    JOptionPane.showMessageDialog(this, "There already exists a "
                            + "file with that name.\nPlease choose another one.",
                            "Rename file", JOptionPane.WARNING_MESSAGE);
                } else if (newName != null) {
                    String currName = (String) fileTable.getValueAt(fileTable.
                            getSelectedRows()[0], 0);
                    DataClient.rename(currName, newName.replaceAll("[^a-zA-Z0-9 ]", ""));
                    updateFileTable();
                    updateLog("Renamed " + currName + " to " + newName);
                }
            } catch (IOException ex) {
                visual.ErrorHandler.showError(ex);
            }
        }
    }//GEN-LAST:event_renameActionPerformed

    private void moveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveActionPerformed
        String[] names = new String[fileTable.getSelectedRows().length];
        int j = 0;

        for (int i : fileTable.getSelectedRows()) {
            names[j] = (String) fileTable.getValueAt(i, 0);
            j++;
        }

        MoveFileFrame.setParams(names);
        MoveFileFrame.main(null);

    }//GEN-LAST:event_moveActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DefaultFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        }

        /* Create and display the form */
        SwingUtilities.invokeLater(() -> {
            try {
                new DefaultFrame().setVisible(true);

            } catch (Exception ex) {
                visual.ErrorHandler.showError(ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JLabel actionLogLabel;
    private static javax.swing.JTextArea actionLogTextArea;
    private javax.swing.JMenuItem benchmarkMenuItem;
    private javax.swing.JMenuItem delete;
    private javax.swing.JMenuItem disconnectMenuItem;
    private javax.swing.JMenuItem download;
    private javax.swing.JMenuItem downloadMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPopupMenu filePopupMenu;
    private static javax.swing.JTable fileTable;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem mkdirMenuItem;
    private javax.swing.JMenuItem move;
    private javax.swing.JMenuItem preferencesMenuItem;
    private javax.swing.JMenuItem refreshMenuItem;
    private javax.swing.JMenuItem rename;
    private javax.swing.JMenuItem rmMenuItem;
    private javax.swing.JPopupMenu.Separator separator0;
    private javax.swing.JPopupMenu.Separator separator1;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JMenuItem uploadMenuItem;
    private javax.swing.JMenu viewMenu;
    // End of variables declaration//GEN-END:variables
}
