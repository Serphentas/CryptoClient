package internal.network;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Control {

    private static TLSClient controlSocket;
    private static final byte DISCONNECT = 0x00,
            LSFILE = 0x10,
            LSDIR = 0x11,
            CD = 0x12,
            CWD = 0x13,
            MKDIR = 0x14,
            RENAME = 0x17,
            RM = 0x18,
            EXISTS = 0x18,
            MKSHARE = 0x20,
            RMSHARE = 0x21;

    public static void init(TLSClient controlSocket) {
        Control.controlSocket = controlSocket;
    }

    public static void disconnect() throws IOException {
        controlSocket.writeByte(DISCONNECT);
    }

    public static void lsfile() throws IOException {
        controlSocket.writeByte(LSFILE);
        int fileCount = controlSocket.readInt();
        Map<String, Long> fileMap = new HashMap<>();

        for (int i = 0; i < fileCount; i++) {
            fileMap.put(controlSocket.readString(), controlSocket.readLong());
        }

        Iterator<Map.Entry<String, Long>> iter = fileMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Long> entry = iter.next();
            System.out.println(entry.getKey() + " " + new Date(entry.getValue()));
        }
    }

    public static void lsdir() throws IOException {
        controlSocket.writeByte(LSDIR);
        int dirCount = controlSocket.readInt();
        Map<String, Long> dirMap = new HashMap<>();

        for (int i = 0; i < dirCount; i++) {
            dirMap.put(controlSocket.readString(), controlSocket.readLong());
        }

        Iterator<Map.Entry<String, Long>> iter = dirMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Long> entry = iter.next();
            System.out.println(entry.getKey() + " " + new Date(entry.getValue()));
        }
    }

    public static void cd(String path) throws IOException {
        controlSocket.writeByte(CD);
        controlSocket.writeString(path);
    }

    public static String cwd() throws IOException {
        controlSocket.writeByte(CWD);
        return controlSocket.readString();
    }

    public static void mkdir(String dirName) throws IOException {
        controlSocket.writeByte(MKDIR);
    }

    public static void rename(String oldName, String newName) throws IOException {
        controlSocket.writeByte(RENAME);
        controlSocket.writeString(oldName);
        controlSocket.writeString(newName);
    }

    public static void rm(String fileName) throws IOException {
        controlSocket.writeByte(RM);
        controlSocket.writeString(fileName);
    }

    public static boolean exists(String fileName) throws IOException {
        controlSocket.writeByte(EXISTS);
        controlSocket.writeString(fileName);
        return controlSocket.readBoolean();
    }

    public static void mkshare(String fileName) throws IOException {
        controlSocket.writeByte(MKSHARE);
    }

    public static void rmshare(String fileName) throws IOException {
        controlSocket.writeByte(RMSHARE);
    }

    public static void test() throws IOException {
        byte asd = 0x3F;
        controlSocket.writeByte(asd);
    }
}
