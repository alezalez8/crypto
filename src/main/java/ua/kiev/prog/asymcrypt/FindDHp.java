package ua.kiev.prog.asymcrypt;

import java.util.Arrays;

public class FindDHp {
    public static void main(String[] args) {
        calc(23);
    }

    // первообразный корень по модулю m
    static void calc(int m) {
        long[] r = new long[m - 1];

        for (int g = 1; g < m; g++) {
            Arrays.fill(r, 0);

            for (int i = 0; i < r.length; i++) {
                r[i] = pow(g, i) % m;
            }

            if (checkSequence(r)) {
                System.out.println("Sqr = " + g);
                System.out.println("Req = " + Arrays.toString(r));
            }
        }
    }

    static boolean checkSequence(long[] seq) {
        Arrays.sort(seq);

        for (int i = 1; i <= seq.length; i++) {
            if (seq[i - 1] != i)
                return false;
        }

        return true;
    }

    static long pow(int x, int n) {
        long res = 1;

        while (n-- > 0)
            res *= x;

        return res;
    }
}
