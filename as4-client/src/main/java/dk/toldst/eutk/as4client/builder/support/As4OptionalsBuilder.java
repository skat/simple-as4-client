package dk.toldst.eutk.as4client.builder.support;

import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.As4ClientInstance;
import dk.toldst.eutk.as4client.builder.As4ClientBuilder;
import dk.toldst.eutk.as4client.builder.interfaces.As4Optionals;
import dk.toldst.eutk.as4client.exceptions.AS4Exception;

import java.net.URI;
import java.net.URISyntaxException;

public class As4OptionalsBuilder implements As4Optionals {
    private As4ClientBuilder as4ClientBuilderInstance;
    private String actor;
    private String toPartyIdentifier;
    private String toPartyRole;
    private String toPartyType;
    private String fromPartyIdentifier;
    private String fromPartyRole;
    private String fromPartyType;
    private String username;
    private URI uri;
    private boolean disableSSl = false;

    private boolean useCompression = false;

    private boolean useBinarySecurity = false;


    public As4OptionalsBuilder(As4ClientBuilder as4ClientBuilderInstance) {
        this.as4ClientBuilderInstance = as4ClientBuilderInstance;
    }

    @Override
    public As4Optionals setAbsoluteURI(URI uri){
        this.uri = uri;
        return this;
    }

    @Override
    public As4Optionals setAbsoluteURI(String url) throws AS4Exception {
        URI uri;
        try{
            uri = new URI(url);
        }
        catch (URISyntaxException e){
            throw new AS4Exception("Failed to convert string to URI", e);
        }
        return setAbsoluteURI(uri);
    }

    @Override
    public As4Optionals setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public As4Optionals useCompression() {
        this.useCompression = true;
        return this;
    }

    @Override
    public As4Optionals useBinarySecurityToken() {
        this.useBinarySecurity = true;
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
    public As4Optionals toParty(String toPartyName, String toPartyRole, String toPartyType) {
        this.toPartyIdentifier = toPartyName;
        this.toPartyRole = toPartyRole;
        this.toPartyType = toPartyType;
        return this;
    }

    @Override
    public As4Optionals fromParty(String fromPartyName, String fromPartyRole, String fromPartyType) {
        this.fromPartyIdentifier = fromPartyName;
        this.fromPartyRole = fromPartyRole;
        this.fromPartyType = fromPartyType;
        return this;
    }

    @Override
    public As4Optionals toParty(String toPartyName, String toPartyRole) {
        return toParty(toPartyName, toPartyRole, "");
    }
    @Override
    public As4Optionals fromParty(String fromPartyName, String fromPartyRole) {
        return fromParty(fromPartyName, fromPartyRole, "");
    }

    @Override
    public As4Client build() throws AS4Exception {
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
            client.getAs4DtoCreator().setFromParty(fromPartyIdentifier, fromPartyRole, fromPartyType);
        }
        if(toPartyIdentifier != null && toPartyRole != null){
            client.getAs4DtoCreator().setToParty(toPartyIdentifier, toPartyRole, toPartyType);
        }
        if(uri != null){
            client.getAs4HttpClient().setEndpointURI(uri);
        }
        if(username != null){
            client.getAs4HttpClient().getSecurityService().setUsername(username);
        }

        if(useCompression){
            client.setCompression(useCompression);
        }

        if(useBinarySecurity){
            client.getAs4HttpClient().getSecurityService().setUseBinary(useBinarySecurity);
        }

        return client;
    }
}
