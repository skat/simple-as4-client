package dk.toldst.eutk.as4client.model.as4;

import dk.toldst.eutk.as4client.As4ClientInstance;
import dk.toldst.eutk.as4client.model.as4.As4Message.As4Part;
import dk.toldst.eutk.as4client.utilities.JaxbThreadSafe;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.dom.DOMResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;

public class As4HttpClient {

    public static final String EBMS_3_0_NAMESPACE_URI =
            "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/";

    private final As4Properties as4Properties;
    private final JaxbThreadSafe marshaller;
    private final SecurityService securityService;
    private final As4ClientInstance as4ClientInstance;

    public As4HttpClient(As4Properties as4Properties, JaxbThreadSafe marshaller, SecurityService securityService, As4ClientInstance as4ClientInstance) {
        this.as4Properties = as4Properties;
        this.marshaller = marshaller;
        this.securityService = securityService;
        this.as4ClientInstance = as4ClientInstance;
    }

    public SOAPMessage sendRequest(Messaging messaging, As4Message as4Message) {
        try {
            return doSendRequest(messaging, as4Message);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong", e);
        }
    }

    private static class TrustAllHosts implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private SOAPMessage doSendRequest(Messaging messaging, As4Message as4Message) throws Exception {
        // The code below is to avoid the certificate check on SIT01
        /*
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new TrustAllHosts());
        HttpsURLConnection httpsConnection = null;
        URL url = new URL(as4Properties.getEndpoint());
        httpsConnection = (HttpsURLConnection) url.openConnection();
        httpsConnection.setHostnameVerifier(new TrustAllHosts());
        httpsConnection.connect();*/

        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = new As4SecurityDecorator(
                soapConnectionFactory.createConnection(), securityService);

        SOAPMessage soapMessage = createSOAPMessage(messaging, as4Message);
        return soapConnection.call(soapMessage, as4Properties.getEndpoint());
    }

    private SOAPMessage createSOAPMessage(Messaging messaging, As4Message as4Message) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage soapMessage = messageFactory.createMessage();

        SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();
        envelope.addNamespaceDeclaration("eb3", EBMS_3_0_NAMESPACE_URI);

        insertMessageToSoapHeader(messaging, soapMessage.getSOAPHeader());
        if (!as4Message.getAttachments().isEmpty()) {
            insertAttachments(soapMessage, as4Message.getAttachments());
        }

        if (as4Message.getBody() != null) {
            String body = new String(Base64.getDecoder().decode(as4Message.getBody().getContent()));
            Node bodyNode = createBodyNode(body);
            Node bodyNodeClone = soapMessage.getSOAPBody().getOwnerDocument().importNode(bodyNode, true);
            soapMessage.getSOAPBody().appendChild(bodyNodeClone);
        }

        return soapMessage;
    }


    private void insertAttachments(SOAPMessage soapMessage, List<As4Part> attachments) throws SOAPException {
        for (As4Part attachment : attachments) {
            AttachmentPart attachmentPart = soapMessage.createAttachmentPart();
            attachmentPart.setContentId(attachment.getId());

            // TODO:
            // insert functionality that checks for the "CompressionType=application/gzip" property and
            // gzip the contents before adding it to the attachement (spec: GZIP [RFC1952])
            byte[] data = Base64.getDecoder().decode(attachment.getContent());// Base64Utils.decodeFromString(attachment.getContent());
            attachmentPart.setRawContent(new ByteArrayInputStream(data), "application/octet-stream");
            attachmentPart.addMimeHeader("Content-Transfer-Encoding", "binary");
            soapMessage.addAttachmentPart(attachmentPart);
        }
    }

    private Node createBodyNode(String body) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();

        StringReader stringReader = new StringReader(body);
        return db.parse(new InputSource(stringReader)).getDocumentElement();
    }

    private void insertMessageToSoapHeader(Messaging messaging, SOAPHeader soapHeader) throws JAXBException {
        DOMResult domResult = new DOMResult(soapHeader);
        QName qName = new QName(EBMS_3_0_NAMESPACE_URI, "Messaging", "eb3");
        JAXBElement<Messaging> m = new JAXBElement<>(qName, Messaging.class, messaging);
        marshaller.marshal(m, domResult);
    }
}
