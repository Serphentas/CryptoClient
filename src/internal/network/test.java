package internal.network;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class test {

    public static void main(String args[]) throws UnknownHostException, IOException {
        /*String[] param = new String[] {"-l","10.0.0.20","asd","asd"};
        FTPClientExample.main(param);
        FTPClient client = new FTPClient();
        client.connect("10.0.0.20");
        client.enterLocalPassiveMode();
        client.login("asd", "asd");

        //client.sendCommand("PROT P");
        System.out.println(client.getReplyString());

        FTPFile[] arr = client.listFiles("/");
        System.out.println("R/W rights owner group         size    Date     Name");
        System.out.println("     |       |     |             |      |         |");
        boolean exists = false;
        String file = "100Mb";
        for (FTPFile f : arr) {
            System.out.println(f);
            if (f.getName().equals(file)) {
                exists = true;
            }
        }
        if (exists) {
            client.deleteFile(file);
        }
        client.storeFile(file, new FileInputStream(new File("E:/test/"+file)));

        client.disconnect();*/
        String[] param = new String[] {"-s","10.0.0.20","asd","asd","10Mb","10Mb"};
        try {
            test2.main(param);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
