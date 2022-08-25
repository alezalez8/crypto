package ua.kiev.prog.pkcs11;

import sun.security.pkcs11.SunPKCS11;
import ua.kiev.prog.asymcrypt.SignFile;

import java.security.*;

public class Pkcs11Signer extends SignFile {

    public Pkcs11Signer(String config) {
        provider = new SunPKCS11(config);
        Security.addProvider(provider);
    }

    public void loadKeys(String pin) {
        try {
            KeyStore ks = KeyStore.getInstance("PKCS11", provider);
            ks.load(null, pin.toCharArray());

            String alias = ks.aliases().nextElement();

            privateKey = (PrivateKey) ks.getKey(alias, pin.toCharArray());
            publicKey = ks.getCertificate(alias).getPublicKey();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
