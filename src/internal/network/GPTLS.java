package internal.network;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

public abstract class GPTLS {

    public static void setTLSParams(SSLSocket socket) {
        // sets the supported TLS protocol and cipher suites
        socket.setEnabledProtocols(new String[]{"TLSv1.2"});
        socket.setEnabledCipherSuites(new String[]{
            "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256"});
    }

    public static SSLContext getContext() throws Exception{
        // creates the SSLContext needed to instantiate the SSLSocket
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
        return sslContext;
    }
}
