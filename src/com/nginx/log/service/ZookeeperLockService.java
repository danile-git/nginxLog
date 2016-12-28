package com.nginx.log.service;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;
import org.apache.log4j.Logger;

import com.nginx.log.Interface.IZKLock;
import com.nginx.log.bean.PropertiesType;
import com.nginx.log.util.ZookeeperLock;

public class ZookeeperLockService {
	private static final Logger logger = Logger.getLogger(ZookeeperLockService.class);
	ZookeeperLock zkLock = new ZookeeperLock();
	zookeeperService zkService = new zookeeperService();
	private String ZK_ADDRESS = null;
	private String ZK_DATA_PATH = null;

	public void initData(String value) {
		CuratorFramework client = getClient();
		logger.info("Connected to zookeeper");
		try {
			if (client.checkExists().forPath(getZK_DATA_PATH()) != null) {
				client.delete().forPath(getZK_DATA_PATH());
			}
			logger.info(String.format("create node init value", getZK_DATA_PATH()));
			createNode(getZK_DATA_PATH(), value);
		} catch (Exception e) {
			logger.info(e.getMessage());
			if (client != null)
				client.close();
			System.exit(1);
		}
	}

	public void setData(String path, Object val) throws Exception {
		zkLock.setData(this.getClient(), path, val);
	}

	public byte[] getData(String path) throws Exception {
		return zkLock.getData(this.getClient(), path);
	}

	public void createNode(String path, Object val) throws Exception {
		zkLock.createNode(this.getClient(), path, val);
	}
	public void createNode(String path, String val) throws Exception {
		zkLock.createNode(this.getClient(), path, val);
	}

	public void deleteNode(String path) throws Exception {
		zkLock.deleteData(this.getClient(), path);
	}

	/**
	 * arg 展示信息 可以未 null
	 * */
	public void dolock(Object arg, String path, IZKLock callback) {
		zkLock.dolock(this.getClient(), path, arg, callback);
	}

	private void initClient() {
		ZK_ADDRESS = zkService.getConf(PropertiesType.ZOOKEEPER_QUORUM);
		CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, new RetryNTimes(10, 5000));
		client.start();
		setClient(client);
	}

	public Object dolock(Object indx) {
		InterProcessMutex lock = new InterProcessMutex(this.getClient(), this.getZK_DATA_PATH());
		try {
			Object obj = getLock(indx, lock);
			if (obj != null)
				return obj;
		} catch (Exception e) {
			try {
				if (lock != null)
					lock.release();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	Object getLock(Object indx, InterProcessMutex lock) throws NumberFormatException, Exception {
		Object obj = null;
		if (lock.acquire(3000, TimeUnit.MILLISECONDS)) {
			int val = Integer.parseInt(new String(this.getData(this.getZK_DATA_PATH())));
			obj = val;
			logger.info(indx + " get it! value: " + obj);
			this.setData(this.getZK_DATA_PATH(),1 + val);
			zkLock.releaseLock(lock);
		} else {
			System.out.println(indx + "does not get it!,try to get it again!");
			getLock(indx, lock);
		}
		return obj;
	}

	public void close() {
		if (client != null) {
			client.close();
		}
	}

	public String getZK_DATA_PATH() {
		if (ZK_DATA_PATH == null)
			ZK_DATA_PATH = String.format("%s/mysqldata", zkService.getConf(PropertiesType.ZOOKEEPER_PATH));
		return ZK_DATA_PATH;
	}

	private CuratorFramework client;

	public CuratorFramework getClient() {
		if (client == null) {
			initClient();
		}
		return client;
	}

	public void setClient(CuratorFramework client) {
		this.client = client;
	}

}
