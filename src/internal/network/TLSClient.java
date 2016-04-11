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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * Serves as an SSLSocket factory
 *
 * @author Serphentas
 */
public abstract class TLSClient {

    private static SSLContext sslContext;
    private static SSLSocket socket;
    private static DataInputStream dis;
    private static DataOutputStream dos;

    /**
     * Instantiates an SSLSocket (strictly using TLS1.2 and bound to the service
     * provider server) and its related I/O streams
     * <p>
     * Must be called before using any of the other methods
     *
     * @param server hostname of the TLS server to connect to
     * @param port port of the TLS server
     * @throws Exception
     */
    public static void init(String server, int port) throws Exception {
        createSSLContext();
        socket = (SSLSocket) sslContext.getSocketFactory().createSocket(
                InetAddress.getByName(server), port);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * Closes the SSLSocket and its related I/O streams, effectively
     * disconnecting from the service provider
     *
     * @throws IOException
     */
    public static void disconnect() throws IOException {
        socket.close();
        dis.close();
        dos.close();
    }

    /**
     * Writes a byte array into the SSLSocket
     *
     * @param input data to send
     * @throws IOException
     */
    public static void write(byte[] input) throws IOException {
        dos.write(input);
    }
    
    public static void writeInt(int input) throws IOException{
        dos.writeInt(input);
    }

    /**
     * Reads an integer from the SSLSocket
     *
     * @return integer to receive
     * @throws IOException
     */
    public static int readInt() throws IOException {
        return dis.readInt();
    }

    /**
     * Reads a boolean from the SSLSocket
     *
     * @return boolean to receive
     * @throws IOException
     */
    public static boolean readBoolean() throws IOException {
        return dis.readBoolean();
    }

    /**
     * Reads a byte array from the SSLSocket and writes it in the provided byte
     * array
     *
     * @param output byte array to write response to
     * @throws IOException
     */
    public static void readByte(byte[] output) throws IOException {
        dis.read(output);
    }

    /**
     * Reads a String from the SSLSocket
     * @return String to receive
     * @throws IOException 
     */
    public static String readString() throws IOException {
        return dis.readUTF();
    }

    /**
     * Creates the SSLContext needed to instantiate the SSLSocket
     *
     * @throws Exception
     */
    public static void createSSLContext() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("ccserver.keystore"), "asdasd".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        TrustManager[] tm = tmf.getTrustManagers();

        sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, tm, new SecureRandom());
    }
}
