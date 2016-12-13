package com.nginx.log.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;

import com.nginx.log.util.*;
import com.nginx.log.bean.*;

public class HBaseService  implements Serializable{
	HBaseUtil hbaseUtil = new HBaseUtil();

	public static final String TABLE_NAME = "nginx_log";

	public static final byte[] FAMILY = "request".getBytes();

	public static final byte[] QUALIFER_COOKIES = "cookies".getBytes();
	public static final byte[] QUALIFER_REQUEST_TIME = "request_time".getBytes();
	public static final byte[] QUALIFER_TYPE = "type".getBytes();
	public static final byte[] QUALIFER_REQUEST_SOURCE = "request_source".getBytes();
	public static final byte[] QUALIFER_HTTP_VERSION = "http_version".getBytes();

	public static final byte[] QUALIFER_MSEC = "http_version".getBytes();
	public static final byte[] QUALIFER_TIME_LOCAL = "time_local".getBytes();
	public static final byte[] QUALIFER_TIMEZONE = "timezone".getBytes();
	public static final byte[] QUALIFER_HTTP_USER_AGENT = "http_user_agent".getBytes();
	public static final byte[] QUALIFER_REMOTE_ADDR = "remote_addr".getBytes();

	public static final byte[] QUALIFER_STATUS = "status".getBytes();
	public static final byte[] QUALIFER_BODY_BYTES_SENT = "body_bytes_sent".getBytes();
	public static final byte[] QUALIFER_HTTP_REFERER = "http_referer".getBytes();
	public static final byte[] QUALIFER_UPSTREAM_ADDR = "upstream_addr".getBytes();

//	String[] propertys = { 
//			PropertiesType.HBASE_BATCH_SIZE,
//			PropertiesType.TASK_BATCH_TIME
//	};
//	zookeeperService zkService = new zookeeperService();
//
//	public HBasePro init() {
//		HBasePro hbasePro = new HBasePro();
//		hbasePro.setHbase_batch_size(Integer.parseInt(zkService.getConf(propertys[0])));
//		 hbasePro.setHbase_batch_time(Integer.parseInt(zkService.getConf(propertys[1])));
//		return hbasePro;
//	}

	public void put(NginxJSON nginxJSON) {
		try {

			Put put = new Put(nginxJSON.getHbase_id().getBytes());

			put.add(FAMILY, QUALIFER_COOKIES, nginxJSON.getCookies().getBytes());
			put.add(FAMILY, QUALIFER_REQUEST_TIME, nginxJSON.getRequest_time().getBytes());
			put.add(FAMILY, QUALIFER_TYPE, String.valueOf(nginxJSON.getType()).getBytes());
			put.add(FAMILY, QUALIFER_REQUEST_SOURCE, nginxJSON.getRequest_source().getBytes());
			put.add(FAMILY, QUALIFER_HTTP_VERSION, nginxJSON.getHttp_version().getBytes());

			put.add(FAMILY, QUALIFER_MSEC, nginxJSON.getMsec().getBytes());
			put.add(FAMILY, QUALIFER_TIME_LOCAL, nginxJSON.getTime_local().getBytes());
			put.add(FAMILY, QUALIFER_TIMEZONE, String.valueOf(nginxJSON.getTimeZone()).getBytes());
			put.add(FAMILY, QUALIFER_HTTP_USER_AGENT, String.valueOf(nginxJSON.getHttp_user_agent()).getBytes());
			put.add(FAMILY, QUALIFER_REMOTE_ADDR, nginxJSON.getRemote_addr().getBytes());

			put.add(FAMILY, QUALIFER_STATUS, nginxJSON.getStatus().getBytes());
			put.add(FAMILY, QUALIFER_BODY_BYTES_SENT, nginxJSON.getBody_bytes_sent().getBytes());
			put.add(FAMILY, QUALIFER_HTTP_REFERER, String.valueOf(nginxJSON.getHttp_referer()).getBytes());
			put.add(FAMILY, QUALIFER_UPSTREAM_ADDR, String.valueOf(nginxJSON.getUpstream_addr()).getBytes());

			hbaseUtil.add(TABLE_NAME, put);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static final byte[] STORM_HBASE_FAMILY = "info".getBytes();
	public static final byte[] STORM_HBASE_QUALIFER_COOKIES = "cookies".getBytes();
	public static final String STORM_HBASE_TABLE_NAME = "member";

	public void insert(NginxJSON nginx) throws IOException {
		Put put = new Put(nginx.getMsec().getBytes());
		put.add(STORM_HBASE_FAMILY, STORM_HBASE_QUALIFER_COOKIES, nginx.getOriginal().getBytes());
		HBaseUtil.Put(STORM_HBASE_TABLE_NAME, put);

	}

	public void insert(List<NginxJSON> nginx) throws IOException {
		List<Put> puts = new ArrayList<Put>();
		for (NginxJSON nginxJSON : nginx) {
			Put put = new Put(nginxJSON.getMsec().getBytes());
			put.add(STORM_HBASE_FAMILY, STORM_HBASE_QUALIFER_COOKIES, nginxJSON.getOriginal().getBytes());
			puts.add(put);
		}
		HBaseUtil.Put(STORM_HBASE_TABLE_NAME, puts);

	}

	public static final String TEMP_HBASE_TABLE_NAME = "_switch_tmp";

	public void insert_tmp(UUID uid, String info) throws IOException {

		Put put = new Put(uid.toString().getBytes());
		put.add(STORM_HBASE_FAMILY, STORM_HBASE_QUALIFER_COOKIES, info.getBytes());

		HBaseUtil.Put(TEMP_HBASE_TABLE_NAME, put);
	}

	public boolean insert_tmp(HashMap<UUID, String> keyMap) throws IOException {
		List<Put> puts = new ArrayList<Put>();
		for (UUID key : keyMap.keySet()) {

			Put put = new Put(key.toString().getBytes());
			put.add(STORM_HBASE_FAMILY, STORM_HBASE_QUALIFER_COOKIES, keyMap.get(key).getBytes());
			puts.add(put);
		}
		return HBaseUtil.Put(TEMP_HBASE_TABLE_NAME, puts);
	}

	public void delete_tmp(String uid) throws IOException {
		Delete del = new Delete(uid.toString().getBytes());
		HBaseUtil.Delete(TEMP_HBASE_TABLE_NAME, del);
	}

	public void delete_tmp(List<NginxJSON> nginx) throws IOException {

		List<Delete> delList = new ArrayList<Delete>();
		for (NginxJSON nginxJSON : nginx) {
			Delete del = new Delete(nginxJSON.getUuid().getBytes());
			delList.add(del);
		}
		HBaseUtil.Delete(TEMP_HBASE_TABLE_NAME, delList);
	}

	public HashMap<String, String> scan(String table) throws IOException {
		ReturnInterchange returnInterchange = null;
		HashMap<String, String> databaseHashMap = new HashMap<String, String>();
		try {
			returnInterchange = HBaseUtil.scanTable(table, null, null);
			ResultScanner resultScanner = returnInterchange.getresultScanner();
			for (Result result : resultScanner) {// 按行去遍历
				for (KeyValue kv : result.raw()) {// 遍历每一行的各列
					String value = Bytes.toString(kv.getValue());
					databaseHashMap.put(Bytes.toString(kv.getRow()), value);
					// System.out.println(value);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			returnInterchange.gethTablePool().close();
		}
		return databaseHashMap;
	}

	public boolean delete_tmp(String uid, String tableName) {
		try {
			Delete del = new Delete(uid.toString().getBytes());
			HBaseUtil.Delete(tableName, del);

		} catch (Exception e) {
			return false;
		}
		return true;

	}
}
