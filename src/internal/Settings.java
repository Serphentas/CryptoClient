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
package internal;

public abstract class Settings {

    private static boolean parallelCrypto = false;
    private static int parallelCryptoThreads = 2;

    public static boolean isParallelCrypto() {
        return parallelCrypto;
    }

    public static void setParallelCrypto(boolean newParallelCrypto) {
        parallelCrypto = newParallelCrypto;
    }

    public static int getParallelCryptoThreads() {
        return parallelCryptoThreads;
    }

    public static void setParallelCryptoThreads(int newParallelCryptoThreads) {
        parallelCryptoThreads = newParallelCryptoThreads;
    }
}
