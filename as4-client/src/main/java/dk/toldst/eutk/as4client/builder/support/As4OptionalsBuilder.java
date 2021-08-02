package dk.toldst.eutk.as4client.builder.support;

import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.builder.As4ClientBuilder;
import dk.toldst.eutk.as4client.builder.interfaces.As4Optionals;

public class As4OptionalsBuilder implements As4Optionals {
    private As4ClientBuilder as4ClientBuilderInstance;
    private String actor;
    private boolean disableSSl;
    private String toParty;
    private String fromParty;


    public As4OptionalsBuilder(As4ClientBuilder as4ClientBuilderInstance) {
        this.as4ClientBuilderInstance = as4ClientBuilderInstance;
    }

    @Override
    public As4Optionals setActor(String actor) {
        this.actor = actor;
        return this;
    }

    @Override
    public As4Optionals noSSL() {
        disableSSl = true;
        return this;
    }

    @Override
    public As4Optionals toParty(String toParty) {
        this.toParty = toParty;
        return this;
    }

    @Override
    public As4Optionals fromParty(String fromParty) {
        this.fromParty = fromParty;
        return this;
    }

    @Override
    public As4Client build() {
        return as4ClientBuilderInstance.build();
    }
}
