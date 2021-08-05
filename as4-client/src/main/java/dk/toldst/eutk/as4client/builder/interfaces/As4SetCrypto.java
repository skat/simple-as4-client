package dk.toldst.eutk.as4client.builder.interfaces;

import org.apache.wss4j.common.crypto.Crypto;

public interface As4SetCrypto {
    As4SetPasswordTokenDetails setCrypto(String filepath);
    As4SetPasswordTokenDetails setCrypto(Crypto cryptoProps);
}
