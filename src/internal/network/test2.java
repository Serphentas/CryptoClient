/* 
 * Copyright (c) 2016, Serphentas
 * All rights reserved.
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/ or send a letter
 * to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
package internal.network;

import java.io.File;
import java.nio.file.Files;


public final class test2 {

    public static final void main(String[] args) throws Exception{
        Files.delete(new File("../CryptoServer/users/asd/wobowobo.txt").toPath());
    }
}
