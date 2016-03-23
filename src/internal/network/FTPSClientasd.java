package internal.network;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;

public class FTPSClientasd {

    private static final FTPSClient ftps = new FTPSClient(false);

    public static void main(String[] args) {
        try {

            ftps.setEnabledProtocols(new String[]{"TLSv1.2"});
            ftps.setEnabledCipherSuites(new String[]{"TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256"});
            ftps.connect("10.0.0.20");
            getReply();

            ftps.enterLocalPassiveMode();
            getReply();
            
            System.out.println(Arrays.toString(ftps.getEnabledCipherSuites()));

            ftps.login("asd", "asd");
            getReply();

            ftps.getSystemName();
            getReply();

            ftps.sendCommand("PROT P");
            getReply();

            String file = "100Mb";

            //client.storeFile(file, new FileInputStream(new File("E:/test/" + file)));
            FTPFile[] arr = ftps.listDirectories("/");

            for (FTPFile f : arr) {
                System.out.println(f.getName());
            }

            ftps.disconnect();
        } catch (Exception ex) {
            Logger.getLogger(FTPSClientasd.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void getReply() {
        System.out.println(Arrays.toString(ftps.getReplyStrings()));
    }
}
