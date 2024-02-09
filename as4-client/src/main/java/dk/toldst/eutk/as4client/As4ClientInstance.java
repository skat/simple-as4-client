package dk.toldst.eutk.as4client;
import com.sun.xml.messaging.saaj.util.JAXMStreamSource;
import dk.toldst.eutk.as4client.as4.As4DtoCreator;
import dk.toldst.eutk.as4client.as4.As4HttpClient;
import dk.toldst.eutk.as4client.as4.As4Message;
import dk.toldst.eutk.as4client.exceptions.AS4Exception;
import org.apache.wss4j.common.util.XMLUtils;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.w3c.dom.Document;

import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import dk.toldst.eutk.as4client.as4.Compression;

public class As4ClientInstance implements As4Client {

    private As4DtoCreator as4DtoCreator;
    private As4HttpClient as4HttpClient;
    private final String defaultMPC = "urn:fdc:ec.europa.eu:2019:eu_ics2_c2t/EORI/DK13116482"; //"urn:fdc:dk.skat.mft.DMS/import2/response";

    public void setCompression(boolean compression) {
        this.compression = compression;
    }

    private boolean compression = false;


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
    public As4ClientResponseDto executePush(String serviceValue, String serviceType, String action, byte[] message, Map<String, String> messageProperties) throws AS4Exception {
        return executePush(serviceValue, serviceType, action, new String(message, StandardCharsets.UTF_8), "declaration.xml", messageProperties);
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
    public As4ClientResponseDto executeDocumentPush(String service, String action, byte[] message, String file, Map<String, String> messageProperties) throws AS4Exception {
        String messageId = UUID.randomUUID().toString();
        return internalDocumentPush(service, action, new String(message, StandardCharsets.UTF_8), file, messageProperties, false, messageId);

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
        return executePush(service, "", action, message, fileName, messageProperties);
    }

    @Override
    public As4ClientResponseDto executePush(String serviceValue, String serviceType, String action, String message, String fileName, Map<String, String> messageProperties) throws AS4Exception {
        String messageId = UUID.randomUUID().toString();
        return internalPush(serviceValue, serviceType, action, message, fileName, messageProperties, true, messageId);
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
        return internalPush(service, "", action, message, fileName, messageProperties,includeAttachment, messageId);
    }

    private As4ClientResponseDto internalPush(String serviceValue, String serviceType, String action, String message, String fileName, Map<String, String> messageProperties, Boolean includeAttachment, String messageId ) throws AS4Exception {
        As4Message as4Message = new As4Message();
        byte[] messageBytes;
        if(includeAttachment) {
            if(compression){

                byte[] data = message.getBytes(StandardCharsets.UTF_8);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ByteArrayOutputStream os2 = new ByteArrayOutputStream();

                GZIPOutputStream stream;
                GZIPInputStream inputStream;
                try {
                    stream = Compression.compress(os);
                    stream.write(data);
                    stream.close();

                    messageBytes = os.toByteArray();

                    /*
                    Base64.getEncoder().encodeToString();

                    compressedMessage = Base64.getDecoder().decode(base64Message);

                    InputStream is = new ByteArrayInputStream(compressedMessage);

                    Compression.decompress(os2, is);

                    decompressedMesssge = os2.toString();*/

                } catch (IOException e) {
                    throw new AS4Exception("Compression failed");
                }
            }
            else {
                messageBytes = message.getBytes(StandardCharsets.UTF_8);
            }
            As4Message.As4Part part = CreatePart(messageBytes, "placeholder".getBytes());
            as4Message.getAttachments().add(part);
        }


        as4Message.setMessageProperties(messageProperties);
        Messaging messaging = as4DtoCreator.createMessaging(serviceValue, serviceType, action, "placeholder", as4Message, messageId);
        As4ClientResponseDto as4ClientResponseDto = new As4ClientResponseDto();

        SOAPMessage soapMessage;
        try {
            soapMessage = as4HttpClient.sendRequest(messaging, as4Message);
            javax.xml.soap.SOAPPart part = soapMessage.getSOAPPart();
            com.sun.xml.messaging.saaj.util.JAXMStreamSource sc = (JAXMStreamSource) part.getContent();

            String a = new String(sc.getInputStream().readAllBytes());
            int i = 0;
            var ret = new As4ClientResponseDto();
            ret.setFirstAttachment(a);

            return ret;
            //return new String(soapMessage.getAttachments().next().getDataHandler().getInputStream().readAllBytes());
        } catch (Exception e) {
            throw new AS4Exception("Failed to send (or receive) message" , e);
        }

    }


    private As4ClientResponseDto internalDocumentPush(String service, String action, String message, String file, Map<String, String> messageProperties, Boolean includeAttachment, String messageId ) throws AS4Exception {
        As4Message as4Message = new As4Message();

        if (includeAttachment) {
            As4Message.As4Part part = CreatePart(message, file);
            as4Message.getAttachments().add(part);
        }
        //TODO: Fix
        return null;
    }


    private As4Message.As4Part CreatePart(byte[] message, byte[] file) throws AS4Exception {
        As4Message.As4Part part = new As4Message.As4Part();
        part.setContent(message);
        if(compression){
            part.setProperties(Map.of("MimeType", "application/xml", "CharacterSet", "utf-8", "CompressionType", "application/gzip"));
        }
        else{
            part.setProperties(Collections.singletonMap("original-file-name", new String(file, StandardCharsets.UTF_8)));
        }

        return part;
    }

    private As4Message.As4Part CreatePart(String message, String file) throws AS4Exception {
        return CreatePart(message.getBytes(), file.getBytes());
    }


    @Override
    public As4ClientResponseDto executePull() throws AS4Exception {
        return executePull(defaultMPC);
    }

    public String getNode(Document doc, String xPathString) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        return xPath.evaluate(xPathString, doc);
    }


    @Override
    public As4ClientResponseDto executePull(String mpc) throws AS4Exception {
        String messageId = UUID.randomUUID().toString();
        Messaging messaging = as4DtoCreator.createPullMessaging(mpc, messageId);
        SOAPMessage soapMessage = null;
        As4ClientResponseDto as4ClientResponseDto = new As4ClientResponseDto();
        try {
            soapMessage = as4HttpClient.sendRequest(messaging, new As4Message());
            String A = "";

            //Could check here if the attachment is compressed, somehow
            //https://stackoverflow.com/questions/56076631/capturing-attachment-from-soap-response-in-java
            if(soapMessage.getAttachments().hasNext() && "application/gzip".equals(soapMessage.getAttachments().next().getContentType())){
                AttachmentPart attachmentPart = (AttachmentPart)soapMessage.getAttachments().next();
                InputStream is = (InputStream)attachmentPart.getRawContent();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                Compression.decompress(os, is);
                A += os.toString();
            }
            //If no attachment - then print whole body
            else{
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                soapMessage.writeTo(out);
                A = out.toString();
            }

            SOAPHeader header = soapMessage.getSOAPHeader();
            //String reftoOriginalID =  tryGetReftoOriginalID(soapMessage);

            //Rewrite using Yammer feedback - Remove Index Offsets
            // getNode(header.getOwnerDocument(), "")
            //String reftoOriginalID =  header.getElementsByTagNameNS("*","Property").
                    //item(0).getChildNodes().item(0).getNodeValue();
            //String a = reftoOriginalID +  new String(soapMessage.getAttachments().next().getDataHandler().getInputStream().readAllBytes());
            as4ClientResponseDto.setFirstAttachment(A);

            return as4ClientResponseDto;

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
