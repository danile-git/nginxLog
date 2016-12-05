package com.nginx.log.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Config implements Serializable {


	HashMap<String, String> config = new HashMap<String, String>();
	
	public  HashMap<String, String> loadConfig(InputStream inputStream) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(inputStream);
		NodeList nodeList = doc.getElementsByTagName("property");
		for (int i = 0; i < nodeList.getLength(); i++) {
			String keyString = doc.getElementsByTagName("name").item(i).getFirstChild().getNodeValue();
			String valueString = doc.getElementsByTagName("value").item(i).getFirstChild().getNodeValue();
			config.put(keyString, valueString);
		}
		return config;
	}
	
//	public  String getConf(String key) {
//		if (config.containsKey(key)) {
//			return config.get(key);
//		} else {
//			throw new RuntimeException(String.format("δ�ҵ����������: %s", key));
//		}
//	}
//
//	public  void _toString() {
//		System.out.println("----------conifg---------");
//		for (String key : config.keySet()) {
//			System.out.println("key : "+key+" value : "+config.get(key));
//		} 
//	}
	
}
