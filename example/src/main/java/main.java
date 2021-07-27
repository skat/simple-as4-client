import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.As4ClientInterface;
import dk.toldst.eutk.as4client.builder.As4ClientBuilder;
import dk.toldst.eutk.as4client.builder.support.As4ClientBuilderInstance;

import java.net.MalformedURLException;
import java.net.URL;

public class main {
    public static void main(String[] args) throws MalformedURLException {
        As4ClientInterface client = new As4ClientBuilderInstance()
                .builder()
                .setEndpoint(new URL("bla"))
                .setCrypto("bla")
                .setUserNameTokenDetails("bla","bla")
                .build();
    }
}
