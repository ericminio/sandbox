package ericminio.crypto;

import ericminio.crypto.support.RSA;
import org.junit.Test;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import static ericminio.crypto.support.Keys.privateKey;
import static ericminio.crypto.support.Keys.publicKey;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class RsaKeyCreationFromFilesTest {

    @Test
    public void usedOnceToGenerateOneKeyPairAndPopulateResourceFiles() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair kp = kpg.generateKeyPair();

        System.out.println("-----BEGIN RSA PUBLIC KEY-----");
        PublicKey publicKey = kp.getPublic();
        assertThat(publicKey, instanceOf(RSAPublicKey.class));
        System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        System.out.println("-----END RSA PUBLIC KEY-----");

        System.out.println("-----BEGIN RSA PRIVATE KEY-----");
        PrivateKey privateKey = kp.getPrivate();
        assertThat(privateKey, instanceOf(RSAPrivateKey.class));
        System.out.println(Base64.getEncoder().encodeToString((privateKey).getEncoded()));
        System.out.println("-----END RSA PRIVATE KEY-----");
    }

    @Test
    public void publicKeyCreation() throws Exception {
        PublicKey publicKey = publicKey(this.getClass());

        assertThat(publicKey.getAlgorithm(), equalTo("RSA"));
    }

    @Test
    public void privateKeyCreation() throws Exception {
        PrivateKey privateKey = privateKey(this.getClass());

        assertThat(privateKey.getAlgorithm(), equalTo("RSA"));
    }

    @Test
    public void roundTrip() throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        byte[] encrypted = RSA.encrypt("hello world", publicKey(this.getClass()), cipher);
        byte[] decrypted = RSA.decrypt(encrypted, privateKey(this.getClass()), cipher);

        assertThat(new String(decrypted).trim(), equalTo("hello world"));
    }

}
