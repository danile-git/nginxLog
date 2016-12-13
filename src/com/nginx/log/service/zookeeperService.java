package com.nginx.log.service;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

import org.slf4j.LoggerFactory;

import com.nginx.log.bean.PropertiesType;
import com.nginx.log.bean.ZookeeperPro;
import com.nginx.log.util.Config;
import com.nginx.log.util.IOUtil;
import com.nginx.log.util.zookeeperUtil;

public class zookeeperService implements Serializable {
	zookeeperUtil zookeeper = new zookeeperUtil();

	Config config = new Config();
	static HashMap<String, String> hashMapConfig = new HashMap<String, String>();
	static ZookeeperPro zookeeperPro = null;
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(zookeeperUtil.class);

	public void init() {
		try {
			byte[] content = covertFileToStream();
			zookeeperPro = new ZookeeperPro();
			hashMapConfig = config.loadConfig(IOUtil.byteTOInputStream(content));
			zookeeperPro.setZookeeper_path(getConf(PropertiesType.ZOOKEEPER_PATH));
			zookeeperPro.setZookeeper_quorum(getConf(PropertiesType.ZOOKEEPER_QUORUM));
			zookeeperPro.setSession_time(Integer.parseInt(getConf(PropertiesType.CFG_ZOOKEEPER_MS)));

			zookeeper.initZooKeeper(zookeeperPro);
			zookeeper.deleteNode(zookeeperPro.getZookeeper_path());
			zookeeper.createNode(zookeeperPro, content);

		} catch (Exception e) {
			// TODO Auto-generated catch block
		System.out.println(e.getMessage());
		}
	}

	private byte[] covertFileToStream() throws Exception {
		InputStream inputStream = IOUtil.fileInputStream("/core.xml");
		byte[] confbyte = IOUtil.readStream(inputStream);
		inputStream.close();
		return confbyte;
	}

	public void _toString() {
		System.out.println("----------conifg---------");
		for (String key : hashMapConfig.keySet()) {
			System.out.println("key : " + key + " value : " + hashMapConfig.get(key));
		}
	}

	private ZookeeperPro getZookeeperCfg() {
		if (zookeeperPro != null) {
			return zookeeperPro;
		} else {
			try {
				byte[] content = covertFileToStream();
				zookeeperPro = new ZookeeperPro();
				HashMap<String, String> hashCfg = config.loadConfig(IOUtil.byteTOInputStream(content));
				zookeeperPro.setZookeeper_path(hashCfg.get(PropertiesType.ZOOKEEPER_PATH));
				zookeeperPro.setZookeeper_quorum(hashCfg.get(PropertiesType.ZOOKEEPER_QUORUM));
				zookeeperPro.setSession_time(Integer.parseInt(hashCfg.get(PropertiesType.CFG_ZOOKEEPER_MS)));
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

		}
		return zookeeperPro;
	}

	public String getConf(String key) {
		try {
			if (hashMapConfig.size() <= 0) {
				logger.info("loading data from zk" + this.toString());			
				byte[] result = zookeeper.initZooKeeper(getZookeeperCfg()).getData(zookeeperPro.getZookeeper_path(), false, null);
				InputStream inputStream = IOUtil.byteTOInputStream(result);
				hashMapConfig = config.loadConfig(inputStream);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		if (hashMapConfig.containsKey(key)) {
			return hashMapConfig.get(key);
		} else {
			throw new RuntimeException(String.format("未找到相关配置项: %s size", key));
		}

	}


}
