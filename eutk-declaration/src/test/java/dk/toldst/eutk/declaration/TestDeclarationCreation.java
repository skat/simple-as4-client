package dk.toldst.eutk.declaration;

import dk.toldst.eutk.declaration.marshallerMap.DeclarationMarshallerMap;
import dk.toldst.eutk.declaration.marshallerMap.DeclarationMarshallerMapBuilder;
import org.junit.jupiter.api.Test;
import wco.datamodel.wco.dec_dms._2.Declarant;
import wco.datamodel.wco.dec_dms._2.DeclarationH7;
import wco.datamodel.wco.dec_dms._2.DeclarationI2;
import wco.datamodel.wco.dec_dms._2.Submitter;
import wco.datamodel.wco.declaration_ds.dms._2.DeclarantIdentificationIDType;
import wco.datamodel.wco.declaration_ds.dms._2.SubmitterIdentificationIDType;

import javax.xml.bind.JAXBException;

public class TestDeclarationCreation {

    @Test
    public void testH7() throws JAXBException {
        DeclarationMarshallerMap map = DeclarationMarshallerMapBuilder.builder()
                .add(DeclarationH7.class, new dk.toldst.eutk.declaration.obejctfactory.h7.ObjectFactory())
                .add(DeclarationI2.class, new dk.toldst.eutk.declaration.obejctfactory.i2.ObjectFactory())
                .build();

        DeclarationH7 h7 = new DeclarationH7();
        Submitter submitter = new Submitter();
        SubmitterIdentificationIDType id = new SubmitterIdentificationIDType();
        id.setValue("hej");
        submitter.setID(id);
        h7.setSubmitter(submitter);

        System.out.println(DeclarationStringinator.init(map).getString(h7));

        DeclarationI2 i2 = new DeclarationI2();
        i2.setSubmitter(submitter);
        System.out.println(DeclarationStringinator.getInstance().getString(i2));
    }
}