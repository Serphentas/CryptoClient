package org.gity.internal.network;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.gity.internal.crypto.DefaultCipher;
import org.gity.internal.crypto.GCMCopy;
import org.gity.internal.crypto.GPCrypto;

public class test2 {

    public static void main(String[] args) throws Exception {
        /*Security.addProvider(new BouncyCastleProvider());
        Cipher c = Cipher.getInstance("AES/CTR/NoPadding",new BouncyCastleProvider());
        System.out.println(c.getProvider());
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, kg.generateKey());
        System.out.println(c.update(new byte[20]).length); // output: 16
        System.out.println(c.update(new byte[1]).length);  // null pointer exception*/

 /*Security.addProvider(new BouncyCastleProvider());
        long time = System.nanoTime();
        System.out.println(Hex.toHexString(SCrypt.generate("asd".getBytes("UTF-8"),
                GPCrypto.randomGen(64), (int) Math.pow(2, 20), 8, 10, 32)));
        System.out.println((System.nanoTime() - time) / 1e9);*/
 /*long time = System.nanoTime();
        System.out.println(Hex.toHexString(SCrypt.generate("asdfgasdfgasdfgasdfg".getBytes("UTF-8"),
                GPCrypto.randomGen(64), (int) Math.pow(2, 21), 8, 1, 32)));
        System.out.println((System.nanoTime() - time) / 1e9);
        time = System.nanoTime();
        System.out.println(Hex.toHexString(SCrypt.generate(GPCrypto.randomGen(64),
                GPCrypto.randomGen(64), (int) Math.pow(2, 18), 8, 1, 32)));
        System.out.println((System.nanoTime() - time) / 1e9);*/
 /*for(int i = 0; i<2; i++){
            new Thread(new shit(i)).start();
        }*/
        Security.addProvider(new BouncyCastleProvider());
        GCMCopy gcm = new GCMCopy();
        DefaultCipher.setEncryptionPassword(new char[]{'a', 's', 'd', 'f', 'a', 's', 'd', 'f', 'a', 's', 'd', 'f', 'a', 's', 'd', 'f', 'a', 's', 'd', 'f'});
        File src = new File("/home/xerxes/Documents/test500"),
                test = new File("/mnt/zshare/documents/coding/CryptoServer/users/asdasd/debian-8.4.0-amd64-netinst.iso"),
                dec = new File("dec"),
                enc = new File("enc");
        gcm.encrypt_V00(src, enc);
        System.out.println("begin dec");
        gcm.decrypt_V00(enc, dec);
    }

    public static void asd(char[] c) {
        SecureRandom rand = new SecureRandom();
        for (int i = 0; i < 10000; i++) {
            Arrays.fill(c, (char) rand.nextInt());
        }
    }

    static class shit implements Runnable {

        private final int iter;

        public shit(int i) {
            this.iter = i;
        }

        @Override
        public void run() {
            System.out.println(iter + " started");
            long time = System.nanoTime();
            try {
                byte[] digest = SCrypt.generate("asdfgasdfgasdfgasdfg".getBytes("UTF-8"), GPCrypto.
                        randomGen(64), (int) Math.pow(2, 21), 8, 1, 32);
                MessageDigest.isEqual(digest, digest);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(test2.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(iter + " done in " + (System.nanoTime() - time) / 1e9);
        }
    }

}
