package ericminio.crypto;

import ericminio.crypto.support.RSA;
import org.junit.Test;

import javax.crypto.Cipher;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

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
        PublicKey publicKey = publicKey();

        assertThat(publicKey.getAlgorithm(), equalTo("RSA"));
    }

    private RSAPublicKey publicKey() throws IOException, URISyntaxException, NoSuchAlgorithmException, InvalidKeySpecException {
        List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("rsa/public.key").toURI()));
        String encodedKey = lines.stream().filter(line ->
                !line.startsWith("-----BEGIN") && !line.startsWith("-----END"))
                .collect(Collectors.joining());
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(decodedKey);

        return (RSAPublicKey) keyFactory.generatePublic(publicSpec);
    }

    @Test
    public void privateKeyCreation() throws Exception {
        PrivateKey privateKey = privateKey();

        assertThat(privateKey.getAlgorithm(), equalTo("RSA"));
    }

    private RSAPrivateKey privateKey() throws IOException, URISyntaxException, NoSuchAlgorithmException, InvalidKeySpecException {
        List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("rsa/private.key").toURI()));
        String encodedKey = lines.stream().filter(line ->
                !line.startsWith("-----BEGIN") && !line.startsWith("-----END"))
                .collect(Collectors.joining());
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(decodedKey);

        return (RSAPrivateKey) keyFactory.generatePrivate(privateSpec);
    }

    @Test
    public void roundTrip() throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        byte[] encrypted = RSA.encrypt("hello world", publicKey(), cipher);
        byte[] decrypted = RSA.decript(encrypted, privateKey(), cipher);

        assertThat(new String(decrypted).trim(), equalTo("hello world"));
    }

}
