package internal.network;

import internal.crypto.DefaultCipher;
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

    public static boolean upload(File input, String remoteFilePath) throws Exception {
        try {
            dos.writeByte(UPLOAD);
            dos.writeUTF(remoteFilePath);
            int reply = dis.readInt();
            
            if (reply == 0) {
                return DefaultCipher.encrypt(input);
            } else {
                switch(reply){
                    case 1:
                        System.out.println("file exists");
                        break;
                    case 2:
                        System.out.println("filename too short");
                        break;
                    case -1:
                        System.out.println("I/O error occured");
                        break;
                }
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
                return DefaultCipher.decrypt(output);
            } else {
                return false;
            }
        } catch (IOException ex) {
            ErrorHandler.showError(ex);
            return false;
        }
    }
}
