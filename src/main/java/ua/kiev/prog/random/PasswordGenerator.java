package ua.kiev.prog.random;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL = "~!@#$%^&*()_+\"{}[];:'><?/.,-=`\\";

    private static SecureRandom random;

    static {
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }

    public static String generatePassword(int length,
                                          boolean useUpperCase,
                                          boolean useLowerCase,
                                          boolean useNumbers,
                                          boolean useSpecial) {
        StringBuilder res = new StringBuilder(length);
        StringBuilder chars = new StringBuilder();
        int index;

        if (useUpperCase)
            chars.append(UPPERCASE);
        if (useLowerCase)
            chars.append(LOWERCASE);
        if (useNumbers)
            chars.append(NUMBERS);
        if (useSpecial)
            chars.append(SPECIAL);

        for (int i = 0; i < length; i++) {
            index = random.nextInt(chars.length());
            res.append(chars.charAt(index));
        }

        seed(); // but not before next* method call !!

        return res.toString();
    }

    public static String generatePassword(int length) {
        return generatePassword(length, true, true, true, true);
    }

    public static byte[] generateBytes(int length) {
        byte[] buf = new byte[length];
        random.nextBytes(buf);

        seed(); // but not before next* method call !!

        return buf;
    }

    private static void seed() {
        random.setSeed(System.currentTimeMillis());
        random.setSeed(Runtime.getRuntime().freeMemory());
        random.setSeed(Runtime.getRuntime().maxMemory());
        random.setSeed(Runtime.getRuntime().totalMemory());
    }
}
