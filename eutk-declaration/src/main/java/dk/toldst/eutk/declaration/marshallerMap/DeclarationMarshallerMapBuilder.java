package dk.toldst.eutk.declaration.marshallerMap;

import dk.toldst.eutk.declaration.obejctfactory.EutkObjectFactory;
import dk.toldst.eutk.utility.jaxb.JaxbThreadSafe;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class DeclarationMarshallerMapBuilder {
    private static DeclarationMarshallerMap marshallerMap = new DeclarationMarshallerMap();
    private static DeclarationMarshallerMapBuilderAddStep declarationMarshallerMapBuilderAddStep = new DeclarationMarshallerMapBuilderAddStep();

    public static IDeclarationMarshallerMapBuilderAddStep builder(){
        return declarationMarshallerMapBuilderAddStep;
    }

    private static class DeclarationMarshallerMapBuilderAddStep implements IDeclarationMarshallerMapBuilderAddStep{

        @Override
        public <T extends EutkObjectFactory> IDeclarationMarshallerMapBuilderAddStep add(Class<?> eutk, EutkObjectFactory objectFactory) throws JAXBException {
            marshallerMap.setMarshaller(eutk, new JaxbThreadSafe(JAXBContext.newInstance(objectFactory.getClass())));
            marshallerMap.setObjectFactory(eutk, objectFactory);
            return declarationMarshallerMapBuilderAddStep;
        }

        @Override
        public DeclarationMarshallerMap build() {
            return marshallerMap;
        }
    }
}
