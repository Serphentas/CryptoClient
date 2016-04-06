package internal.network;

import java.security.NoSuchAlgorithmException;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.util.encoders.Hex;

public class test {
    public static void main(String[] args) throws NoSuchAlgorithmException{
        /*String[] arr = new String[]{"-b","10.0.0.21","asd","asd","wpdb.sql","E:/test/wpdb.sql"};
        test2.main(arr);*/
        long time = System.nanoTime();
        System.out.println(Hex.toHexString(SCrypt.generate("asd".getBytes(), "asd".getBytes(), 524288, 16, 1, 32)));
        System.out.println((System.nanoTime()-time)/1e9);
        time = System.nanoTime();
        System.out.println(Hex.toHexString(SCrypt.generate("asd".getBytes(), "asd".getBytes(), 524288, 8, 1, 32)));
        System.out.println((System.nanoTime()-time)/1e9);
        time = System.nanoTime();
        
    }
}
