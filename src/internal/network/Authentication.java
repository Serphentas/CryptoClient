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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public final class Authentication {

    private static final int SERVER_PORT = 440;
    private static final String SERVER_NAME = "localhost";
    private static Socket socket;
    private static DataInputStream input;
    private static DataOutputStream output;

    public static int login(String username, String password) throws Exception {
        // opening socket
        socket = new Socket(SERVER_NAME, SERVER_PORT);
        System.out.println("Connected to " + socket.getRemoteSocketAddress());

        // opening I/O streams
        output = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());

        // sending credentials
        System.out.print("Logging in... ");
        output.write(GPCrypto.passHash(username));
        output.write(GPCrypto.passHash(password));
        output.flush();

        // returning response
        if (input.readInt() == 1) {
            System.out.print("success.\n");
            return 1;
        } else {
            System.out.print("failure.\n");
            return 0;
        }
    }
}
