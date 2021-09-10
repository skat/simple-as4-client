package dk.toldst.eutk.as4client;

import dk.skat.mft.dms_declaration_status._1.StatusResponseType;
import dk.toldst.eutk.as4client.exceptions.AS4Exception;

public interface As4Client {
    /**
     * Transmits a message, and requests an action from a service.
     * @param service the service requested from DMS, example: "DMS.Import"
     * @param action the action requested from DMS, example: "Declaration.Submit"
     * @param message the XML message to be sent to DMS, in the form of a String
     * @return a StatusResponseType object with the reply from the service.
     * @throws AS4Exception
     */
    StatusResponseType executePush(String service, String action, String message) throws AS4Exception;

    /**
     * Transmits a message, and requests an action from a service.
     * @param service the service requested from DMS, example: "DMS.Import"
     * @param action the action requested from DMS, example: "Declaration.Submit"
     * @param message the XML message to be sent to DMS, in the form of a byte stream
     * @return a StatusResponseType object with the reply from the service.
     * @throws AS4Exception
     */
    StatusResponseType executePush(String service, String action, byte[] message) throws AS4Exception;
    StatusResponseType executePull() throws AS4Exception;
}
