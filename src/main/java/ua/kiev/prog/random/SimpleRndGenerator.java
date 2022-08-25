package ua.kiev.prog.random;

public class SimpleRndGenerator {

    private static final int A = 1103515245;
    private static final int M = Integer.MAX_VALUE;
    private static final int C = 12345;

    private int seed;

    public SimpleRndGenerator() {
        seed = (int)System.currentTimeMillis() % Integer.MAX_VALUE;
        seed += Runtime.getRuntime().freeMemory();
    }

    public int nextInt() {
        seed = (A * seed + C) % M;
        return seed;
    }
}
