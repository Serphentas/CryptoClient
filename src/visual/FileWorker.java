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
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.net.ftp.FTPFile;

public class FileWorker extends SwingWorker<Integer, String> {

    private static int[] rows;
    private static boolean isUL;
    private static JTextArea log;
    private static JFileChooser fc;
    private static JTable fileTable;
    private static File[] localFiles;
    private static FTPFile[] dirs, remoteFiles;
    private static String[] buttons = {"Yes", "Yes to all", "No", "Cancel"};

    private static void failIfInterrupted() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("FileWorker interrupted");
        }
    }

    /**
     * Creates a FileWorker with the given JTable and JTextArea as parameters
     *
     * @param fileTable JTable to work with
     * @param log JTextArea to log to
     */
    public FileWorker(JTable fileTable, JTextArea log) {
        FileWorker.log = log;
        FileWorker.fileTable = fileTable;
    }

    /**
     * Sets the upload parameters, so that the doInBackground method will handle
     * the task properly
     *
     * @param files files to upload
     */
    public static void setUploadParams(File[] files) {
        FileWorker.localFiles = files;
        isUL = true;
    }

    /**
     * Sets the download parameters, so that the doInBackground method will
     * handle the task properly
     *
     * @param rows rows from the table to download (i.e. files)
     */
    public static void setDownloadParams(int[] rows) {
        FileWorker.rows = rows;
        isUL = false;
    }

    /**
     * action: 0=upload, 1=download
     *
     * @return @throws Exception
     */
    @Override
    protected Integer doInBackground() throws Exception {
        publish("[" + new Date() + "] Begin file transfer");
        Settings.setIsWorking(true);
        int i = 1, returnVal = -1;
        boolean all = false;

        if (isUL) {
            for (File f : localFiles) {
                if(returnVal!=3){
                                    if (!all && DataClient.exists(f.getName())) {
                    returnVal = JOptionPane.showOptionDialog(null, "File "
                            + f.getName() + " already exists. Overwrite ?",
                            "Overwrite file", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, buttons,
                            buttons[2]);
                    all = returnVal == 1;
                }

                if (returnVal < 2) {
                    DataClient.rm(f.getName(), 0);
                    DataClient.send(f, f.getName());
                    publish("[" + new Date() + "] File " + f.getName() + " sent");
                }

                setProgress((i / localFiles.length) * 100);
                updateTable();
                i++;
                }
            }
        } else {
            String dlDir = new String();

            if (Settings.isDlDir()) {
                dlDir = Settings.getWorkingDir();
            } else {
                fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setMultiSelectionEnabled(false);
                fc.setVisible(true);

                returnVal = fc.showDialog(new JFrame(), "Choose download folder");
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    dlDir = fc.getSelectedFile().getPath().replace("\\", "/");
                }
            }

            if (returnVal != JFileChooser.CANCEL_OPTION) {
                for (int row : rows) {
                    if (returnVal != 3) {
                        String s = (String) fileTable.getValueAt(row, 0);
                        File destFile = new File(dlDir + "/" + s);

                        if (!all && destFile.isFile() && destFile.exists()) {
                            returnVal = JOptionPane.showOptionDialog(null, "File "
                                    + destFile.getName() + " already exists. Overwrite ?",
                                    "Overwrite file", JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.QUESTION_MESSAGE, null, buttons,
                                    buttons[2]);
                            all = returnVal == 1;
                        }

                        if (returnVal < 2) {
                            DataClient.receive(s, destFile);
                            publish("[" + new Date() + "] File " + s + " received");
                            setProgress((i / rows.length) * 100);
                        }
                        i++;
                    }
                }
            }
        }
        publish("[" + new Date() + "] Done");
        Settings.setIsWorking(false);
        return 1;
    }

    private void updateTable() throws IOException {
        int i = 0;
        dirs = DataClient.listDirs();
        remoteFiles = DataClient.listFiles();
        DefaultTableModel dtm = (DefaultTableModel) fileTable.getModel();

        if (!DataClient.isAtRoot()) {
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
        }
    }

    @Override
    protected void process(List<String> chunks) {
        chunks.stream().forEach((s) -> {
            log.append(s + "\n");
        });
    }
}
