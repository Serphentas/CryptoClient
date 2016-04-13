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
package internal.network;

import internal.Settings;
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
    public static final String HOSTNAME = "data.theswissbay.ch";
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
            ftp.setSoTimeout(0);
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
     * Disconnects from the data server
     *
     * @throws IOException
     */
    public static void disconnect() throws IOException {
        ftp.disconnect();
        Settings.setIsWorking(false);
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
     * Returns the current working directory
     *
     * @return current working directory
     * @throws IOException
     */
    public static String currentDir() throws IOException {
        return ftp.printWorkingDirectory();
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

    /**
     * Checks whether a give file or directory exists
     *
     * @param path path to the file or directory to look for
     * @return true if such a file or directory exists, else false
     * @throws IOException
     */
    public static boolean exists(String path) throws IOException {
        for (FTPFile f : listFiles()) {
            if (f.getName().equals(path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes a file or directory
     *
     * @param fileName path to the file or directory to be deleted
     * @param type 0 if file, 1 if directory
     * @throws IOException
     */
    public static void rm(String fileName, int type) throws IOException {
        if (type == 0) {
            ftp.deleteFile(fileName);
        } else {
            ftp.removeDirectory(fileName);
        }
    }

    /**
     * Renames or moves a file or directory
     * <p>
     * Also serves as mv command if the destination folder is specified in the
     * new name. <i>Eg: file.txt -> asd/file.txt -> newName=asd/file.txt</i>
     *
     * @param path path to the file or directory to rename
     * @param newName new name
     * @return true if successfully renamed, else false
     * @throws IOException
     */
    public static boolean rename(String path, String newName) throws IOException {
        return ftp.rename(path, newName);
    }

    /**
     * Creates a directory using the specified path
     *
     * @param path relative or absolute path to the new directory
     * @return
     */
    public static boolean mkdir(String path) throws IOException {
        return ftp.makeDirectory(path);
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
        ftp.completePendingCommand();
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
        ftp.completePendingCommand();
    }
}
