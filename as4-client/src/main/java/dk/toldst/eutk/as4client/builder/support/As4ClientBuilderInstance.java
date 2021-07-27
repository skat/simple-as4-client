package dk.toldst.eutk.as4client.builder.support;

import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.As4ClientInstance;
import dk.toldst.eutk.as4client.builder.As4ClientBuilder;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetCrypto;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetEndpoint;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetUsernameTokenDetails;
import org.apache.wss4j.common.crypto.Crypto;
import org.apache.wss4j.common.crypto.CryptoFactory;
import org.apache.wss4j.common.ext.WSSecurityException;

import java.net.URL;
import java.util.Properties;

public class As4ClientBuilderInstance implements As4ClientBuilder {

    private As4SetUsernameTokenDetailsInstance as4SetUsernameTokenDetailsInstance;
    private As4SetCryptoInstance as4SetCryptoInstance;
    private As4SetEndpointInstance as4SetEndpointInstance;

    //Username -> Client
    public As4Client build() {
        As4ClientInstance as4ClientInstance = new As4ClientInstance();
        as4ClientInstance.setCrypto(as4SetCryptoInstance.crypto);
        as4ClientInstance.setPassword(as4SetUsernameTokenDetailsInstance.password);
        as4ClientInstance.setUsername(as4SetUsernameTokenDetailsInstance.username);
        as4ClientInstance.setUrl(as4SetEndpointInstance.url);
        return as4ClientInstance;
    }

    //Builder -> Endpoint
    public As4SetEndpoint builder() {
        as4SetEndpointInstance = new As4SetEndpointInstance();
        return as4SetEndpointInstance;
    }

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
        private Crypto crypto;

        @Override
        public As4SetUsernameTokenDetails setCrypto(String filepath) {
            Properties cryptoProperties;
            try {
                cryptoProperties = CryptoFactory
                        .getProperties(filepath, CryptoFactory.class.getClassLoader());
                crypto = CryptoFactory.getInstance(cryptoProperties);
            } catch (WSSecurityException e) {
                //TODO BRJ Fix this catch
            }

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


}
