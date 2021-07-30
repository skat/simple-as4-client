package dk.toldst.eutk.as4client;

import com.google.common.io.CharStreams;
import org.apache.wss4j.common.crypto.Crypto;

import dk.toldst.eutk.as4client.model.as4.*;
import dk.toldst.eutk.as4client.utilities.JaxbThreadSafe;
import org.apache.wss4j.common.util.XMLUtils;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;
import java.nio.charset.StandardCharsets;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.transform.TransformerException;

import java.util.*;

public class As4ClientInstance implements As4Client {

    private Crypto crypto;
    private String securityUsername;
    private String securityPassword;

    private Properties cryptoProperties;

    private As4DtoCreator as4DtoCreator;
    private As4HttpClient as4HttpClient;

    public Crypto getCrypto() {
        return crypto;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    public String getSecurityUsername() {
        return securityUsername;
    }

    public void setSecurityUsername(String securityUsername) {
        this.securityUsername = securityUsername;
    }

    public String getSecurityPassword() {
        return securityPassword;
    }

    public void setSecurityPassword(String securityPassword) {
        this.securityPassword = securityPassword;
    }

    public Properties getCryptoProperties() {
        return cryptoProperties;
    }

    public void setCryptoProperties(Properties cryptoProperties) {
        this.cryptoProperties = cryptoProperties;
    }

    public As4DtoCreator getAs4DtoCreator() {
        return as4DtoCreator;
    }

    public void setAs4DtoCreator(As4DtoCreator as4DtoCreator) {
        this.as4DtoCreator = as4DtoCreator;
    }

    public As4HttpClient getAs4HttpClient() {
        return as4HttpClient;
    }

    public void setAs4HttpClient(As4HttpClient as4HttpClient) {
        this.as4HttpClient = as4HttpClient;
    }

    @Override
    public String executePush(String service, String action, byte[] message) throws IOException, TransformerException {
        return executePush(service, action, new String(message, StandardCharsets.UTF_8));
    }

    @Override
    public String executePush(String service, String action, String message) throws IOException, TransformerException {
        String messageId = UUID.randomUUID().toString();

        As4Message as4Message = new As4Message();
        As4Message.As4Part part = new As4Message.As4Part();
        part.setContent(message);
        part.setProperties(Collections.singletonMap("original-file-name", "declaration.xml"));
        as4Message.setMessageProperties(Map.of("procedureType", "H7", "lang","EN"));
        as4Message.getAttachments().add(part);

        UserMessage userMessage = as4DtoCreator.createUserMessaging(service, action, "placeholder", as4Message, messageId);

        Messaging messaging = new Messaging();
        messaging.setMustUnderstandAttributeS12(true);
        messaging.getUserMessage().add(userMessage);

        SOAPMessage soapMessage = null;
        try {
            soapMessage = as4HttpClient.sendRequest(messaging, as4Message);
        } catch (Exception e) {
            //TODO BRJ
            e.printStackTrace();
        }

        return getResponseString(soapMessage);
    }

    @Override
    public String executePull() {
        return null;
    }

    private String getResponseString(SOAPMessage soapMessage) throws IOException, TransformerException {
        String responseAttachmentMessage = null;
        try {
            AttachmentPart attachmentPart = soapMessage.getAttachments().next();
            responseAttachmentMessage = CharStreams.toString(new InputStreamReader((attachmentPart).getDataHandler().getDataSource().getInputStream()));
        } catch (IOException | SOAPException e) {
            int i = 0;
        }
        String responseSOAPmessage = XMLUtils.prettyDocumentToString(soapMessage.getSOAPPart().getOwnerDocument()) + responseAttachmentMessage;

        return responseSOAPmessage + responseAttachmentMessage;
    }

}
