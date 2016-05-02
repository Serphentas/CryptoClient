package internal.network;

import java.io.File;

public class IO {

    private static TLSClient IOSocket;

    public static void init(TLSClient IOSocket) {
        IO.IOSocket = IOSocket;
    }

    public static void send(File input) {

    }

    public static void receive(String fileName, File output) {

    }
}
