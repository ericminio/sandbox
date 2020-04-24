package ericminio.opensaml;

import net.shibboleth.utilities.java.support.xml.ParserPool;
import net.shibboleth.utilities.java.support.xml.QNameSupport;
import net.shibboleth.utilities.java.support.xml.SerializeSupport;
import net.shibboleth.utilities.java.support.xml.XMLParserException;
import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Test;
import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.Marshaller;
import org.opensaml.core.xml.io.Unmarshaller;
import org.opensaml.core.xml.io.UnmarshallingException;
import org.opensaml.security.credential.Credential;
import org.opensaml.xmlsec.algorithm.AlgorithmSupport;
import org.opensaml.xmlsec.encryption.EncryptedData;
import org.opensaml.xmlsec.encryption.EncryptedKey;
import org.opensaml.xmlsec.encryption.support.*;
import org.opensaml.xmlsec.keyinfo.impl.StaticKeyInfoCredentialResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import support.SignableSimpleXMLObject;

import javax.crypto.SecretKey;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class DataRoundTripTest {

    private static Logger log = LoggerFactory.getLogger(DataRoundTripTest.class);

    private ParserPool parserPool;

    @Test
    public void exploration() throws Exception {

        InitializationService.initialize();

        Encrypter encrypter = new Encrypter();
        assertNotNull(encrypter);

        String encURI = EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128;
        String kekURI = EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP;

        Credential encCred = AlgorithmSupport.generateSymmetricKeyAndCredential(encURI);
        assertNotNull(encCred);

        SecretKey encKey = encCred.getSecretKey();
        assertNotNull(encKey);

        StaticKeyInfoCredentialResolver keyResolver = new StaticKeyInfoCredentialResolver(encCred);
        DataEncryptionParameters encParams = new DataEncryptionParameters();
        encParams.setAlgorithm(encURI);
        encParams.setEncryptionCredential(encCred);

        Credential kekCred = AlgorithmSupport.generateKeyPairAndCredential(kekURI, 1024, true);
        assertNotNull(kekCred);

        StaticKeyInfoCredentialResolver kekResolver = new StaticKeyInfoCredentialResolver(kekCred);
        KeyEncryptionParameters kekParams = new KeyEncryptionParameters();
        kekParams.setAlgorithm(kekURI);
        kekParams.setEncryptionCredential(kekCred);

        parserPool = XMLObjectProviderRegistrySupport.getParserPool();
        assertNotNull(parserPool);

        EncryptedKey encryptedKey = encrypter.encryptKey(encKey, kekParams, parserPool.newDocument());
        assertNotNull(encryptedKey);

        String targetFile = "SimpleDecryptionTest.xml";
        SignableSimpleXMLObject targetObject = (SignableSimpleXMLObject) unmarshallElement(targetFile);
        EncryptedData encryptedData = encrypter.encryptElement(targetObject, encParams);
        assertNotNull(encryptedData);

        Decrypter decrypter = new Decrypter(keyResolver, null, null);
        XMLObject decryptedXMLObject = decrypter.decryptData(encryptedData);

        Document expectedDom = parserPool.parse(this.getClass().getClassLoader().getResourceAsStream(targetFile));
        Element actualDom = getMarshaller(decryptedXMLObject).marshall(decryptedXMLObject, parserPool.newDocument());

        XMLAssert.assertXMLEqual(SerializeSupport.nodeToString(expectedDom), SerializeSupport.nodeToString(actualDom));
    }


    protected XMLObject unmarshallElement(String elementFile) {
        try {
            Document doc = this.parseXMLDocument(elementFile);
            Element element = doc.getDocumentElement();
            Unmarshaller unmarshaller = this.getUnmarshaller(element);
            XMLObject object = unmarshaller.unmarshall(element);
            assertNotNull(object);
            return object;
        } catch (XMLParserException var6) {
            fail("Unable to parse element file " + elementFile);
        } catch (UnmarshallingException var7) {
            fail("Unmarshalling failed when parsing element file " + elementFile + ": " + var7);
        }

        return null;
    }

    protected Document parseXMLDocument(String xmlFilename) throws XMLParserException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(xmlFilename);
        Document doc = parserPool.parse(is);
        return doc;
    }

    protected Unmarshaller getUnmarshaller(Element element) {
        Unmarshaller unmarshaller = XMLObjectProviderRegistrySupport.getUnmarshallerFactory().getUnmarshaller(element);
        if (unmarshaller == null) {
            fail("no unmarshaller registered for " + QNameSupport.getNodeQName(element));
        }

        return unmarshaller;
    }
    protected Marshaller getMarshaller(XMLObject xmlObject) {
        Marshaller marshaller = XMLObjectProviderRegistrySupport.getMarshallerFactory().getMarshaller(xmlObject);
        if (marshaller == null) {
            fail("no marshaller registered for " + xmlObject.getClass().getName());
        }

        return marshaller;
    }
}
