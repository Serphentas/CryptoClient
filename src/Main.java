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
import internal.network.DataClient;
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

        // starting GUI
        LoginForm.main(null);

        // initializing I/O handlers
        DataClient.init();
    }
}
