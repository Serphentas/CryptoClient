package internal.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.SecureRandom;

/**
 * General purpose cryptographic class
 * <p>
 * Methods which are used for cryptographic yet general purpose are defined here,
 * so that the GCMCipher class only contains encryption/decryption-related code.
 */
public final class GPCrypto {
    private static final SecureRandom rand = new SecureRandom();
     /**
     * Generates a random array of bytes
     * 
     * @param size
     *      width of the byte array
     * @return
     *      a random array of bytes, of length size
     */
    public static byte[] randomGen(int size){
        byte[] randBytes = new byte[size];
        rand.nextBytes(randBytes);
        return randBytes;
    }
    
    /**
     * Fills a byte array with random values to prevent future retrieval of its
     * original state
     * 
     * @param array 
     *      byte array to sanitize
     * @param passCount
     *      number of passes to make
     */
    public static void sanitize(byte[] array, int passCount){
        for(int i=0; i<passCount; i++){
            rand.nextBytes(array);
        }
    }
    
    /**
     * Overwrites a file with random bytes to prevent future retrieval of its
     * original state
     * 
     * @param input
     *      file to sanitize
     * @param passCount
     *      number of passes to make
     * @throws Exception 
     */
    public static void sanitize(File input, int passCount) throws Exception{
        final FileOutputStream fos = new FileOutputStream(input);

        if(input.length()>Math.pow(2,20)){
            FileInputStream fis = new FileInputStream(input);
            byte[] buffer = new byte[1024];
            int r;
            while((r=fis.read(buffer))>0){
                fos.write(buffer,0,r);
            }
            fos.close();
            fis.close();
        } else {
            for(int i=0; i<passCount; i++){
                rand.nextBytes(Files.readAllBytes(input.toPath()));
            }
            fos.close();
        }
    }
}
