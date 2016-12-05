package com.nginx.log.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HDFSUtil {
	/**
	 * 修改文件名
	 * */
	public void reName(FileSystem HDFSfileSystem, Path or_path, String name) {
		String parent_path = or_path.getParent().toString();

		try {
			HDFSfileSystem.rename(or_path, new Path(parent_path + "/" + name));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取后缀名
	 * */
	public String getEx(String str) {

		return str.substring(str.lastIndexOf(".") + 1);
	}

	public FileStatus[] getFiles(FileSystem HDFSfileSystem, String file_directory_name) throws FileNotFoundException,
			IllegalArgumentException, IOException {

		return HDFSfileSystem.listStatus(new Path(file_directory_name));
	}

	public FSDataInputStream openFile(FileSystem HDFSfileSystem, Path filePath) {

		FSDataInputStream stream = null;
		try {
			stream = HDFSfileSystem.open(filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stream;
	}

	/**
	 * 初始化
	 * */
	public FileSystem initFileSystem(String HDFS_uri) {

		Configuration conf = new Configuration();
		
		try {
			return FileSystem.get(URI.create(HDFS_uri), conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * HA 初始化
	 * */
	public FileSystem initFileSystem() {

		Configuration conf = new Configuration();
		//Hadoop HA P配置
		conf.set("fs.defaultFS", "hdfs://bdpha");
		conf.set("dfs.nameservices", "bdpha");
		conf.set("dfs.ha.namenodes.bdpha", "nn1,nn2");
		conf.set("dfs.namenode.rpc-address.bdpha.nn1", "master2:9000");
		conf.set("dfs.namenode.rpc-address.bdpha.nn2", "master1:9000");
		conf.set("dfs.client.failover.proxy.provider.bdpha",
				"org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
		
		try {
			return FileSystem.get(conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
