package dk.toldst.eutk.as4client.builder;

import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.builder.interfaces.As4Optionals;

public interface As4ClientBuilder {
    As4Client build();

    As4Optionals optionals();
}
