package dk.toldst.eutk.as4client.builder.support;

import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.As4ClientInterface;
import dk.toldst.eutk.as4client.builder.As4ClientBuilder;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetCrypto;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetEndpoint;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetUsernameTokenDetails;

import java.net.MalformedURLException;
import java.net.URL;

public class As4ClientBuilderInstance implements As4ClientBuilder {

    private String fpath;
    private String Username;
    private String Password;
    private URL Url;

    //Username -> Client
    public As4ClientInterface build() {
        return new As4Client(fpath, Username, Password, Url);
    }

    private class As4SetUsernameTokenDetailsInstance implements As4SetUsernameTokenDetails {

        @Override
        public As4ClientBuilder setUserNameTokenDetails(String username, String password) {
            Username = username;
            Password = password;
            return As4ClientBuilderInstance.this;
        }
    }

    //Crypto -> User
    private class AsSetCryptoInstance implements As4SetCrypto {
        @Override
        public As4SetUsernameTokenDetails setCrypto(String filepath) {
            fpath = filepath;
            return new As4SetUsernameTokenDetailsInstance();
        }
    }

    private AsSetCryptoInstance as4SetCryptoInstance = new AsSetCryptoInstance();

    //Endpoint -> Crypto
    private class AsSetEndpointInstance implements As4SetEndpoint {
        @Override
        public As4SetCrypto setEndpoint(URL url) {
            Url = url;
            return new AsSetCryptoInstance();
        }
    }

    private AsSetEndpointInstance as4SetEndpointInstance = new AsSetEndpointInstance();

    //Builder -> Endpoint
    public As4SetEndpoint builder() {
        return new AsSetEndpointInstance();
    }

    //Builder -> Endpoint -> Crypto -> Username -> Build
    public void test() throws MalformedURLException {
        As4ClientInterface id = this.builder().
                setEndpoint(new URL("Bla")).
                setCrypto("bla").
                setUserNameTokenDetails("bla", "bla").
                build();
    }
}
