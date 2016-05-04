package internal.network;

import java.io.File;

public abstract class IO {

    private static TLSClient IOSocket;

    public static void init(TLSClient IOSocket) {
        IO.IOSocket = IOSocket;
    }

    public static void send(File input) {

    }

    public static void receive(String fileName, File output) {

    }
}
