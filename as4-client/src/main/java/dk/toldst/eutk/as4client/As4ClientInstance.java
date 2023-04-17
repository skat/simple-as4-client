package dk.toldst.eutk.as4client;
import dk.toldst.eutk.as4client.as4.As4DtoCreator;
import dk.toldst.eutk.as4client.as4.As4HttpClient;
import dk.toldst.eutk.as4client.as4.As4Message;
import dk.toldst.eutk.as4client.exceptions.AS4Exception;
import org.apache.commons.lang.ObjectUtils;
import org.apache.wss4j.common.util.XMLUtils;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import java.util.*;

public class As4ClientInstance implements As4Client {

    private As4DtoCreator as4DtoCreator;
    private As4HttpClient as4HttpClient;
    private final String defaultMPC = "urn:fdc:dk.skat.mft.DMS/import2/response";

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
    public As4ClientResponseDto executePush(String service, String action, byte[] message, Map<String, String> messageProperties) throws AS4Exception {
        return executePush(service, action, new String(message, StandardCharsets.UTF_8), "declaration.xml", messageProperties);
    }

    @Override
    public As4ClientResponseDto executePush(String service, String action, Map<String, String> messageProperties) throws AS4Exception {
        String messageId = UUID.randomUUID().toString();
        return internalPush(service, action, "", "Declaration.xml", messageProperties, false, messageId);
    }

    @Override
    public As4ClientResponseDto executePush(String service, String action, byte[] message, String fileName, Map<String, String> messageProperties, String messageId) throws AS4Exception {
        return internalPush(service, action, new String(message, StandardCharsets.UTF_8), fileName, messageProperties, false, messageId);
    }

    @Override
    public As4ClientResponseDto executePush(String service, String action, byte[] message, Map<String, String> messageProperties, String messageId) throws AS4Exception {
        return executePush(service, action, new String(message, StandardCharsets.UTF_8), "Declaration.xml", messageProperties, messageId);
    }

    @Override
    public As4ClientResponseDto executePush(String service, String action, byte[] message, String fileName, Map<String, String> messageProperties) throws AS4Exception {
        String messageId = UUID.randomUUID().toString();
        return internalPush(service, action, new String(message, StandardCharsets.UTF_8), fileName, messageProperties, true, messageId);
    }

    @Override
    public As4ClientResponseDto executePush(String service, String action, String message, String fileName, Map<String, String> messageProperties, String messageId) throws AS4Exception {
        return internalPush(service, action, message, fileName, messageProperties, true,messageId);
    }

    @Override
    public As4ClientResponseDto executePush(String service, String action, String message, Map<String, String> messageProperties, String messageId) throws AS4Exception {
        return internalPush(service, action, message, "declaration.xml", messageProperties, true, messageId);
    }

    @Override
    public As4ClientResponseDto executePush(String service, String action, String message, String fileName, Map<String, String> messageProperties) throws AS4Exception {
        String messageId = UUID.randomUUID().toString();
        return internalPush(service, action, message, fileName, messageProperties, true, messageId);
    }

    @Override
    public As4ClientResponseDto executePush(String service, String action, String message, Map<String, String> messageProperties) throws AS4Exception {
        String messageId = UUID.randomUUID().toString();
        return internalPush(service, action, message, "declaration.xml", messageProperties, false, messageId);
    }

    @Override
    public As4ClientResponseDto executePush(String service, String action, Map<String, String> messageProperties, String messageId) throws AS4Exception {
        return internalPush(service, action, "", "Declaration.xml", messageProperties, false, messageId);
    }

    private As4ClientResponseDto internalPush(String service, String action, String message, String fileName, Map<String, String> messageProperties, Boolean includeAttachment, String messageId ) throws AS4Exception {
        As4Message as4Message = new As4Message();

        if(includeAttachment)
        {
            As4Message.As4Part part = CreatePart(message, fileName);
            as4Message.getAttachments().add(part);
        }

        messageProperties = null;
        //as4Message.setMessageProperties(messageProperties);
        Messaging messaging = as4DtoCreator.createMessaging(service, action, "placeholder", as4Message, messageId);
        As4ClientResponseDto as4ClientResponseDto = new As4ClientResponseDto();

        SOAPMessage soapMessage;
        try {
            soapMessage = as4HttpClient.sendRequest(messaging, as4Message);
            as4ClientResponseDto.setFirstAttachment(tryGetFirstAttachment(soapMessage));
            return as4ClientResponseDto;
        } catch (Exception e) {
            throw new AS4Exception("Failed to send (or receive) message" , e);
        }

    }

    private As4Message.As4Part CreatePart(String message, String fileName) {
        As4Message.As4Part part = new As4Message.As4Part();
        part.setContent(message);
        part.setProperties(Collections.singletonMap("original-file-name", fileName));
        return part;
    }


    @Override
    public As4ClientResponseDto executePull() throws AS4Exception {
        return executePull(defaultMPC);
    }

    @Override
    public As4ClientResponseDto executePull(String mpc) throws AS4Exception {
        String messageId = UUID.randomUUID().toString();
        Messaging messaging = as4DtoCreator.createPullMessaging(mpc, messageId);
        SOAPMessage soapMessage = null;
        As4ClientResponseDto as4ClientResponseDto = new As4ClientResponseDto();
        try {
            soapMessage = as4HttpClient.sendRequest(messaging, new As4Message());
            SOAPHeader header = soapMessage.getSOAPHeader();
            String reftoOriginalID =  tryGetReftoOriginalID(soapMessage);

            as4ClientResponseDto.setReftoOriginalID(reftoOriginalID);
            as4ClientResponseDto.setFirstAttachment(tryGetFirstAttachment(soapMessage));

            return as4ClientResponseDto;
        } catch (Exception e) {
            String debugMessage = null;
            try {
                if (soapMessage != null && soapMessage.getSOAPPart() != null && soapMessage.getSOAPPart().getOwnerDocument() != null) {
                    debugMessage = XMLUtils.prettyDocumentToString(soapMessage.getSOAPPart().getOwnerDocument());
                }
            } catch (IOException ex) {
                throw new AS4Exception("Failed to send (or receive) message" , e);
            } catch (TransformerException ex) {
                throw new AS4Exception("Failed to send (or receive) message" , e);
            }
            throw new AS4Exception("Failed to send (or receive) message, recieved from server: " + debugMessage , e);
        }
    }

    private String tryGetFirstAttachment(SOAPMessage soapMessage) {
        try {
            return new String(soapMessage.getAttachments().next().getDataHandler().getInputStream().readAllBytes());
        }
        catch (Exception e){
            return null;
        }
    }

    private String tryGetReftoOriginalID(SOAPMessage soapMessage) {
        try {
            SOAPHeader header = soapMessage.getSOAPHeader();
            return header.getElementsByTagNameNS("*","Property").item(0).getChildNodes().item(0).getNodeValue();
        }catch (Exception e){
            return null;
        }

    }
}
