package internal.network;

import internal.crypto.GCMCipher;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import visual.ErrorHandler;

/**
 * Contains file I/O methods, used for uploading and downloading binary data
 *
 * @author Serphentas
 */
public abstract class IO {

    private static final byte DISCONNECT = 0x00,
            UPLOAD = 0x10,
            DOWNLOAD = 0x11;

    private static GCMCipher gcmc;
    private static DataOutputStream dos;
    private static DataInputStream dis;

    public static void init(DataOutputStream dos, DataInputStream dis) throws IOException, Exception {
        gcmc = new GCMCipher(dos, dis);
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

    public static boolean upload(File input, String remoteFilePath) throws Exception {
        try {
            dos.writeByte(UPLOAD);
            dos.writeUTF(remoteFilePath);
            if (dis.readBoolean()) {
                return gcmc.encrypt(input);
            } else {
                return false;
            }
        } catch (IOException ex) {
            ErrorHandler.showError(ex);
            return false;
        }
    }

    public static boolean download(String fileName, File output) throws Exception {
        try {
            dos.writeByte(DOWNLOAD);
            dos.writeUTF(fileName);
            if (dis.readBoolean()) {
                return gcmc.decrypt(fileName, output);
            } else {
                return false;
            }
        } catch (IOException ex) {
            ErrorHandler.showError(ex);
            return false;
        }
    }
}
