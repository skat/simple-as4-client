package dk.toldst.eutk.as4client.builder.support;

import dk.toldst.eutk.as4client.As4ClientInstance;
import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.builder.As4ClientBuilder;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetCrypto;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetEndpoint;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetUsernameTokenDetails;

import java.net.MalformedURLException;
import java.net.URL;

public class As4ClientBuilderInstance implements As4ClientBuilder {

    private As4SetUsernameTokenDetailsInstance as4SetUsernameTokenDetailsInstance;
    private As4SetCryptoInstance as4SetCryptoInstance;
    private As4SetEndpointInstance as4SetEndpointInstance;

    //Builder -> Client
    public As4Client build() {
        As4ClientInstance as4ClientInstance = new As4ClientInstance();
        as4ClientInstance.setCryptoPath(as4SetCryptoInstance.path);
        as4ClientInstance.setPassword(as4SetUsernameTokenDetailsInstance.password);
        as4ClientInstance.setUsername(as4SetUsernameTokenDetailsInstance.username);
        as4ClientInstance.setUrl(as4SetEndpointInstance.url);
        return as4ClientInstance;
    }

    //Username -> Builder
    private class As4SetUsernameTokenDetailsInstance implements As4SetUsernameTokenDetails {
        private String username;
        private String password;

        @Override
        public As4ClientBuilder setUserNameTokenDetails(String username, String password) {
            this.username = username;
            this.password = password;
            return As4ClientBuilderInstance.this;
        }
    }

    //Crypto -> User
    private class As4SetCryptoInstance implements As4SetCrypto {
        private String path;
        @Override
        public As4SetUsernameTokenDetails setCrypto(String filepath) {
            path = filepath;
            as4SetUsernameTokenDetailsInstance = new As4SetUsernameTokenDetailsInstance();
            return as4SetUsernameTokenDetailsInstance;
        }
    }

    //Endpoint -> Crypto
    private class As4SetEndpointInstance implements As4SetEndpoint {
        private URL url;
        @Override
        public As4SetCrypto setEndpoint(URL url) {
            this.url = url;
            as4SetCryptoInstance = new As4SetCryptoInstance();
            return as4SetCryptoInstance;
        }
    }

    //Builder -> Endpoint
    public As4SetEndpoint builder() {
        as4SetEndpointInstance = new As4SetEndpointInstance();
        return as4SetEndpointInstance;
    }


}
