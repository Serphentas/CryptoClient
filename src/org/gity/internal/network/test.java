/* 
 * Copyright (c) 2016, Serphentas
 * All rights reserved.
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/ or send a letter
 * to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
package org.gity.internal.network;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.crypto.generators.SCrypt;
import org.gity.internal.crypto.GPCrypto;

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

        // testing collision
        /*Security.addProvider(new BouncyCastleProvider());
        Field field = Class.forName("javax.crypto.JceSecurity").
                getDeclaredField("isRestricted");
        field.setAccessible(true);
        field.set(null, java.lang.Boolean.FALSE);
        String filename="somerandomshityoufaggot", file="gofuckyourselfbitch";
        
        System.out.println("original filename " + Hex.toHexString(filename.getBytes("UTF-8")));
        System.out.println("original file     " + Hex.toHexString(file.getBytes("UTF-8")));

        // instantiating AES-256 w/ GCM from Bouncy Castle
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        final byte[] shit = SCrypt.generate(GPCrypto.randomGen(128), GPCrypto.randomGen(128), 1024, 8, 1, 32);
        final SecretKey K = new SecretKeySpec(shit, "AES");
        final byte[] N = SCrypt.generate(GPCrypto.randomGen(128), GPCrypto.randomGen(128), 1024, 8, 1, 12);

        cipher.init(Cipher.ENCRYPT_MODE, K, new GCMParameterSpec(
                128, N, 0, 12));
        byte[] fileNameEnc = cipher.doFinal(filename.getBytes("UTF-8")),
                fileEnc = cipher.doFinal(file.getBytes("UTF-8"));
        System.out.println("filnename enc " + Hex.toHexString(fileNameEnc));
        System.out.println("file enc      " + Hex.toHexString(fileEnc));

        int i = 0;
        byte[] xored = new byte[fileNameEnc.length];
        for (byte b : fileNameEnc) {
            xored[i] = (byte) (b ^ fileEnc[i]);
        }

        cipher.init(Cipher.DECRYPT_MODE, K, new GCMParameterSpec(
                128, N, 0, 12));
        byte[] fileNameDec = cipher.doFinal(fileNameEnc);

        System.out.println("decrypted " + Hex.toHexString(fileNameDec));
        System.out.println("decrypted " + new String(fileNameDec, "UTF-8"));*/

 /*File f = new File("Z:/Documents/Coding/CryptoServer/users/asd/key2.asc");
        System.out.println(f.length());
        System.out.println(f.length() == (new File(
                "E:/key2.asc").length() + 553 + 16));*/

 /*Security.addProvider(new BouncyCastleProvider());
        GCMCopy gcm = new GCMCopy();
        gcm.setPassword(new char[]{'a','s','d'});
        File src = new File("README.md"),
                enc = new File("enc"),
                dec = new File("dec");
        gcm.encrypt(src, enc);
        gcm.decrypt(enc, dec);*/
 /*byte[] salt = new byte[]{0x6A, 0x57, 0x39, 0x6B, 0x78, 0x6A, 0x48, 0x75, 0x44, 0x54, 0x73, 0x3D};
        System.out.println("begin kdf");
        System.out.println(Hex.toHexString("jW9kxjHuDTs=".getBytes("UTF-8")));
        long time = System.nanoTime();
        System.out.println(Hex.toHexString(SCrypt.generate("fag".getBytes("UTF-8"), salt, 524288, 8, 1, 16)));
        System.out.println((System.nanoTime() - time) / 1e9);*/

        for(int i = 0; i<70; i++){
            new Thread(new shit(i)).start();
        }
 
    }
    
    static class shit implements Runnable{
        private int iter;
        
        public shit(int i){
            iter = i;
        }
        @Override
        public void run(){
            long time = System.nanoTime();
            try {
                byte[] digest = SCrypt.generate("asdfgasdfgasdfgasdfg".getBytes("UTF-8"), GPCrypto.
                        randomGen(64), (int) Math.pow(2, 14), 8, 1, 32);
                MessageDigest.isEqual(digest, digest);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
            }
            double time2 = (System.nanoTime() - time) / 1e9;
            System.out.println(iter + " done in " + time2);
        }
    }
}
