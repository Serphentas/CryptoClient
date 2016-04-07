package internal.network;

import internal.crypto.GCMCipher;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import visual.ErrorHandler;

/**
 * Allows I/O from the data server, using FTP
 *
 * @author Dreadlockyx
 */
public class DataClient {

    private static FTPClient ftp;
    private static GCMCipher gcmc;
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 21;

    /**
     * Initializes the FTPClient
     */
    public static void init() {
        ftp = new FTPClient();
        try {
            gcmc = new GCMCipher();
        } catch (Exception ex) {
            ErrorHandler.showError(ex);
        }
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
            connect(HOSTNAME, PORT);
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
     * Encrypts and sends a local file to the data server
     *
     * @param inputFile source File
     * @param remoteFilePath remote file location
     * @throws IOException
     * @throws Exception
     */
    public static void send(File inputFile, String remoteFilePath) throws IOException, Exception {
        for (FTPFile f : ftp.listFiles()) {
            if (f.getName().equals(remoteFilePath)) {
                ftp.deleteFile(remoteFilePath);
            }
        }
        gcmc.encrypt(new FileInputStream(inputFile), ftp.storeFileStream(remoteFilePath));
        DataClient.completePendingCommand();
    }

    /**
     * Receives and decrypts a remote file to the local filesystem
     *
     * @param outputFile destination File
     * @param remoteFilePath remote file location
     * @throws IOException
     */
    public static void receive(String remoteFilePath, File outputFile) throws IOException, Exception {
        gcmc.decrypt(ftp.retrieveFileStream(remoteFilePath), new FileOutputStream(outputFile));
        DataClient.completePendingCommand();
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
}
