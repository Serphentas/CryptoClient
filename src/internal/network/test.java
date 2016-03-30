package internal.network;

import internal.file.FileHandler;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class test {

    private static final String host = "10.0.0.20";
    private static final int port = 21;
    private static final String[] credentials = new String[]{"asd", "asd"};

    public static void main(String args[]) throws UnknownHostException, IOException {
        try {
            Security.addProvider(new BouncyCastleProvider());
            DataClient.init();
            FileHandler.init();

            DataClient.connect(host, port);
            DataClient.login(credentials[0], credentials[1]);

            FileHandler.send(new File("E:/test/shitplain"), "shit");
            FileHandler.receive("shit", new File("E:/test/shitdec"));
        } catch (Exception ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}