package com.nginx.log.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nginx.log.bean.ZookeeperPro;

public class zookeeperUtil implements Watcher, Serializable {

	private final Logger logger = LoggerFactory.getLogger(zookeeperUtil.class);// Logger.getLogger(zookeeperUtil.class);

	// private static String zkQu =
	// "master2:2181,node1:2181,node2:2181,node3:2181,node4:2181";

	public ZooKeeper initZooKeeper(ZookeeperPro zookeeper) {
		if (!isConnected()) {
			try {
				connect(zookeeper);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				logger.error(e.getMessage());
			}
		}
		return zooKeeper;
	}

	// 缓存时间
	// private static final int SESSION_TIME = 20000;
	private ZooKeeper zooKeeper;
	// protected CountDownLatch countDownLatch = new CountDownLatch(1);
	private boolean flg = true;

	// 连接zk集群
	private void connect(ZookeeperPro zookeeperPro) throws IOException, InterruptedException {
		// zkQu = hosts;
		zooKeeper = new ZooKeeper(zookeeperPro.getZookeeper_quorum(), zookeeperPro.getSession_time(), this);
		System.out.println("Connect to zookeeper..");
		while (flg) {
			Thread.sleep(500);
			System.out.print(".");
		}
		// System.out.println(flg);
		// countDownLatch.await();
	}

	// // 连接zk集群
	// public void connect(String hosts) throws IOException,
	// InterruptedException {
	// // zkQu = hosts;
	// zooKeeper = new ZooKeeper(hosts, SESSION_TIME, this);
	//
	// System.out.println("Connect to zookeeper..");
	// while (flg) {
	// Thread.sleep(500);
	// System.out.print(".");
	// }
	// // System.out.println(flg);
	// // countDownLatch.await();
	// }

	// zk处理
	@Override
	public void process(WatchedEvent event) {
		logger.info(event.getState().toString());
		if (event.getState() == KeeperState.SyncConnected) {
			flg = false;
			// countDownLatch.countDown();
		}
	}

	// 关闭集群
	public void close() throws InterruptedException {
		zooKeeper.close();
	}

	/**
	 * zookeeper是否连接服务器
	 * 
	 * @return
	 */
	public boolean isConnected() {
		if (zooKeeper == null)
			return false;
		return zooKeeper.getState().isConnected();
	}

	/**
	 * 是否存在path路径节点
	 * 
	 * @param path
	 * @return
	 */
	public boolean exists(String path) {
		try {
			return zooKeeper.exists(path, false) != null;
		} catch (Exception e) {
			// logger.error(e.getMessage());
			System.out.println(e.getMessage());
		}
		return false;
	}

	public void createNode(ZookeeperPro zookeeperPro, byte[] content) {
		if (isConnected()) {
			try {
				zooKeeper.create(zookeeperPro.getZookeeper_path(), content, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			} catch (Exception e) {
				logger.error(e.getMessage());

			}
		}
		System.out.println(String.format("zookeeper state = [{%s}]", zooKeeper.getState()));
	}

	public boolean deleteNode(String path) {
		if (isConnected()) {
			try {
				zooKeeper.delete(path, -1);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		System.out.println(String.format("zookeeper state = [{%s}]", zooKeeper.getState()));
		return false;
	}

}
