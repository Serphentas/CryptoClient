package internal.network;

import java.io.File;

public class test2 {

    public static void main(String[] args) throws Exception {
        String path = "E:/Saved pictures";
        File f = new File(path);
        System.out.println(f.exists());
    }

}
