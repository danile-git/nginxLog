package com.nginx.log.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import kafka.javaapi.producer.Producer;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;

import com.nginx.log.bean.NginxJSON;
import com.nginx.log.bean.ReturnInterchange;
import com.nginx.log.service.HBaseService;
//import compojure.core.ANY;

public class test {
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// kafka.send(producer, topic, "2222222222222");
	// }
	//
	// String topic = "d_test_0001";
	// KAFKAUtil kafka = new KAFKAUtil();
	// Producer<String, String> producer = kafka.getProducer();

	public static void main(String[] args) {
		int progress;
		try{
		progress=Integer.parseInt(args[0]);
		}catch(Exception e){
			progress=10;
		}
		String topic = "xinhua";
		KAFKAUtil kafka = new KAFKAUtil();
		Producer<String, String> producer = kafka.getProducer();
		int stage=0;
	while(true){
		stage++;
		//	List<Put> puts=new ArrayList<Put>();
		for (int i = 0; i <1000; i++) {
			Thread thread = new TestRunnable(kafka, producer, topic, UUID.randomUUID());
			thread.run();
			//System.out.println(i);
		}
		System.out.println(stage);
	
		 try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

		// HBaseService hBaseService = new HBaseService();
		// HashMap<String, String> databaseHashMap;
		// try {
		// databaseHashMap = hBaseService.scan(table);
		// for (String key : databaseHashMap.keySet()) {
		// hBaseService.delete_tmp(key, table);
		// }
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// HBaseService hBaseService=new HBaseService();
		// NginxJSON nginxJSON=new NginxJSON();
		// nginxJSON.setMsec("666666666");
		// hBaseService.insert_tmp(nginxJSON);
		// HBaseUtil habsBaseUtil=new HBaseUtil("master2,node2,node4",2181);
		// HTable tabe = new HTable(habsBaseUtil.hbaseConfig, "member");
		// Put put=new Put("55ee".getBytes());
		// put.add("info".getBytes(), "adddeee".getBytes(),
		// "eeyyyyyeeeeeeeed".getBytes());
		// habsBaseUtil.add("member", put);
		// ResultScanner _resultScanner= habsBaseUtil.ScanHBase(tabe, 5);
		// for (Result result : _resultScanner) {
		// KeyValue[] _keyKeyValues = result.raw();
		//
		// for (int i = 0; i < _keyKeyValues.length; i++) {
		//
		// String fieldName = new String(
		// _keyKeyValues[i].getQualifier());
		// String fieldValue = new String(_keyKeyValues[i].getValue());
		// System.out.println(fieldName+fieldValue);
		// }
		// }
		// FileSystem fs= hdfsUtil.initFileSystem(FlumeKafkaTopology.HDFS);
		// FileStatus[] HDFS_files= hdfsUtil.getFiles(fs,
		// FlumeKafkaTopology.FILE_DIRECTORY);
		// for (FileStatus HDFS_file : HDFS_files) {
		// Path HDFS_filePath = HDFS_file.getPath();
		// System.out.println(HDFS_filePath.getName());
		// }

		// // TODO Auto-generated method stub
		// HBaseUtil hBaseUtil = new HBaseUtil();
		// HTable tabe = new HTable(hBaseUtil.hbaseConfig, "nginx_log");
		// while (true) {
		// String keyString = hBaseUtil.getRow(tabe, "");
		// if(keyString!=null)
		// System.out.println(keyString);
		// }
	}

}
