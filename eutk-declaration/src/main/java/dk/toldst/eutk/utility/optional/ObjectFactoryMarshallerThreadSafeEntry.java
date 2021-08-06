package dk.toldst.eutk.utility.optional;

import dk.toldst.eutk.declaration.obejctfactory.EutkObjectFactory;
import dk.toldst.eutk.declaration.obejctfactory.ObjectFactoryMarshallerEntry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class ObjectFactoryMarshallerThreadSafeEntry<T> extends ObjectFactoryMarshallerEntry<T> {
    public ObjectFactoryMarshallerThreadSafeEntry(EutkObjectFactory<T> objectFactory) throws JAXBException {
        super(objectFactory, new JaxbThreadSafe(JAXBContext.newInstance(objectFactory.getClass())));
    }
}
