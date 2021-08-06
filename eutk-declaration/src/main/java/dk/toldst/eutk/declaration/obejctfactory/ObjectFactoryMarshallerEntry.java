package dk.toldst.eutk.declaration.obejctfactory;

import dk.toldst.eutk.utility.jaxb.Marshalling;

public class ObjectFactoryMarshallerEntry<T> {
    private final EutkObjectFactory<T> objectFactory;
    private final Marshalling marshaller;

    public ObjectFactoryMarshallerEntry(EutkObjectFactory<T> objectFactory, Marshalling marshaller) {
        this.objectFactory = objectFactory;
        this.marshaller = marshaller;
    }

    public EutkObjectFactory<T> getObjectFactory() {
        return objectFactory;
    }

    public Marshalling getMarshaller() {
        return marshaller;
    }

    public Class<T> getEutk() {
        return objectFactory.getType();
    }
}
