package dk.toldst.eutk.declaration.obejctfactory.h7;

import dk.toldst.eutk.declaration.obejctfactory.EutkObjectFactory;
import wco.datamodel.wco.dec_dms._2.DeclarationH7;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory extends wco.datamodel.wco.dec_dms._2.ObjectFactory implements EutkObjectFactory {
    private final static QName _Declaration_QNAME = new QName("urn:wco:datamodel:WCO:DEC-DMS:2", "Declaration");

    @XmlElementDecl(namespace = "urn:wco:datamodel:WCO:DEC-DMS:2", name = "Declaration")
    public JAXBElement<DeclarationH7> createDeclaration(DeclarationH7 value) {
        return new JAXBElement<DeclarationH7>(_Declaration_QNAME, DeclarationH7 .class, null, value);
    }

    @Override
    public JAXBElement<Object> getJaxbElement(Object o) {
        return (JAXBElement)createDeclaration((DeclarationH7) o);
    }
}
