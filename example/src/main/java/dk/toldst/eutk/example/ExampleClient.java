package dk.toldst.eutk.example;

import dk.skat.mft.dms_declaration_status._1.StatusResponseType;
import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.As4ClientResponseDto;
import dk.toldst.eutk.as4client.builder.support.As4ClientBuilderInstance;
import dk.toldst.eutk.as4client.exceptions.AS4Exception;
import dk.toldst.eutk.as4client.utilities.Tools;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ExampleClient {
    public static void main(String[] args) throws AS4Exception {



        As4Client client = new As4ClientBuilderInstance().builder()
                .setEndpoint("https://conformance.customs.ec.europa.eu:8445/domibus/services/msh")
                .setCrypto("security/eu3.properties")
                .setPassword("")
                .optionals()
                .useCompression()
                .useBinarySecurityToken()
                .toParty("sti-taxud","Customs", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:eu-customs:auth")
                .fromParty("DK13116482", "Trader", "urn:oasis:names:tc:ebcore:partyid-type:unregistered:eu-customs:EORI")

                .setAbsoluteURI("https://conformance.customs.ec.europa.eu:8445/domibus/services/msh")
                .build();

        As4ClientResponseDto res;
        for(String arg: args)
        {
            System.out.println("pushing " + arg);
            res = PushArg(client, arg);
            System.out.println(res.getFirstAttachment());
        }
        //res = CTPush(client);
        //System.out.println(res.getFirstAttachment());


    }

    private static As4ClientResponseDto PushArg(As4Client client, String arg) throws AS4Exception {
        As4ClientResponseDto res;
        try {
            res = client.executePush(
                    "eu_ics2_t2c",
                    "eu-customs-service-type",
                    "IE3A13",
                    ExampleClient.class.getResource(arg).openStream().readAllBytes(),
                    null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    private static As4ClientResponseDto EmptyQueue(As4Client client) throws AS4Exception{
        //Vi laver en resultat variabel og en stringbuilder (Den kan lave strings hurtigt)
        As4ClientResponseDto res = new As4ClientResponseDto();
        var sb = new StringBuilder();

        //Teorien for denne do while er at vi kører den en gang, tjekker om der står 'EmptyMessagePartitionChannel'
        // nogen steder, og hvis der ikke gør det så gentager vi indtil der gør.
        do {
            var temp = client.executePull();
            sb.append(temp.getFirstAttachment());
        } while (sb.indexOf("EmptyMessagePartitionChannel") == -1);

        //Når der så står det overføre vi ALT den data vi har fået til et objekt og skriver det ud.
        res.setFirstAttachment(sb.toString());
        return res;
    }

    private static As4ClientResponseDto PingTest(As4Client client) throws AS4Exception {
        return client.executePush(
                "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/service",
                "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/test",

                null);
    }

    private static As4ClientResponseDto CTPush(As4Client client) throws AS4Exception {
        As4ClientResponseDto res;
        try {
            res = client.executePush(
                    "eu_ics2_t2c",
                    "eu-customs-service-type",
                    "IE3A13",
                    ExampleClient.class.getResource("/Carrier/F13/Most Common/F13.xml").openStream().readAllBytes() ,
                    //ExampleClient.class.getResourceAsStream("Carrier/F13/Most Common/F13.xml").readAllBytes(),
                    null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
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
                .fromParty("CVR_13116482_UID_50151991" + "_AS4", "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/initiator", "")
                .toParty("SKAT-MFT-AS4","http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/responder", "")
                .setActor("ebms")
                .setAbsoluteURI("http://localhost:8384/exchange/CVR_13116482_UID_50151991")
                .setUsername("CVR_13116482_UID_50151991")
                .build();
    }
}
