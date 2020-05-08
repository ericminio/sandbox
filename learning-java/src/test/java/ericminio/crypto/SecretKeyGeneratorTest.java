package ericminio.crypto;

import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import static ericminio.crypto.support.RSA.decrypt;
import static ericminio.crypto.support.RSA.encrypt;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class SecretKeyGeneratorTest {

    @Test
    public void canGenerateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256, new SecureRandom());
        SecretKey key = keyGenerator.generateKey();
        System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));

        assertThat(key.getAlgorithm(), equalTo("AES"));
        assertThat(key, instanceOf(SecretKey.class));
    }

    @Test
    public void allowsRoundTripWithOneSingleKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256, new SecureRandom());
        SecretKey key = keyGenerator.generateKey();

        Cipher cipher = Cipher.getInstance("AES");
        byte[] encrypted = encrypt("hello secret key", key, cipher);
        byte[] decrypted = decrypt(encrypted, key, cipher);

        assertThat(new String(decrypted).trim(), equalTo("hello secret key"));
    }
}
