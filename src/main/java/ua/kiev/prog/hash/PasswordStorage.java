package ua.kiev.prog.hash;

import ua.kiev.prog.random.PasswordGenerator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PasswordStorage {
    private static final byte[] SEPARATOR = { (byte)':' };

    private MessageDigest hash;
    private Map<String, User> accounts = new HashMap<>();

    public PasswordStorage() {
        try {
            hash = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(String login, String password) {
        byte[] salt = PasswordGenerator.generateBytes(16);
        byte[] passHash = calculateHash(salt, password);

        accounts.put(login, new User(salt, passHash));
    }

    public boolean check(String login, String password) {
        User user = accounts.get(login);
        if (user == null)
            return false;

        byte[] passHash = calculateHash(user.getSalt(), password);
        return Arrays.equals(passHash, user.getPassHash());
    }

    private byte[] calculateHash(byte[] salt, String password) {
        byte[] passBytes = password.getBytes(StandardCharsets.UTF_8);

        hash.reset();
        hash.update(salt);
        hash.update(SEPARATOR);
        hash.update(passBytes);

        return hash.digest();
    }

    @Override
    public String toString() {
        return "PasswordStorage {accounts=" + accounts + '}';
    }

    static class User {
        private final byte[] salt;
        private final byte[] passHash;

        public User(byte[] salt, byte[] passHash) {
            this.salt = salt;
            this.passHash = passHash;
        }

        public byte[] getSalt() {
            return salt;
        }

        public byte[] getPassHash() {
            return passHash;
        }

        @Override
        public String toString() {
            return "User {" +
                    "salt=" + Arrays.toString(salt) +
                    ", passHash=" + Arrays.toString(passHash) +
                    '}';
        }
    }
}
