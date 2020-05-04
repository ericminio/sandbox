package ericminio.crypto.support;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSA {

    public static byte[] decript(byte[] encrypted, RSAPrivateKey privateKey, Cipher cipher) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return doIt(cipher, 128, encrypted);
    }

    public static byte[] encrypt(String input, RSAPublicKey publicKey, Cipher cipher) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return doIt(cipher, 117, input.getBytes());
    }

    private static byte[] doIt(Cipher c, int chunkSize, byte[] source) throws IllegalBlockSizeException, BadPaddingException {
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

    private static byte[] widen(byte[] decrypted, int length) {
        byte[] tmp = new byte[decrypted.length + length];
        System.arraycopy(decrypted, 0, tmp, 0, decrypted.length);
        decrypted = tmp;

        return decrypted;
    }
}
