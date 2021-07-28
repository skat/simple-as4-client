package dk.toldst.eutk.as4client;

import org.apache.wss4j.common.crypto.Crypto;

import dk.toldst.eutk.as4client.model.as4.*;
import dk.toldst.eutk.as4client.utilities.JaxbThreadSafe;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.soap.SOAPMessage;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class As4ClientInstance implements As4Client {

    private Crypto crypto;
    private String username;
    private String password;
    private URL url;

    private As4DtoCreator as4DtoCreator;
    private As4HttpClient as4HttpClient;
    private As4Properties as4Properties;
    private SecurityService securityService;
    private JaxbThreadSafe jaxbThreadSafe;


    public Crypto getCrypto() {
        return crypto;



    public String getCryptoPath() {
        return cryptoPath;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }


    private void setup (){
        if(url != null && password != null && username != null && cryptoPath != null){
            as4Properties = new As4Properties();
            setupProperties(as4Properties);


            as4DtoCreator = new As4DtoCreator(as4Properties);
            securityService = new SecurityService(as4Properties);
            jaxbThreadSafe = new JaxbThreadSafe();
            as4HttpClient = new As4HttpClient(as4Properties, jaxbThreadSafe, securityService);
        }
    }

    private void setupProperties(As4Properties as4Prop) {
        as4Prop.setSoapMessageActor("ebms");
        as4Prop.setCryptoPropertiesPath(cryptoPath);
        as4Prop.setEndpoint(url.toString());
        as4Prop.setSecurityUserName(username);
        as4Prop.setSecurityPassword(password);
        As4Properties.Party from = new As4Properties.Party();
        from.setRole("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/initiator");
        from.setId(username + "_AS4");
        as4Prop.setTo(from);
        As4Properties.Party to = new As4Properties.Party();
        to.setRole("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/responder");
        to.setId("SKAT-MFT-AS4");
        as4Prop.setTo(to);
    }

    @Override
    public String executePush(String service, String action, String message) {
        String messageId = UUID.randomUUID().toString();
/*
        UserMessage userMessage = as4DtoCreator
                .createUserMessaging(service, action, null, message, messageId);

        Messaging messaging = new Messaging();
        messaging.setMustUnderstandAttributeS12(true);
        messaging.getUserMessage().add(userMessage);

        SOAPMessage soapMessage = as4HttpClient.sendRequest(messaging, message);*/




        return null;
    }

    @Override
    public String executePull() {
        return null;
    }
}
