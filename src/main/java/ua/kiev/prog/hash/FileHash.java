package ua.kiev.prog.hash;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public abstract class FileHash {
    protected String inputFile;
    protected String hashValue;

    public FileHash(String algorithm) {
        hashInit(algorithm);
    }

    public void reset() {
        inputFile = null;
        hashValue = null;
    }

    public void generateHash() {
        byte[] res = calculateHash();
        hashValue = Hex.encodeHexString(res);
    }

    public boolean checkHash() {
        try {
            byte[] fileHash = calculateHash();
            byte[] userHash = Hex.decodeHex(hashValue);

            return Arrays.equals(fileHash, userHash);
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
    }

    protected byte[] calculateHash() {
        try (InputStream is = new FileInputStream(inputFile)) {
            byte[] buf = new byte[102400];
            int r;

            hashReset();

            do {
                r = is.read(buf);
                if (r > 0) {
                    hashUpdate(buf, 0, r);
                }
            } while (r != -1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

       return hashFinal();
    }

    protected abstract void hashInit(String algorithm);
    protected abstract void hashReset();
    protected abstract void hashUpdate(byte[] buf, int offset, int count);
    protected abstract byte[] hashFinal();

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }
}
