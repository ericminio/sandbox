package ericminio.crypto;

import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class RsaTest {

    @Test
    public void roundTrip() throws Exception {
        String input = "Hello world --- anyhting else -- just trying to exceed 128 --- to see if we can handle it by encrypting one chunk at a time -- no other way it seems";
        String output = rsaRoundTrip(input);

        assertThat(output, equalTo(input));
    }

    private String rsaRoundTrip(String input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair kp = kpg.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");

        byte[] encrypted = encrypt(input, publicKey, cipher);
        byte[] decrypted = decript(encrypted, privateKey, cipher);

        return new String(decrypted).trim();
    }

    private byte[] decript(byte[] encrypted, RSAPrivateKey privateKey, Cipher cipher) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return doIt(cipher, 128, encrypted);
    }

    private byte[] encrypt(String input, RSAPublicKey publicKey, Cipher cipher) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return doIt(cipher, 117, input.getBytes());
    }

    private byte[] doIt(Cipher c, int chunkSize, byte[] source) throws IllegalBlockSizeException, BadPaddingException {
        int position;
        byte[] output = new byte[0];
        position = 0;
        while (position < source.length) {
            byte[] chunk = new byte[chunkSize];
            int size = chunk.length;
            if (position + size > source.length) {
                size = source.length - position;
            }
            System.arraycopy(source, position, chunk, 0, size);
            byte[] cipher = c.doFinal(chunk);

            output = widen(output, cipher.length);
            System.arraycopy(cipher, 0, output, output.length - cipher.length, cipher.length);

            position += chunk.length;
        }
        return output;
    }

    private byte[] widen(byte[] decrypted, int length) {
        byte[] tmp = new byte[decrypted.length + length];
        System.arraycopy(decrypted, 0, tmp, 0, decrypted.length);
        decrypted = tmp;

        return decrypted;
    }
}
