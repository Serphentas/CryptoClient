/*
 * Copyright (C) 2016 Xerxes
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

import java.io.FileInputStream;
import java.net.InetAddress;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author Xerxes
 */
public class TLSClient {
    private static SSLContext sslContext;
    
    public static SSLSocket socketFactory() throws Exception{
        return (SSLSocket) sslContext.getSocketFactory().createSocket(InetAddress.getByName("localhost"), 440);
    }
    
    private static void createSSLContext() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("ccserver.keystore"), "asdasd".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        TrustManager[] tm = tmf.getTrustManagers();

        sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, tm, new SecureRandom());
    }
}
