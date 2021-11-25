import dk.skat.mft.dms_declaration_status._1.StatusResponseType;
import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.builder.support.As4ClientBuilderInstance;
import dk.toldst.eutk.as4client.exceptions.AS4Exception;
import dk.toldst.eutk.as4client.utilities.Tools;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class ExampleClient {
    public static void main(String[] args) throws AS4Exception {
        As4Client client = SimpleTest();

        StatusResponseType declarationStatus = SubmitDeclarationExample(client);

        //String notificationResult = RetrieveNotificationExample(client);


        System.out.println("Result: " + declarationStatus.getCode() + (declarationStatus.getMessage() != null ? " and message: " + declarationStatus.getMessage() : ""));
        //System.out.println("Result: " + notificationResult);
    }

    private static String RetrieveNotificationExample(As4Client client) throws AS4Exception {
        String notificationResult = client.executePush("DMS.Import", "Notification",
                 Map.of("lang", "EN", "submitterId", "13116482", "dateFrom", "2021-10-01T06:00:00.000", "dateTo", "2021-10-02T06:00:00.000"));
        return notificationResult;
    }

    private static StatusResponseType SubmitDeclarationExample(As4Client client) throws AS4Exception {
        // Submitting a declaration
        String declaration = "";
        try{
            declaration = new String( ExampleClient.class.getResourceAsStream("decl.xml").readAllBytes() ) ;
        }
        catch (IOException e){

        }

        String declarationResult = client.executePush("DMS.Import", "Declaration.Submit",
                declaration.getBytes(StandardCharsets.UTF_8), Map.of("procedureType", "H7"));

        StatusResponseType declarationStatus =  Tools.getStatus(declarationResult);
        return declarationStatus;
    }

    /**
     * This test is meant to show a simple usecase of the simple AS4 client
     * @return
     * @throws AS4Exception
     */
    public static As4Client SimpleTest() throws AS4Exception {
        return new As4ClientBuilderInstance().builder()
                .setEndpoint("https://secureftpgatewaytest.skat.dk:6384")
                .setCrypto("security/as4crypto-holodeck.properties")
                .setPassword("HBNRsvph68").build();
    }

    /**
     * This test is meant to show an advanced usecase of the simple AS4 client.
     *  In this case we are using a proxy, overwriting the parties, the actor, and the URL
     *  Additionally we have disabled SSL (HTTPS) and set the username manually.
     * @return
     * @throws AS4Exception
     */
    public static As4Client AdvancedTest() throws AS4Exception {
        return new As4ClientBuilderInstance().builder()
                .setEndpoint("http://wrongurlfortest.com:8384")
                .setCrypto("security/as4crypto-holodeck.properties")
                .setPassword("HBNRsvph68")
                .optionals()
                .noSSL()
                .fromParty("CVR_13116482_UID_50151991" + "_AS4", "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/initiator")
                .toParty("SKAT-MFT-AS4","http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/responder")
                .setActor("ebms")
                .setAbsoluteURI("http://localhost:8384/exchange/CVR_13116482_UID_50151991")
                .setUsername("CVR_13116482_UID_50151991")
                .build();
    }



}
