package dk.toldst.eutk.as4client.utilities;


import javax.xml.bind.JAXBException;
import javax.xml.transform.Result;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

public interface Marshalling {
    public final String defaultPackages = "org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704:dk.skat.mft.dms_declaration_status._1";
    Object unmarshal(InputStream is) throws JAXBException;

    Object unmarshal(File f) throws JAXBException;

    void marshal(Object jaxbElement, OutputStream os) throws JAXBException;

    void marshal(Object jaxbElement, File output) throws JAXBException;

    void marshal(Object jaxbElement, Result result) throws JAXBException;
    void marshal(Object declaration, Writer sw) throws JAXBException;
}
