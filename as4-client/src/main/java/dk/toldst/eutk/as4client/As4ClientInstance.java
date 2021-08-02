package dk.toldst.eutk.as4client;

import com.google.common.io.CharStreams;

import dk.toldst.eutk.as4client.as4.As4DtoCreator;
import dk.toldst.eutk.as4client.as4.As4HttpClient;
import dk.toldst.eutk.as4client.as4.As4Message;
import org.apache.wss4j.common.util.XMLUtils;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;

import java.nio.charset.StandardCharsets;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.transform.TransformerException;

import java.util.*;

public class As4ClientInstance implements As4Client {

    private As4DtoCreator as4DtoCreator;
    private As4HttpClient as4HttpClient;

    public As4DtoCreator getAs4DtoCreator() {
        return as4DtoCreator;
    }

    public As4HttpClient getAs4HttpClient() {
        return as4HttpClient;
    }

    public As4ClientInstance(As4DtoCreator as4DtoCreator, As4HttpClient as4HttpClient) {
        this.as4DtoCreator = as4DtoCreator;
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
