package ua.kiev.prog.asymcrypt;

import ua.kiev.prog.symcrypt.SymCryptor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public abstract class AsymBase {
    protected PrivateKey privateKey;
    protected PublicKey publicKey;

    public void generateKeys() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();

            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void saveKeys(String publicFile, String privateFile, String password) {
        byte[] pub = publicKey.getEncoded();
        byte[] priv = privateKey.getEncoded();

        try {
            Files.write(Paths.get(publicFile), pub);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        SymCryptor crypt = new SymCryptor();
        crypt.setInput(priv);
        crypt.setOutputFile(privateFile);
        crypt.setPassword(password);
        crypt.encrypt();
    }

    public void loadKeys(String publicFile, String privateFile, String password) {
        try {
            SymCryptor crypt = new SymCryptor();
            crypt.setInputFile(privateFile);
            crypt.setPassword(password);
            crypt.decrypt();

            byte[] priv = crypt.getOutput();
            byte[] pub = Files.readAllBytes(Paths.get(publicFile));

            KeyFactory factory = KeyFactory.getInstance("RSA");

            publicKey = factory.generatePublic(new X509EncodedKeySpec(pub));
            privateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(priv));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
