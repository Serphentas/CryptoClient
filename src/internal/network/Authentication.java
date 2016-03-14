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

import internal.crypto.GPCrypto;

/**
 * Contains user authentication methods
 *
 * @author Serphentas
 */
public abstract class Authentication {
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
        TLSClient.initAuth();

        // sending credentials
        TLSClient.write(GPCrypto.SHA384(username));
        TLSClient.write(GPCrypto.SHA384(password));

        // getting token and returning response
        if(TLSClient.readBoolean()){
            TLSClient.readByte(token);
            return true;
        } else {
            return false;
        }
    }
}
