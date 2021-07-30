import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.builder.support.As4ClientBuilderInstance;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class main {
    public static void main(String[] args) throws IOException, TransformerException {
        As4Client client = new As4ClientBuilderInstance().builder().
            setEndpoint(new URL("http://localhost:8384/exchange/CVR_13116482_UID_50151991"))
                .setCrypto("security/as4crypto-holodeck.properties")
                .setUserNameTokenDetails("CVR_13116482_UID_50151991", "HBNRsvph68").
                build();

        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns2:Declaration xmlns=\"urn:wco:datamodel:WCO:Declaration_DS:DMS:2\" xmlns:ns2=\"urn:wco:datamodel:WCO:DEC-DMS:2\"><ns2:FunctionCode>9</ns2:FunctionCode><ns2:ProcedureCategory>H7</ns2:ProcedureCategory><ns2:FunctionalReferenceID>CBMUserCredTest9</ns2:FunctionalReferenceID><ns2:TypeCode>IMA</ns2:TypeCode><ns2:GoodsItemQuantity>1</ns2:GoodsItemQuantity><ns2:DeclarationOfficeID>DK003103</ns2:DeclarationOfficeID><ns2:TotalGrossMassMeasure>1.5</ns2:TotalGrossMassMeasure> <ns2:Submitter><ns2:Name>13116482</ns2:Name><ns2:ID>13116482</ns2:ID></ns2:Submitter><ns2:Agent><ns2:ID>DK13421730</ns2:ID><ns2:FunctionCode>2</ns2:FunctionCode></ns2:Agent><ns2:Declarant><ns2:Name>Declarant Name</ns2:Name><ns2:ID>DK09999981</ns2:ID><ns2:Address><ns2:CityName>Copenhagen</ns2:CityName><ns2:CountryCode>DK</ns2:CountryCode><ns2:Line>Copenhagen City centre 123</ns2:Line><ns2:PostcodeID>9922</ns2:PostcodeID></ns2:Address></ns2:Declarant><ns2:Exporter><ns2:Name>Exporter Name</ns2:Name><ns2:Address><ns2:CityName>Oslo</ns2:CityName><ns2:CountryCode>NO</ns2:CountryCode><ns2:Line>Street Address</ns2:Line><ns2:PostcodeID>1345</ns2:PostcodeID></ns2:Address></ns2:Exporter><ns2:GoodsShipment><ns2:Consignment><ns2:GoodsLocation><ns2:Name>DKFDH</ns2:Name><ns2:ID>0003</ns2:ID><ns2:TypeCode>A</ns2:TypeCode><ns2:Address><ns2:TypeCode>U</ns2:TypeCode><!-->    <ns2:CityName>Copenhagen</ns2:CityName> --><ns2:CountryCode>DK</ns2:CountryCode> <!-->    <ns2:Line>Copenhagen dock 123</ns2:Line><ns2:PostcodeID>1289</ns2:PostcodeID> --></ns2:Address></ns2:GoodsLocation></ns2:Consignment><ns2:CustomsValuation><ns2:FreightChargeAmount currencyID=\"DKK\">141.64</ns2:FreightChargeAmount></ns2:CustomsValuation><ns2:GovernmentAgencyGoodsItem><ns2:CustomsValueAmount currencyID=\"DKK\">139.53</ns2:CustomsValueAmount><ns2:SequenceNumeric>1</ns2:SequenceNumeric><ns2:Commodity><ns2:Description>Single focus spectacle lenses of glass.</ns2:Description><ns2:Classification><ns2:SequenceNumeric>1</ns2:SequenceNumeric><ns2:ID>900140</ns2:ID><ns2:IdentificationTypeCode>TSP</ns2:IdentificationTypeCode></ns2:Classification><ns2:GoodsMeasure><ns2:GrossMassMeasure>1.5</ns2:GrossMassMeasure></ns2:GoodsMeasure></ns2:Commodity><ns2:CustomsValuation><ns2:FreightChargeAmount currencyID=\"DKK\">141.64</ns2:FreightChargeAmount></ns2:CustomsValuation><ns2:GovernmentProcedure><ns2:SequenceNumeric>1</ns2:SequenceNumeric><ns2:CurrentCode>40</ns2:CurrentCode><ns2:PreviousCode>00</ns2:PreviousCode></ns2:GovernmentProcedure><ns2:GovernmentProcedure><ns2:SequenceNumeric>2</ns2:SequenceNumeric><ns2:CurrentCode>C08</ns2:CurrentCode></ns2:GovernmentProcedure><!--         <ns2:GovernmentProcedure><ns2:SequenceNumeric>3</ns2:SequenceNumeric><ns2:CurrentCode>F49</ns2:CurrentCode></ns2:GovernmentProcedure> --><ns2:Packaging><ns2:SequenceNumeric>1</ns2:SequenceNumeric><ns2:QuantityQuantity>1</ns2:QuantityQuantity></ns2:Packaging><ns2:PreviousDocument><ns2:SequenceNumeric>1</ns2:SequenceNumeric><ns2:CategoryCode>Y</ns2:CategoryCode><ns2:ID>ID13</ns2:ID><ns2:TypeCode>380</ns2:TypeCode><ns2:LineNumeric>1</ns2:LineNumeric></ns2:PreviousDocument></ns2:GovernmentAgencyGoodsItem><ns2:Importer><ns2:Name>Importer Name</ns2:Name><ns2:ID>DK09999981</ns2:ID><ns2:Address><ns2:CityName>Copenhagen</ns2:CityName><ns2:CountryCode>DK</ns2:CountryCode><ns2:Line>Copenhagen City centre 123</ns2:Line><ns2:PostcodeID>9922</ns2:PostcodeID></ns2:Address></ns2:Importer></ns2:GoodsShipment>\n" +
                "</ns2:Declaration>";

        String res = client.executePush("DMS.Import", "Declaration.Submit", content.getBytes(StandardCharsets.UTF_8));

        System.out.println(res);


    }

}
