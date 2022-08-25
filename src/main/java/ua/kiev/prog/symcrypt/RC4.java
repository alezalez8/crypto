package ua.kiev.prog.symcrypt;

public class RC4 {

    private byte state[] = new byte[256];
    private int x;
    private int y;

    public RC4(byte[] key) {
        reset(key);
    }

    public void reset(byte[] key) {
        if (key == null || key.length == 0)
            throw new NullPointerException("Empty encryption key");

        for (int i = 0; i < 256; i++) {
            state[i] = (byte)i;
        }

        int j = 0;
        for (int i = 0; i < 256; i++) {
            j = (j + state[i] + key[i % key.length]) & 0xFF; /// !!! remove negative bit
            swap(i, j);
        }

        x = y = 0;
    }

    public byte[] crypt(byte[] buf) {
        byte[] result = new byte[buf.length];
        int index;
        byte key;

        for (int i = 0; i < buf.length; i++) {
            x = (x + 1) & 0xFF;
            y = (y + state[x]) & 0xFF;

            swap(x, y);

            index = (state[x] + state[y]) & 0xFF;
            key = state[index];

            result[i] = (byte) (buf[i] ^ key);
        }

        return result;
    }

    private void swap(int idx1, int idx2) {
        byte tmp = state[idx1];
        state[idx1] = state[idx2];
        state[idx2] = tmp;
    }
}