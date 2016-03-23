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

import internal.network.DataClient;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileHandler {

    private static final byte[] buffer = new byte[1024];

    private final File f;

    public FileHandler(File input) {
        this.f = input;
    }

    public File getFile() {
        return f;
    }

    /**
     * Writes to a file, using the InputStream as source
     *
     * @param output destination file
     * @param input InputStream from which to read data
     * @throws IOException
     */
    public static void receive(File output, InputStream input) throws IOException {
        OutputStream os = new FileOutputStream(output);
        int r = 0;

        while ((r = input.read(buffer)) > 0) {
            os.write(buffer, 0, r);
        }

        input.close();
        os.close();
    }

    public static void send(File inputFile, String remoteFilePath) throws IOException {
        OutputStream output = DataClient.outputStream(remoteFilePath);
        
        
        output.close();
    }
}
