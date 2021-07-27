import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.As4ClientInterface;
import dk.toldst.eutk.as4client.builder.As4ClientBuilder;
import dk.toldst.eutk.as4client.builder.support.As4ClientBuilderInstance;

import java.net.MalformedURLException;
import java.net.URL;

public class main {
    public static void main(String[] args) throws MalformedURLException {
        As4ClientInterface client = new As4ClientBuilderInstance().builder().
            setEndpoint(new URL("https://tst02b2bidmz1.dmz23.local:6384/exchange/CVR_13116482_UID_50151991")).
            setCrypto("as4crypto-holodeck.properties").
            setUserNameTokenDetails("CVR_13116482_UID_50151991", "BKOMamgh13").
            build();

        //Message messageObj = client.encrypt("xmlfilepath");
        //Reply reply = client.send(messageObj);


    }
}
