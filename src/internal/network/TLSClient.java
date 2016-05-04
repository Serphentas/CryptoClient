/* 
 * Copyright (c) 2016, Serphentas
 * All rights reserved.
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/ or send a letter
 * to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
package internal.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

/**
 * Serves as an SSLSocket factory, connecting to the specified server
 *
 * @author Serphentas
 */
public class TLSClient {

    //private static SSLContext sslContext = null;
    private final SSLSocket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;

    /**
     * Instantiates an SSLSocket (strictly using TLSv1.2 and bound to provided
     * server) and its related I/O streams
     * <p>
     * Must be called before using any of the other methods
     *
     * @param server server hostname
     * @param port server port
     * @throws java.security.NoSuchAlgorithmException if no Provider supports a
     * SSLContextSpi implementation for TLS
     * @throws java.io.IOException if the socket cannot be created
     * @throws java.security.KeyManagementException if the SSLContext
     * initialization fails
     */
    public TLSClient(String server, int port) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        // creates the SSLContext needed to instantiate the SSLSocket
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);

        // creates an SSLSocket bound to the provided server
        socket = (SSLSocket) sslContext.getSocketFactory().createSocket(
                InetAddress.getByName(server), port);

        // sets the supported TLS protocol and cipher suites
        socket.setEnabledProtocols(new String[]{"TLSv1.2"});
        socket.setEnabledCipherSuites(new String[]{
            "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256"});

        // sets the I/O streams
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    /**
     * Closes the SSLSocket and its related I/O streams
     *
     * @throws IOException
     */
    public void disconnect() throws IOException {
        socket.close();
        dis.close();
        dos.close();
    }

    /**
     * Writes a byte into the SSLSocket
     *
     * @param input data to send
     * @throws IOException
     */
    public void writeByte(byte input) throws IOException {
        dos.write(input);
    }

    /**
     * Writes a byte array into the SSLSocket
     *
     * @param input data to send
     * @throws IOException
     */
    public void writeBytes(byte[] input) throws IOException {
        dos.write(input);
    }

    /**
     * Write an integer into the SSLSocket
     *
     * @param input integer to send
     * @throws IOException
     */
    public void writeInt(int input) throws IOException {
        dos.writeInt(input);
    }

    /**
     * Write a string into the SSLSocket, using UTF-8 as encoding method
     *
     * @param input string to send
     * @throws IOException
     */
    public void writeUTF(String input) throws IOException {
        dos.writeUTF(input);
    }

    /**
     * Write a string into the SSLSocket as a sequence of characters
     *
     * @param input string to send
     * @throws IOException
     */
    public void writeChar(String input) throws IOException {
        dos.writeChars(input);
    }

    /**
     * Reads an integer from the SSLSocket
     *
     * @return integer to receive
     * @throws IOException
     */
    public int readInt() throws IOException {
        return dis.readInt();
    }

    /**
     * Reads a long from the SSLSocket
     *
     * @return integer to receive
     * @throws IOException
     */
    public long readLong() throws IOException {
        return dis.readLong();
    }

    /**
     * Reads a boolean from the SSLSocket
     *
     * @return boolean to receive
     * @throws IOException
     */
    public boolean readBoolean() throws IOException {
        return dis.readBoolean();
    }

    /**
     * Reads a byte array from the SSLSocket and writes it in the provided byte
     * array
     *
     * @param output byte array to writeBytes response to
     * @throws IOException
     */
    public void readByte(byte[] output) throws IOException {
        dis.read(output);
    }

    /**
     * Reads a string from the SSLSocket
     *
     * @return string to receive
     * @throws IOException
     */
    public String readUTF() throws IOException {
        return dis.readUTF();
    }
}
