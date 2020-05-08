package ericminio.crypto;

import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import static ericminio.crypto.support.RSA.decrypt;
import static ericminio.crypto.support.RSA.encrypt;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class RsaKeysMatchNeedTest {

    Map<RSAPublicKey, RSAPrivateKey> keys;
    private RSAPublicKey first;
    private RSAPublicKey second;

    @Before
    public void havingTwoKeyPairs() throws NoSuchAlgorithmException {
        keys = new HashMap<>();
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);

        KeyPair kp;

        kp = kpg.generateKeyPair();
        first = (RSAPublicKey) kp.getPublic();
        keys.put(first, (RSAPrivateKey) kp.getPrivate());

        kp = kpg.generateKeyPair();
        second = (RSAPublicKey) kp.getPublic();
        keys.put(second, (RSAPrivateKey) kp.getPrivate());
    }

    @Test
    public void onePairCanBeUsedForRoundTrip() throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        byte[] encrypted = encrypt("hello world", first, cipher);
        byte[] decrypted = decrypt(encrypted, keys.get(first), cipher);

        assertThat(new String(decrypted).trim(), equalTo("hello world"));
    }

    @Test
    public void wrongKeyFailsToDecryptMessage() throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        byte[] encrypted = encrypt("hello world", first, cipher);

        try {
            decrypt(encrypted, keys.get(second), cipher);
            fail();
        }
        catch (Exception e) {
            assertThat(e, instanceOf(BadPaddingException.class));
        }
    }

}
