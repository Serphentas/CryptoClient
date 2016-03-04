package Main;

import internal.crypto.GCMCipher;
import java.io.File;

public class testConcurrent implements Runnable {
    int i = 0;
    
    public testConcurrent(int i){
        this.i=i;
    }

    @Override
    public void run() {
        try {
            GCMCipher gcmc = new GCMCipher();
            gcmc.encrypt(new File("H:/test"+i));           
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
