package dk.toldst.eutk.as4client.as4;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;

import static org.junit.jupiter.api.Assertions.*;

class As4DtoCreatorTest {

    @Test
    @DisplayName("Messaging can be created")
    void createMessagingTest() {
        As4DtoCreator as4DtoCreator = new As4DtoCreator("a","b");
        Messaging msg = as4DtoCreator.createMessaging("a","b","c", "d", new As4Message(), "e");
        Assertions.assertNotNull(msg);
    }
}