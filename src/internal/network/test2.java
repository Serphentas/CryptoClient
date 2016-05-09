package internal.network;

import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class test2 {

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Cipher c = Cipher.getInstance("AES/CTR/NoPadding");
        System.out.println(c.getProvider());
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, kg.generateKey());
        System.out.println(c.update(new byte[20]).length); // output: 16
        System.out.println(c.update(new byte[1]).length);  // null pointer exception
    }

    public static void asd(char[] c) {
        SecureRandom rand = new SecureRandom();
        for (int i = 0; i < 10000; i++) {
            Arrays.fill(c, (char) rand.nextInt());
        }
    }

}
