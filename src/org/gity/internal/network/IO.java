package org.gity.internal.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import org.gity.internal.crypto.DefaultCipher;
import org.gity.visual.ErrorHandler;

/**
 * Contains file I/O methods, used for uploading/downloading binary data
 *
 * @author Serphentas
 */
public abstract class IO {

    private static final byte DISCONNECT = 0x00,
            UPLOAD = 0x10,
            DOWNLOAD = 0x11,
            MKSHARE = 0x20,
            RMSHARE = 0x21;
    private static final int EXEC_OK = 0;

    private static DataOutputStream dos;
    private static DataInputStream dis;

    public static void init(DataOutputStream dos, DataInputStream dis) throws IOException, Exception {
        DefaultCipher.init(dos, dis);
        IO.dos = dos;
        IO.dis = dis;
    }

    /**
     * Disconnects from the I/O server
     *
     * @throws IOException if an I/O error occurs
     */
    public static void disconnect() throws IOException {
        dos.writeByte(DISCONNECT);
    }

    /**
     * Sends a file to the I/O server
     *
     * @param input
     * @param remoteFilePath
     * @return
     * @throws Exception
     */
    public static int upload(File input, String remoteFilePath) throws Exception {
        try {
            dos.writeByte(UPLOAD);
            dos.writeUTF(remoteFilePath);

            int reply = dis.readInt();

            if (reply == EXEC_OK) {
                return DefaultCipher.encrypt(input);
            } else {
                return reply;
            }
        } catch (IOException ex) {
            ErrorHandler.showError(ex);
            return -2;
        }
    }

    /**
     * Downloads a file from the I/O server
     *
     * @param fileName
     * @param output
     * @return
     * @throws Exception
     */
    public static int download(String fileName, File output) throws Exception {
        try {
            dos.writeByte(DOWNLOAD);
            dos.writeUTF(fileName);

            int reply = dis.readInt();

            if (reply == EXEC_OK) {
                return DefaultCipher.decrypt(output);
            } else {
                return reply;
            }
        } catch (IOException ex) {
            ErrorHandler.showError(ex);
            return -2;
        }
    }
}
