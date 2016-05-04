package internal.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Control {

    private static TLSClient controlSocket;
    private static final byte DISCONNECT = 0x00,
            LSFILE = 0x10,
            LSDIR = 0x11,
            CD = 0x12,
            CWD = 0x13,
            MKDIR = 0x14,
            RENAME = 0x15,
            RM = 0x16,
            EXISTS = 0x17,
            MKSHARE = 0x20,
            RMSHARE = 0x21;

    /**
     * Sets the TLSClient instance bound to the control server
     *
     * @param controlSocket
     */
    public static void init(TLSClient controlSocket) {
        Control.controlSocket = controlSocket;
    }

    /**
     * Disconnects from the control server
     *
     * @throws IOException if an I/O error occurs
     */
    public static void disconnect() throws IOException {
        controlSocket.writeByte(DISCONNECT);
    }

    /**
     * Lists the files in the current working directory
     *
     * @return a map with each file's name and the last modified time, in Epoch
     * seconds
     * @throws IOException if an I/O error occurs
     */
    public static Map<String, Long[]> lsfile() throws IOException {
        Map<String, Long[]> fileMap = new HashMap<>();

        controlSocket.writeByte(LSFILE);
        int fileCount = controlSocket.readInt();

        if (fileCount != 0) {
            for (int i = 0; i < fileCount; i++) {
                fileMap.put(controlSocket.readUTF(), new Long[]{controlSocket.
                    readLong(), controlSocket.readLong()});
            }
        }

        return fileMap;
    }

    /**
     * Lists the directories in the current working directory
     *
     * @return a map with each directory's name and the last modified time, in
     * Epoch seconds
     * @throws IOException if an I/O error occurs
     */
    public static Map<String, Long> lsdir() throws IOException {
        Map<String, Long> dirMap = new HashMap<>();

        controlSocket.writeByte(LSDIR);
        int dirCount = controlSocket.readInt();

        if (dirCount != 0) {
            for (int i = 0; i < dirCount; i++) {
                dirMap.put(controlSocket.readUTF(), controlSocket.readLong());
            }
        }

        return dirMap;
    }

    /**
     * Changes the working directory
     *
     * @param path new directory path
     * @return true if the operation completed successfully, else false
     * @throws IOException if an I/O error occurs
     */
    public static boolean cd(String path) throws IOException {
        controlSocket.writeByte(CD);
        controlSocket.writeUTF(path);
        return controlSocket.readBoolean();
    }

    /**
     * Returns the current working directory
     *
     * @return current working directory on the control server
     * @throws IOException if an I/O error occurs
     */
    public static String cwd() throws IOException {
        controlSocket.writeByte(CWD);
        return controlSocket.readUTF();
    }

    /**
     * Creates a new directory
     *
     * @param dirName directory name
     * @return true if the operation completed successfully, false if the
     * specified directory already exists or if an I/O error occurs
     * @throws IOException if an I/O error occurs
     */
    public static boolean mkdir(String dirName) throws IOException {
        controlSocket.writeByte(MKDIR);
        controlSocket.writeUTF(dirName);
        return controlSocket.readBoolean();
    }

    /**
     * Renames a file or directory
     *
     * @param oldName old name
     * @param newName new name
     * @return true if the operation completed successfully, else false
     * @throws IOException if an I/O error occurs
     */
    public static boolean rename(String oldName, String newName) throws IOException {
        controlSocket.writeByte(RENAME);
        controlSocket.writeUTF(oldName);
        controlSocket.writeUTF(newName);
        return controlSocket.readBoolean();
    }

    /**
     * Deletes a file or directory
     *
     * @param fileName name of item to be deleted
     * @return true if the operation completed successfully, else false
     * @throws IOException if an I/O error occurs
     */
    public static boolean rm(String fileName) throws IOException {
        controlSocket.writeByte(RM);
        controlSocket.writeUTF(fileName);
        return controlSocket.readBoolean();
    }

    /**
     * Checks whether a file or directory exists
     *
     * @param fileName name of item to look for
     * @return true if the item exists, else false
     * @throws IOException if an I/O error occurs
     */
    public static boolean exists(String fileName) throws IOException {
        controlSocket.writeByte(EXISTS);
        controlSocket.writeUTF(fileName);
        return controlSocket.readBoolean();
    }

    /**
     * Creates a share link for the specified file or directory
     *
     * @param fileName item to be shared
     * @throws IOException if an I/O error occurs
     */
    public static void mkshare(String fileName) throws IOException {
        controlSocket.writeByte(MKSHARE);
        controlSocket.writeUTF(fileName);
    }

    /**
     * Revokes the share link of the specified file or directory
     *
     * @param fileName item whose share link is to be revoked
     * @return true if the operation completed successfully, else false
     * @throws IOException if an I/O error occurs
     */
    public static boolean rmshare(String fileName) throws IOException {
        controlSocket.writeByte(RMSHARE);
        controlSocket.writeUTF(fileName);
        return controlSocket.readBoolean();
    }

    /**
     * Checks whether the current working directory is the root
     *
     * @return true if the current working directory is the root, else false
     * @throws IOException
     */
    public static boolean isAtRoot() throws IOException {
        return cwd().equals("/");
    }
}
