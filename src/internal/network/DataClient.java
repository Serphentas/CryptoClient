package internal.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * Allows I/O from the data server, using FTP
 *
 * @author Dreadlockyx
 */
public class DataClient implements Runnable {

    private static FTPClient ftp;
    public static final String HOSTNAME = "10.0.0.20";
    public static final int PORT = 21;

    /**
     * Initializes the FTPClient
     */
    public static void init() {
        ftp = new FTPClient();
    }

    /**
     * Connects to the data server via FTP
     *
     * @param host server hostname
     * @param port server port
     * @throws IOException
     */
    public static void connect(String host, int port) throws IOException {
        ftp.connect(host, port);
    }

    /**
     * Disconnects from the data server
     *
     * @throws IOException
     */
    public static void disconnect() throws IOException {
        ftp.disconnect();
    }

    /**
     * Logs into the data server, using the supplied credentials
     * <p>
     * Connects to the server if not already connected
     *
     * @param username username
     * @param password password
     * @return true if login successful, false if not
     * @throws IOException
     */
    public static boolean login(String username, String password) throws IOException {
        if (!ftp.isConnected()) {
            connect(DataClient.HOSTNAME, DataClient.PORT);
        }

        if (ftp.login(username, password)) {
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            return true;
        }

        return false;
    }

    public static void cd(String path) throws IOException {
        ftp.changeWorkingDirectory(path);
    }

    public static FTPFile[] listDirs() throws IOException {
        return ftp.listDirectories();
    }

    public static FTPFile[] listFiles() throws IOException {
        return ftp.listFiles();
    }

    /**
     * Returns an OutputStream through which a file can be sent, using the
     * supplied string as the remote file path
     * <p>
     * Since only one remote file path is given, the OutputStream should not be
     * used to transfer more than one file (else it will append data to the
     * previous one).
     *
     * @param remoteFilePath file path on the remote server to write to
     * @return OutputStream through which a file can be sent
     * @throws IOException
     */
    public static OutputStream outputStream(String remoteFilePath) throws IOException {
        for (FTPFile f : ftp.listFiles()) {
            if (f.getName().equals(remoteFilePath)) {
                ftp.deleteFile(remoteFilePath);
            }
        }
        return ftp.storeFileStream(remoteFilePath);
    }

    /**
     * Returns an InputStream through which a file can be read, using the
     * supplied string as the remote file path
     *
     * @param remoteFilePath file path on the remote server to read from
     * @return InputStream through which a file can be read
     * @throws IOException
     */
    public static InputStream inputStream(String remoteFilePath) throws IOException {
        return ftp.retrieveFileStream(remoteFilePath);
    }

    public static void delete(String fileName, int type) throws IOException {
        if (type == 0) {
            ftp.deleteFile(fileName);
        } else {
            ftp.removeDirectory(fileName);
        }
    }

    public static void completePendingCommand() throws IOException {
        ftp.completePendingCommand();
    }

    @Override
    public void run() {

    }
}
