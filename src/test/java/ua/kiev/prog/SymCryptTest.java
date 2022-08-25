package ua.kiev.prog;

import com.sun.tools.javac.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ua.kiev.prog.random.PasswordGenerator;
import ua.kiev.prog.symcrypt.RC4;
import ua.kiev.prog.symcrypt.SymCryptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SymCryptTest extends BaseTest {
    private static final String INPUT_FILE = "symtest_in.txt";
    private static final String OUTPUT_FILE = "symtest_out.dat";
    private static final String DECRYPTED_FILE = "symtest_dec.txt";
    private static final String TEXT = "I'm a very secret message :)";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void cleanup() {
        deleteFiles(List.of(INPUT_FILE, OUTPUT_FILE, DECRYPTED_FILE));
    }

    @Test
    public void testXor() {
        String text = "Hello world";
        byte[] textBuf = text.getBytes(StandardCharsets.UTF_8);

        byte[] key = PasswordGenerator.generateBytes(textBuf.length);

        // encrypt
        for (int i = 0; i < textBuf.length; i++)
            textBuf[i] ^= key[i];

        // decrypt
        for (int i = 0; i < textBuf.length; i++)
            textBuf[i] ^= key[i];

        assertEquals(text, new String(textBuf));
    }

    @Test
    public void passwordTest() throws IOException {
        byte[] inBuf = TEXT.getBytes(StandardCharsets.UTF_8);
        Files.write(Paths.get(INPUT_FILE), inBuf);

        String password = PasswordGenerator.generatePassword(16);

        // encrypt
        SymCryptor crypt = new SymCryptor();
        crypt.setInputFile(INPUT_FILE);
        crypt.setOutputFile(OUTPUT_FILE);
        crypt.setPassword(password);
        crypt.encrypt();

        crypt = new SymCryptor();
        crypt.setInputFile(OUTPUT_FILE);
        crypt.setOutputFile(DECRYPTED_FILE);
        crypt.setPassword(password);
        crypt.decrypt();

        byte[] decrypted = Files.readAllBytes(Paths.get(DECRYPTED_FILE));
        assertArrayEquals(inBuf, decrypted);

        log("Decrypted message: " + new String(decrypted, StandardCharsets.UTF_8));
    }

    @Test
    public void rc4test() {
        byte[] key = PasswordGenerator.generateBytes(32);
        byte[] inBuf = TEXT.getBytes(StandardCharsets.UTF_8);

        RC4 cipher = new RC4(key);
        byte[] outBuf = cipher.crypt(inBuf);

        cipher.reset(key);
        byte[] newBuf = cipher.crypt(outBuf);

        assertArrayEquals(newBuf, inBuf);
    }
}
