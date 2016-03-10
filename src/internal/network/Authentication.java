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

import internal.crypto.GPCrypto;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.Security;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import visual.ErrorHandler;

public final class Authentication {

    private static final int SERVER_PORT = 440;
    private static final String SERVER_NAME = "localhost";
    private static Socket socket;
    private static DataInputStream input;
    private static DataOutputStream output;
    private static SSLContext sslContext;

    public static int login(String username, String password) throws Exception {
        // opening socket and I/O streams
        socket = new Socket(SERVER_NAME, SERVER_PORT);
        output = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());

        // sending credentials
        output.write(GPCrypto.SHA384(username));
        output.write(GPCrypto.SHA384(password));
        output.flush();

        // returning response
        if (input.readInt() == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int loginTLS(String username, String password) throws Exception {
        createSSLContext();
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        SSLSocket ss = (SSLSocket) ssf.createSocket(SERVER_NAME, SERVER_PORT);

        output = new DataOutputStream(ss.getOutputStream());
        input = new DataInputStream(ss.getInputStream());

        // sending credentials
        output.write(GPCrypto.SHA384(username));
        output.write(GPCrypto.SHA384(password));
        output.flush();

        // returning response
        if (input.readInt() == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    private static void createSSLContext() {
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("ccserver.keystore"), "asdasd".toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, "asdasd".toCharArray());
            KeyManager[] km = kmf.getKeyManagers();

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);
            TrustManager[] tm = tmf.getTrustManagers();

            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(km, tm, new SecureRandom());
        } catch (Exception e) {
            ErrorHandler.showError(e);
        }
    }
}
