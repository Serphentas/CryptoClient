package org.gity.internal.network;

import java.io.IOException;
import java.net.InetAddress;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.gity.visual.ErrorHandler;

/**
 * Contains NTP-related code
 *
 * Primarily used by the GCMCipher class for nonce generation
 *
 * @author Serphentas
 */
public class NTP {

    /**
     * Returns the current Unix epoch (in milliseconds), obtained by polling the
     * following NTP server:
     * <ul>
     * <li>0.ch.pool.ntp.org</li>
     * </ul>
     *
     * @return current Unix epoch, in milliseconds
     */
    public static long getTime() {
        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout(3000);
        String host = "0.ch.pool.ntp.org";
        long epoch = 0;

        try {
            InetAddress hostAddr = InetAddress.getByName(host);
            epoch = client.getTime(hostAddr).getReturnTime();
        } catch (IOException ex) {
            ErrorHandler.showError(ex);
        }

        client.close();
        return epoch;
    }
}
