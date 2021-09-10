package dk.toldst.eutk.as4client.builder.interfaces;

import dk.toldst.eutk.as4client.exceptions.AS4Exception;
import org.apache.wss4j.common.crypto.Crypto;

public interface As4SetCrypto {
    /**
     * Sets the Crypto property.
     * @param filepath the path to the crypto.properties file.
     * @return a As4SetPasswordTokenDetails object, which is used to continue the builder pattern and set the password for the crypto.
     * @throws AS4Exception if the file is not currectly read, or a Crypto object cannot be built.
     */
    As4SetPasswordTokenDetails setCrypto(String filepath) throws AS4Exception;
    //As4SetPasswordTokenDetails setCrypto(Crypto cryptoProps);
}
