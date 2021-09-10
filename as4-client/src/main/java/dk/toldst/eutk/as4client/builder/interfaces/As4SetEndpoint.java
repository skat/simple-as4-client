package dk.toldst.eutk.as4client.builder.interfaces;

import dk.toldst.eutk.as4client.exceptions.AS4Exception;

import java.net.URI;

public interface As4SetEndpoint {
    /**
     *
     * @param url the url of the Axway portal
     * @return an As4SetCrypto instance, on which the crypto file path can be set.
     */
    As4SetCrypto setEndpoint(URI url);

    /**
     *
     * @param url the url of the Axway portal
     * @return an As4SetCrypto instance, on which the crypto file path can be set.
     * @throws AS4Exception
     */
    As4SetCrypto setEndpoint(String url) throws AS4Exception;
}
