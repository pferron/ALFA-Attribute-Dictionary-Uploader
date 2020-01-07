package com.axiomatics.asm.admin.client;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.axiomatics.asm.client.AsmAdminClient;
import com.axiomatics.asm.client.ClientInfo;

public class InitClient {
	
	String asmUrl = null;
	String user = null;
	String password = null;
	String trustStore = null;
	String trustStoreType = null;
	String trustStorePassword = null;
	String wsdlUrl = null;
	
	AsmAdminService service = null;    
	
	static Logger logger = Logger.getLogger(InitClient.class.getName());
    
   public InitClient(ClientInfo clientInfo) throws AsmAccessDenied_Exception, AsmWebServiceFault_Exception, IOException {

	   logger.info("====== Getting web service client ======");
        service = getService(clientInfo);
    }
    
    public AsmAdminService getService(ClientInfo clientInfo) throws AsmAccessDenied_Exception, AsmWebServiceFault_Exception, IOException {

    	asmUrl = clientInfo.getAsmUrl();
    	user = clientInfo.getUser();
        password = clientInfo.getPassword();
        trustStore = clientInfo.getTrustStore();
        trustStoreType = clientInfo.getTrustStoreType();
        trustStorePassword = clientInfo.getTrustStorePassword();
        wsdlUrl = clientInfo.getWsdlUrl();
    	
        AsmAdminService srv = null;
        // Get the JAX-WS connection to ASM. The following is the actual entrypoint to the client
        // library provided.
        AsmAdminClient asmAdminClient = new AsmAdminClient(clientInfo);
        try {
            srv = asmAdminClient.getConnection();
        } catch (Exception e) {
        	logger.info("Connection could not be established. Check the connection parameters and make sure that the ASM is up");
            throw new RuntimeException(e);
        }
        return srv;
    }
}
