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

import visual.DefaultFrame;

/**
 * Main method
 * <p>
 * Sets up the GUI, so that the user can login in and start using the cloud.
 */
public class Main {

    public static void main(String args[]) throws Exception {
        // starting GUI
        /*long time = System.nanoTime();
         System.out.println("Time taken: " + (System.nanoTime() - time) / 1e12 + "ms");*/
        DefaultFrame.main(null);
    }
}
