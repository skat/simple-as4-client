package dk.toldst.eutk.as4client.builder.interfaces;

import org.apache.wss4j.common.crypto.Crypto;

public interface As4SetCrypto {
    As4SetUsernameTokenDetails setCrypto(String filepath);
    As4SetUsernameTokenDetails setCrypto(Crypto cryptoProps);
}
