package dk.toldst.eutk.as4client.utilities;
import javax.xml.bind.*;
import javax.xml.transform.Result;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;

/**
 * Simpler thread-safe jaxb marshalling - unmarshalling
 */
public class JaxbThreadSafe {
    private ThreadLocal<Unmarshaller> unmarshaller;
    private ThreadLocal<Marshaller> marshaller;

    public JaxbThreadSafe(JAXBContext jaxb) {
        this.unmarshaller = ThreadLocal.withInitial(() -> safe(jaxb::createUnmarshaller));
        this.marshaller = ThreadLocal.withInitial(() -> safe(jaxb::createMarshaller));
    }

    public JaxbThreadSafe(Class ...classes) {
        this(safe(() -> JAXBContext.newInstance(classes)));
    }

    private static <T> T safe(Callable<T> fn) {
        try {
            return fn.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object unmarshal(InputStream is) throws JAXBException {
        return unmarshaller.get().unmarshal(is);
    }

    public Object unmarshal(File f) throws JAXBException {
        return unmarshaller.get().unmarshal(f);
    }

    public void marshal(Object jaxbElement, OutputStream os) throws JAXBException {
        marshaller.get().marshal(jaxbElement, os);
    }

    public void marshal(Object jaxbElement, File output) throws JAXBException {
        marshaller.get().marshal(jaxbElement, output);

    }

    public void marshal( Object jaxbElement, Result result ) throws JAXBException {
        marshaller.get().marshal(jaxbElement, result);
    }
}