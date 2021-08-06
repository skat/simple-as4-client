package dk.toldst.eutk.declaration;

import dk.toldst.eutk.declaration.obejctfactory.EutkObjectFactory;
import dk.toldst.eutk.declaration.obejctfactory.ObjectFactoryMarshallerEntry;
import dk.toldst.eutk.utility.jaxb.Marshalling;

import javax.xml.bind.JAXBException;
import java.io.StringWriter;
import java.util.List;

public class DeclarationStringinator {
    private static DeclarationStringinator instance = null;
    private static List<ObjectFactoryMarshallerEntry> ofEntries;

    public static DeclarationStringinator getInstance(){
        if(null == instance){
            instance = new DeclarationStringinator();
        }
        return instance;
    }

    private DeclarationStringinator() {
    }

    public static DeclarationStringinator init(List<ObjectFactoryMarshallerEntry> entries){
        ofEntries = entries;
        return getInstance();
    }

    public <T> String getString(T o){
        EutkObjectFactory of = ofEntries.stream().filter(x -> x.getEutk().equals(o.getClass())).findFirst().get().getObjectFactory();
        Marshalling marshaller = ofEntries.stream().filter(x -> x.getEutk().equals(o.getClass())).findFirst().get().getMarshaller();
        if(null == instance){
            throw new RuntimeException("This is a singleton class, please run its init function");
        }
        StringWriter sw = new StringWriter();
        try {
            marshaller.marshal(of.getJaxbElement(o), sw);
        } catch (JAXBException e) {
            throw new RuntimeException("An error happened in the marshalling");
        }
        return sw.toString();
    }
}
