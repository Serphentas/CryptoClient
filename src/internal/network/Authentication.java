/* 
 * Copyright (c) 2016, Serphentas
 * All rights reserved.
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/ or send a letter
 * to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
package internal.network;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Contains user authentication methods
 *
 * @author Serphentas
 */
public abstract class Authentication {

    private static final String AUTH_SERVER_NAME = "10.0.0.10",
            DATA_SERVER_NAME = "10.0.0.10";
    private static final int AUTH_SERVER_PORT = 4400,
            DATA_SERVER_PORT = 4410;
    private static final byte[] token_auth = new byte[128],
            token_data = new byte[128];

    /**
     * Authenticates the user against the server using TLS
     *
     * @param username username
     * @param password password
     * @return true if credentials are correct, false if they aren't
     * @throws java.io.IOException
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.KeyManagementException
     */
    public static boolean login(String username, String password) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        // initializing the TLSClient to enable user authentication
        TLSClient auth = new TLSClient(AUTH_SERVER_NAME, AUTH_SERVER_PORT);

        // sending credentials
        auth.writeUTF(username);
        auth.writeUTF(password);

        // getting token and returning response
        if (auth.readBoolean()) {
            auth.readByte(token_auth);
            auth.readByte(token_data);

            TLSClient data_ctrl = new TLSClient(DATA_SERVER_NAME, DATA_SERVER_PORT);
            data_ctrl.writeUTF(username);
            data_ctrl.writeBytes(token_auth);
            final boolean ctrlStatus = data_ctrl.readBoolean();

            /*TLSClient data_io = new TLSClient(DATA_SERVER_NAME, DATA_SERVER_PORT);
            data_io.writeUTF(username);
            data_io.writeBytes(token_data);
            final boolean ioStatus = data_io.readBoolean();*/
            if(ctrlStatus){
                Control.init(data_ctrl);
                //IO.init(data_ctrl);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
