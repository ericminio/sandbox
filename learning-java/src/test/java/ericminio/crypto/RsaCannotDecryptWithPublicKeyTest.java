package ericminio.crypto;

import ericminio.crypto.support.RSA;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;

import static ericminio.crypto.support.Keys.privateKey;
import static ericminio.crypto.support.Keys.publicKey;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class RsaCannotDecryptWithPublicKeyTest {

   @Test
    public void ofCourse() throws Exception {
       Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");

       byte[] encrypted = RSA.encrypt("hello world", publicKey(this.getClass()), cipher);
       try {
           RSA.decrypt(encrypted, publicKey(this.getClass()), cipher);
           fail();
       }
       catch (BadPaddingException e) {
           assertThat(e.getMessage(), equalTo("Decryption error"));

           byte[] decrypted = RSA.decrypt(encrypted, privateKey(this.getClass()), cipher);
           assertThat(new String(decrypted).trim(), equalTo("hello world"));
       }
    }
}
