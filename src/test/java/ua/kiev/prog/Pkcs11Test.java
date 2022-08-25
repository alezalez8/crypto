package ua.kiev.prog;

import org.junit.Test;
import ua.kiev.prog.asymcrypt.SignFile;
import ua.kiev.prog.pkcs11.Pkcs11Signer;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertTrue;

public class Pkcs11Test extends BaseTest {

    private static final String PKCS11_CONFIG = "/Users/ievgiienko/MEGA/Prog.kiev.ua/crypto/pkcs11.config";

    @Test
    public void testSign() {
        Pkcs11Signer signer = new Pkcs11Signer(PKCS11_CONFIG);
        signer.loadKeys("Qweasdzxc1234=");

        signer.setInputFile("/Users/ievgiienko/Desktop/crypto/test.dat");
        String signature = signer.sign();
        log("Signature: " + signature);

        assertTrue(signer.verify(signature));
    }
}
