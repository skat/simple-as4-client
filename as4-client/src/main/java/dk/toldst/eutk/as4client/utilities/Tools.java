package dk.toldst.eutk.as4client.utilities;

import dk.skat.mft.dms_declaration_status._1.StatusResponseType;
import dk.toldst.eutk.as4client.exceptions.AS4Exception;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class Tools {
    public static StatusResponseType getStatus(String string) throws AS4Exception {
        StatusResponseType responseType;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance("dk.skat.mft.dms_declaration_status._1");
            JaxbThreadSafe jaxbThreadSafe = new JaxbThreadSafe(jaxbContext);
            var element = (JAXBElement<StatusResponseType>) jaxbThreadSafe.unmarshal(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8)));
            responseType = element.getValue();
        }
        catch (JAXBException | ClassCastException e){
            throw new AS4Exception("Converting message from XML to StatusResponseType failed" , e);
        }
        return responseType;
    }

}
