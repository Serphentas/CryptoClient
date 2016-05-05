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

import java.security.Security;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

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
        // add BC as provider and set the trust store
        
        Security.addProvider(new BouncyCastleProvider());
        System.setProperty("javax.net.ssl.trustStore", "TSBTrustStore");
        Scanner scanner = new Scanner(System.in);
        if (Authentication.login("asd", "asdasd")) {
            System.out.println("Client up");
            System.out.println("Commands:");
            System.out.println("\tlsdir");
            System.out.println("\tlsfile");
            System.out.println("\tcwd");
            System.out.println("\tcd");
            System.out.println("\tmkdir");
            System.out.println("\tdisconnect");
            boolean asd = true;
            String input;
            Iterator<Map.Entry<String, Long>> iterDirMap;
            Iterator<Map.Entry<String, Long[]>> iterFileMap;
            while (asd) {
                String cmd = scanner.next();
                switch (cmd) {
                    case "lsdir":
                        Map<String, Long> dirMap = Control.lsdir(Control.cwd());

                        iterDirMap = dirMap.entrySet().iterator();
                        while (iterDirMap.hasNext()) {
                            Map.Entry<String, Long> entry = iterDirMap.next();
                            System.out.println(entry.getKey() + " " + new Date(entry.getValue()));
                        }
                        break;
                    case "lsfile":
                        Map<String, Long[]> fileMap = Control.lsfile(Control.cwd());

                        iterFileMap = fileMap.entrySet().iterator();
                        while (iterFileMap.hasNext()) {
                            Map.Entry<String, Long[]> entry = iterFileMap.next();
                            System.out.println(entry.getKey() + " " + new Date(
                                    entry.getValue()[0]) + " " + entry.
                                    getValue()[1]);
                        }
                        break;
                    case "cwd":
                        System.out.println(Control.cwd());
                        break;
                    case "cd":
                        input = scanner.next();
                        System.out.println(Control.cd(input));
                        break;
                    case "mkdir":
                        input = scanner.next();
                        System.out.println(Control.mkdir(input));
                        break;
                    case "rm":
                        input = scanner.next();
                        System.out.println(Control.rm(input));
                        break;
                    case "disconnect":
                        asd = false;
                        Control.disconnect();
                        break;
                }
            }
        }
        
    }
}
