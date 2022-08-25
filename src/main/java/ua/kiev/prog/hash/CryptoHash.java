package ua.kiev.prog.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoHash extends FileHash {
    private MessageDigest hash;

    public CryptoHash(String algorithm) {
        super(algorithm);
    }

    @Override
    protected void hashInit(String algorithm) {
        try {
            hash = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void hashReset() {
        hash.reset();
    }

    @Override
    protected void hashUpdate(byte[] buf, int offset, int count) {
        hash.update(buf, offset, count);
    }

    @Override
    protected byte[] hashFinal() {
        return hash.digest();
    }
}
