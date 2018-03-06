package com.iw2fag.lab.security.encryption;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.UUID;
import java.util.Vector;

public abstract class CertificateUtil {

    private Logger logger = null;

    public static final String KEYSTORE_EXT = ".jks";
    public static final String PEM_EXT = ".pem";
    public static final String CSR_EXT = "_csr.pem";
    public static final String CRL_EXT = ".srl";
    public static final String PKCS_EXT = ".p12";
    public static final String CRT_EXT = ".crt";
    public static final String CERTIFICATE_PROPERTIES = "crt.properties";
    public static final String ALIAS = "connector";
    public static final String VALIDITY = "1095";  //3 years
    public static final String VALIDITY_DEFAULT = "1095"; //3 years
    public static final String DNAME_DEFAULT = "/title=hpmc/C=IL/ST=Yehud/O=HPMC/OU=HP/CN=Connector";
    public static final String ERR_FILE_DEFAULT = "err.txt";
    public static final String OUT_FILE_DEFAULT = "out.txt";
    public static final String MASTER_KEY_NAME = "hp4mcakey.pem";
    public static final String MASTER_CRT_NAME = "hp4mcacert.pem";

    public static final String JETTY_FILE = "jetty.xml";
    public static final String SECURITY = "Security";

    public static final String WORKING_DIR_DEFAULT = getHomePath() + File.separator + "tmp" + File.separator;

    public static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    public static final String END_CERT = "-----END CERTIFICATE-----";

    public static final String HP4M_HOME = "console.home";
    public static final String CONFIGURATION_FOLDER = "conf";

    private final ConsoleProcessExecutor consoleProcessExecutor = ConsoleProcessExecutor.getInstance();

    /**
     * @param dbPropertyFileName
     */
    public CertificateUtil(String dbPropertyFileName, Logger loggerParam) {
        this.logger = loggerParam;
    }

    /**
     * @param homePath
     * @return
     */
    public abstract CipherUtil getCipherUtil(final String homePath);

    public static String getHomePath() {
        String result = System.getProperty(HP4M_HOME);
        if (result == null) {
            return ".";
        }
        return result;
    }

    /**
     * Remove used certificate files:
     * rm -f $cert.jks
     * rm -f $cert.pkcs12
     * rm -f $cert.crt
     *
     * @param path
     * @param prefix
     * @return
     */
    public boolean removeCertificateFiles(String path, String prefix) {

        try {
            String[] suffixArray = {KEYSTORE_EXT, PKCS_EXT, CRT_EXT, PEM_EXT, CSR_EXT, CRL_EXT};
            for (String suffix : suffixArray) {
                File certificateFile = FileUtils.getFile(path, prefix + suffix);
                if (certificateFile.exists()) {
                    FileUtils.forceDelete(certificateFile);
                }
            }

        } catch (Exception e) {
            logger.error("Cannot remove certificate files : " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Create master certificate
     *
     * @param home
     */
    public boolean createMasterCertificates(String home, String password) {
        return consoleProcessExecutor.createMaster(home, password);
    }

    public String getStorePassword(String path) {
        File jetty = new File(path + JETTY_FILE);
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setValidating(false);
            docFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();


            //must! Since jetty dtd's IS NOT VALID DTD !!! (FOR VERSION 9.0.XX)
            docBuilder.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId, String systemId) {
                    return new InputSource(
                            new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes()));
                }
            });

            Document doc = docBuilder.parse(jetty);
            doc.getDocumentElement().normalize();

            Element node = null;
            NodeList properties = doc.getElementsByTagName("Call");

            for (int i = 0; i < properties.getLength(); i++) {

                node = (Element) properties.item(i);
                String value = node.getElementsByTagName("Arg").item(0).getFirstChild().getNodeValue();
                if (value != null && value.equalsIgnoreCase("javax.net.ssl.trustStorePassword")) {
                    //update "javax.net.ssl.trustStorePassword" argument
                    return node.getElementsByTagName("Arg").item(1).getFirstChild().getNodeValue();
                }
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("Cannot update jetty files : " + pce.getMessage());
            pce.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("Cannot update jetty files : " + ioe.getMessage());
            ioe.printStackTrace();
        } catch (SAXException sae) {
            System.out.println("Cannot update jetty files : " + sae.getMessage());
            sae.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Cannot update jetty files : " + ex.getMessage());
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Load certificate properties.
     * TBD: Try avoid using properties file !
     *
     * @param home
     */
    public Properties loadProperties(String home, String name) {

        if (name == null)
            return null;

        Properties loadedProperties = new Properties();
        InputStream stream = null;
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("trying to load properties from the file [" + name + "].");
            }
            //if path is not provided - try system property
            if (home == null) {
                home = System.getProperty(HP4M_HOME);
                if (home == null) {
                    //get from class loader
                    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                    stream = classloader.getResourceAsStream(name);
                    loadedProperties.load(stream);
                } else {
                    String confFile = home + File.separator + CONFIGURATION_FOLDER + File.separator + name;
                    logger.info("load properties from the file [" + confFile + "].");
                    stream = new FileInputStream(confFile);
                }
            } else {
                String confFile = home + File.separator + CONFIGURATION_FOLDER + File.separator + name;
                logger.info("load properties from the file [" + confFile + "].");
                stream = new FileInputStream(confFile);
            }

            if (stream != null) {
                loadedProperties.load(stream);
            } else {
                logger.error("Cannot load certificate properties file ... ");
            }

        } catch (FileNotFoundException e) {
            logger.error("Can not load properties from the file [" + CERTIFICATE_PROPERTIES + "].", e);
        } catch (IOException e) {
            logger.error("Can not load properties from the file [" + CERTIFICATE_PROPERTIES + "].", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
        return loadedProperties;
    }

    public String getUUID() {
        return UUID.randomUUID().toString();
    }

    public String signPEM(String connectorId, String home, String password, String key) {
        String ret = null;
        try {
            String name = ALIAS + "_" + connectorId;
            consoleProcessExecutor.createCSRFile(name, home, connectorId);
            password = getCipherUtil(home).decode(password, key);
            X509CertificateObject pemCertificate = consoleProcessExecutor.signCSRFile(name, home, password);
            ret = getPEMCert(pemCertificate);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return ret;
    }

    /**
     * Convert x.509 certificate to string
     *
     * @param cert
     * @return
     * @throws NoSuchAlgorithmException
     * @throws CertificateEncodingException
     */
    public String getPEMCert(X509Certificate cert)
            throws NoSuchAlgorithmException, CertificateEncodingException {

        byte[] der = cert.getEncoded();
        String pemCertPre = new String(Base64.encode(der));
        String ret = BEGIN_CERT + "\n" + pemCertPre + "\n" + END_CERT;
        logger.debug(" ---  return :  \n" + ret);
        return ret;

    }

    /**
     * Validate connector client certificate
     * @param pem
     * @return
     */
    public boolean verifyConnectorCert(String pem, String connectorId){

        String workDir = getHomePath() + File.separator + CertificateUtil.SECURITY + File.separator;

        if(workDir==null){
            workDir = CertificateUtil.WORKING_DIR_DEFAULT;
            workDir = workDir + File.separator;
        }

        File pbc = new File(workDir + MASTER_CRT_NAME);
        //needs, since JSON comes without special characters
        String key = pem.replaceAll(CertificateUtil.BEGIN_CERT, "").replaceAll(CertificateUtil.END_CERT, "");
        pem = CertificateUtil.BEGIN_CERT + "\n" + key.trim() + "\n" + CertificateUtil.END_CERT;

        try{
            PublicKey publicKey = getPublicCrt(pbc.getPath());
            Security.addProvider(new BouncyCastleProvider());
            PEMReader reader = new PEMReader(new StringReader(pem));
            Object pemObject = reader.readObject();
            if (pemObject instanceof X509Certificate) {
                X509Certificate cert = (X509Certificate)pemObject;
                cert.checkValidity(); // to check it's valid in time
                cert.verify(publicKey); // verify the sig. using the issuer's public key

                //check CN connector id value
                if(connectorId!=null && !connectorId.isEmpty()){
                    X509Principal principal = PrincipalUtil.getSubjectX509Principal(cert);
                    Vector<?> values = principal.getValues(X509Name.CN);
                    String cn = (String) values.get(0);
                    if(!cn.endsWith(connectorId)){
                        logger.error("Failed to verify connector cert. cn doesn't end with connectorId " + connectorId);
                        return false;
                    }
                }

            }
        }catch (Exception ex){
            logger.error("Failed to verify connector cert", ex);
            return false;
        }

        return true;
    }

    /**
     * Read PEM file from file
     *
     * @param keyPath
     * @return
     */
    public static X509CertificateObject readPEMFile(String keyPath, Logger logger) {
        logger.debug("--- start read PEM certificate {}", keyPath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(keyPath));
            Security.addProvider(new BouncyCastleProvider());
            //java.security.KeyPair
            Object pemObj = new PEMReader(br).readObject();
            if (pemObj instanceof X509CertificateObject) {
                X509CertificateObject crt = (X509CertificateObject) pemObj;
                br.close();
                return crt;
            }

        } catch (Exception ex) {
            logger.error("Unable to get PEM certificate from file " + keyPath, ex);
        }
        return null;
    }

    /**
     * Load public certificate from file
     * @param filename
     * @return
     * @throws Exception
     */
    public PublicKey getPublicCrt(String filename)
            throws Exception {

        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);

        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        X509Certificate cer = (X509Certificate) fact.generateCertificate(fis);
        PublicKey key = cer.getPublicKey();
        fis.close();
        return key;
    }

    public String signPEMWithSeedAndPass(String connectorID, String seed_id, String masterPass) {
        return signPEM(connectorID, getHomePath(), masterPass, seed_id);
    }

}
