package dk.toldst.eutk.as4client;

import dk.toldst.eutk.as4client.builder.As4ClientBuilder;
import dk.toldst.eutk.as4client.builder.interfaces.As4Optionals;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetCrypto;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetEndpoint;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetPasswordTokenDetails;
import dk.toldst.eutk.as4client.builder.support.As4ClientBuilderInstance;
import dk.toldst.eutk.as4client.exceptions.AS4Exception;

public class TestHelperTools {

    static As4SetEndpoint as4SetEndpoint;
    static As4SetCrypto as4SetCrypto;
    static As4SetPasswordTokenDetails as4SetPasswordTokenDetails;
    static As4ClientBuilder as4ClientBuilder;
    static As4Optionals as4Optionals;

    public static As4SetEndpoint getAs4SetEndpoint() {
        SetupBuilder();
        return as4SetEndpoint;
    }

    public static As4SetCrypto getAs4SetCrypto() throws AS4Exception {
        SetupBuilder();
        SetupEndpoint();
        return as4SetCrypto;
    }

    public static As4SetPasswordTokenDetails getAs4SetPasswordTokenDetails() throws AS4Exception {
        SetupBuilder();
        SetupEndpoint();
        SetupCrypto();
        return as4SetPasswordTokenDetails;
    }

    public static As4ClientBuilder getAs4ClientBuilder() throws AS4Exception {
        SetupBuilder();
        SetupEndpoint();
        SetupCrypto();
        SetupPassword();
        return as4ClientBuilder;
    }

    public static As4Optionals getAs4Optionals() throws AS4Exception{
        SetupBuilder();
        SetupEndpoint();
        SetupCrypto();
        SetupPassword();
        SetupOptionals();
        return as4Optionals;
    }

    private static void SetupBuilder() {
        as4SetEndpoint = new As4ClientBuilderInstance().builder();
    }

    private static void SetupEndpoint() throws AS4Exception {
        as4SetCrypto = as4SetEndpoint.setEndpoint("nourl.dk");
    }

    private static void SetupCrypto() throws AS4Exception {
        as4SetPasswordTokenDetails = as4SetCrypto.setCrypto("security/as4crypto-holodeck.properties");
    }

    private static void SetupPassword() {
        as4ClientBuilder = as4SetPasswordTokenDetails.setPassword("NotAPassword");
    }

    private static void SetupOptionals(){
        as4Optionals = as4ClientBuilder.optionals();
    }
}
