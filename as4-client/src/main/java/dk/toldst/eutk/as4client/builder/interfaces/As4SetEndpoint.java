package dk.toldst.eutk.as4client.builder.interfaces;

import dk.toldst.eutk.as4client.userinformation.AS4Exception;

import java.net.URI;
import java.net.URL;

public interface As4SetEndpoint {
    As4SetCrypto setEndpoint(URI url);
    As4SetCrypto setEndpoint(String url) throws AS4Exception;
}
