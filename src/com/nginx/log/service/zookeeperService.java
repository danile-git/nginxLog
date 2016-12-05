package com.nginx.log.service;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.slf4j.LoggerFactory;

import backtype.storm.command.config_value__init;

import com.nginx.log.bean.PropertiesType;
import com.nginx.log.util.Config;
import com.nginx.log.util.IOUtil;
import com.nginx.log.util.zookeeperUtil;

public class zookeeperService implements Serializable {
	zookeeperUtil zookeeper = new zookeeperUtil();
	public static String zookeeperPath = "/nginxlog/config";
	Config config = new Config();
	HashMap<String, String> hashMapConfig = new HashMap<String, String>();
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(zookeeperUtil.class);

	public void init() {
		try {
			InputStream inputStream = IOUtil.fileInputStream("/core.xml");
			byte[] confbyte = IOUtil.readStream(inputStream);
			hashMapConfig = config.loadConfig(IOUtil.byteTOInputStream(confbyte));

			zookeeper.connect(getConf(PropertiesType.ZOOKEEPER_QUORUM));
			//zookeeperPath = getConf(PropertiesType.ZOOKEEPER_PATH);
			// System.out.println("-------deleteNode---");
			zookeeper.deleteNode(zookeeperPath);
			// System.out.println("----deleteNode done---");
			// System.out.println("------create----");
			zookeeper.getZooKeeper().create(zookeeperPath, confbyte, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			// System.out.println("------create done-----");
			_toString();
			inputStream.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void _toString() {
		System.out.println("----------conifg---------");
		for (String key : hashMapConfig.keySet()) {
			System.out.println("key : " + key + " value : " + hashMapConfig.get(key));
		}
	}

	public String getConf(String key) {
		String msString = "";
		try {
			if (hashMapConfig.size() <= 0) {
				byte[] result = zookeeper.getZooKeeper().getData(zookeeperPath, false, null);
				InputStream inputStream = IOUtil.byteTOInputStream(result);
				hashMapConfig = config.loadConfig(inputStream);
				msString += result.toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			msString += "ex" + e.getMessage();
			logger.error(e.getMessage());
		}
		if (hashMapConfig.containsKey(key)) {
			return hashMapConfig.get(key);
		} else {
			throw new RuntimeException(String.format("未找到相关配置项: %s size" + hashMapConfig.size() + "  --->" + msString,
					key));
		}

	}
}
