package dk.toldst.eutk.as4client;

import dk.toldst.eutk.as4client.builder.As4ClientBuilder;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetCrypto;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetEndpoint;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetPasswordTokenDetails;
import dk.toldst.eutk.as4client.builder.support.As4ClientBuilderInstance;
import dk.toldst.eutk.as4client.exceptions.AS4Exception;
import org.junit.jupiter.api.*;

import java.net.URI;

public class As4ClientTests {

    As4SetEndpoint as4SetEndpoint;
    As4SetCrypto as4SetCrypto;
    As4SetPasswordTokenDetails as4SetPasswordTokenDetails;
    As4ClientBuilder as4ClientBuilder;

    @BeforeEach
    void Setup() throws AS4Exception {
        as4SetEndpoint = new As4ClientBuilderInstance().builder();
        as4SetCrypto = as4SetEndpoint.setEndpoint("nourl.dk");
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
    void setEndpointToCorrectURLReturnsNotNull() throws AS4Exception {
        //Act
        As4SetCrypto crypto = as4SetEndpoint.setEndpoint("nourl.dk");
        //Assert
        Assertions.assertNotNull(crypto);
    }

    @Test
    void setCryptoToCorrectFileReturnsThrows() {
        //Assert
        Assertions.assertThrows(AS4Exception.class, () -> {
            as4SetCrypto.setCrypto("file");
        });
    }

/*

    @Test
    void setCryptoToCorrectFileReturnsNotNull() throws AS4Exception {
        //Assert
        Assertions.assertNotNull(as4SetCrypto.setCrypto("security/as4crypto-holodeck.properties"));
    }
    @Test
    void setAs4SetPasswordTokenDetailsReturnsNotNull() {
        Assertions.assertNotNull(as4ClientBuilder);
    }
*/
}
