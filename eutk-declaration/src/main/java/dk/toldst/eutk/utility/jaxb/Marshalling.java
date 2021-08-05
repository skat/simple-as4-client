package dk.toldst.eutk.utility.jaxb;


import javax.xml.bind.JAXBException;
import javax.xml.transform.Result;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

public interface Marshalling {
    Object unmarshal(InputStream is) throws JAXBException;

    Object unmarshal(File f) throws JAXBException;

    void marshal(Object jaxbElement, OutputStream os) throws JAXBException;

    void marshal(Object jaxbElement, File output) throws JAXBException;

    void marshal(Object jaxbElement, Result result) throws JAXBException;
    void marshal(Object declaration, Writer sw) throws JAXBException;
}
