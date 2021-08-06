package dk.toldst.eutk.declaration;

import dk.toldst.eutk.declaration.obejctfactory.ObjectFactoryMarshallerEntry;
import dk.toldst.eutk.utility.optional.ObjectFactoryMarshallerThreadSafeEntry;
import org.junit.jupiter.api.Test;
import wco.datamodel.wco.dec_dms._2.DeclarationH7;
import wco.datamodel.wco.dec_dms._2.DeclarationI2;
import wco.datamodel.wco.dec_dms._2.Submitter;
import wco.datamodel.wco.declaration_ds.dms._2.SubmitterIdentificationIDType;

import javax.xml.bind.JAXBException;
import java.util.Arrays;
import java.util.List;

public class TestDeclarationCreation {

    @Test
    public void testH7() throws JAXBException {

        List<ObjectFactoryMarshallerEntry> list = Arrays.asList(
                new ObjectFactoryMarshallerThreadSafeEntry<>(new dk.toldst.eutk.declaration.obejctfactory.h7.ObjectFactory()),
                new ObjectFactoryMarshallerThreadSafeEntry<>(new dk.toldst.eutk.declaration.obejctfactory.i2.ObjectFactory())
        );

        DeclarationH7 h7 = new DeclarationH7();
        Submitter submitter = new Submitter();
        SubmitterIdentificationIDType id = new SubmitterIdentificationIDType();
        id.setValue("hej");
        submitter.setID(id);
        h7.setSubmitter(submitter);

        System.out.println(DeclarationStringinator.init(list).getString(h7));

        DeclarationI2 i2 = new DeclarationI2();
        i2.setSubmitter(submitter);
        System.out.println(DeclarationStringinator.getInstance().getString(i2));
    }
}