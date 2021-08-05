package dk.toldst.eutk.as4client.builder;

import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.builder.interfaces.As4Optionals;

import java.net.URISyntaxException;

public interface As4ClientBuilder {
    As4Client build() throws URISyntaxException;

    As4Optionals optionals();
}
