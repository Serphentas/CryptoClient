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

import internal.crypto.GPCrypto;

/**
 * Contains user authentication methods
 *
 * @author Serphentas
 */
public abstract class Authentication {

    private static final String AUTH_SERVER_NAME = "localhost",
            DATA_SERVER_NAME = "localhost";
    private static final int AUTH_SERVER_PORT = 440,
            DATA_SERVER_PORT = 441;
    private static final byte[] token = new byte[48];

    /**
     * Authenticates the user against the server using TLS
     *
     * @param username username
     * @param password password
     * @return true if credentials are correct, false if they aren't
     * @throws Exception
     */
    public static boolean login(String username, String password) throws Exception {
        // initializing the TLSClient to enable user authentication
        TLSClient.init(AUTH_SERVER_NAME, AUTH_SERVER_PORT);

        // sending credentials
        TLSClient.write(GPCrypto.SHA384(username));
        TLSClient.write(GPCrypto.SHA384(password));

        // getting token and returning response
        if (TLSClient.readBoolean()) {
            TLSClient.readByte(token);

            TLSClient.init(DATA_SERVER_NAME, DATA_SERVER_PORT);
            TLSClient.write(GPCrypto.SHA384(username));
            TLSClient.write(token);

            return TLSClient.readBoolean();
        } else {
            return false;
        }
    }

}
