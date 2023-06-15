package dk.toldst.eutk.as4client.builder.support;

import dk.toldst.eutk.as4client.As4Client;
import dk.toldst.eutk.as4client.As4ClientInstance;
import dk.toldst.eutk.as4client.builder.As4ClientBuilder;
import dk.toldst.eutk.as4client.builder.interfaces.As4Optionals;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetCrypto;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetEndpoint;
import dk.toldst.eutk.as4client.builder.interfaces.As4SetPasswordTokenDetails;
import dk.toldst.eutk.as4client.as4.As4DtoCreator;
import dk.toldst.eutk.as4client.as4.As4HttpClient;
import dk.toldst.eutk.as4client.as4.SecurityService;
import dk.toldst.eutk.as4client.exceptions.AS4Exception;
import dk.toldst.eutk.as4client.userinformation.As4UserInformation;
import dk.toldst.eutk.as4client.userinformation.As4UserInformationType;
import dk.toldst.eutk.as4client.utilities.JaxbThreadSafe;
import org.apache.http.client.utils.URIBuilder;
import org.apache.wss4j.common.crypto.Crypto;
import org.apache.wss4j.common.crypto.CryptoFactory;
import org.apache.wss4j.common.crypto.Merlin;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.xml.security.Init;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An As4Client Builder class instance.
 */
public class As4ClientBuilderInstance implements As4ClientBuilder {


    private As4SetPasswordTokenDetailsInstance as4SetUsernameTokenDetailsInstance;
    private As4SetCryptoInstance as4SetCryptoInstance;
    private As4SetEndpointInstance as4SetEndpointInstance;

    //Username -> Client

    /**
     * Builds the client with the set parameters.
     * @return the client
     * @throws AS4Exception
     */
    public As4Client build() throws AS4Exception {

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance("org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704");
        } catch (JAXBException e) {
            throw new AS4Exception("Failed to establish JAXBContext" , e);
        }

        SecurityService securityService = new SecurityService(
                as4SetCryptoInstance.username,
                as4SetUsernameTokenDetailsInstance.password,
                as4SetCryptoInstance.crypto,
                as4SetCryptoInstance.cryptoProperties
        );

        JaxbThreadSafe jaxbThreadSafe = new JaxbThreadSafe(jaxbContext);

        URIBuilder builder = new URIBuilder();
        builder.setPort(as4SetEndpointInstance.urlBase.getPort());
        builder.setScheme(as4SetEndpointInstance.urlBase.getScheme());
        builder.setScheme(as4SetEndpointInstance.urlBase.getScheme());
        builder.setHost(as4SetEndpointInstance.urlBase.getHost());
        builder.setPathSegments("exchange", as4SetCryptoInstance.username);

        As4ClientInstance as4Client;
        try{
            As4HttpClient as4HttpClient = new As4HttpClient(jaxbThreadSafe, securityService, builder.build());
            As4DtoCreator as4DtoCreator = new As4DtoCreator(as4SetCryptoInstance.username + "_AS4", "SKAT-MFT-AS4");
            as4Client = new As4ClientInstance(as4DtoCreator, as4HttpClient);
        }
        catch (URISyntaxException e){
            throw new AS4Exception("Attempting to build URI failed" , e);
        }
        return as4Client;
    }


    @Override
    public As4Optionals optionals() {
        return new As4OptionalsBuilder(this);
    }

    //Builder -> Endpoint

    /**
     * Invokes the builder, starting the pipeline process of building a client.
     * @return an AS4SetEndpoint Instance, on which the endpoint of the client can be set.
     */
    public As4SetEndpoint builder() {
        as4SetEndpointInstance = new As4SetEndpointInstance();
        return as4SetEndpointInstance;
    }

    private class As4SetPasswordTokenDetailsInstance implements As4SetPasswordTokenDetails {
        private String password;

        @Override

        public As4ClientBuilder setPassword(String password) {
            this.password = password;
            return As4ClientBuilderInstance.this;
        }
    }

    //Crypto -> User
    private class As4SetCryptoInstance implements As4SetCrypto {
        private Crypto crypto;
        private Properties cryptoProperties;
        private String username;

        /**
         * Use this for generic loading of crypto properties. This should be used if your project doesn't have resource loading, or similar.
         * @param filepath the file to read security properties from.
         * @return Next step in the builder pattern.
         */
        @Override
        public As4SetPasswordTokenDetails setCrypto(String filepath) throws AS4Exception {
            System.out.println("running crypto setup");
            try {
                Init.init();
                cryptoProperties = CryptoFactory
                        .getProperties(filepath, CryptoFactory.class.getClassLoader());
                crypto = CryptoFactory.getInstance(cryptoProperties);

                setUsernameFromCertificate(crypto, cryptoProperties);
            } catch (WSSecurityException | KeyStoreException | AS4Exception e) {
                throw new AS4Exception("Creating crypto and crypto properties failed" , e);
            }
            as4SetUsernameTokenDetailsInstance = new As4SetPasswordTokenDetailsInstance();
            return as4SetUsernameTokenDetailsInstance;
        }

        /**
         * Use this to set the crypto, if your project has the ability to load resources.
         * @param crypto The crypto object, supplying certificate and Private Key
         * @param cryptoProperties Additional properties required for processing
         * @return Next step in the builder pattern.
         */
        @Override
        public As4SetPasswordTokenDetails setCrypto(Merlin crypto, Properties cryptoProperties) throws AS4Exception {
            try {
                this.cryptoProperties = cryptoProperties;
                this.crypto = crypto;

                setUsernameFromCertificate(crypto, cryptoProperties);
            } catch (KeyStoreException | AS4Exception e) {
                throw new AS4Exception("Creating crypto and crypto properties failed" , e);
            }

            as4SetUsernameTokenDetailsInstance = new As4SetPasswordTokenDetailsInstance();
            return as4SetUsernameTokenDetailsInstance;
        }

        private void setUsernameFromCertificate(Crypto crypto, Properties cryptoProperties) throws KeyStoreException, AS4Exception {
            var certificates = new X509Certificate[] { (X509Certificate)(((Merlin) crypto).getKeyStore()
                    .getCertificate(cryptoProperties.getProperty("org.apache.wss4j.crypto.merlin.keystore.alias"))) };
            var userInfo = mapCertificateToUserInformation(certificates);
            username = mapUserInformationToUsernameString(userInfo);
        }
    }

    //Endpoint -> Crypto
    private class As4SetEndpointInstance implements As4SetEndpoint {
        private java.net.URI urlBase;

        @Override
        public As4SetCrypto setEndpoint(java.net.URI url) {
            this.urlBase = url;
            as4SetCryptoInstance = new As4SetCryptoInstance();
            return as4SetCryptoInstance;
        }

        @Override
        public As4SetCrypto setEndpoint(String url) throws AS4Exception {
            URI uri;
            try {
                uri = new URI(url);
            }
            catch (URISyntaxException e){
                throw new AS4Exception("Failed to convert string to URI", e);
            }
            return setEndpoint(uri);
        }
    }

    public static String mapUserInformationToUsernameString(As4UserInformation information)
    {
        StringBuilder res = new StringBuilder();
        res.append("CVR_");
        res.append(information.getCvr());
        res.append("_");
        res.append(information.getType().name());
        res.append("_");
        res.append(information.getId());
        return res.toString();
    }


    public static As4UserInformation mapCertificateToUserInformation(final X509Certificate[] certificates) throws AS4Exception {
        if (certificates == null || certificates.length < 1 || certificates[0] == null) {
            throw new AS4Exception("The certificate array does not have any certificates");
        }
        // Get first certificate in chain and extract the serial number
        final X509Certificate certificate = certificates[0];
        String serialNumber;
        serialNumber = certificate.getSubjectDN().getName();
        if (!(serialNumber.contains("SERIALNUMBER") || serialNumber.startsWith("CN=CVR:"))) {
            throw new AS4Exception("Attribute for serial number was not found in the certificate");
        }
        // We support all four types of OCES certificates. Examples:
        // SERIALNUMBER = CVR:30808460-UID:1237552804997
        // SERIALNUMBER = CVR:30808460-RID:1237553529373
        // SERIALNUMBER = CVR:30808460-FID:1255692730737
        // SERIALNUMBER = PID:9208-2002-2-735089857982
        // CN=CVR:12635729
        As4UserInformation userInformation;
        // Try to get a RID ("medarbejdercertifikat")
        Matcher matcherSN = Pattern.compile("SERIALNUMBER=CVR:(\\w+)-RID:(\\w+)").matcher(serialNumber);
        if (matcherSN.find()) {
            final String cvr = matcherSN.group(1);
            final String rid = matcherSN.group(2);
            userInformation = new As4UserInformation(As4UserInformationType.RID, rid, cvr);
        } else {
            // Try to get a UID ("virksomhedscertifikat")
            matcherSN = Pattern.compile("SERIALNUMBER=CVR:(\\w+)-UID:(\\w+)").matcher(serialNumber);
            if (matcherSN.find()) {
                final String cvr = matcherSN.group(1);
                final String uid = matcherSN.group(2);
                userInformation = new As4UserInformation(As4UserInformationType.UID, uid, cvr);
            } else {
                // Try to get a PID ("personcertifikat")
                matcherSN = Pattern.compile("PID:(\\w+)").matcher(serialNumber);
                if (matcherSN.find()) {
                    final String pid = matcherSN.group(1);
                    userInformation = new As4UserInformation(As4UserInformationType.PID, pid, null);
                } else {
                    // Try to get a FID ("funktionscertifikat")
                    matcherSN = Pattern.compile("SERIALNUMBER=CVR:(\\w+)-FID:(\\w+)").matcher(serialNumber);
                    if (matcherSN.find()) {
                        final String cvr = matcherSN.group(1);
                        final String fid = matcherSN.group(2);
                        userInformation = new As4UserInformation(As4UserInformationType.FID, fid, cvr);
                    } else {
                        // Try to get a CVR only. No other IDs
                        matcherSN = Pattern.compile("CN=CVR:(\\w+)").matcher(serialNumber);
                        if (matcherSN.find()) {
                            final String cvr = matcherSN.group(1);
                            userInformation = new As4UserInformation(As4UserInformationType.NID, null, cvr);
                        } else {
                            // This is probably not an OCES certificate
                            throw new AS4Exception("Could not find a supported OCES type (RID, UID, PID or FID) in the serial number: " + serialNumber);
                        }
                    }
                }
            }
        }
        return userInformation;
    }
}
