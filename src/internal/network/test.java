package internal.network;

import internal.crypto.GPCrypto;
import java.security.NoSuchAlgorithmException;
import org.bouncycastle.util.encoders.Hex;

public class test {
    public static void main(String[] args) throws NoSuchAlgorithmException{
        /*String[] arr = new String[]{"-b","10.0.0.21","asd","asd","wpdb.sql","E:/test/wpdb.sql"};
        test2.main(arr);*/
        long time = System.nanoTime();
        System.out.println(Hex.toHexString(GPCrypto.randomGen(2048)));
        System.out.println((System.nanoTime()-time)/1e9);
        time = System.nanoTime();
        System.out.println(Hex.toHexString(GPCrypto.randomGen(32)));
        System.out.println((System.nanoTime()-time)/1e9);
        time = System.nanoTime();
        
    }
}
