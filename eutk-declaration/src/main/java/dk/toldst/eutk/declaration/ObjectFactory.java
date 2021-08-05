package dk.toldst.eutk.declaration;

import wco.datamodel.wco.dec_dms._2.DeclarationH7;
//import wco.datamodel.wco.dec_dms._2.DeclarationI2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory extends wco.datamodel.wco.dec_dms._2.ObjectFactory {
    private final static QName _Declaration_QNAME = new QName("urn:wco:datamodel:WCO:DEC-DMS:2", "Declaration");

    @XmlElementDecl(namespace = "urn:wco:datamodel:WCO:DEC-DMS:2", name = "Declaration")
    public JAXBElement<DeclarationH7> createDeclaration(DeclarationH7 value) {
        return new JAXBElement<DeclarationH7>(_Declaration_QNAME, DeclarationH7 .class, null, value);
    }
}
