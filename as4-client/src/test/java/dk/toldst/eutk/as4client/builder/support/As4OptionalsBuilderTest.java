package dk.toldst.eutk.as4client.builder.support;

import dk.toldst.eutk.as4client.TestHelperTools;
import dk.toldst.eutk.as4client.builder.interfaces.As4Optionals;
import dk.toldst.eutk.as4client.exceptions.AS4Exception;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class As4OptionalsBuilderTest {

    As4Optionals as4Optionals;
    @BeforeEach
    void Setup() throws AS4Exception {
        as4Optionals = TestHelperTools.getAs4Optionals();
    }

    @Test
    void TestOptionalIsNotNull(){
        Assertions.assertNotNull(as4Optionals);
    }

    @Test
    @DisplayName("Optionals Can Build With No Options")
    void TestOptionalCanBuildWithNoOptions() throws AS4Exception {
        Assertions.assertNotNull(as4Optionals.build());
    }

    @Test
    @DisplayName("Optionals Can Build With ALL Options")
    void TestOptionalCanBuildWithAllOptions() throws AS4Exception {
        as4Optionals.noSSL()
                .setUsername("NotAUsername")
                .setAbsoluteURI("nowhere.com")
                .setActor("NotAnActor")
                .toParty("toName", "toRole", "")
                .fromParty("fromName", "fromRole", "");

        Assertions.assertNotNull(as4Optionals.build());
    }

}