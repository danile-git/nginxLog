package com.nginx.log.bean;
import java.util.HashMap;
public class ConfigPro {
		static HashMap<String, String> config = new HashMap<String, String>();
		public static String getConf(String key) {
			if (config.containsKey(key)) {
				return config.get(key);
			} else {
				throw new RuntimeException(String.format("未找到相关属性: %s", key));
			}
		}
		public static void _toString() {
			System.out.println("----------conifg---------");
			for (String key : config.keySet()) {
				System.out.println("key : "+key+" value : "+config.get(key));
			} 
		}
		public static void setConfig(HashMap<String, String> config) {
			ConfigPro.config = config;
		}
}
