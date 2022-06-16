package dk.toldst.eutk.as4client;
import dk.toldst.eutk.as4client.as4.As4DtoCreator;
import dk.toldst.eutk.as4client.as4.As4HttpClient;
import dk.toldst.eutk.as4client.as4.As4Message;
import dk.toldst.eutk.as4client.exceptions.AS4Exception;
import org.apache.wss4j.common.util.XMLUtils;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
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
    public String executePush(String service, String action, byte[] message, Map<String, String> messageProperties) throws AS4Exception {
        return executePush(service, action, new String(message, StandardCharsets.UTF_8), messageProperties);
    }

    @Override
    public String executePush(String service, String action, Map<String, String> messageProperties) throws AS4Exception {
        return internalPush(service, action, "", messageProperties, false);
    }

    @Override
    public String executePush(String service, String action, String message, Map<String, String> messageProperties) throws AS4Exception {
        return internalPush(service, action, message, messageProperties, true);
    }

    private String internalPush(String service, String action, String message, Map<String, String> messageProperties, Boolean includeAttachment) throws AS4Exception {

        String messageId = UUID.randomUUID().toString();
        As4Message as4Message = new As4Message();

        if(includeAttachment)
        {
            As4Message.As4Part part = CreatePart(message);
            as4Message.getAttachments().add(part);
        }

        as4Message.setMessageProperties(messageProperties);
        Messaging messaging = as4DtoCreator.createMessaging(service, action, "placeholder", as4Message, messageId);

        SOAPMessage soapMessage;
        try {
            soapMessage = as4HttpClient.sendRequest(messaging, as4Message);
            return new String(soapMessage.getAttachments().next().getDataHandler().getInputStream().readAllBytes());
        } catch (Exception e) {
            throw new AS4Exception("Failed to send (or receive) message" , e);
        }

    }

    private As4Message.As4Part CreatePart(String message) {
        As4Message.As4Part part = new As4Message.As4Part();
        part.setContent(message);
        part.setProperties(Collections.singletonMap("original-file-name", "declaration.xml"));
        return part;
    }


    @Override
    public String executePull() throws AS4Exception {
        return executePull(defaultMPC);
    }

    public String getNode(Document doc, String xPathString) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        return xPath.evaluate(xPathString, doc);
    }


    @Override
    public String executePull(String mpc) throws AS4Exception {
        String messageId = UUID.randomUUID().toString();
        Messaging messaging = as4DtoCreator.createPullMessaging(mpc, messageId);
        SOAPMessage soapMessage = null;
        try {

            soapMessage = as4HttpClient.sendRequest(messaging, new As4Message());
            SOAPHeader header = soapMessage.getSOAPHeader();

            //Rewrite using Yammer feedback - Remove Index Offsets
            // getNode(header.getOwnerDocument(), "")
            String reftoOriginalID =  header.getElementsByTagNameNS("*","Property").
                    item(0).getChildNodes().item(0).getNodeValue();
            return reftoOriginalID +  new String(soapMessage.getAttachments().next().getDataHandler().getInputStream().readAllBytes());
        } catch (Exception e) {
            String debugMessage = null;
            try {
                debugMessage = XMLUtils.prettyDocumentToString(soapMessage.getSOAPPart().getOwnerDocument());
            } catch (IOException | TransformerException ex) {
                throw new AS4Exception("Failed to send (or receive) message" , e);
            }
            throw new AS4Exception("Failed to send (or receive) message, recieved from server: " + debugMessage , e);
        }
    }
}
