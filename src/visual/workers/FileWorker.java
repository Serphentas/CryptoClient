/* 
 * Copyright (c) 2016, Serphentas
 * All rights reserved.
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/ or send a letter
 * to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
package visual.workers;

import internal.Settings;
import internal.network.Control;
import internal.network.IO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import visual.DefaultFrame;

public class FileWorker extends SwingWorker<Integer, String> {

    private static final String[] buttons = {"Yes", "Yes to all", "No", "Cancel"};

    private static JTable fileTable, fileQueue;
    private static String filePath, dlDir;
    private static JFileChooser fc;

    private static void failIfInterrupted() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("FileWorker interrupted");
        }
    }

    /**
     * Creates a FileWorker with the given file table and file queue as
     * parameters
     *
     * @param fileTable file table to work with
     * @param fileQueue file queue to work with
     */
    public FileWorker(JTable fileTable, JTable fileQueue) {
        FileWorker.fileTable = fileTable;
        FileWorker.fileQueue = fileQueue;
    }

    @Override
    protected Integer doInBackground() throws Exception {
        Settings.setIsWorking(true);
        int returnVal = -1;
        boolean yesForAll = false, isQueueEmpty = false, existsFile = false,
                isSetDlDir = false;

        while (!isQueueEmpty) {
            fileQueue.setValueAt("In progress", 0, 1);
            filePath = (String) fileQueue.getValueAt(0, 0);

            if (fileQueue.getValueAt(0, 2).equals("Upload")) {
                File source = new File(filePath);
                if (!yesForAll && Control.exists(source.getName())) {
                    returnVal = JOptionPane.showOptionDialog(null, "File "
                            + source.getName() + " already exists. Overwrite ?",
                            "Overwrite", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, buttons,
                            buttons[2]);
                    yesForAll = returnVal == 1;
                    existsFile = true;
                }

                if (returnVal < 2) {
                    if (existsFile) {
                        Control.rm(source.getName());
                        existsFile = false;
                    }
                    IO.upload(source);
                }
            } else if (fileQueue.getValueAt(0, 2).equals("Download")) {
                if (Settings.isDlDir()) {
                    dlDir = Settings.getWorkingDir();
                    isSetDlDir = true;
                } else if (!isSetDlDir) {
                    fc = new JFileChooser();
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fc.setMultiSelectionEnabled(false);
                    fc.setVisible(true);

                    returnVal = fc.showDialog(new JFrame(), "Choose download folder");
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        dlDir = fc.getSelectedFile().getPath().replace("\\", "/");
                        if (!dlDir.endsWith("/")) {
                            dlDir += "/";
                        }
                        isSetDlDir = true;
                    }
                }

                if (isSetDlDir) {
                    filePath = (String) fileQueue.getValueAt(0, 0);
                    File destFile = new File(dlDir + filePath.substring(
                            filePath.lastIndexOf("/"), filePath.length()));

                    if (!yesForAll && destFile.isFile() && destFile.exists()) {
                        returnVal = JOptionPane.showOptionDialog(null, "File "
                                + destFile.getName() + " already exists. Overwrite ?",
                                "Overwrite", JOptionPane.DEFAULT_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, buttons,
                                buttons[2]);
                        yesForAll = returnVal == 1;
                    }

                    if (returnVal < 2) {
                        IO.download(filePath, destFile);
                    }
                }
            }

            DefaultTableModel dtm = (DefaultTableModel) fileQueue.getModel();
            dtm.removeRow(0);
            fileQueue.setModel(dtm);
            isQueueEmpty = fileQueue.getRowCount() == 0;
            DefaultFrame.updateFileTable();
        }

        Settings.setIsWorking(false);
        return 1;
    }

    private void updateTable() throws IOException {
        int i = 0;
        /*dirs = Control.lsdir();
        remoteFiles = Control.lsfile();*/
        DefaultTableModel dtm = (DefaultTableModel) fileTable.getModel();

        /*if (!Control.isAtRoot()) {
            dtm.setRowCount(remoteFiles.length + 1);
            fileTable.setModel(dtm);
            fileTable.setValueAt("..", i, 0);
            fileTable.setValueAt("", i, 1);
            fileTable.setValueAt("Directory", i, 2);
            fileTable.setValueAt("", i, 3);
            i++;
        } else {
            dtm.setRowCount(remoteFiles.length);
            fileTable.setModel(dtm);
        }

        for (FTPFile f : dirs) {
            fileTable.setValueAt(f.getName(), i, 0);
            fileTable.setValueAt(f.getTimestamp().getTime(), i, 1);
            fileTable.setValueAt("Directory", i, 2);
            fileTable.setValueAt("", i, 3);

            i++;
        }

        for (FTPFile f : remoteFiles) {
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
        }*/
    }

    @Override
    protected void process(List<String> chunks) {
        chunks.stream().forEach((s) -> {
            //log.append(s + "\n");
        });
    }
}
