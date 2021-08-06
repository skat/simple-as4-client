package dk.toldst.eutk.declaration.objectfactory;

import dk.toldst.eutk.as4client.utilities.Marshalling;

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
