package internal.network;

import java.security.MessageDigest;

public class Connect {

    private String USERNAME = "java";

    private String PASSWORD = "java";
    private int PORT = 9090;
    private String HOSTNAME = "localhost";

    public String getUsername() {
        return this.USERNAME;
    }

    public String getPassword() {

        return this.PASSWORD;
    }

    public int getPort() {
        return this.PORT;
    }

    public String gethostName() {
        return this.HOSTNAME;
    }
}
