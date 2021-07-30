package dk.toldst.eutk.as4client.model.as4;

import com.google.common.io.CharStreams;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.apache.wss4j.common.util.XMLUtils;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.SignalMessage;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class As4Client {

    private final As4DtoCreator as4DtoCreator;
    private final As4HttpClient as4HttpClient;
    //private final static Logger LOGGER = LogManager.getLogger(As4SecurityDecorator.class.getName());

    public As4Client(As4DtoCreator as4DtoCreator, As4HttpClient as4HttpClient) {
        this.as4DtoCreator = as4DtoCreator;
        this.as4HttpClient = as4HttpClient;
    }

    public String executePush(String service, String action, String conversationId, As4Message as4Message) throws IOException, TransformerException {
        String messageId = UUID.randomUUID().toString();

        UserMessage userMessage = as4DtoCreator
                .createUserMessaging(service, action, conversationId, as4Message, messageId);

        Messaging messaging = new Messaging();
        messaging.setMustUnderstandAttributeS12(true);
        messaging.getUserMessage().add(userMessage);

        SOAPMessage soapMessage = null;
        try {
            soapMessage = as4HttpClient.sendRequest(messaging, as4Message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getResponseString(soapMessage);
    }

    public String executePull(String mpc) throws IOException, TransformerException {
        String messageId = UUID.randomUUID().toString();

        SignalMessage signal = as4DtoCreator.createPullRequest(mpc, messageId);
        Messaging messaging = new Messaging();
        messaging.setMustUnderstandAttributeS12(true);
        messaging.getSignalMessage().add(signal);

        SOAPMessage soapMessage = null;
        try {
            soapMessage = as4HttpClient.sendRequest(messaging, new As4Message());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getResponseString(soapMessage);
    }

    private String getResponseString(SOAPMessage soapMessage) throws IOException, TransformerException {
        String responseAttachmentMessage = null;
        try {
            AttachmentPart attachmentPart = soapMessage.getAttachments().next();
            responseAttachmentMessage = CharStreams.toString(new InputStreamReader((attachmentPart).getDataHandler().getDataSource().getInputStream()));
        } catch (IOException | SOAPException e) {
            //LOGGER.error(e.getMessage());
        }
        //LOGGER.info("response attachment message: " + responseAttachmentMessage);
        String responseSOAPmessage = XMLUtils.prettyDocumentToString(soapMessage.getSOAPPart().getOwnerDocument()) + responseAttachmentMessage;

        return responseSOAPmessage + responseAttachmentMessage;
    }
}
