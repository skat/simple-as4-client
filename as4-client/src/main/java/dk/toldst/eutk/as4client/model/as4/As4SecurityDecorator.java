package dk.toldst.eutk.as4client.model.as4;

import org.apache.wss4j.common.util.XMLUtils;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * TODO: It's technically a decorator, but it is more like an interceptor. Name changing in the future?
 */
public class As4SecurityDecorator extends SOAPConnection {


    private final SOAPConnection soapConnection;
    private final SecurityService securityService;
    //private final static Logger LOGGER = LogManager.getLogger(As4SecurityDecorator.class.getName());

    public As4SecurityDecorator(SOAPConnection soapConnection, SecurityService securityService) {
        this.soapConnection = soapConnection;
        this.securityService = securityService;
    }

    @Override
    public SOAPMessage call(SOAPMessage soapMessage, Object o) throws SOAPException {
        String userTokenId = securityService.usernameToken(soapMessage);
        securityService.signAndEncryptAs4(soapMessage, userTokenId);
        //logSOAPDocument(soapMessage, "Request:");
        SOAPMessage response = soapConnection.call(soapMessage, o);
        //logSOAPDocument(response, "Response:");
        securityService.decryptAndValidateAs4(response);
        return response;
    }

    /*
    private void logSOAPDocument(SOAPMessage soapMessage, String msgInfo) {
        try {
            LOGGER.info(msgInfo + XMLUtils.prettyDocumentToString(soapMessage.getSOAPPart().getOwnerDocument()));
        } catch (IOException | TransformerException e) {
            LOGGER.error(e.getMessage());
        }
    } */

    @Override
    public void close() throws SOAPException {
        soapConnection.close();
    }
}
