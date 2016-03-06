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
package Main;

import java.io.File;
import internal.crypto.*;
import java.lang.reflect.Field;

/**
 * Main method
 * <p>
 * Sets up the GUI, so that the user can login in and start using the cloud.
 */
public class Main {

    public static void main(String args[]) throws Exception {
        // starting GUI
        long time = System.nanoTime();
        for (int i = 1; i < 8; i++) {
            new Thread(new testConcurrent(i)).start();
        }

        System.out.println("Time taken: " + (System.nanoTime() - time) / 1e12 + "ms");
        //visual.LoginForm.main(null);
        /*GCMCipher gcmc = new GCMCipher();
         //for(int i = 1; i <10000; i*=10){
         //System.out.println("***"+Integer.toString(i)+"Mb***");

         //c.encrypt(f=new File("E:/test/"+Integer.toString(i)+"Mb"));
         File f1 = new File("E:/test/test");
         File f2 = new File("E:/test/1000Mb.encrypted");
         System.out.println("Beginning encryption...");
         gcmc.encrypt(f1);
         System.out.println("Done in " + (System.nanoTime() - gcmc.time) / 1e6 + "ms with avg. speed of " + (f1.length() / ((System.nanoTime() - gcmc.time) / 1e9)) / (Math.pow(2, 20)) + " MiB/s");
         System.out.println("Beginning decryption...");
         gcmc.decrypt(f2);
         System.out.println("Done in " + (System.nanoTime() - gcmc.time) / 1e6 + "ms with avg. speed of " + (f2.length() / ((System.nanoTime() - gcmc.time) / 1e9)) / (Math.pow(2, 20)) + " MiB/s");
         */
    }
}
