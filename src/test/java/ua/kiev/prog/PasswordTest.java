package ua.kiev.prog;

import org.junit.Test;
import ua.kiev.prog.random.PasswordGenerator;
import ua.kiev.prog.random.SimpleRndGenerator;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PasswordTest extends BaseTest {
    @Test
    public void passwordTest() {
        String pass1 = PasswordGenerator.generatePassword(16,
                true, true, true, true);
        String pass2 = PasswordGenerator.generatePassword(16,
                true, true, true, false);

        log(pass1);
        log(pass2);

        assertEquals(16, pass1.length());
    }

    @Test
    public void simpleTest() {
        SimpleRndGenerator generator = new SimpleRndGenerator();

        int[] buf = new int[10];
        for (int i = 0; i < buf.length; i++)
            buf[i] = generator.nextInt();

        log(Arrays.toString(buf));
    }
}
