import dk.skat.mft.dms_declaration_status._1.StatusResponseType;
import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.As4ClientResponseDto;
import dk.toldst.eutk.as4client.builder.support.As4ClientBuilderInstance;
import dk.toldst.eutk.as4client.exceptions.AS4Exception;
import dk.toldst.eutk.as4client.utilities.Tools;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

public class ExampleClient {
    public static void main(String[] args) throws AS4Exception {
        As4Client client = SimpleTest();

        SendAndPrintNotificationExample(client, "DMS.Export2");
        PullsAndPrints(client);

        SendAndPrintDeclarationExample(client);


        //SendAndPrintNotificationExample(client, "DMS.Import");
    }

    private static void PullsAndPrints(As4Client client) throws AS4Exception {
        while (true) {
            As4ClientResponseDto pullResponse = client.executePull();
            System.out.println("ReftoOriginalID: " + pullResponse.getReftoOriginalID()+ " FirstAttachment: " + pullResponse.getFirstAttachment());

            if (pullResponse.getReftoOriginalID() == null){
                break;
            }
        }

    }

    private static void SendAndPrintDeclarationExample(As4Client client) throws AS4Exception {
        StatusResponseType declarationStatus = SubmitDeclarationExample(client);
        System.out.println("Result: " + declarationStatus.getCode() + (declarationStatus.getMessage() != null ? " and message: " + declarationStatus.getMessage() : ""));
    }

    private static void SendAndPrintNotificationExample(As4Client client, String service) throws AS4Exception {
        String messageId = UUID.randomUUID().toString();
        As4ClientResponseDto notificationResult = RetrieveNotificationExample(client, "DMS.Export2",messageId);
        System.out.println("messageId: "+messageId+ "Result: " + notificationResult.getFirstAttachment());
    }

    private static void SendAndPrintNotificationExample(As4Client client) throws AS4Exception {
        SendAndPrintNotificationExample(client, "DMS.Export2");
    }

    private static As4ClientResponseDto RetrieveNotificationExample(As4Client client, String Service, String messageId) throws AS4Exception
    {
        As4ClientResponseDto as4ClientResponseDto = client.executePush(Service, "Notification",
                 Map.of("lang", "EN", "submitterId", "30808460", "dateFrom", "2022-03-22T12:30:00.000", "dateTo", "2022-03-22T12:35:00.000"),messageId ); // "functionalReferenceId", "CBMFT-16927TFETest2")); //CBM011205 CBMFT-16927TFETest
                 //Map.of("lang", "EN", "submitterId", "30808460", "functionalReferenceId", "CBMTeamDemo01")); //  //CBMDuplicateTest CBMFT-16927TFETest
        return as4ClientResponseDto;
    }

    private static StatusResponseType SubmitDeclarationExample(As4Client client) throws AS4Exception {
        // Submitting a declaration
        String declaration = "";
        try{
            declaration = new String(ExampleClient.class.getResourceAsStream("base.xml").readAllBytes() ) ;
        }
        catch (IOException e){

        }
        var declarationBytes= declaration.getBytes(StandardCharsets.UTF_8);

        var  declarationResult = client.executePush("DMS.Export2", "Declaration.Submit",
                declarationBytes, "declaration.xml", Map.of("procedureType", "C1"));

        StatusResponseType declarationStatus =  Tools.getStatus(declarationResult.getFirstAttachment());
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
                //.setEndpoint("http://localhost:8384")
                .setCrypto("security/as4crypto-holodeckSt.properties")
                .setPassword("YDZYalux67")

                /*
                .setCrypto("security/as4crypto-holodeck.properties")
                .setPassword("HBNRsvph68")*/
                //.optionals().noSSL()
                .build();
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
