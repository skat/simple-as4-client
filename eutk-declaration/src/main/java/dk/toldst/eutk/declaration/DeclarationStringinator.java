package dk.toldst.eutk.declaration;

import dk.toldst.eutk.declaration.marshallerMap.DeclarationMarshallerMap;
import dk.toldst.eutk.declaration.obejctfactory.EutkObjectFactory;
import dk.toldst.eutk.utility.jaxb.Marshalling;

import javax.xml.bind.JAXBException;
import java.io.StringWriter;

public class DeclarationStringinator {
    private static DeclarationStringinator instance = null;
    private static DeclarationMarshallerMap map;

    public static DeclarationStringinator getInstance(){
        if(null == instance){
            instance = new DeclarationStringinator();
        }
        return instance;
    }

    private DeclarationStringinator() {
    }

    public static DeclarationStringinator init(DeclarationMarshallerMap marshallerMap){
        map = marshallerMap;
        return getInstance();
    }
    public <T> String getString(T o){
        EutkObjectFactory of = map.getObjectFactory(o.getClass());
        Marshalling marshaller = map.getMarshaller(o.getClass());
        if(null == instance){
            throw new RuntimeException("This is a singleton class, please run its init function");
        }
        StringWriter sw = new StringWriter();
        try {
            marshaller.marshal(of.getJaxbElement(o), sw);
        } catch (JAXBException e) {
            //TODO
        }
        return sw.toString();
    }
}
