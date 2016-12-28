package com.nginx.log.service;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
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
			initProperty();
			byte[] content = covertFileToStream();
			zookeeperPro = new ZookeeperPro();
			loadConfig(content);
			// hashMapConfig =
			// config.loadConfig(IOUtil.byteTOInputStream(content));
			zookeeperPro.setZookeeper_path(getConf(PropertiesType.ZOOKEEPER_PATH));
			zookeeperPro.setZookeeper_quorum(getConf(PropertiesType.ZOOKEEPER_QUORUM));
			zookeeperPro.setSession_time(Integer.parseInt(getConf(PropertiesType.CFG_ZOOKEEPER_MS)));

			initClient(zookeeperPro);
			deleteData(zookeeperPro.getZookeeper_path());
			createNode(zookeeperPro.getZookeeper_path(), content);
			// zookeeper.initZooKeeper(zookeeperPro);
			// zookeeper.deleteNode(zookeeperPro.getZookeeper_path());
			// zookeeper.createNode(zookeeperPro, content);

		} catch (Exception e) {
			// TODO Auto-generated catch block

			System.out.println(e.getMessage());
		} finally {
			close();
		}
	}

	void initProperty() {
		// hashMapConfig.put(PropertiesType.TASK_BATCH_TIME, "300");
	}

	void loadConfig(byte[] content) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		try {
			hashMap = config.loadConfig(IOUtil.byteTOInputStream(content));
			for (String key : hashMap.keySet()) {
				hashMapConfig.put(key, hashMap.get(key));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				throw new RuntimeException(String.format("fail to init zookeeperPro: %s ", e.getMessage()));
				// logger.error(e.getMessage());
			}

		}
		return zookeeperPro;
	}

	public String getConf(String key) {
		try {
			if (hashMapConfig == null || hashMapConfig.size() <= 0) {
				logger.info("loading data from zk" + this.toString());
				zookeeperPro = getZookeeperCfg();
				byte[] result = getData(zookeeperPro.getZookeeper_path());
				InputStream inputStream = IOUtil.byteTOInputStream(result);
				hashMapConfig = config.loadConfig(inputStream);
				if (inputStream != null)
					inputStream.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("无法加载conf ,原因 :" + e.getMessage());
			// logger.error(e.getMessage());
		}
		if (hashMapConfig.containsKey(key)) {
			return hashMapConfig.get(key);
		} else {
			throw new RuntimeException(String.format("未找到相关配置项: %s , Cfg_size: %d  ,zookeeperPro quorum %s", key,
					hashMapConfig.size(), zookeeperPro.getZookeeper_quorum()));
		}

	}

	private void setData(String path, Object val) throws Exception {
		this.getClient().setData().forPath(path, val.toString().getBytes());
	}

	private void deleteData(String path) throws Exception {
		if (this.getClient().checkExists().forPath(path) != null)
			this.getClient().delete().deletingChildrenIfNeeded().forPath(path);
	}

	private void createNode(String path, byte[] val) throws Exception {
		this.getClient().create().forPath(path, val);
	}

	private byte[] getData(String path) throws Exception {
		return this.getClient().getData().forPath(path);
	}

	private CuratorFramework client;

	private CuratorFramework getClient() {
		if (client == null) {
			initClient(zookeeperPro);
		}
		return client;
	}

	private void close() {
		if (client != null) {
			client.close();
		}
	}

	private void setClient(CuratorFramework client) {
		this.client = client;
	}

	private void initClient(ZookeeperPro zookeeperPro) {
		CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperPro.getZookeeper_quorum(),
				new RetryNTimes(10, 5000));
		client.start();
		setClient(client);
	}

}
