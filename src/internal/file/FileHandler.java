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
package internal.file;

import internal.crypto.GCMCipher;
import internal.network.DataClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHandler {

    private static GCMCipher gcmc;

    public static void init() throws Exception {
        gcmc = new GCMCipher();
    }

    /**
     * Receives and decrypts a remote file to the local filesystem
     *
     * @param outputFile destination File
     * @param remoteFilePath remote file location
     * @throws IOException
     */
    public static void receive(String remoteFilePath, File outputFile) throws IOException, Exception {
        gcmc.decrypt(DataClient.inputStream(remoteFilePath), new FileOutputStream(outputFile));

    }

    /**
     * Encrypts and sends a local file to the data server
     *
     * @param inputFile source File
     * @param remoteFilePath remote file location
     * @throws IOException
     * @throws Exception
     */
    public static void send(File inputFile, String remoteFilePath) throws IOException, Exception {
        gcmc.encrypt(new FileInputStream(inputFile), DataClient.outputStream(remoteFilePath));
    }
}
