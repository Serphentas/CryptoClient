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

    private static int action;
    private static int[] rows;
    private static JTextArea log;
    private static JFileChooser fc;
    private static JTable fileTable;
    private static File[] localFiles;
    private static FTPFile[] dirs, remoteFiles;

    private static void failIfInterrupted() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("FileWorker interrupted");
        }
    }

    public FileWorker(JTable fileTable, JTextArea log) {
        FileWorker.log = log;
        FileWorker.fileTable = fileTable;
    }

    public static void setUploadParams(File[] files, int action) {
        FileWorker.localFiles = files;
        FileWorker.action = action;
    }

    public static void setDownloadParams(int[] rows, int action) {
        FileWorker.rows = rows;
        FileWorker.action = action;
    }

    /**
     * action: 0=upload, 1=download
     *
     * @return @throws Exception
     */
    @Override
    protected Integer doInBackground() throws Exception {
        publish("Begin file transfer");
        int i = 1;
        switch (action) {
            case 0:
                for (File f : localFiles) {
                    if (DataClient.existsFile(f.getName())) {
                        int reply = JOptionPane.showConfirmDialog(null,
                                "File already exists. Overwrite ?",
                                "Overwrite file", JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.YES_OPTION) {
                            DataClient.delete(f.getName(), 0);
                        }
                    }

                    DataClient.send(f, f.getName());
                    publish("[" + new Date() + "] File " + f.getName() + " sent");
                    /*setProgress((i / localFiles.length) * 100);
                    i++;*/
                    updateTable();
                }
                break;
            case 1:
                String dlDir = new String();

                if (Settings.isDlDir()) {
                    dlDir = Settings.getWorkingDir();
                } else {
                    fc = new JFileChooser();
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fc.setMultiSelectionEnabled(false);
                    fc.setVisible(true);

                    int returnVal = fc.showDialog(new JFrame(), "Choose download folder");
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        dlDir = fc.getSelectedFile().getPath().replace("\\", "/");
                    }
                }
                
                for (int row : rows) {
                    String s = (String) fileTable.getValueAt(row, 0);
                    DataClient.receive(s, new File(dlDir + "/" + s));
                    publish("[" + new Date() + "] File " + s + " received");
                    /*setProgress((i / rows.length) * 100);
                    i++;*/
                }
                break;
        }
        publish("Done");
        return 1;
    }

    private void updateTable() throws IOException {
        dirs = DataClient.listDirs();
        remoteFiles = DataClient.listFiles();

        DefaultTableModel dtm = (DefaultTableModel) fileTable.getModel();
        dtm.setRowCount(remoteFiles.length);
        fileTable.setModel(dtm);

        int i = 0;

        if (!DataClient.isAtRoot()) {
            System.out.println("asd");
            fileTable.setValueAt("..", i, 0);
            i++;
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
