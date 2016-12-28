package com.nginx.log.util;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;

import com.nginx.log.Interface.IZKLock;

public class ZookeeperLock {
	
	public void dolock(CuratorFramework client, String path, Object indx, IZKLock callback) {
		InterProcessMutex lock = new InterProcessMutex(client, path);
		try {
			getLock(client, indx, lock, callback);
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
	}

	void getLock(CuratorFramework client, Object indx, InterProcessMutex lock, IZKLock callback)
			throws NumberFormatException, Exception {
		if (lock.acquire(3000, TimeUnit.MILLISECONDS)) {
			callback.callback();
			releaseLock(lock);
		} else {
			System.out.println(indx + "does not get it!,try to get it again!");
			getLock(client, indx, lock, callback);
		}
	}

	public void releaseLock(InterProcessMutex lock) throws Exception {
		if (lock != null)
			lock.release();
	}

	public void setData(CuratorFramework client, String path, Object val) throws Exception {
		client.setData().forPath(path, val.toString().getBytes());
	}

	public void deleteData(CuratorFramework client, String path) throws Exception {
		if (client.checkExists().forPath(path) != null)
			client.delete().deletingChildrenIfNeeded().forPath(path);
	}

	public void createNode(CuratorFramework client, String path, Object val) throws Exception {
		client.create().forPath(path, val.toString().getBytes());
	}
	public void createNode(CuratorFramework client, String path, String val) throws Exception {
		client.create().forPath(path, val.getBytes());
	}
	public byte[] getData(CuratorFramework client, String path) throws Exception {
		return client.getData().forPath(path);
	}
	// this.getClient().

}
