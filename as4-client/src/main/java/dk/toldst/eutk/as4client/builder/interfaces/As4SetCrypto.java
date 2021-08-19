package dk.toldst.eutk.as4client.builder.interfaces;

import dk.toldst.eutk.as4client.userinformation.AS4Exception;
import org.apache.wss4j.common.crypto.Crypto;

public interface As4SetCrypto {
    As4SetPasswordTokenDetails setCrypto(String filepath) throws AS4Exception;
    As4SetPasswordTokenDetails setCrypto(Crypto cryptoProps);
}
