package dk.toldst.eutk.as4client.builder.support;

import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.As4ClientInstance;
import dk.toldst.eutk.as4client.builder.As4ClientBuilder;
import dk.toldst.eutk.as4client.builder.interfaces.As4Optionals;

import java.net.URI;
import java.net.URISyntaxException;

public class As4OptionalsBuilder implements As4Optionals {
    private As4ClientBuilder as4ClientBuilderInstance;
    private String actor;
    private String toPartyIdentifier;
    private String toPartyRole;
    private String fromPartyIdentifier;
    private String fromPartyRole;
    private String username;
    private URI uri;
    private boolean disableSSl = false;

    public As4OptionalsBuilder(As4ClientBuilder as4ClientBuilderInstance) {
        this.as4ClientBuilderInstance = as4ClientBuilderInstance;
    }

    @Override
    public As4Optionals setAbsoluteURI(URI uri){
        this.uri = uri;
        return this;
    }

    @Override
    public As4Optionals setUsername(String username) {
        this.username = username;
        return this;
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
    public As4Optionals toParty(String toPartyName, String toPartyRole) {
        this.toPartyIdentifier = toPartyName;
        this.toPartyRole = toPartyRole;
        return this;
    }

    @Override
    public As4Optionals fromParty(String fromPartyName, String fromPartyRole) {
        this.fromPartyIdentifier = fromPartyName;
        this.fromPartyRole = fromPartyRole;
        return this;
    }

    @Override
    public As4Client build() throws URISyntaxException {
        //Change stuff
        As4ClientInstance client = (As4ClientInstance) as4ClientBuilderInstance.build();
        if(disableSSl)
        {
            client.getAs4HttpClient().setDisableSSL(disableSSl);
        }
        if(actor != null)
        {
            client.getAs4HttpClient().getSecurityService().setActor(actor);
        }
        if(fromPartyIdentifier != null && fromPartyRole != null){
            client.getAs4DtoCreator().setFromParty(fromPartyIdentifier, fromPartyRole);
        }
        if(toPartyIdentifier != null && toPartyRole != null){
            client.getAs4DtoCreator().setToParty(toPartyIdentifier, toPartyRole);
        }
        if(uri != null){
            client.getAs4HttpClient().setEndpointURI(uri);
        }
        if(username != null){
            client.getAs4HttpClient().getSecurityService().setUsername(username);
        }
        return client;
    }
}
