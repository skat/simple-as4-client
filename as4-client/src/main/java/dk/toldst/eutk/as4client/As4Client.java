package dk.toldst.eutk.as4client;

import dk.toldst.eutk.as4client.exceptions.AS4Exception;

import java.util.Map;

public interface As4Client {
    /**
     * Transmits a message, and requests an action from a service.
     * @param service the service requested from DMS, example: "DMS.Import"
     * @param action the action requested from DMS, example: "Declaration.Submit"
     * @param message the XML message to be sent to DMS, in the form of a String
     * @param file the name of the file you are sending in, in the form of a string
     * @param messageId the messageId to be sent to DMS, in the form of a String  example:  70a4c99c-c321-4265-b3a6-eebfceae9fdd
     * @return a StatusResponseType object with the reply from the service.
     * @throws AS4Exception
     */
    As4ClientResponseDto executePush(String service, String action, String message, String file, Map<String, String> messageProperties, String messageId ) throws AS4Exception;
    As4ClientResponseDto executePush(String service, String action, String message, Map<String, String> messageProperties, String messageId ) throws AS4Exception;
    As4ClientResponseDto executePush(String service, String action, String message, String file, Map<String, String> messageProperties) throws AS4Exception;

    As4ClientResponseDto executePush(String serviceValue, String serviceType, String action, String message, String fileName, Map<String, String> messageProperties) throws AS4Exception;

    As4ClientResponseDto executePush(String service, String action, String message, Map<String, String> messageProperties) throws AS4Exception;
    As4ClientResponseDto executePush(String service, String action, Map<String, String> messageProperties, String messageId ) throws AS4Exception;
    As4ClientResponseDto executePush(String service, String action, Map<String, String> messageProperties) throws AS4Exception;

    /**
     * Transmits a message, and requests an action from a service.
     * @param service the service requested from DMS, example: "DMS.Import"
     * @param action the action requested from DMS, example: "Declaration.Submit"
     * @param message the XML message to be sent to DMS, in the form of a byte stream
     * @param file the name of the file you are sending in, in the form of a string
     * @param messageId the messageId to be sent to DMS, in the form of a String  example:  70a4c99c-c321-4265-b3a6-eebfceae9fdd
     * @return a StatusResponseType object with the reply from the service.
     * @throws AS4Exception
     */
    As4ClientResponseDto executePush(String service, String action, byte[] message, String file, Map<String, String> messageProperties, String messageId) throws AS4Exception;
    As4ClientResponseDto executePush(String service, String action, byte[] message, Map<String, String> messageProperties, String messageId) throws AS4Exception;

    As4ClientResponseDto executeDocumentPush(String service, String action, byte[] message, String file, Map<String, String> messageProperties) throws AS4Exception;

    As4ClientResponseDto executePush(String service, String action, byte[] message, String file, Map<String, String> messageProperties) throws AS4Exception;

    As4ClientResponseDto executePush(String serviceValue, String serviceType, String action, byte[] message, Map<String, String> messageProperties) throws AS4Exception;

    As4ClientResponseDto executePush(String service, String action, byte[] message, Map<String, String> messageProperties) throws AS4Exception;
    /**
     * Executes a pull request to the default Message Partition Channel (MPC)
     * @return the response from the AS4-GW MPC in String format.
     * @throws AS4Exception
     */
    As4ClientResponseDto executePull() throws AS4Exception;

    /**
     * Executes a pull request to a Message Partition Channel (MPC)
     * @param mpc the Message Partition Channel from which a pull is requested
     * @return the response from the AS4-GW MPC in String format.
     * @throws AS4Exception
     */
    As4ClientResponseDto executePull(String mpc) throws AS4Exception;
}
