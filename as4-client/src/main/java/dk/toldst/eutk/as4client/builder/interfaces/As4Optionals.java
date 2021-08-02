package dk.toldst.eutk.as4client.builder.interfaces;

import dk.toldst.eutk.as4client.As4Client;

public interface As4Optionals {
    As4Optionals setActor();
    As4Optionals noSSL();
    As4Optionals toParty();
    As4Optionals fromParty();
    As4Client build();
}
