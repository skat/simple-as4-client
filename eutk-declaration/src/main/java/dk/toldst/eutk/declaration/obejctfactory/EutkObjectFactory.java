package dk.toldst.eutk.declaration.obejctfactory;

import wco.datamodel.wco.dec_dms._2.DeclarationH7;

import javax.xml.bind.JAXBElement;

public interface EutkObjectFactory {
    public JAXBElement<Object> getJaxbElement(Object o);
}
