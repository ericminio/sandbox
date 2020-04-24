package ericminio.opensaml;

import net.shibboleth.utilities.java.support.collection.LazySet;
import net.shibboleth.utilities.java.support.xml.XMLParserException;
import org.junit.Before;
import org.junit.Test;
import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.security.credential.Credential;
import org.opensaml.security.credential.CredentialContextSet;
import org.opensaml.security.credential.UsageType;
import org.opensaml.xmlsec.encryption.support.DataEncryptionParameters;
import org.opensaml.xmlsec.encryption.support.Encrypter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class DataEncryptionTest {

    private static Logger log = LoggerFactory.getLogger(DataEncryptionTest.class);
    private Encrypter encrypter;

    @Before
    public void initialization() throws Exception {
        InitializationService.initialize();
        encrypter = new Encrypter();
    }

    @Test
    public void hasNoDeterministicContentWhenUsingOnlySecretKey() throws Exception {
        Document document = parseXmlFileIntoDocument("data-sample.xml");
        XMLObject source = buildXmlObjectFromDocument(document);

        SecretKey secretKey = new SecretKey() {
            @Override
            public String getAlgorithm() {
                return "AES";
            }

            @Override
            public String getFormat() {
                return "RAW";
            }

            @Override
            public byte[] getEncoded() {
                return "1234567887654321".getBytes();
            }
        };
        DataEncryptionParameters customParameters = new DataEncryptionParameters();
        customParameters.setEncryptionCredential(buildCredentialWithSecretKey(secretKey));

        String first = encrypter.encryptElement(source, customParameters).getCipherData().getCipherValue().getValue();
        String second = encrypter.encryptElement(source, customParameters).getCipherData().getCipherValue().getValue();
        assertThat(first, not(equalTo(second)));

        int firstSize = first.length();
        int secondSize = second.length();
        assertThat(firstSize, equalTo(secondSize));
        assertThat(firstSize, equalTo(563));
    }

    private Credential buildCredentialWithSecretKey(SecretKey secretKey) {
        return new Credential() {
            @Nullable
            @Override
            public String getEntityId() {
                return null;
            }

            @Nullable
            @Override
            public UsageType getUsageType() {
                return UsageType.UNSPECIFIED;
            }

            @Nonnull
            @Override
            public Collection<String> getKeyNames() {
                return new LazySet<>();
            }

            @Nullable
            @Override
            public PublicKey getPublicKey() {
                return null;
            }

            @Nullable
            @Override
            public PrivateKey getPrivateKey() {
                return null;
            }

            @Nullable
            @Override
            public SecretKey getSecretKey() {
                return secretKey;
            }

            @Nullable
            @Override
            public CredentialContextSet getCredentialContextSet() {
                return new CredentialContextSet();
            }

            @Nonnull
            @Override
            public Class<? extends Credential> getCredentialType() {
                return null;
            }
        };
    }

    private XMLObject buildXmlObjectFromDocument(Document document) throws Exception {
        Element element = document.getDocumentElement();
        return XMLObjectProviderRegistrySupport.getUnmarshallerFactory().getUnmarshaller(element).unmarshall(element);
    }

    protected Document parseXmlFileIntoDocument(String xmlFilename) throws XMLParserException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(xmlFilename);
        Document document = XMLObjectProviderRegistrySupport.getParserPool().parse(is);

        return document;
    }
}
