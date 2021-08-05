package dk.toldst.eutk.declaration;

import org.junit.jupiter.api.Test;
import wco.datamodel.wco.dec_dms._2.Declarant;
import wco.datamodel.wco.dec_dms._2.DeclarationH7;
import wco.datamodel.wco.declaration_ds.dms._2.DeclarantIdentificationIDType;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Set;

public class TestDeclarationCreation{
    @Test
    public void unmarshallTest(){
        String h7String = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns2:Declaration xmlns=\"urn:wco:datamodel:WCO:Declaration_DS:DMS:2\" xmlns:ns2=\"urn:wco:datamodel:WCO:DEC-DMS:2\">\n" +
                "    <ns2:FunctionCode>9</ns2:FunctionCode>\n" +
                "\t<ns2:ProcedureCategory>H7</ns2:ProcedureCategory>\n" +
                "    <ns2:FunctionalReferenceID>{{LRN}}</ns2:FunctionalReferenceID>\n" +
                "    <ns2:TypeCode>IMA</ns2:TypeCode>\n" +
                "    <ns2:GoodsItemQuantity>1</ns2:GoodsItemQuantity>\n" +
                "    <ns2:DeclarationOfficeID>DK003103</ns2:DeclarationOfficeID>\n" +
                "    <ns2:TotalGrossMassMeasure>1</ns2:TotalGrossMassMeasure>\n" +
                "    <ns2:Submitter>\n" +
                "        <ns2:Name>swp.tdp01.b2b</ns2:Name>\n" +
                "        <ns2:ID>adf0-9b4774cb1f1d</ns2:ID>\n" +
                "    </ns2:Submitter>\n" +
                "    <ns2:Declarant>\n" +
                "        <ns2:ID>DK15527137</ns2:ID>\n" +
                "    </ns2:Declarant>\n" +
                "    <ns2:Exporter>\n" +
                "        <ns2:Name>Name</ns2:Name>\n" +
                "        <ns2:Address>\n" +
                "            <ns2:CityName>Oslo</ns2:CityName>\n" +
                "            <ns2:CountryCode>NO</ns2:CountryCode>\n" +
                "            <ns2:Line>Street Address 123</ns2:Line>\n" +
                "            <ns2:PostcodeID>1345</ns2:PostcodeID>\n" +
                "        </ns2:Address>\n" +
                "    </ns2:Exporter>\n" +
                "    <ns2:GoodsShipment>\n" +
                "        <ns2:Consignment>\n" +
                "            <ns2:GoodsLocation>\n" +
                "                <ns2:Name>DKFDH</ns2:Name>\n" +
                "                <ns2:ID>0003</ns2:ID>\n" +
                "                <ns2:TypeCode>A</ns2:TypeCode>\n" +
                "                <ns2:Address>\n" +
                "                    <ns2:TypeCode>U</ns2:TypeCode>\n" +
                "                    <ns2:CityName>Copenhagen</ns2:CityName>\n" +
                "                    <ns2:CountryCode>DK</ns2:CountryCode>\n" +
                "                    <ns2:Line>Copenhagen dock 123</ns2:Line>\n" +
                "                    <ns2:PostcodeID>1289</ns2:PostcodeID>\n" +
                "                </ns2:Address>\n" +
                "            </ns2:GoodsLocation>\n" +
                "        </ns2:Consignment>\n" +
                "        <ns2:CustomsValuation>\n" +
                "            <ns2:FreightChargeAmount currencyID=\"DKK\">1</ns2:FreightChargeAmount>\n" +
                "        </ns2:CustomsValuation>\n" +
                "        <ns2:GovernmentAgencyGoodsItem>\n" +
                "            <ns2:CustomsValueAmount currencyID=\"DKK\">45</ns2:CustomsValueAmount>\n" +
                "            <ns2:SequenceNumeric>1</ns2:SequenceNumeric>\n" +
                "            <ns2:AdditionalDocument>\n" +
                "                <ns2:SequenceNumeric>1</ns2:SequenceNumeric>\n" +
                "                <ns2:CategoryCode>Y</ns2:CategoryCode>\n" +
                "                <ns2:ID>1231</ns2:ID>\n" +
                "                <ns2:TypeCode>900</ns2:TypeCode>\n" +
                "            </ns2:AdditionalDocument>\n" +
                "            <ns2:Commodity>\n" +
                "                <ns2:Description>Single focus spectacle lenses of glass.</ns2:Description>\n" +
                "                <ns2:Classification>\n" +
                "                    <ns2:SequenceNumeric>1</ns2:SequenceNumeric>\n" +
                "                    <ns2:ID>900140</ns2:ID>\n" +
                "                    <ns2:IdentificationTypeCode>TSP</ns2:IdentificationTypeCode>\n" +
                "                </ns2:Classification>\n" +
                "                <ns2:GoodsMeasure>\n" +
                "                    <ns2:GrossMassMeasure>1</ns2:GrossMassMeasure>\n" +
                "                </ns2:GoodsMeasure>\n" +
                "            </ns2:Commodity>\n" +
                "            <ns2:GovernmentProcedure>\n" +
                "                <ns2:SequenceNumeric>1</ns2:SequenceNumeric>\n" +
                "                <ns2:CurrentCode>40</ns2:CurrentCode>\n" +
                "                <ns2:PreviousCode>00</ns2:PreviousCode>\n" +
                "            </ns2:GovernmentProcedure>\n" +
                "            <ns2:GovernmentProcedure>\n" +
                "                <ns2:SequenceNumeric>2</ns2:SequenceNumeric>\n" +
                "                <ns2:CurrentCode>C07</ns2:CurrentCode>\n" +
                "            </ns2:GovernmentProcedure>\n" +
                "            <ns2:Packaging>\n" +
                "                <ns2:SequenceNumeric>1</ns2:SequenceNumeric>\n" +
                "                <ns2:QuantityQuantity>1</ns2:QuantityQuantity>\n" +
                "            </ns2:Packaging>\n" +
                "            <ns2:PreviousDocument>\n" +
                "                <ns2:SequenceNumeric>1</ns2:SequenceNumeric>\n" +
                "                <ns2:CategoryCode>Y</ns2:CategoryCode>\n" +
                "                <ns2:ID>CommercialInvoiceDOCID1</ns2:ID>\n" +
                "                <ns2:TypeCode>380</ns2:TypeCode>\n" +
                "                <ns2:LineNumeric>1</ns2:LineNumeric>\n" +
                "            </ns2:PreviousDocument>\n" +
                "        </ns2:GovernmentAgencyGoodsItem>\n" +
                "        <ns2:Importer>\n" +
                "            <ns2:ID>DK15527137</ns2:ID>\n" +
                "            <ns2:Address>\n" +
                "                <ns2:CountryCode>DK</ns2:CountryCode>\n" +
                "            </ns2:Address>\n" +
                "        </ns2:Importer>\n" +
                "    </ns2:GoodsShipment>\n" +
                "</ns2:Declaration>";

        JAXBContext jaxbContext = null;
        Unmarshaller m = null;
        try {
            jaxbContext = JAXBContext.newInstance("dk.toldst.eutk.declaration");
            m = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        StringReader reader = new StringReader(h7String);
        JAXBElement<DeclarationH7> h7 = null;
        try {
            h7 = (JAXBElement<DeclarationH7>) m.unmarshal(reader);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        System.out.println(h7.getValue().getGoodsShipment().getImporter().getID().getValue());
    }

    @Test
    public void marshallTest(){
        DeclarationH7 h7 = new DeclarationH7();
        Declarant declarant = new Declarant();
        DeclarantIdentificationIDType id = new DeclarantIdentificationIDType();
        id.setValue("hej");
        declarant.setID(id);
        h7.setDeclarant(declarant);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<DeclarationH7>> violations = validator.validate(h7);
        int y = 0;

//        ObjectFactory of = new ObjectFactory();
//
//        JAXBContext jaxbContext = null;
//        Marshaller m = null;
//        StringWriter sw = new StringWriter();
//        try {
//            jaxbContext = JAXBContext.newInstance("dk.toldst.eutk.declaration");
//            m = jaxbContext.createMarshaller();
//            m.marshal(of.createDeclaration(h7), sw);
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }


    }

}