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

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPSClient;

public class FTPSClientasd {

    private static final FTPSClient ftps = new FTPSClient(false);

    public static void main(String[] args) {
        try {

            ftps.setEnabledProtocols(new String[]{"TLSv1.2"});
            //ftps.setEnabledCipherSuites(new String[]{"TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256","TLS_DHE_RSA_WITH_AES_128_GCM_SHA256"});
            ftps.connect("10.0.0.21");
            ftps.enterLocalPassiveMode();
            ftps.setFileType(FTPSClient.BINARY_FILE_TYPE);
            getReply();

            ftps.login("asd", "asd");
            getReply();

            ftps.sendCommand("PROT P");
            ftps.completePendingCommand();
            getReply();

            String file = "100Mb";
            ftps.retrieveFile("wpdb.sql", new FileOutputStream(new File("E:/test/wpdb.sql")));

            //client.storeFile(file, new FileInputStream(new File("E:/test/" + file)));
            //FTPFile[] arr = ftps.listFiles();
            getReply();

            /*for (FTPFile f : arr) {
                System.out.println(f.getName());
            }*/

            ftps.disconnect();
        } catch (Exception ex) {
            Logger.getLogger(FTPSClientasd.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void getReply() {
        System.out.println(Arrays.toString(ftps.getReplyStrings()));
    }
}
