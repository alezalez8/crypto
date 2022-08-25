package ua.kiev.prog.asymcrypt;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;

public class SignFile extends AsymBase {
    private String inputFile;
    private Signature signature;

    protected Provider provider;

    public String sign() {
        initialize();

        try (InputStream is = new FileInputStream(inputFile)) {
            signature.initSign(privateKey);
            processFile(is);

            return Hex.encodeHexString(signature.sign());

        } catch (IOException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verify(String sign) {
        initialize();

        try (InputStream is = new FileInputStream(inputFile)) {
            signature.initVerify(publicKey);
            processFile(is);

            byte[] signBytes = Hex.decodeHex(sign);
            return signature.verify(signBytes);

        } catch (IOException | InvalidKeyException | SignatureException | DecoderException e) {
            throw new RuntimeException(e);
        }
    }

    private void initialize()  {
        if (signature == null) {
            try {
                signature = Signature.getInstance("SHA256withRSA", provider);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processFile(InputStream is) throws IOException, SignatureException {
        byte[] buf = new byte[102400];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) {
                signature.update(buf, 0, r);
            }
        } while (r != -1);
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }
}
