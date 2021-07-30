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
    private URL endpoint;
    private Party from;
    private Party to;
    private String localHostName;

    private Properties cryptoProperties;

    public Properties getCryptoProperties() {
        return cryptoProperties;
    }

    public void setCryptoProperties(Properties cryptoProperties) {
        this.cryptoProperties = cryptoProperties;
    }

    private String soapMessageActor;

    public String getSoapMessageActor() {
        return soapMessageActor;
    }

    public void setSoapMessageActor(String soapMessageActor) {
        this.soapMessageActor = soapMessageActor;
    }



    public String getLocalHostName() {
        return localHostName;
    }

    public void setLocalHostName(String localHostName) {
        this.localHostName = localHostName;
    }

    public Party getFrom() {
        return from;
    }

    public void setFrom(Party from) {
        this.from = from;
    }

    public Party getTo() {
        return to;
    }

    public void setTo(Party to) {
        this.to = to;
    }

    private As4DtoCreator as4DtoCreator;
    private As4HttpClient as4HttpClient;
    private SecurityService securityService;
    private JaxbThreadSafe jaxbThreadSafe;


    public Crypto getCrypto() {
        return crypto;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
        setup();
    }

    public String getSecurityUsername() {
        return securityUsername;
    }

    public void setSecurityUsername(String securityUsername) {
        this.securityUsername = securityUsername;
        setup();
    }

    public String getSecurityPassword() {
        return securityPassword;
    }

    public void setSecurityPassword(String securityPassword) {

        this.securityPassword = securityPassword;
        setup();
    }

    public URL getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(URL endpoint) {

        this.endpoint = endpoint;
        setup();
    }


    private void setup ()  {
        if(endpoint != null && securityPassword != null && securityUsername != null && crypto != null){
            setupProperties();
            try{
                JAXBContext jaxbContext = JAXBContext.newInstance("org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704");
                as4DtoCreator = new As4DtoCreator(this);
                securityService = new SecurityService(this);
                jaxbThreadSafe = new JaxbThreadSafe(jaxbContext);
                as4HttpClient = new As4HttpClient(jaxbThreadSafe, securityService, this);
            }
            catch (JAXBException e){
                int i = 0;
            }
        }
    }

    private void setupProperties() {
        Party from = new Party();
        from.setRole("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/initiator");
        from.setId(securityUsername + "_AS4");
        Party to = new Party();
        to.setRole("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/responder");
        to.setId("SKAT-MFT-AS4");
        setSoapMessageActor("ebms");
        setTo(to);
        setFrom(from);
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

        SOAPMessage soapMessage = as4HttpClient.sendRequest(messaging, as4Message);

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

    public static class Party {
        private String id;
        private String role;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

}
