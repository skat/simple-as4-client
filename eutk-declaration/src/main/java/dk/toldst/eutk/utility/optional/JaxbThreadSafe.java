package dk.toldst.eutk.utility.optional;

import dk.toldst.eutk.as4client.utilities.Marshalling;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.concurrent.Callable;

/**
 * Use only if your framework does not supply a jaxb solution.
 * If you use Spring  JAXB2Marshaller, or MOXy, implement the Marshalling interface, and supply in the builder.
 */
public class JaxbThreadSafe implements Marshalling {
    private final ThreadLocal<Unmarshaller> unmarshaller;
    private final ThreadLocal<Marshaller> marshaller;

    public JaxbThreadSafe(JAXBContext jaxb) {
        this.unmarshaller = ThreadLocal.withInitial(() -> safe(jaxb::createUnmarshaller));
        this.marshaller = ThreadLocal.withInitial(() -> safe(jaxb::createMarshaller));
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

    @Override
    public Object unmarshal(InputStream is) throws JAXBException {
        return unmarshaller.get().unmarshal(is);
    }

    @Override
    public Object unmarshal(File f) throws JAXBException {
        return unmarshaller.get().unmarshal(f);
    }

    @Override
    public void marshal(Object jaxbElement, OutputStream os) throws JAXBException {
        marshaller.get().marshal(jaxbElement, os);
    }

    @Override
    public void marshal(Object jaxbElement, File output) throws JAXBException {
        marshaller.get().marshal(jaxbElement, output);
    }

    @Override
    public void marshal(Object jaxbElement, Result result) throws JAXBException {
        marshaller.get().marshal(jaxbElement, result);
    }

    @Override
    public void marshal(Object jaxbElement, Writer result) throws JAXBException {
        marshaller.get().marshal(jaxbElement, result);
    }

}