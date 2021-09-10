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

    /**
     * Overrides the toParty - defaults to SKAT-MFT-AS4
     * @param toPartyName
     * @param toPartyRole
     * @return
     */
    As4Optionals toParty(String toPartyName, String toPartyRole);

    /**
     * Overrides the fromParty - defaults to certificate CVR and RID.
     * @param fromParty
     * @param fromPartyRole
     * @return
     */
    As4Optionals fromParty(String fromParty, String fromPartyRole);


    /**
     * Sets the absolute URI of the client.
     * @param uri
     * @return
     */
    As4Optionals setAbsoluteURI(URI uri);

    /**
     * Sets the absolute URI of the client.
     * @param uri
     * @return
     * @throws AS4Exception
     */
    As4Optionals setAbsoluteURI(String uri) throws AS4Exception;
    As4Optionals setUsername(String username);

    /**
     * Calls the underlying build function on the builder object.
     * @return the client
     * @throws AS4Exception
     */
    As4Client build() throws AS4Exception;
}
