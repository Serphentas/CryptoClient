package internal.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Control {

    private static DataOutputStream dos;
    private static DataInputStream dis;
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
     * @param dos
     * @param dis
     */
    public static void init(DataOutputStream dos, DataInputStream dis) {
        Control.dos = dos;
        Control.dis = dis;
    }

    /**
     * Disconnects from the control server
     *
     * @throws IOException if an I/O error occurs
     */
    public static void disconnect() throws IOException {
        dos.writeByte(DISCONNECT);
    }

    /**
     * Lists the files in the specified directory
     *
     * @param dirPath path to directory
     * @return a map with each file's name and the last modified time, in Epoch
     * seconds
     * @throws IOException if an I/O error occurs
     */
    public static Map<String, Long[]> lsfile(String dirPath) throws IOException {
        Map<String, Long[]> fileMap = new HashMap<>();

        dos.writeByte(LSFILE);
        dos.writeUTF(dirPath);
        int fileCount = dis.readInt();

        if (fileCount != 0) {
            for (int i = 0; i < fileCount; i++) {
                fileMap.put(dis.readUTF(), new Long[]{dis.readLong(), dis.readLong()});
            }
        }

        return fileMap;
    }

    /**
     * Lists the directories in the specified directory
     *
     * @param dirPath path to directory
     * @return a map with each directory's name and the last modified time, in
     * Epoch seconds
     * @throws IOException if an I/O error occurs
     */
    public static Map<String, Long> lsdir(String dirPath) throws IOException {
        Map<String, Long> dirMap = new HashMap<>();

        dos.writeByte(LSDIR);
        dos.writeUTF(dirPath);
        int dirCount = dis.readInt();

        if (dirCount != 0) {
            for (int i = 0; i < dirCount; i++) {
                dirMap.put(dis.readUTF(), dis.readLong());
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
        dos.writeByte(CD);
        dos.writeUTF(path);
        return dis.readBoolean();
    }

    /**
     * Returns the current working directory
     *
     * @return current working directory on the control server
     * @throws IOException if an I/O error occurs
     */
    public static String cwd() throws IOException {
        dos.writeByte(CWD);
        return dis.readUTF();
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
        dos.writeByte(MKDIR);
        dos.writeUTF(dirName);
        return dis.readBoolean();
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
        dos.writeByte(RENAME);
        dos.writeUTF(oldName);
        dos.writeUTF(newName);
        return dis.readBoolean();
    }

    /**
     * Deletes a file or directory
     *
     * @param fileName name of item to be deleted
     * @return true if the operation completed successfully, else false
     * @throws IOException if an I/O error occurs
     */
    public static boolean rm(String fileName) throws IOException {
        dos.writeByte(RM);
        dos.writeUTF(fileName);
        return dis.readBoolean();
    }

    /**
     * Checks whether a file or directory exists
     *
     * @param fileName name of item to look for
     * @return true if the item exists, else false
     * @throws IOException if an I/O error occurs
     */
    public static boolean exists(String fileName) throws IOException {
        dos.writeByte(EXISTS);
        dos.writeUTF(fileName);
        return dis.readBoolean();
    }

    /**
     * Creates a share link for the specified file or directory
     *
     * @param fileName item to be shared
     * @throws IOException if an I/O error occurs
     */
    public static void mkshare(String fileName) throws IOException {
        dos.writeByte(MKSHARE);
        dos.writeUTF(fileName);
    }

    /**
     * Revokes the share link of the specified file or directory
     *
     * @param fileName item whose share link is to be revoked
     * @return true if the operation completed successfully, else false
     * @throws IOException if an I/O error occurs
     */
    public static boolean rmshare(String fileName) throws IOException {
        dos.writeByte(RMSHARE);
        dos.writeUTF(fileName);
        return dis.readBoolean();
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
