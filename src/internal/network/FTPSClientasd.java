package internal.network;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;

public class FTPSClientasd {

    private static final FTPSClient client = new FTPSClient(false);

    public static void main(String[] args) {
        try {

            client.setRemoteVerificationEnabled(false);

            client.setEnabledProtocols(new String[]{"TLSv1.2"});
            client.connect("10.0.0.20");
            getReply();

            System.out.println("Remote system is " + client.getSystemName());
            getReply();

            client.enterLocalPassiveMode();
            getReply();

            client.login("asd", "asd");
            getReply();

            client.sendCommand("PROT P");
            getReply();

            String file = "100Mb";

            //client.storeFile(file, new FileInputStream(new File("E:/test/" + file)));
            FTPFile[] arr = client.mlistDir();
            getReply();
            for (FTPFile f : arr) {
                System.out.println(f.getName());
            }

            client.disconnect();
        } catch (Exception ex) {
            Logger.getLogger(FTPSClientasd.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void getReply() {
        System.out.println(Arrays.toString(client.getReplyStrings()));
    }
}
