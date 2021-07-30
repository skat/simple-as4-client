package dk.toldst.eutk.as4client;

import com.google.common.io.CharStreams;
import org.apache.wss4j.common.crypto.Crypto;

import dk.toldst.eutk.as4client.model.as4.*;
import dk.toldst.eutk.as4client.utilities.JaxbThreadSafe;
import org.apache.wss4j.common.util.XMLUtils;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.transform.TransformerException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class As4ClientInstance implements As4Client {

    private Crypto crypto;
    private String username;
    private String password;
    private URL url;
    private Map<String, String> messageProperties;


    private As4DtoCreator as4DtoCreator;
    private As4HttpClient as4HttpClient;
    private As4Properties as4Properties;
    private SecurityService securityService;
    private JaxbThreadSafe jaxbThreadSafe;

    public Map<String, String> getMessageProperties() {
        return messageProperties;
    }

    public void setMessageProperties(Map<String, String> messageProperties) {
        this.messageProperties = messageProperties;
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
        setup();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        setup();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = password;
        setup();
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {

        this.url = url;
        setup();
    }


    private void setup ()  {
        if(url != null && password != null && username != null && crypto != null){
            System.out.println("running");
            as4Properties = new As4Properties();
            setupProperties(as4Properties);

            try{
                JAXBContext jaxbContext = JAXBContext.newInstance("org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704");
                as4DtoCreator = new As4DtoCreator(as4Properties, this);
                securityService = new SecurityService(as4Properties, this);
                jaxbThreadSafe = new JaxbThreadSafe(jaxbContext);
                as4HttpClient = new As4HttpClient(as4Properties, jaxbThreadSafe, securityService, this);
            }
            catch (JAXBException e){
                int i = 0;
            }
        }
    }

    private void setupProperties(As4Properties as4Prop) {
        as4Prop.setSoapMessageActor("ebms");
        as4Prop.setCryptoPropertiesPath("as4crypto-holodeck.properties");
        as4Prop.setEndpoint(url.toString());
        as4Prop.setSecurityUserName(username);
        as4Prop.setSecurityPassword(password);
        As4Properties.Party from = new As4Properties.Party();
        from.setRole("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/initiator");
        from.setId(username + "_AS4");
        as4Prop.setFrom(from);
        As4Properties.Party to = new As4Properties.Party();
        to.setRole("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/responder");
        to.setId("SKAT-MFT-AS4");
        as4Prop.setTo(to);
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


        //UserMessage userMessage = as4DtoCreator.createUserMessaging(service, action, "placeholder", message, messageId);
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
}
