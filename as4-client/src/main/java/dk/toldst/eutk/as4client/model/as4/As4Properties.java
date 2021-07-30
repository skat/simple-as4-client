package dk.toldst.eutk.as4client.model.as4;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import dk.toldst.eutk.as4client.builder.support.As4ClientBuilderInstance;
import dk.toldst.eutk.as4client.As4Client;

public class As4Properties {


    private Party from;
    private Party to;
    private String cryptoPropertiesPath;
    private String localHostName;
    private String endpoint;
    private String securityUserName;
    private String securityPassword;
    private String soapMessageActor;

    public String getSecurityUserName() {
        return securityUserName;
    }

    public void setSecurityUserName(String securityUserName) {
        this.securityUserName = securityUserName;
    }

    public String getSecurityPassword() {
        return securityPassword;
    }

    public void setSecurityPassword(String securityPassword) {
        this.securityPassword = securityPassword;
    }

    public String getCryptoPropertiesPath() {
        return cryptoPropertiesPath;
    }

    public void setCryptoPropertiesPath(String cryptoPropertiesPath) {
        this.cryptoPropertiesPath = cryptoPropertiesPath;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getLocalHostName() {
        return localHostName;
    }

    public void setLocalHostName(String localHostName) {
        this.localHostName = localHostName;
    }

    public Party getFrom() {
        return from;
    }

    public void setFrom(Party from) {
        this.from = from;
    }

    public Party getTo() {
        return to;
    }

    public void setTo(Party to) {
        this.to = to;
    }

    public String getSoapMessageActor() {return this.soapMessageActor;}

    public void setSoapMessageActor(String soapMessageActor) {this.soapMessageActor = soapMessageActor;}

    public static class Party {
        private String id;
        private String role;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
