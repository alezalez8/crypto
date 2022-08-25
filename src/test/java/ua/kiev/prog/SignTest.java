package ua.kiev.prog;

import org.junit.Test;
import ua.kiev.prog.asymcrypt.SignFile;

import static org.junit.Assert.assertTrue;

public class SignTest extends BaseTest {
    @Test
    public void signTest() {
        SignFile signFile = new SignFile();
        signFile.generateKeys();

        signFile.setInputFile("/Users/ievgiienko/Desktop/crypto/crypto.ppt");
        String signature = signFile.sign();
        log("Signature: " + signature);

        assertTrue(signFile.verify(signature));
    }
}
