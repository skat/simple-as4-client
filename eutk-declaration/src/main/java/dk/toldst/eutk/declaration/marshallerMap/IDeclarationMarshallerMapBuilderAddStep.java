package dk.toldst.eutk.declaration.marshallerMap;


import dk.toldst.eutk.declaration.obejctfactory.EutkObjectFactory;

import javax.xml.bind.JAXBException;

public interface IDeclarationMarshallerMapBuilderAddStep {
    <T extends EutkObjectFactory> IDeclarationMarshallerMapBuilderAddStep add(Class<?> eutk, EutkObjectFactory objectFactoryClass) throws JAXBException;

    DeclarationMarshallerMap build();
}
