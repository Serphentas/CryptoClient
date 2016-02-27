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
        gcmc.encrypt(new File("testPlaintext"));
        gcmc.decrypt(new File("testPlaintext.encrypted"));
        byte[] testPlaintextDecryptedBytes = Files.readAllBytes(new File("testPlaintext.decrypted").toPath());
        byte[] testPlaintextBytes = Files.readAllBytes(new File("testPlaintext").toPath());
        assertTrue(Arrays.equals(testPlaintextBytes, testPlaintextDecryptedBytes));
    }

}
