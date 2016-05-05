/* 
 * Copyright (c) 2016, Serphentas
 * All rights reserved.
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/ or send a letter
 * to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import visual.LoginForm;

/**
 * Main method
 *
 * @author Serphentas
 */
public class Main {

    /**
     * Sets up the GUI, so that the user can login in and start using the
     * service.
     * <p>
     * Also adds Bouncy Castle as provider.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        // adding Bouncy Castle as provider
        Security.addProvider(new BouncyCastleProvider());
        
        // settings the TrustStore to The Swiss Bay
        System.setProperty("javax.net.ssl.trustStore", "TSBTrustStore");

        // starting GUI
        LoginForm.main(null);
    }
}
