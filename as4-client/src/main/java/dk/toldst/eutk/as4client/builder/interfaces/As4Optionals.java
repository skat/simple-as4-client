package dk.toldst.eutk.as4client.builder.interfaces;

import dk.toldst.eutk.as4client.As4Client;

public interface As4Optionals {
    As4Optionals setActor(String actor);
    As4Optionals noSSL();
    As4Optionals toParty(String toParty);
    As4Optionals fromParty(String fromParty);
    As4Client build();
}
