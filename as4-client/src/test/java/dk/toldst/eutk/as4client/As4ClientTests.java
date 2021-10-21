package dk.toldst.eutk.as4client;

import dk.skat.mft.dms_declaration_status._1.StatusResponseType;
import dk.toldst.eutk.as4client.builder.As4ClientBuilder;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetCrypto;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetEndpoint;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetPasswordTokenDetails;
import dk.toldst.eutk.as4client.builder.support.As4ClientBuilderInstance;
import dk.toldst.eutk.as4client.exceptions.AS4Exception;
import dk.toldst.eutk.as4client.utilities.Tools;
import org.junit.jupiter.api.*;

import java.net.URI;

public class As4ClientTests {

    As4SetEndpoint as4SetEndpoint;
    As4SetCrypto as4SetCrypto;
    As4SetPasswordTokenDetails as4SetPasswordTokenDetails;
    As4ClientBuilder as4ClientBuilder;

    @BeforeEach
    void Setup() throws AS4Exception {
        SetupBuilder();
    }

    private void SetupBuilder() {
        as4SetEndpoint = new As4ClientBuilderInstance().builder();
    }

    private void SetupEndpoint() throws AS4Exception {
        as4SetCrypto = as4SetEndpoint.setEndpoint("nourl.dk");
    }

    private void SetupCrypto() throws AS4Exception {
        as4SetPasswordTokenDetails = as4SetCrypto.setCrypto("security/as4crypto-holodeck.properties");
    }

    private void SetupPassword() {
        as4ClientBuilder = as4SetPasswordTokenDetails.setPassword("NotAPassword");
    }



    @Test
    void setEndpointToCorrectURLReturnsNotNull() throws AS4Exception {
        //Arrange
        SetupEndpoint();

        //Act
        As4SetCrypto crypto = as4SetEndpoint.setEndpoint("nourl.dk");
        //Assert
        Assertions.assertNotNull(crypto);
    }

    @Test
    void setCryptoToCorrectFileReturnsNotNull() throws AS4Exception {
        //Arrange
        SetupEndpoint();

        //Assert
        Assertions.assertNotNull(as4SetCrypto.setCrypto("security/as4crypto-holodeck.properties"));
    }



    @Test
    void setCryptoToWrongFileFileReturnsThrows() throws AS4Exception {
        //Arrange
        SetupEndpoint();

        //Assert
        Assertions.assertThrows(AS4Exception.class, () -> {
            as4SetCrypto.setCrypto("file");
        });
    }

    @Test
    @DisplayName("Trying to input a wrong url as a class should result in an exception")
    void setEndpointWrongURLStringThrowsAS4ExceptionTest(){

        //Assert
        Assertions.assertThrows(AS4Exception.class, () -> {
            as4SetEndpoint.setEndpoint("wr ong");
        });
    }

    @Test
    void setAs4SetPasswordTokenDetailsReturnsNotNull() throws AS4Exception {
        //Arrange
        SetupEndpoint();
        SetupCrypto();
        //Assert
        Assertions.assertNotNull(as4SetPasswordTokenDetails.setPassword("NotAPassword"));
    }

    @Test
    @DisplayName("Clientbuilder can build")
    void setCreateClientWithBuilderReturnsNotNull() throws AS4Exception {
        //Arrange
        SetupEndpoint();
        SetupCrypto();
        SetupPassword();
        //Assert
        Assertions.assertNotNull( as4ClientBuilder.build() );
    }



    @Test
    @DisplayName("Tools: Correctly converts status response string to object.")
    void toolTestStatusResponseOK() throws AS4Exception {
        //Arrange
        String response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><st:StatusResponse xmlns:st=\"urn:dk:skat:mft:DMS.declaration.status:1\"><st:Code>OK</st:Code></st:StatusResponse>";
        //Act
        StatusResponseType result = Tools.getStatus(response);
        //Assert
        Assertions.assertEquals(result.getCode(), "OK" );
    }

}
