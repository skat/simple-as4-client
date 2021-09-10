package dk.toldst.eutk.as4client.builder;

import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.builder.interfaces.As4Optionals;
import dk.toldst.eutk.as4client.exceptions.AS4Exception;

/**
 * builds a client using the Builder pattern.
 */
public interface As4ClientBuilder {
    /**
     * Builds an AS4 client with the supplied information
     * @return the client
     * @throws AS4Exception
     */
    As4Client build() throws AS4Exception;

    /**
     *
     * @return A collection of optional interfaces that can be added onto a finished builder before building.
     */
    As4Optionals optionals();
}
