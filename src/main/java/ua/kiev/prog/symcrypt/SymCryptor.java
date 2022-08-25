package ua.kiev.prog.symcrypt;

import ua.kiev.prog.random.PasswordGenerator;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class SymCryptor {
    private static final int SALT_LENGTH = 16;

    private String inputFile;
    private byte[] input;
    private String outputFile;
    private ByteArrayOutputStream output;
    private String password;
    private Cipher cipher;

    public SymCryptor() {
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public void reset() {
        inputFile = null;
        outputFile = null;
        input = null;
        output = null;
        password = null;
    }

    public void encrypt() {
        try (InputStream is = getInputStream();
             OutputStream os = getOutputStream()) {

            IvParameterSpec iv = generateIV();
            KeyAndSalt keySalt = passwordToKey(null);

            cipher.init(Cipher.ENCRYPT_MODE, keySalt.getKey(), iv);

            os.write(iv.getIV()); // write vector
            os.write(keySalt.getSalt());

            process(is, os);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void decrypt() {
        try (InputStream is = getInputStream();
             OutputStream os = getOutputStream()) {

            byte[] iv = new byte[cipher.getBlockSize()];
            if (is.read(iv) != iv.length)
                throw new RuntimeException("Wrong input file format!");

            byte[] salt = new byte[SALT_LENGTH];
            if (is.read(salt) != SALT_LENGTH)
                throw new RuntimeException("Wrong input file format!");

            KeyAndSalt keySalt = passwordToKey(salt);

            cipher.init(Cipher.DECRYPT_MODE, keySalt.getKey(), new IvParameterSpec(iv));
            process(is, os);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream getInputStream() throws FileNotFoundException {
        return (inputFile != null) ? new FileInputStream(inputFile) : new ByteArrayInputStream(input);
    }

    private OutputStream getOutputStream() throws FileNotFoundException {
        if (outputFile != null) {
            return new FileOutputStream(outputFile);
        } else {
            output = new ByteArrayOutputStream();
            return output;
        }
    }

    private void process(InputStream is, OutputStream os) throws IOException, BadPaddingException, IllegalBlockSizeException {
        byte[] inBuf = new byte[102400];
        byte[] outBuf;
        int r;

        do {
            r = is.read(inBuf);
            if (r > 0) {
                outBuf = cipher.update(inBuf, 0, r);
                os.write(outBuf);
            }
        } while (r != -1);

        outBuf = cipher.doFinal();
        os.write(outBuf);
    }

    private IvParameterSpec generateIV() {
        byte[] iv = PasswordGenerator.generateBytes(cipher.getBlockSize());
        return new IvParameterSpec(iv);
    }

    private KeyAndSalt passwordToKey(byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] newSalt = (salt != null) ? salt : PasswordGenerator.generateBytes(SALT_LENGTH);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), newSalt, 1000, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec key = new SecretKeySpec(tmp.getEncoded(), "AES");

        return new KeyAndSalt(key, newSalt);
    }

    public byte[] getInput() {
        return input;
    }

    public byte[] getOutput() {
        return (output == null) ? null : output.toByteArray();
    }

    public void setInput(byte[] input) {
        this.input = input;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
