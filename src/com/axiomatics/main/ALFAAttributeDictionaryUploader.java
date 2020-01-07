package com.axiomatics.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.axiomatics.alfa.ALFAParser;
import com.axiomatics.asm.admin.client.AttributeDictionnary;
import com.axiomatics.asm.admin.client.InitClient;
import com.axiomatics.asm.client.ClientInfo;
import com.axiomatics.data.ALFAAttribute;


public class ALFAAttributeDictionaryUploader {
	
    // property file containing the ASM connection details
	private static final String PROPERTIES_FILE = "./asm_connection.properties";
    
    static String asmUrl 						= null;
    static String user 							= null;
    static String password 						= null;
    static String trustStore 					= null;
    static String trustStoreType 				= null;
    static String trustStorePassword 			= null;
    static String wsdlUrl 						= null;
    static String currentProject				= null;
    static String alfaFilename					= null;
	
	static Logger logger = Logger.getLogger(ALFAAttributeDictionaryUploader.class.getName());

	public static void main(String[] args) {
		
		ClientInfo clientInfo	 					= null;
	    String strFileContent 						= null;
		List<ALFAAttribute> attributeList 				= new ArrayList<>();
		ALFAParser parser 							= new ALFAParser();
		AttributeDictionnary attibuteDictionnary 	= new AttributeDictionnary();
		
		try {
			InitClient aClient 				= null;
			
			if (args != null && args.length == 9) 
			{			
				logger.info("Loading ASM connection parameters from command-line arguments");
				asmUrl 							= args[0];
			    user 							= args[1];
			    password 						= args[2];
			    trustStoreType 					= args[3];
			    trustStore 						= args[4];
			    trustStorePassword 				= args[5];
			    wsdlUrl 						= args[6];
			    alfaFilename 	= args[7];
			    currentProject 					= args[8];	    
			} 
			else 
			{
				logger.info("Loading ASM connection parameters from ASM Properties file");
	        	initProperties();
			}
			
			/*Reading a text file as String */
	    	strFileContent = new String(Files.readAllBytes(Paths.get(alfaFilename)));	    	

			parser.ALFAAttributeParser(strFileContent, attributeList);
			logger.info("total of Attribute being parsed: " + attributeList.size() );
			
			
			clientInfo = new ClientInfo(asmUrl);
		    clientInfo.setUser(user);
		    clientInfo.setWsdlUrl(wsdlUrl);
		    clientInfo.setPassword(password);
		    clientInfo.setTrustStoreType(trustStoreType);
		    clientInfo.setTrustStore(trustStore);
		    clientInfo.setTrustStorePassword(trustStorePassword);
	        aClient = new InitClient(clientInfo);
	            
	        logger.info("====== Testing Connection ======");
		    String testConnection = aClient.getService(clientInfo).testConnection();
		    logger.info("--- TEST CONNECTION ---\n" + testConnection);	        
	        
      
            for(int i=0;i<attributeList.size();i++)
    		{
            	String id = attributeList.get(i).getXacmlId();
            	String cat = attributeList.get(i).getXacmlCategory();
            	String type = attributeList.get(i).getXacmlDataType();
            	String name = attributeList.get(i).getXacmlAttributeName();
            	String nameSpace = attributeList.get(i).getXacmlNameSpace();
            	
            	logger.info("====== Creating an Attribute in Dictionary ======");
	            attibuteDictionnary.createAttributeInDictionary(id, cat, type, name, nameSpace, currentProject, aClient, clientInfo);
    		}
            
            aClient.getService(clientInfo).logout();
        
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
        	logger.info("Properties file cannot be found");
            printUsage();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
		

	    logger.info("Uploading ALFA Attribute Dictionary completed !!!");
	    
	}
		
	/**
     * Method that initializes the connection parameters
     */
    private static void initProperties() {
        try {
            Properties asmConnectionProps = new Properties();
            asmConnectionProps.load(new FileInputStream(PROPERTIES_FILE));

            asmUrl 							= asmConnectionProps.getProperty("asmUrl");
            user 							= asmConnectionProps.getProperty("user");
            password 						= asmConnectionProps.getProperty("password");
            trustStoreType 					= asmConnectionProps.getProperty("trustStoreType");
            trustStore 						= asmConnectionProps.getProperty("trustStore");
            trustStorePassword 				= asmConnectionProps.getProperty("trustStorePassword");
            wsdlUrl 						= asmConnectionProps.getProperty("wsdlUrl");
            currentProject 					= asmConnectionProps.getProperty("projectName");
            alfaFilename					= asmConnectionProps.getProperty("alfaFilename");
            logger.info("Initialized all properties from " + PROPERTIES_FILE);
        } catch (FileNotFoundException e) {
        	logger.info("Properties file cannot be found");
            printUsage();
            System.exit(-1);
        } catch (IOException e) {
            printUsage();
            System.exit(-1);
        }

        if (wsdlUrl == null) {
            printUsage();
            System.exit(-1);
        }
    }
    
    private static void printUsage() {
    	logger.info("Usage: asm_connection.properties should be provided in classpath (Or)"
                + ALFAAttributeDictionaryUploader.class.getSimpleName()
                + " <asmUrl> <user> <password> <trustStoreType> <trustStore> <trustStorePassword> <wsldUrl> <attributeDictionnaryFilename> <projectName>");

    }

}

