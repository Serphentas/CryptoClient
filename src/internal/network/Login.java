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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.management.remote.JMXConnectorFactory;
import javax.swing.JOptionPane;

public final class Login {
    private static final int SERVER_PORT = 440;
    private static final String SERVER_NAME = "localhost";
    private final String FILENAME = null;
    private Socket socket;
    private BufferedReader read;
    private PrintWriter output;

    public void connect(String username, String password) throws Exception{
        //Create socket connection
        socket = new Socket(SERVER_NAME, SERVER_PORT);

        //create printwriter for sending login to server
        output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));


        //send username to server
        output.println(username);

        //send password to server
        output.println(GPCrypto.passHash(password));
        output.flush();

        //create Buffered reader for reading response from server
        read = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        //read response from server
        String response = read.readLine();
        System.out.println("Server says: " + response);

        //display response
        JOptionPane.showMessageDialog(null, response);
    }
}
