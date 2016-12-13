package com.nginx.log.core;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nginx.log.bean.Type;
import com.nginx.log.util.Config;
import com.nginx.log.util.IOUtil;

public class test<T>{

	private final static Logger logger = LoggerFactory.getLogger(test.class);
	public static void main(String[] arg) {
		// getConf(test.class);

	}
	Config config = new Config();
	public void initTest() {
		try {
			InputStream inputStream = IOUtil.fileInputStream("/core.xml");
			byte[] confbyte = IOUtil.readStream(inputStream);
			HashMap<String, String> sssHashMap = config.loadConfig(IOUtil.byteTOInputStream(confbyte));
			for (String key : sssHashMap.keySet()) {
				logger.info("---------key : " + key + " value : " + sssHashMap.get(key));
			}
			logger.info("--------------" + confbyte.toString());
			inputStream.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("----------");

	}


	public Class<?> getConf(Class<?> type) {
		Type converType = Type.None;
		try {
			converType = Type.valueOf(type.getSimpleName());
		} catch (Exception e) {
			converType = Type.None;
		}

		switch (converType) {
		case Integer:
			System.out.println(converType);
			break;
		case String:
			System.out.println(converType);
			break;
		default:
			System.out.println("other");
			break;
		}
		return type;
	}
}
