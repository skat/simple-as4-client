package dk.toldst.eutk.as4client;

import dk.skat.mft.dms_declaration_status._1.StatusResponseType;
import dk.toldst.eutk.as4client.as4.As4DtoCreator;
import dk.toldst.eutk.as4client.as4.As4HttpClient;
import dk.toldst.eutk.as4client.as4.As4Message;
import dk.toldst.eutk.as4client.utilities.Marshalling;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class As4ClientInstance implements As4Client {

    private final As4DtoCreator as4DtoCreator;
    private final As4HttpClient as4HttpClient;
    private Marshalling marshalling;

    public As4ClientInstance(As4DtoCreator as4DtoCreator, As4HttpClient as4HttpClient, Marshalling marshalling) {
        this.as4DtoCreator = as4DtoCreator;
        this.as4HttpClient = as4HttpClient;
        this.marshalling = marshalling;
    }

    public As4DtoCreator getAs4DtoCreator() {
        return as4DtoCreator;
    }

    public As4HttpClient getAs4HttpClient() {
        return as4HttpClient;
    }

    @Override
    public StatusResponseType executePush(String service, String action, byte[] message) {
        return executePush(service, action, new String(message, StandardCharsets.UTF_8));
    }

    @Override
    public StatusResponseType executePush(String service, String action, String message)  {
        String messageId = UUID.randomUUID().toString();

        As4Message as4Message = new As4Message();
        As4Message.As4Part part = new As4Message.As4Part();
        part.setContent(message);
        part.setProperties(Collections.singletonMap("original-file-name", "declaration.xml"));
        as4Message.setMessageProperties(Map.of("procedureType", "H7"));
        as4Message.getAttachments().add(part);

        Messaging messaging = as4DtoCreator.createMessaging(service, action, "placeholder", as4Message, messageId);

        SOAPMessage soapMessage = null;
        try {
            soapMessage = as4HttpClient.sendRequest(messaging, as4Message);
        } catch (Exception e) {
            //TODO BRJ
            e.printStackTrace();
        }
        return getStatus(soapMessage);
    }

    private StatusResponseType getStatus(SOAPMessage soapMessage) {
        StatusResponseType responseType = null;
        AttachmentPart attachmentPart = soapMessage.getAttachments().next();
        JAXBElement<StatusResponseType> element = null;
        try {
            element = (JAXBElement<StatusResponseType>) marshalling.unmarshal(attachmentPart.getDataHandler().getInputStream());
        } catch (JAXBException | IOException | SOAPException e) {
            e.printStackTrace();
        }
        responseType = element.getValue();
        return responseType;
    }

    @Override
    public StatusResponseType executePull() {
        return null;
    }
}
