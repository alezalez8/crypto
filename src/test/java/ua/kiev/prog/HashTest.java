package ua.kiev.prog;

import org.junit.Test;
import ua.kiev.prog.hash.Crc32Hash;
import ua.kiev.prog.hash.CryptoHash;
import ua.kiev.prog.hash.FileHash;
import ua.kiev.prog.hash.PasswordStorage;
import ua.kiev.prog.hmac.HmacFile;

import static org.junit.Assert.*;

public class HashTest extends BaseTest {
    @Test
    public void cryptoHashTest() {
        FileHash fileHash = new CryptoHash("SHA-256");

        fileHash.setInputFile("/Users/ievgiienko/Desktop/crypto/crypto.ppt");
        fileHash.generateHash();

        log("Hash: " + fileHash.getHashValue());
        log("Check: " + fileHash.checkHash());

        assertEquals(64, fileHash.getHashValue().length());
        assertTrue(fileHash.checkHash());
    }

    @Test
    public void crc32HashTest() {
        FileHash fileHash = new Crc32Hash("CRC32");

        fileHash.setInputFile("/Users/ievgiienko/Desktop/crypto/crypto.ppt");
        fileHash.generateHash();

        log("Hash: " + fileHash.getHashValue());
        log("Check: " + fileHash.checkHash());

        assertEquals(8, fileHash.getHashValue().length());
        assertTrue(fileHash.checkHash());
    }

    @Test
    public void passwordStorageTest() {
        PasswordStorage storage = new PasswordStorage();

        storage.add("admin", "password");
        storage.add("user", "iloveyou");
        storage.add("tester", "iloveyou");

        assertTrue(storage.check("admin", "password"));
        assertTrue(storage.check("user", "iloveyou"));
        assertFalse(storage.check("user", "hello111"));

        log(storage.toString());
    }

    @Test
    public void hmacTest()  {
        HmacFile fileHmac = new HmacFile("HmacSHA256");

        fileHmac.setInputFile("/Users/ievgiienko/Desktop/crypto/crypto.ppt");
        fileHmac.setKey("secretkey");

        fileHmac.generateHash();

        log("HMAC: " + fileHmac.getHashValue());
        log("Check: " + fileHmac.checkHash());

        assertEquals(64, fileHmac.getHashValue().length());
        assertTrue(fileHmac.checkHash());
    }

    @Test
    public void hmacTest2()  {
        HmacFile fileHmac = new HmacFile("HmacSHA256");

        fileHmac.setInputFile("/Users/ievgiienko/Desktop/crypto/crypto.ppt");
        fileHmac.setKey("secretkey");
        fileHmac.generateHash();

        fileHmac.setKey("secretkey111"); // !!!

        log("Check: " + fileHmac.checkHash());

        assertFalse(fileHmac.checkHash());
    }
}
