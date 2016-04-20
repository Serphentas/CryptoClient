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
package internal.network;

public class test {

    public static void main(String[] args) throws Exception {
        /*String[] arr = new String[]{"-b","10.0.0.21","asd","asd","wpdb.sql","E:/test/wpdb.sql"};
        System.out.println(Hex.toHexString(GPCrypto.randomGen(2048)));
        test2.main(arr);*/
        /*CipherKeyGenerator gen = new Poly1305KeyGenerator();
        gen.init(new KeyGenerationParameters(new SecureRandom(), 256));
        byte[] key = gen.generateKey(), nonce = GPCrypto.randomGen(16);
        byte[] P = GPCrypto.randomGen(16), C = new byte[16];
        System.out.println(Hex.toHexString(P));

        Mac asd = new Poly1305(new AESEngine());
        CipherParameters cp = new KeyParameter(key);
        ParametersWithIV params = new ParametersWithIV(cp, nonce);
        asd.init(params);
        asd.update(P, 0, 0);
        asd.doFinal(C, 0);
        System.out.println(Hex.toHexString(C));
        
        ChaChaEngine cha = new ChaChaEngine();
        byte[] K = GPCrypto.randomGen(32), N = GPCrypto.randomGen(8);
        CipherParameters CP = new KeyParameter(K);
        ParametersWithIV PWI = new ParametersWithIV(CP, N);
        cha.init(true, PWI);*/
        /*CPCipher chacha = new CPCipher();
        //byte[] key = GPCrypto.randomGen(32), nonce = GPCrypto.randomGen(8);
        byte[] key = new byte[]{0x00, 0x00},
                nonce = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

        System.out.println(key.length
                + " " + nonce.length);

        InputStream fileplain = new FileInputStream(new File("E:/test/test.7z")),
                fileencread = new FileInputStream(new File("E:/test/chachaenc"));
        OutputStream fileenc = new FileOutputStream(new File("E:/test/chachaenc")),
                filedec = new FileOutputStream(new File("E:/test/chachadec"));

        CPCipher.setBufferSize(
                2048);

        long time = System.nanoTime();

        chacha.encrypt(key, nonce, fileplain, fileenc);

        System.out.println(
                238 / ((System.nanoTime() - time) / 1e9) + " MiB/s");

        time = System.nanoTime();

        chacha.decrypt(key, nonce, fileencread, filedec);

        System.out.println(
                238 / ((System.nanoTime() - time) / 1e9) + " MiB/s");*/
    }
}
