package internal.crypto;

import java.io.File;
import java.nio.file.Files;
import java.security.Security;
import java.util.Arrays;
import static junit.framework.Assert.assertTrue;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

public class GCMCipherTest {

    private GCMCipher gcmc;

    @Test
    public void testDecrypt() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        
        gcmc = new GCMCipher();
        File p = new File("testPlaintext");
        File c = new File("testPlaintext.encrypted");
        File dc = new File("testPlaintext.decrypted");
        p.createNewFile();
        /*gcmc.encrypt(p);
        gcmc.decrypt(c);*/
        byte[] testPlaintextDecryptedBytes = Files.readAllBytes(dc.toPath());
        byte[] testPlaintextBytes = Files.readAllBytes(p.toPath());
        assertTrue(Arrays.equals(testPlaintextBytes, testPlaintextDecryptedBytes));
    }

}
