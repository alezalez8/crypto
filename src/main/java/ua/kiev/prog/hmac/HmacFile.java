package ua.kiev.prog.hmac;

import ua.kiev.prog.hash.FileHash;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacFile extends FileHash {

    private final String algorithm;

    private String key;
    private Mac mac;

    public HmacFile(String algorithm) {
        super(algorithm);
        this.algorithm = algorithm;
    }

    @Override
    protected void hashInit(String algorithm) {
        try {
            mac = Mac.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void hashReset() {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
        try {
            mac.init(keySpec);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        mac.reset();
    }

    @Override
    protected void hashUpdate(byte[] buf, int offset, int count) {
        mac.update(buf, offset, count);
    }

    @Override
    protected byte[] hashFinal() {
        return mac.doFinal();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
