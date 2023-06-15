package dk.toldst.eutk.as4client.builder.interfaces;

import java.util.Properties;

import org.apache.wss4j.common.crypto.Merlin;

import dk.toldst.eutk.as4client.exceptions.AS4Exception;

public interface As4SetCrypto {
    /**
     * Sets the Crypto property.
     * @param filepath the path to the crypto.properties file.
     * @return a As4SetPasswordTokenDetails object, which is used to continue the builder pattern and set the password for the crypto.
     * @throws AS4Exception if the file is not currectly read, or a Crypto object cannot be built.
     */
    As4SetPasswordTokenDetails setCrypto(String filepath) throws AS4Exception;

    /**
     * Sets the Crypto object.
     * @param crypto a crypto object containing Keystore/Truststore, which mist be of type "Merlin"
     * @param crypto the cryptoProperties, describing how the Keystore returned by the crypto object should be handled
     * @return a As4SetPasswordTokenDetails object, which is used to continue the builder pattern and set the password for the crypto.
     * @throws AS4Exception if the crypto object is invalid (for example: does not contain any certificates)
     */
    As4SetPasswordTokenDetails setCrypto(Merlin crypto, Properties cryptoProperties) throws AS4Exception;
}
