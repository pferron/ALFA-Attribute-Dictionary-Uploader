package com.axiomatics.asm.admin.client;

import java.io.IOException;

import com.axiomatics.asm.client.ClientInfo;

public class AttributeDictionnary {
	
	 public void createAttributeInDictionary(String id, String cat, String type, String name, String nameSpace,
			 				String currentProject, InitClient aClient, ClientInfo clientInfo) throws AsmAccessDenied_Exception, AsmWebServiceFault_Exception, IOException {
         
         TxAttribute createAttribute = aClient.getService(clientInfo).createAttribute(id, name, nameSpace, type, 
         														cat, currentProject);
         System.out.println("Attribute Created successfully");
         System.out.println("Attribute id: " + createAttribute.getXacmlId());
         System.out.println("Attribute name: " + createAttribute.getName());
         System.out.println("Attribute namespace: " + createAttribute.getNamespace());
         System.out.println("Attribute category: " + createAttribute.getCategory());
         System.out.println("Attribute data type: " + createAttribute.getXacmlDataType());
     }
}
