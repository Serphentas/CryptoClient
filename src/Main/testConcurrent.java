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

import internal.crypto.GCMCipher;
import java.io.File;

public class testConcurrent implements Runnable {
    int i = 0;
    
    public testConcurrent(int i){
        this.i=i;
    }

    @Override
    public void run() {
        try {
            GCMCipher gcmc = new GCMCipher();
            gcmc.encrypt(new File("E:/test/test"+i));           
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
