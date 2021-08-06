package dk.toldst.eutk.as4client;

import dk.skat.mft.dms_declaration_status._1.StatusResponseType;
import dk.toldst.eutk.as4client.userinformation.AS4Exception;

import javax.xml.transform.TransformerException;
import java.io.IOException;

public interface As4Client {
    StatusResponseType executePush(String service, String action, String message) throws AS4Exception;
    StatusResponseType executePush(String service, String action, byte[] message) throws AS4Exception;
    StatusResponseType executePull() throws AS4Exception;
}
