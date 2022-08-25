package ua.kiev.prog.symcrypt;

import javax.crypto.spec.SecretKeySpec;

public class KeyAndSalt {
    private final SecretKeySpec key;
    private final byte[] salt;

    public KeyAndSalt(SecretKeySpec key, byte[] salt) {
        this.key = key;
        this.salt = salt;
    }

    public SecretKeySpec getKey() {
        return key;
    }

    public byte[] getSalt() {
        return salt;
    }
}
