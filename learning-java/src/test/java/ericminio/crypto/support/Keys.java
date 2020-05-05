package ericminio.crypto.support;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class Keys {

    public static RSAPublicKey publicKey(Class aClass) throws IOException, URISyntaxException, NoSuchAlgorithmException, InvalidKeySpecException {
        List<String> lines = Files.readAllLines(Paths.get(aClass.getClassLoader().getResource("rsa/public.key").toURI()));
        String encodedKey = lines.stream().filter(line ->
                !line.startsWith("-----BEGIN") && !line.startsWith("-----END"))
                .collect(Collectors.joining());
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(decodedKey);

        return (RSAPublicKey) keyFactory.generatePublic(publicSpec);
    }

    public static RSAPrivateKey privateKey(Class aClass) throws IOException, URISyntaxException, NoSuchAlgorithmException, InvalidKeySpecException {
        List<String> lines = Files.readAllLines(Paths.get(aClass.getClassLoader().getResource("rsa/private.key").toURI()));
        String encodedKey = lines.stream().filter(line ->
                !line.startsWith("-----BEGIN") && !line.startsWith("-----END"))
                .collect(Collectors.joining());
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(decodedKey);

        return (RSAPrivateKey) keyFactory.generatePrivate(privateSpec);
    }
}
