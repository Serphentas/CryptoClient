package internal.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int currentTot;
    ServerSocket serversocket;
    Socket client;
    int bytesRead;
    Connect c = new Connect();
    BufferedReader input;
    PrintWriter output;

    public void start() throws IOException {
        System.out.println("Connection starting on port " + c.getPort());
        //make connection to client on port specified
        serversocket = new ServerSocket(c.getPort());

        //accept connection from client
        client = serversocket.accept();

        System.out.println("Waiting for connection from client...");

        try {
            logInfo();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void logInfo() throws Exception {
        //open buffered reader for reading data from client
        input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        String username = input.readLine();
        System.out.println("Serverside username: " + username);
        String password = input.readLine();
        System.out.println("Serverside password: " + password);

        //open printwriter for writing data to client
        output = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));

        if (username.equals(c.getUsername()) && password.equals(c.getPassword())) {
            output.println("Welcome, " + username);
        } else {
            output.println("Login Failed");
        }
        output.flush();
        output.close();
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
