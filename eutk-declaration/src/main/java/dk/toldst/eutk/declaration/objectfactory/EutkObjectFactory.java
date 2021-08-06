package dk.toldst.eutk.declaration.objectfactory;

import javax.xml.bind.JAXBElement;

public interface EutkObjectFactory<T> {
    public JAXBElement<T> getJaxbElement(T o);
    public Class<T> getType();
}
