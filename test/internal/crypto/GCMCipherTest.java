package internal.crypto;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

public class GCMCipherTest {

    private GCMCipher gcmc;

    @Test
    public void testDecrypt() throws Exception {
        gcmc = new GCMCipher();
        File p = new File("testPlaintext");
        File c = new File("testPlaintext.encrypted");
        File dc = new File("testPlaintext.decrypted");
        p.createNewFile();
        gcmc.encrypt(p);
        gcmc.decrypt(c);
        byte[] testPlaintextDecryptedBytes = Files.readAllBytes(dc.toPath());
        byte[] testPlaintextBytes = Files.readAllBytes(p.toPath());
        assertTrue(Arrays.equals(testPlaintextBytes, testPlaintextDecryptedBytes));
    }

}
