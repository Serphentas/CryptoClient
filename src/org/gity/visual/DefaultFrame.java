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

import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import org.gity.internal.Settings;
import org.gity.internal.network.Control;
import org.gity.internal.network.IO;
import org.gity.visual.workers.FileWorker;

/**
 * Main UI
 *
 * @author Serphentas
 */
public class DefaultFrame extends javax.swing.JFrame {

    private FileDialog fd;
    private String newDirName, cwd;
    private static boolean disconnecting = false;
    private static DefaultTableModel dtmFileTable, dtmFileQueue;
    private static Iterator<Map.Entry<String, Long>> dirMapIter;
    private static Iterator<Map.Entry<String, Long[]>> fileMapIter;

    public DefaultFrame() throws IOException {
        initComponents();
        setLocationRelativeTo(null);
        updateFileTable();
    }

    /**
     * Updates the file table view, so that new files and folders are shown
     *
     * @throws IOException
     */
    public static void updateFileTable() throws IOException {
        Map<String, Long> dirMap = Control.lsdir(Control.cwd());
        Map<String, Long[]> fileMap = Control.lsfile(Control.cwd());
        int i = 0, itemCount = dirMap.size() + fileMap.size();

        if (!Control.isAtRoot()) {
            setFileTableSize(itemCount + 1);
            fileTable.setValueAt("..", i, 0);
            fileTable.setValueAt("", i, 1);
            fileTable.setValueAt("Directory", i, 2);
            fileTable.setValueAt("", i, 3);
            i++;
        } else {
            setFileTableSize(itemCount);
        }

        if (!dirMap.isEmpty()) {
            dirMapIter = dirMap.entrySet().iterator();
            while (dirMapIter.hasNext()) {
                Map.Entry<String, Long> entry = dirMapIter.next();
                fileTable.setValueAt(entry.getKey(), i, 0);
                fileTable.setValueAt(new Date(entry.getValue()), i, 1);
                fileTable.setValueAt("Directory", i, 2);
                fileTable.setValueAt("", i, 3);
                i++;
            }
        }

        if (!fileMap.isEmpty()) {
            fileMapIter = fileMap.entrySet().iterator();
            while (fileMapIter.hasNext()) {
                Map.Entry<String, Long[]> entry = fileMapIter.next();
                fileTable.setValueAt(entry.getKey(), i, 0);
                fileTable.setValueAt(new Date(entry.getValue()[0]), i, 1);
                fileTable.setValueAt("File", i, 2);
                fileTable.setValueAt(longToSize(entry.getValue()[1]), i, 3);
                i++;
            }
        }
    }

    /**
     * Sets a new row count for the file table
     *
     * @param newRowCount new row count
     */
    public static void setFileTableSize(int newRowCount) {
        dtmFileTable = (DefaultTableModel) fileTable.getModel();
        dtmFileTable.setRowCount(newRowCount);
        fileTable.setModel(dtmFileTable);
    }

    /**
     * Sets a new row count for the file queue
     *
     * @param newRowCount new row count
     */
    public static void setFileQueueSize(int newRowCount) {
        dtmFileQueue = (DefaultTableModel) fileQueue.getModel();
        dtmFileQueue.setRowCount(dtmFileQueue.getRowCount() + newRowCount);
        fileQueue.setModel(dtmFileQueue);
    }

    /**
     * Sets a new status for the first item in the file queue
     *
     * @param status new status
     */
    public static void setFileQueueItemStatus(String status) {
        fileQueue.setValueAt(status, 0, 2);
    }

    private static String longToSize(long longSize) {
        String size = new String();
        if (longSize / 1024 < 1024) {
            size = longSize / 1024 + " KiB";
        } else if (longSize / 1048576 < 1024) {
            size = longSize / 1048576 + " MiB";
        } else if (longSize / 1073741824 < 1024) {
            size = longSize / 1073741824 + " GiB";
        } else if (longSize / Math.pow(1024, 4) < 1024) {
            size = longSize / Math.pow(1024, 4) + " TiB";
        }
        return size;
    }

    private String sanitize(String s) {
        s = s.replaceAll("/", "");
        return s.replace("..", "");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileTablePopupMenu = new javax.swing.JPopupMenu();
        download = new javax.swing.JMenuItem();
        separator0 = new javax.swing.JPopupMenu.Separator();
        move = new javax.swing.JMenuItem();
        separator1 = new javax.swing.JPopupMenu.Separator();
        delete = new javax.swing.JMenuItem();
        rename = new javax.swing.JMenuItem();
        fileQueuePopupMenu = new javax.swing.JPopupMenu();
        deleteQueueItem = new javax.swing.JMenuItem();
        jScrollPane2 = new javax.swing.JScrollPane();
        fileTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        fileQueue = new javax.swing.JTable();
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
        fileTablePopupMenu.add(download);
        fileTablePopupMenu.add(separator0);

        move.setText("Move");
        move.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveActionPerformed(evt);
            }
        });
        fileTablePopupMenu.add(move);
        fileTablePopupMenu.add(separator1);

        delete.setMnemonic('r');
        delete.setText("Delete");
        delete.setToolTipText("Deletes file(s) from the server");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });
        fileTablePopupMenu.add(delete);

        rename.setText("Rename");
        rename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renameActionPerformed(evt);
            }
        });
        fileTablePopupMenu.add(rename);

        deleteQueueItem.setText("Cancel");
        deleteQueueItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteQueueItemActionPerformed(evt);
            }
        });
        fileQueuePopupMenu.add(deleteQueueItem);

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
                java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
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
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fileTableMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(fileTable);
        if (fileTable.getColumnModel().getColumnCount() > 0) {
            fileTable.getColumnModel().getColumn(0).setMinWidth(250);
            fileTable.getColumnModel().getColumn(0).setMaxWidth(999999999);
            fileTable.getColumnModel().getColumn(1).setMinWidth(165);
            fileTable.getColumnModel().getColumn(1).setPreferredWidth(165);
            fileTable.getColumnModel().getColumn(1).setMaxWidth(150);
            fileTable.getColumnModel().getColumn(2).setMinWidth(100);
            fileTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            fileTable.getColumnModel().getColumn(2).setMaxWidth(100);
            fileTable.getColumnModel().getColumn(3).setMinWidth(100);
            fileTable.getColumnModel().getColumn(3).setPreferredWidth(100);
            fileTable.getColumnModel().getColumn(3).setMaxWidth(100);
        }

        jLabel1.setText("File queue");

        fileQueue.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Source", "Destination", "Status", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        fileQueue.setRowHeight(20);
        fileQueue.setShowHorizontalLines(false);
        fileQueue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fileQueueMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(fileQueue);
        if (fileQueue.getColumnModel().getColumnCount() > 0) {
            fileQueue.getColumnModel().getColumn(0).setPreferredWidth(200);
            fileQueue.getColumnModel().getColumn(1).setPreferredWidth(200);
            fileQueue.getColumnModel().getColumn(2).setMinWidth(150);
            fileQueue.getColumnModel().getColumn(2).setPreferredWidth(150);
            fileQueue.getColumnModel().getColumn(2).setMaxWidth(150);
            fileQueue.getColumnModel().getColumn(3).setResizable(false);
            fileQueue.getColumnModel().getColumn(3).setPreferredWidth(100);
        }

        fileMenu.setMnemonic('F');
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

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setMnemonic('E');
        editMenu.setText("Edit");

        preferencesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        preferencesMenuItem.setText("Preferences");
        preferencesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preferencesMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(preferencesMenuItem);

        menuBar.add(editMenu);

        viewMenu.setMnemonic('V');
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

        toolsMenu.setMnemonic('T');
        toolsMenu.setText("Tools");

        benchmarkMenuItem.setText("Benchmark");
        benchmarkMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                benchmarkMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(benchmarkMenuItem);

        menuBar.add(toolsMenu);

        helpMenu.setMnemonic('H');
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
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
                    .addComponent(jScrollPane3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        int reply;
        if (Settings.isWorking()) {
            reply = JOptionPane.showConfirmDialog(this, "There is a task "
                    + "running in the background.\nAre you sure you want to "
                    + "exit ?", "Exit", JOptionPane.YES_NO_OPTION);
        } else {
            reply = JOptionPane.showConfirmDialog(this, "Are you sure you want"
                    + " to exit ?", "Exit", JOptionPane.YES_NO_OPTION);
        }

        if (reply == JOptionPane.YES_OPTION) {
            try {
                Control.disconnect();
                IO.disconnect();
            } catch (IOException ex) {
                ErrorHandler.showError(ex);
            }
            System.exit(0);
        }
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void uploadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadMenuItemActionPerformed
        fd = new FileDialog(this, "Upload file", FileDialog.LOAD);
        try {
            cwd = Control.cwd();
        } catch (IOException ex) {
            ErrorHandler.showError(ex);
        }

        fd.setMultipleMode(true);
        fd.setVisible(true);
        if (fd.getFiles().length > 0) {
            int i = fileQueue.getRowCount();
            setFileQueueSize(fd.getFiles().length);

            for (File f : fd.getFiles()) {
                fileQueue.setValueAt(f.getAbsolutePath(), i, 0);
                fileQueue.setValueAt(cwd + f.getName(), i, 1);
                fileQueue.setValueAt("Pending", i, 2);
                fileQueue.setValueAt("Upload", i, 3);
                i++;
            }

            if (!Settings.isWorking()) {
                new FileWorker(fileTable, fileQueue).execute();
            }
        }
    }//GEN-LAST:event_uploadMenuItemActionPerformed


    private void benchmarkMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_benchmarkMenuItemActionPerformed
        ToolsFrame.main(null);
    }//GEN-LAST:event_benchmarkMenuItemActionPerformed

    private void preferencesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preferencesMenuItemActionPerformed
        PreferencesFrame.main(null);
    }//GEN-LAST:event_preferencesMenuItemActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        if (!disconnecting) {
            exitMenuItemActionPerformed(null);
        } else {
            this.dispose();
        }
    }//GEN-LAST:event_formWindowClosed

    private void disconnectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectMenuItemActionPerformed
        int reply;
        if (Settings.isWorking()) {
            reply = JOptionPane.showConfirmDialog(this, "There is a task "
                    + "running in the background.\nAre you sure you want to "
                    + "disconnect ?", "Disconnect", JOptionPane.YES_NO_OPTION);
        } else {
            reply = JOptionPane.showConfirmDialog(this, "Are you sure you want "
                    + "to disconnect ?", "Disconnect", JOptionPane.YES_NO_OPTION);
        }

        if (reply == JOptionPane.YES_OPTION) {
            try {
                Control.disconnect();
                IO.disconnect();
                disconnecting = true;
            } catch (IOException ex) {
                ErrorHandler.showError(ex);
            }
            this.dispose();
            LoginForm.main(null);
        }
    }//GEN-LAST:event_disconnectMenuItemActionPerformed

    private void fileTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileTableMouseReleased
        if (evt.isPopupTrigger() && fileTable.getSelectedRowCount() > 0) {
            fileTablePopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_fileTableMouseReleased

    private void downloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadActionPerformed
        try {
            cwd = Control.cwd();
        } catch (IOException ex) {
            ErrorHandler.showError(ex);
        }

        boolean isSetDLDir = false;
        String dlDir = new String();
        int i = fileQueue.getRowCount(), reply;

        if (!Settings.isDLDir()) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setMultiSelectionEnabled(false);
            fc.setVisible(true);

            reply = fc.showDialog(new JFrame(), "Choose download folder");
            if (reply == JFileChooser.APPROVE_OPTION) {
                dlDir = fc.getSelectedFile().getPath().replace("\\", "/");
                if (!dlDir.endsWith("/")) {
                    dlDir += "/";
                }
                isSetDLDir = true;
            }
        } else {
            dlDir = Settings.getDLDir();
            isSetDLDir = true;
        }

        if (isSetDLDir && fileTable.getSelectedRows().length > 0) {
            setFileQueueSize(fileTable.getSelectedRows().length);

            for (int row : fileTable.getSelectedRows()) {
                fileQueue.setValueAt(cwd + (String) fileTable.getValueAt(
                        row, 0), i, 0);
                fileQueue.setValueAt(dlDir + fileTable.getValueAt(row, 0),
                        i, 1);
                fileQueue.setValueAt("Pending", i, 2);
                fileQueue.setValueAt("Download", i, 3);
                i++;
            }

            if (!Settings.isWorking()) {
                new FileWorker(fileTable, fileQueue).execute();
            }
        }


    }//GEN-LAST:event_downloadActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        if (Settings.isWorking()) {
            JOptionPane.showMessageDialog(this, "There is already a task running"
                    + " in the background.\nWait or cancel it and try again.",
                    "Delete", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int reply = JOptionPane.showConfirmDialog(null,
                    "Delete selected item(s) ?", "Delete", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                try {
                    cwd = Control.cwd();
                    for (int i : fileTable.getSelectedRows()) {
                        Control.rm(cwd + (String) fileTable.getValueAt(i, 0));
                    }
                    updateFileTable();
                } catch (IOException ex) {
                    org.gity.visual.ErrorHandler.showError(ex);
                }
            }
        }
    }//GEN-LAST:event_deleteActionPerformed

    private void fileTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileTableMouseClicked
        if (evt.getClickCount() == 2) {
            try {
                if (!fileTable.getValueAt(fileTable.getSelectedRow(), 2).equals("File")) {
                    String newDir = (String) fileTable.getValueAt(fileTable.
                            getSelectedRow(), 0);
                    if (newDir.equals("..")) {
                        Control.cd("..");
                    } else {
                        Control.cd(Control.cwd() + newDir + "/");
                    }
                    updateFileTable();
                } else {
                    downloadActionPerformed(null);
                }
            } catch (IOException ex) {
                org.gity.visual.ErrorHandler.showError(ex);
            }
        }
    }//GEN-LAST:event_fileTableMouseClicked

    private void refreshMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshMenuItemActionPerformed
        try {
            updateFileTable();
        } catch (IOException ex) {
            org.gity.visual.ErrorHandler.showError(ex);
        }
    }//GEN-LAST:event_refreshMenuItemActionPerformed

    private void downloadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadMenuItemActionPerformed
        downloadActionPerformed(evt);
    }//GEN-LAST:event_downloadMenuItemActionPerformed

    private void mkdirMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mkdirMenuItemActionPerformed
        newDirName = JOptionPane.showInputDialog(this, "Name", "New folder",
                JOptionPane.QUESTION_MESSAGE);
        try {
            Control.mkdir(Control.cwd() + sanitize(newDirName));
            updateFileTable();
        } catch (IOException ex) {
            org.gity.visual.ErrorHandler.showError(ex);
        }
    }//GEN-LAST:event_mkdirMenuItemActionPerformed

    private void rmMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rmMenuItemActionPerformed
        deleteActionPerformed(evt);
    }//GEN-LAST:event_rmMenuItemActionPerformed

    private void renameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renameActionPerformed
        if (fileTable.getSelectedRows().length == 0) {
            JOptionPane.showMessageDialog(this, "Please select a file or directory.",
                    "Rename", JOptionPane.INFORMATION_MESSAGE);
        } else if (fileTable.getSelectedRows().length > 1) {
            JOptionPane.showMessageDialog(this, "Please select only one item.",
                    "Rename", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String newName = JOptionPane.showInputDialog(this, "New name",
                    "Rename", JOptionPane.QUESTION_MESSAGE);

            if (newName != null) {
                try {
                    String cwd = Control.cwd();

                    if (Control.exists(cwd + newName)) {
                        JOptionPane.showMessageDialog(this, "There already exists a "
                                + "file with that name.\nPlease choose another one.",
                                "Rename", JOptionPane.WARNING_MESSAGE);
                    } else {
                        String currName = (String) fileTable.getValueAt(fileTable.
                                getSelectedRow(), 0);
                        Control.rename(cwd + currName, cwd + sanitize(newName));
                        updateFileTable();
                    }
                } catch (IOException ex) {
                    org.gity.visual.ErrorHandler.showError(ex);
                }
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

    private void deleteQueueItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteQueueItemActionPerformed
        for (int row : fileQueue.getSelectedRows()) {
            fileQueue.setValueAt("Cancelled", row, 2);
        }
    }//GEN-LAST:event_deleteQueueItemActionPerformed

    private void fileQueueMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileQueueMouseReleased
        if (evt.isPopupTrigger()) {
            fileQueuePopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_fileQueueMouseReleased

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
                org.gity.visual.ErrorHandler.showError(ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem benchmarkMenuItem;
    private javax.swing.JMenuItem delete;
    private javax.swing.JMenuItem deleteQueueItem;
    private javax.swing.JMenuItem disconnectMenuItem;
    private javax.swing.JMenuItem download;
    private javax.swing.JMenuItem downloadMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private static javax.swing.JTable fileQueue;
    private javax.swing.JPopupMenu fileQueuePopupMenu;
    private static javax.swing.JTable fileTable;
    private javax.swing.JPopupMenu fileTablePopupMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
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
