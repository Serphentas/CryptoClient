package internal.network;

import internal.crypto.GCMCipher;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * Allows I/O from the data server, using FTP
 *
 * @author Dreadlockyx
 */
public abstract class DataClient {

    private static FTPClient ftp;
    private static GCMCipher gcmc;

    /**
     * Initializes the FTPClient and the GCMCipher
     *
     * @throws Exception
     */
    public static void init() throws Exception {
        ftp = new FTPClient();
        gcmc = new GCMCipher();
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
     *
     * @param username username
     * @param password password
     * @throws IOException
     */
    public static void login(String username, String password) throws IOException {
        ftp.login(username, password);
    }

    public static FTPFile[] listDirs(String path) throws IOException {
        return ftp.listDirectories(path);
    }

    public static FTPFile[] listFiles(String path) throws IOException {
        return ftp.listFiles(path);
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
    public static InputStream inputStream(String remoteFilePath) throws Exception {
        return ftp.retrieveFileStream(remoteFilePath);
    }

}
