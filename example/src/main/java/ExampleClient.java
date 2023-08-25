import dk.skat.mft.dms_declaration_status._1.StatusResponseType;
import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.As4ClientResponseDto;
import dk.toldst.eutk.as4client.builder.support.As4ClientBuilderInstance;
import dk.toldst.eutk.as4client.exceptions.AS4Exception;
import dk.toldst.eutk.as4client.utilities.Tools;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

public class ExampleClient {
    public static void main(String[] args) throws AS4Exception {
        As4Client client = new As4ClientBuilderInstance().builder()
                //.setEndpoint("https://customs.ec.europa.eu:8445/domibus/services/msh")
                .setEndpoint("https://conformance.customs.ec.europa.eu:8445/domibus/services/msh")
                //.setEndpoint("https://147.67.18.14:8445/domibus/services/msh")
                //.setEndpoint("http://localhost:8384")
                .setCrypto("security/eu3.properties")
                .setPassword("")
                .optionals()
                .useCompression()
                .useBinarySecurityToken()
                .toParty("sti-taxud","Customs")
                .fromParty("DK13116482", "Trader")

                //.noSSL()
                //.setAbsoluteURI("https://customs.ec.europa.eu:8445/domibus/services/msh")
                .setAbsoluteURI("https://conformance.customs.ec.europa.eu:8445/domibus/services/msh")
                //.setAbsoluteURI("https://147.67.18.14:8445/domibus/services/msh")
                //.setAbsoluteURI("http://localhost:8384")
                .build();
        
        As4ClientResponseDto res = client.executePush(
                "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/service",
                "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/test",
                null);

        System.out.println(res.getFirstAttachment());

        //while (true){
        //PullAndPrint(client);
        //}
        //SendAndPrintNotificationExample(client, "DMS.Import2");
        //SendAndPrintNotificationExample(client, "DMS.Import");
    }

    private static void PullAndPrint(As4Client client) throws AS4Exception {
        As4ClientResponseDto pullResponse = client.executePull();
        System.out.println("Result: " + pullResponse);
    }

    private static void SendAndPrintDeclarationExample(As4Client client) throws AS4Exception {
        StatusResponseType declarationStatus = SubmitDeclarationExample(client);
        System.out.println("Result: " + declarationStatus.getCode() + (declarationStatus.getMessage() != null ? " and message: " + declarationStatus.getMessage() : ""));
    }

    private static void SendAndPrintNotificationExample(As4Client client, String service) throws AS4Exception {
        As4ClientResponseDto notificationResult = RetrieveNotificationExample(client, service);
        System.out.println("Result: " + notificationResult);
    }

    private static void SendAndPrintNotificationExample(As4Client client) throws AS4Exception {
        SendAndPrintNotificationExample(client, "DMS.Import2");
    }

    private static As4ClientResponseDto RetrieveNotificationExample(As4Client client) throws AS4Exception {
        return RetrieveNotificationExample(client, "DMS.Import2");
    }

    private static As4ClientResponseDto RetrieveNotificationExample(As4Client client, String Service) throws AS4Exception {
        As4ClientResponseDto notificationResult = client.executePush(Service, "Notification",
                 Map.of("lang", "EN", "submitterId", "30808460", "dateFrom", "2022-09-22T10:30:00.000", "dateTo", "2022-09-22T14:00:00.000")); // "functionalReferenceId", "CBMFT-16927TFETest2")); //CBM011205 CBMFT-16927TFETest
                 //Map.of("lang", "EN", "submitterId", "30808460", "functionalReferenceId", "CBMTeamDemo01")); //  //CBMDuplicateTest CBMFT-16927TFETest
        return notificationResult;
    }

    private static StatusResponseType SubmitDeclarationExample(As4Client client) throws AS4Exception {
        // Submitting a declaration
        String declaration = "";
        try{
            declaration = new String(ExampleClient.class.getResourceAsStream("smoke.xml").readAllBytes() ) ;
        }
        catch (IOException e){

        }
        String action =  "Declaration.Submit";
        As4ClientResponseDto declarationResult = client.executePush("DMS.Import2", action, declaration.getBytes(StandardCharsets.UTF_8), Map.of("procedureType", "H7"));

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


                .build();
    }

    public static As4Client ICS2Client() throws AS4Exception {
        return new As4ClientBuilderInstance().builder()
                .setEndpoint("https://customs.ec.europa.eu:8445/domibus/services/msh")
                .setCrypto("security/eu.properties")
                .setPassword("")
                .optionals().useCompression().useBinarySecurityToken()
                .setAbsoluteURI("https://customs.ec.europa.eu:8445/domibus/services/msh")
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
