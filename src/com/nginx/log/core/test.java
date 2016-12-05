package com.nginx.log.core;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.nginx.log.bean.Booleans;
import com.nginx.log.util.Config;

public class test {
public static  void main (String [] arg) throws Exception {
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder();
	Document doc = db.parse(Config.class.getClass().getResourceAsStream("/topology.xml"));
	NodeList nodeList = doc.getElementsByTagName("topology");
	for (int i = 0; i < nodeList.getLength(); i++) {
	
		//System.out.println(nodeList.item(i).getNodeName());
 NodeList nodeList2= nodeList.item(i).getChildNodes();
 for (int j = 0; j < nodeList2.getLength(); j++) {
	System.out.println(nodeList2.item(j));
}
 //		String keyString = doc.getElementsByTagName("name").item(i).getFirstChild().getNodeValue();
//		String valueString = doc.getElementsByTagName("value").item(i).getFirstChild().getNodeValue();
		
	}
}
}
