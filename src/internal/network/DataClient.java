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
    private static boolean isAtRoot = true;
    public static final String HOSTNAME = "10.0.0.21";
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
     * @throws IOException If an I/O error occurs while either sending the
     * command or receiving the server reply.
     */
    public static boolean login(String username, String password) throws IOException {
        if (!ftp.isConnected()) {
            ftp.connect(HOSTNAME, PORT);
        }

        if (ftp.login(username, password)) {
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            return true;
        } else {
            disconnect();
            return false;
        }
    }

    /**
     * Changes the working directory to the specified path
     *
     * @param path path to the new working directory
     * @throws IOException
     */
    public static void cd(String path) throws IOException {
        ftp.changeWorkingDirectory(path);
        isAtRoot = ftp.printWorkingDirectory().equals("/");
    }

    /**
     * Returns true if the current working directory is the remote root
     *
     * @return true if the current working directory is the remote root
     */
    public static boolean isAtRoot() {
        return isAtRoot;
    }

    /**
     * Lists directories in the current working directory, in alphabetical order
     *
     * @return FTPFile array of directories in the current working directory
     * @throws IOException
     */
    public static FTPFile[] listDirs() throws IOException {
        return ftp.listDirectories();
    }

    /**
     * Lists files in the current working directory, in alphabetical order
     * <p>
     * Note: this also includes folders
     *
     * @return FTPFile array of files in the current working directory
     * @throws IOException
     */
    public static FTPFile[] listFiles() throws IOException {
        return ftp.listFiles();
    }

    public static boolean existsFile(String path) throws IOException {
        boolean existance = false;
        for (FTPFile f : listFiles()) {
            if (f.getName().equals(path)) {
                existance = true;
            }
        }
        return existance;
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
