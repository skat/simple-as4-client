package dk.toldst.eutk.declaration.objectfactory.i2;

import dk.toldst.eutk.declaration.objectfactory.EutkObjectFactory;
import wco.datamodel.wco.dec_dms._2.DeclarationI2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory extends wco.datamodel.wco.dec_dms._2.ObjectFactory implements EutkObjectFactory<DeclarationI2> {
    private final static QName _Declaration_QNAME = new QName("urn:wco:datamodel:WCO:DEC-DMS:2", "Declaration");

    @XmlElementDecl(namespace = "urn:wco:datamodel:WCO:DEC-DMS:2", name = "Declaration")
    public JAXBElement<DeclarationI2> createDeclaration(DeclarationI2 value) {
        return new JAXBElement<>(_Declaration_QNAME, DeclarationI2 .class, null, value);
    }

    @Override
    public JAXBElement<DeclarationI2> getJaxbElement(DeclarationI2 o) {
        return createDeclaration(o);
    }

    @Override
    public Class<DeclarationI2> getType() {
        return DeclarationI2.class;
    }
}
