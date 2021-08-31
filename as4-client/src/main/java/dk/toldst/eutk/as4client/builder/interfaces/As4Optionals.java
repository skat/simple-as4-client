package dk.toldst.eutk.as4client.builder.interfaces;

import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.exceptions.AS4Exception;

import java.net.URI;

/**
 * A collection of optional interfaces that can be added onto a finished builder before building.
 */
public interface As4Optionals {
    /**
     * Sets the actor the system uses, defaults to "ebms"
     * @param actor
     * @return
     */
    As4Optionals setActor(String actor);

    /**
     * Disables SSL for the connection, use this is you are connecting though HTTP. (For example by utilizing a http to https proxy service)
     * @return
     */
    As4Optionals noSSL();
    As4Optionals toParty(String toPartyName, String toPartyRole);
    As4Optionals fromParty(String fromParty, String fromPartyRole);
    As4Optionals setAbsoluteURI(URI uri);
    As4Optionals setAbsoluteURI(String uri) throws AS4Exception;
    As4Optionals setUsername(String username);

    /**
     * Calls the underlying build function on the builder object.
     * @return
     * @throws AS4Exception
     */
    As4Client build() throws AS4Exception;
}
