package dk.toldst.eutk.as4client.as4;

import org.apache.wss4j.common.WSEncryptionPart;
import org.apache.wss4j.common.WSS4JConstants;
import org.apache.wss4j.common.crypto.Crypto;
import org.apache.wss4j.common.crypto.SantuarioUtil;
import org.apache.wss4j.common.ext.Attachment;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.message.WSSecEncrypt;
import org.apache.wss4j.dom.message.WSSecHeader;
import org.apache.wss4j.dom.message.WSSecSignature;
import org.apache.wss4j.dom.message.WSSecUsernameToken;
import org.apache.wss4j.dom.transform.AttachmentContentSignatureTransformProvider;

import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.security.Security;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.wss4j.common.WSS4JConstants.ELEM_BODY;
import static org.apache.wss4j.common.WSS4JConstants.URI_SOAP12_ENV;
import static org.apache.wss4j.common.crypto.WSProviderConfig.addJceProvider;

public class SecurityService {

    private String username;
    private String password;
    private Crypto crypto;
    private Properties properties;

    public void setUseBinary(boolean useBinary) {
        this.useBinary = useBinary;
    }

    private boolean useBinary = false;


    public void setUsername(String username) {
        this.username = username;
    }
    public void setActor(String actor) {
        this.actor = actor;
    }

    private String actor = "ebms";

    public SecurityService(String username, String password, Crypto crypto, Properties properties) {
        this.username = username;
        this.password = password;
        this.crypto = crypto;
        this.properties = properties;

        System.setProperty("org.apache.xml.security.ignoreLineBreaks", "true");
        org.apache.xml.security.Init.init();
        if(Security.getProvider(WSConstants.SWA_ATTACHMENT_CONTENT_SIG_TRANS) == null) {
            Security.addProvider(new AttachmentContentSignatureTransformProvider());
        }
    }

    static {
        Security.removeProvider("ApacheXMLDSig");
        addJceProvider("ApacheXMLDSig", SantuarioUtil.getSantuarioProvider());
    }

    public String usernameToken(SOAPMessage soapMessage) throws SOAPException {
        try {
            // build header
            WSSecHeader secHeader = new WSSecHeader(actor, soapMessage.getSOAPHeader().getOwnerDocument());
            secHeader.insertSecurityHeader();

            WSSecUsernameToken usernametoken = new WSSecUsernameToken(secHeader);
            usernametoken.setPasswordType(WSConstants.PASSWORD_DIGEST);
            usernametoken.setUserInfo(username, password);
            usernametoken.addCreated();
            usernametoken.addNonce();
            usernametoken.build();
            return usernametoken.getId();

        } catch (Exception e) {
            throw new SOAPException(e.getMessage() + " Could add username token ");
        }

    }


    /*
    https://www.tabnine.com/code/java/classes/org.apache.wss4j.common.token.BinarySecurity
    https://stackoverflow.com/questions/23744513/howto-correct-securitytokenreference-using-wss4j-to-sign-soap
    https://stackoverflow.com/questions/49077876/signing-soap-message-using-wss4j-in-java
    ^ This is a great idea I think,
    sign.setKeyIdentifierType(WSConstants.BST_DIRECT_REFERENCE); // Binary Security Token - SecurityTokenReference
    https://sourceforge.net/p/signsoaprequest/code/HEAD/tree/SignSOAPRequest/trunk/src/main/java/br/gov/dataprev/soaptools/sign/WSSecurityHandler.java
     */
    public void signAndEncryptAs4(SOAPMessage soapMessage, String usernameTokenId) {
        try {
            // build header
            WSSecHeader secHeader = new WSSecHeader(soapMessage.getSOAPHeader().getOwnerDocument());

            secHeader.insertSecurityHeader();
            
            AttachmentCallbackHandler attachmentCallback = createAttachmentCallback(soapMessage.getAttachments());

            WSSecSignature wsSecSignature = new WSSecSignature(secHeader);
            setupSignature(wsSecSignature, properties);

            wsSecSignature.setAttachmentCallbackHandler(attachmentCallback);
            wsSecSignature.getParts().addAll(getPartsToSign(usernameTokenId));

            wsSecSignature.build(crypto);


        } catch (Exception e) {
            throw new RuntimeException("Could not sign document", e);
        }
    }

    private AttachmentCallbackHandler createAttachmentCallback(Iterator<AttachmentPart> attachmentParts) {
        ArrayList<Attachment> attachments = new ArrayList<>();
        while (attachmentParts.hasNext()) {
            AttachmentPart part = attachmentParts.next();
            Attachment attachment = new Attachment();
            attachment.setId(part.getContentId().replaceAll("<|>", ""));
            try {
                attachment.setSourceStream(part.getRawContent());
            } catch (Exception e) {
                throw new RuntimeException("Could not get attachment source", e);
            }
            attachments.add(attachment);
        }

        return new AttachmentCallbackHandler(attachments);
    }


    private void setupEncryption(WSSecEncrypt wsSecEncrypt, Properties cryptoProperties) {
        wsSecEncrypt.setUserInfo(
                cryptoProperties.getProperty("org.apache.wss4j.crypto.merlin.truststore.alias"));
        wsSecEncrypt.setDigestAlgorithm(WSS4JConstants.SHA256);
        wsSecEncrypt.setMGFAlgorithm(WSS4JConstants.MGF_SHA256);
        wsSecEncrypt.setKeyEncAlgo(WSS4JConstants.KEYTRANSPORT_RSAOAEP_XENC11);

    }

    private void setupSignature(WSSecSignature wsSecSignature, Properties cryptoProperties) {
        if(useBinary){
            wsSecSignature.setKeyIdentifierType(WSConstants.BST_DIRECT_REFERENCE);
            //This changes the token profile according to the specification of Interface Control Document: X509PKIPathv1
            wsSecSignature.setUseSingleCertificate(false);
        }
        else {
            wsSecSignature.setKeyIdentifierType(WSConstants.ISSUER_SERIAL);
        }

        wsSecSignature.setSignatureAlgorithm(WSS4JConstants.RSA_SHA256);

        wsSecSignature.setSigCanonicalization(WSS4JConstants.C14N_EXCL_OMIT_COMMENTS);
        wsSecSignature.setDigestAlgo(WSS4JConstants.SHA256);
        wsSecSignature.setUserInfo(
                cryptoProperties.getProperty("org.apache.wss4j.crypto.merlin.keystore.alias"),
                cryptoProperties.getProperty("org.apache.wss4j.crypto.merlin.keystore.private.password"));
        wsSecSignature.setIncludeSignatureToken(false);
    }

    private List<WSEncryptionPart> getPartsToSign(String usernameTokenId) {
        List<WSEncryptionPart> wsEncryptionParts = new ArrayList<>();
        WSEncryptionPart body = new WSEncryptionPart("Body", WSS4JConstants.URI_SOAP12_ENV, "");
        WSEncryptionPart messaging = new WSEncryptionPart("Messaging", As4HttpClient.EBMS_3_0_NAMESPACE_URI, "");
        WSEncryptionPart usernameToken = new WSEncryptionPart(usernameTokenId);
        wsEncryptionParts.add(body);
        wsEncryptionParts.add(messaging);
        wsEncryptionParts.add(usernameToken);
        wsEncryptionParts.add(new WSEncryptionPart("cid:Attachments", "Content"));
        return wsEncryptionParts;
    }

    private List<WSEncryptionPart> getPartsToEncrypt(SOAPMessage soapMessage) throws SOAPException {
        List<WSEncryptionPart> wsEncryptionParts = new ArrayList<>();
        if (hasBody(soapMessage)) {
            WSEncryptionPart body = new WSEncryptionPart(ELEM_BODY, URI_SOAP12_ENV, "Content");
            wsEncryptionParts.add(body);
        }
        if (hasAttachments(soapMessage)) {
            wsEncryptionParts.add(new WSEncryptionPart("cid:Attachments", "Content"));
        }
        return wsEncryptionParts;
    }

    public void decryptAndValidateAs4(SOAPMessage response) {

    }

    private boolean hasBody(SOAPMessage soapMessage) throws SOAPException {
        return soapMessage.getSOAPBody().getFirstChild() != null;
    }

    private boolean hasAttachments(SOAPMessage soapMessage) {
        return soapMessage.getAttachments().hasNext();
    }
}
