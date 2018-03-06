package com.iw2fag.lab.security.encryption;


import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: davidofv
 * Date: 30/06/15
 * Time: 16:35
 * To change this template use File | Settings | File Templates.
 */

public class ConsoleProcessExecutor {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleProcessExecutor.class);
    public static String OS_MODE = System.getProperty("os.name");

    /**
     * Start process, redirect its streams to pointed streams and return only after finishing of this process
     *
     * @param args        process arguments including executable file
     * @param runtime just Runtime object for process
     * @param out         stream for redirecting System.out of process
     * @param err         stream for redirecting System.err of process
     * @throws IOException
     * @throws InterruptedException
     */
    public void execute(String[] args, Runtime runtime, OutputStream out, OutputStream err)
            throws IOException, InterruptedException
    {

        logger.debug("start execute process .... ");
        Process process = runtime.exec(args);

        Thread outThread = new Thread( new StreamReader( process.getInputStream(), out ) );
        outThread.start();
        Thread errThread = new Thread( new StreamReader( process.getErrorStream(), err ) );
        errThread.start();
        int rc = process.waitFor();
        outThread.join(1500);
        errThread.join(1500);
        if( rc != 0 ) {
            throw new RuntimeException( "Process execution failed. Return code: " + rc );
        }
    }

    public void createCSRFile(String name, String home, String connectorId){

        logger.debug(" ---  start createCSRFile ---- ");

        String certCSRFileName = name + CertificateUtil.CSR_EXT;
        String certKeyFileName=name + ".key";

        String workDir = home + File.separator + CertificateUtil.SECURITY + File.separator;
        String console_cmd = "";
        String openSSL = workDir + "openssl.cnf";

        if(OS_MODE.startsWith("Windows")){
            // workDir = opensslPath;
            console_cmd = "cmd.exe";
        }else{
            // workDir = opensslPath;
            console_cmd = "sudo";
        }

        String errFile = CertificateUtil.ERR_FILE_DEFAULT;

        String outFile = CertificateUtil.OUT_FILE_DEFAULT;

        String dname = CertificateUtil.DNAME_DEFAULT;
        dname = dname + "_"+connectorId;

        try{

            OutputStream out = new FileOutputStream(workDir + outFile);
            OutputStream err = new FileOutputStream(workDir + errFile);


            if(OS_MODE.startsWith("Windows")){

                //create private key
                String[] command = {console_cmd, "/c", "openssl", "genrsa", "-out", workDir + certKeyFileName, "2048"};
                execute(command,Runtime.getRuntime(), out,err);

                //create CSR request pem
                String[] command2 = {console_cmd, "/c", "openssl", "req", "-new", "-sha256", "-nodes", "-key" , workDir + certKeyFileName, "-out", workDir + certCSRFileName, "-subj", dname, "-config", openSSL};
                execute(command2,Runtime.getRuntime(), out,err);

                //save private key as PEM format.
                String[] command3 = {console_cmd, "/c", "openssl", "rsa", "-in", workDir + certKeyFileName, "-out", workDir + certKeyFileName+ CertificateUtil.PEM_EXT, "-outform", "PEM"};
                execute(command3,Runtime.getRuntime(), out,err);

            } else {

                //create private key
                String[] command = {"openssl", "genrsa", "-out", workDir + certKeyFileName, "2048"};
                execute(command,Runtime.getRuntime(), out,err);

                //create CSR request pem
                String[] command2 = {"openssl", "req", "-new", "-sha256", "-nodes", "-key" , workDir + certKeyFileName, "-out", workDir + certCSRFileName, "-subj", dname, "-config", openSSL};
                execute(command2,Runtime.getRuntime(), out,err);

                //save private key as PEM format.
                String[] command3 = {"openssl", "rsa", "-in", workDir + certKeyFileName, "-out", workDir + certKeyFileName+".pem", "-outform", "PEM"};
                execute(command3,Runtime.getRuntime(), out,err);

            }

        }catch (Exception exc){

            logger.error("Cannot generate CSR request. Please see openSSL logs for more details.");
        }
    }


    /**
     * Generate master key and certificate.
     * used in installation process.
     */
    public boolean createMaster(String home, String password){
        logger.debug(" ---  start createMaster ---- ");


        String console_cmd = "";
        String workDir =  home + File.separator + CertificateUtil.SECURITY + File.separator;
        String openSSL = workDir + "openssl.cnf";
        String masterPassword = null;


        if(OS_MODE.startsWith("Windows")){
            console_cmd = "cmd.exe";
        }else{
            console_cmd = "sudo";
        }

        String errFile = CertificateUtil.ERR_FILE_DEFAULT;

        String outFile = CertificateUtil.OUT_FILE_DEFAULT;

        String dname = CertificateUtil.DNAME_DEFAULT;

        if(password != null && !password.isEmpty()){
            masterPassword = password;
        } else {
            logger.error("Cannot generate master certificate - no password provided ... ");
            return false;
        }


        try{

            OutputStream out = new FileOutputStream(workDir + outFile);
            OutputStream err = new FileOutputStream(workDir + errFile);

            //create Master certificates
            if(OS_MODE.startsWith("Windows")){
                String[] command = {console_cmd, "/c", "openssl", "req", "-new", "-x509", "-days" , CertificateUtil.VALIDITY, "-keyout" , workDir + CertificateUtil.MASTER_KEY_NAME , "-out", workDir + CertificateUtil.MASTER_CRT_NAME, "-subj", dname, "-config", openSSL, "-passin", "pass:"+masterPassword, "-passout", "pass:"+masterPassword};
                execute(command,Runtime.getRuntime(), out,err);

            }else{
                //linux
                //openssl req -config "C:\Dev\openssl\openssl.cnf" -new -x509 -keyout cakey.pem -out cacert.pem -days 1095
                String[] command = {"openssl", "req", "-new", "-x509", "-days" , CertificateUtil.VALIDITY, "-keyout" , workDir + CertificateUtil.MASTER_KEY_NAME, "-out", workDir + CertificateUtil.MASTER_CRT_NAME , "-subj", dname, "-config", openSSL, "-passin", "pass:"+masterPassword, "-passout", "pass:"+masterPassword};
                execute(command,Runtime.getRuntime(), out,err);
            }

        }catch (Exception exc){
            logger.error("Cannot generate master certificate. Please see openSSL logs for more details.");
            return false;
        } finally {
            masterPassword = null;
        }

        return true;
    }


    /**
     * Sign client certificate
     * @return
     */
    public X509CertificateObject signCSRFile(String name, String home, String password){

        logger.debug(" ---  start signCSRFile ---- ");

        X509CertificateObject pemCertificate = null;

        String certCSRFileName=name + CertificateUtil.CSR_EXT;
        String certPEMFileName=name + CertificateUtil.PEM_EXT;
        String certKeyFileName=name + ".key";

        String workDir = home + File.separator + CertificateUtil.SECURITY + File.separator;
        String console_cmd = "";
        String ca = workDir + CertificateUtil.MASTER_CRT_NAME;
        String ca_key = workDir + CertificateUtil.MASTER_KEY_NAME;
        String openSSL = workDir + "openssl.cnf";

        if(OS_MODE.startsWith("Windows")){
            // workDir = opensslPath;
            console_cmd = "cmd.exe";
        }else{
            //workDir = opensslPath;
            console_cmd = "sudo";
        }

        String masterPassword = null;
        if(password != null && !password.isEmpty()){
            masterPassword = password;
        }else{
            logger.error("Cannot sign CSR request - no password provided");
            return null;
        }


        String validity = CertificateUtil.VALIDITY_DEFAULT;

        String errFile = CertificateUtil.ERR_FILE_DEFAULT;

        String outFile = CertificateUtil.OUT_FILE_DEFAULT;

        try{
            OutputStream out = new FileOutputStream(workDir + outFile);
            OutputStream err = new FileOutputStream(workDir + errFile);

            //sign CSR request
            if(OS_MODE.startsWith("Windows")){
                //windows
                String[] command = {console_cmd, "/c", "openssl", "x509", "-req", "-in", workDir + certCSRFileName, "-days", validity, "-CA", ca, "-CAkey", ca_key, "-passin", "pass:"+masterPassword, "-CAcreateserial", "-out", workDir + certPEMFileName};
                execute(command,Runtime.getRuntime(), out,err);

            } else{
                String[] command = {"openssl", "x509", "-req", "-in", workDir + certCSRFileName, "-days", validity, "-CA", ca, "-CAkey", ca_key, "-passin", "pass:"+masterPassword, "-CAcreateserial", "-out", workDir + certPEMFileName};
                execute(command,Runtime.getRuntime(), out,err);
            }

            pemCertificate = CertificateUtil.readPEMFile(workDir + certPEMFileName, logger);

            //clear created files
            FileUtils.forceDelete(new File(workDir + certCSRFileName));
            FileUtils.forceDelete(new File(workDir + certPEMFileName));
            FileUtils.forceDelete(new File(workDir + certKeyFileName));
            FileUtils.forceDelete(new File(workDir + certKeyFileName + CertificateUtil.PEM_EXT));

            out.close();
            err.close();

        } catch (Exception exc){
            logger.error("Cannot sign CSR request. Please see openSSL logs for more details.");
        } finally {
            masterPassword = null;
        }

        return pemCertificate;
    }

    private static ConsoleProcessExecutor instance;
    public static ConsoleProcessExecutor getInstance(){
        if (instance == null){
            instance = new ConsoleProcessExecutor();
        }
        return instance;
    }
}

