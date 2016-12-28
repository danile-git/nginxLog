package com.nginx.log.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.LoggerFactory;

import com.nginx.log.bean.PropertiesType;
import com.nginx.log.bean.ReturnInterchange;
public class HBaseUtil implements Serializable {

	private static HConnection connection = null;
	private static Configuration conf = null;
	private static final  org.slf4j.Logger logger =LoggerFactory.getLogger(HBaseUtil.class);
	public static Boolean IS_CONNECTION() {
		boolean _flg = false;
		try {
			if (getConnection() != null) {
				_flg = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return _flg;
	}

	static {
		try {
			conf = HBaseConfiguration.create();
			conf.set(PropertiesType.HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT, PropertiesType.ZOOKEEPER_PORT);
			conf.set(PropertiesType.HBASE_ZOOKEEPER_QUORUM, PropertiesType.ZOOKEEPER_SERVERS);
			getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static HConnection getConnection() throws IOException {
		if (connection == null)
			connection = HConnectionManager.createConnection(conf);
		return connection;
	}

	public static void Put(String tableName, Put put) throws IOException {
		HTableInterface tabe = null;
		try {
			HConnection conne = getConnection();
			if (conne != null) {
				tabe = conne.getTable(tableName);
				tabe.put(put);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (tabe != null)
				tabe.close();
		}

	}

	public static boolean Put(String tableName, List<Put> put) throws IOException  {
		HTableInterface tabe = null;
		boolean flg = false;
		try {
			HConnection conne = getConnection();
			tabe = conne.getTable(tableName);
		//tabe.setWriteBufferSize(6 * 1024 * 1024);
			//tabe.setAutoFlush(true);
			tabe.put(put);
			flg = true;
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			if (tabe != null)
				tabe.close();
		}

		return flg;
	}

	
	public static void Delete(String tableName, org.apache.hadoop.hbase.client.Delete delete) throws IOException {
		HTableInterface tabe = null;
		try {
			HConnection conne = getConnection();
			if (conne != null) {
				tabe = conne.getTable(tableName);
				tabe.delete(delete);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (tabe != null)
				tabe.close();
		}

	}

	public static void Delete(String tableName, List<org.apache.hadoop.hbase.client.Delete> delete) throws IOException {
		HTableInterface tabe = null;
		try {
			HConnection conne = getConnection();
			if (conne != null) {
				tabe = conne.getTable(tableName);
				tabe.delete(delete);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (tabe != null)
				tabe.close();
		}

	}

	public static ReturnInterchange scanTable(String tablename, String startRow, String stopRow) throws Exception {
		HTablePool pool = new HTablePool(conf, 2);
		HTableInterface hbTable = pool.getTable(tablename); // 表名
		Scan scan = new Scan();
		// scan.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("qual1"));扫某一列
		if (startRow != null) { // 设置扫描的范围
			scan.setStartRow(Bytes.toBytes(startRow));
		}
		if (stopRow != null) {
			scan.setStopRow(Bytes.toBytes(stopRow));
		}
		ResultScanner resultScanner = hbTable.getScanner(scan);
		hbTable.close();
		return new ReturnInterchange(resultScanner, pool);
	}

	public static void showAll(String tableName) throws IOException {
		HTableInterface tabe = null;
		try {
			tabe = getConnection().getTable(tableName);
			Scan scan = new Scan();
			ResultScanner _resultScanner = tabe.getScanner(scan);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (tabe != null)
				tabe.close();
		}
	}

	public HBaseConfiguration hbaseConfig;

	// public HBaseUtil() {
	// Configuration config = new Configuration();
	//
	// config.set("hbase.zookeeper.quorum", FlumeKafkaTopology.SERVERS);
	//
	// config.set("hbase.zookeeper.property.clientPort",
	// String.valueOf(FlumeKafkaTopology.ZOOKEEPER_PORT));
	// hbaseConfig = new HBaseConfiguration(config);
	// }
	// public HBaseUtil(String servers,int zooport) {
	// Configuration config = new Configuration();
	//
	// config.set("hbase.zookeeper.quorum", servers);
	//
	// config.set("hbase.zookeeper.property.clientPort",
	// String.valueOf(zooport));
	// hbaseConfig = new HBaseConfiguration(config);
	// }

	public ResultScanner ScanHBase(HTable _table, int rows) throws IOException {

		Scan _scan = new Scan();
		_scan.setCaching(rows);
		_scan.setBatch(1);
		_scan.setMaxResultSize(rows);

		ResultScanner _resultScanner = _table.getScanner(_scan);
		/*
		 * try { for (Result result : _resultScanner) { KeyValue[] _keyKeyValues
		 * = result.raw();
		 * 
		 * for (int i = 0; i < _keyKeyValues.length; i++) {
		 * 
		 * String fieldName = new String(_keyKeyValues[i].getQualifier());
		 * String fieldValue = new String(_keyKeyValues[i].getValue());
		 * System.out.println(fieldName + "  -> " + fieldValue); } }
		 * 
		 * } finally { _resultScanner.close(); _table.close();
		 * 
		 * }
		 */
		return _resultScanner;
	}

	public ResultScanner ScanHBaseByPage(HTable _table, int rows) throws IOException {

		Scan _scan = new Scan();
		_scan.setCaching(rows);
		_scan.setBatch(1);
		_scan.setMaxResultSize(rows);

		ResultScanner _resultScanner = _table.getScanner(_scan);

		return _resultScanner;
	}

	String key = "";

	public String getStartKey(HTable _table, int num) throws IOException {

		Scan _scan = new Scan();
		_scan.setCaching(num);
		String tmpString = null;
		// _scan.setBatch(1);
		if (key != "") {
			_scan.setStartRow(key.getBytes());
		}
		Filter filter = new PageFilter(num); // scan.setFilter(filter);
		_scan.setFilter(filter);
		ResultScanner _resultScanner = _table.getScanner(_scan);

		for (Result result : _resultScanner) {
			String stri = new String(result.getRow());
			if (stri.equals(key)) {
				continue;
			}
			tmpString = key = new String(result.getRow());
			KeyValue[] kv = result.raw();
			for (int i = 0; i < kv.length; i++) {
				String fieldName = new String(kv[i].getQualifier());
				String fieldValue = new String(kv[i].getValue());
				System.out.println(fieldName + "  -> " + fieldValue);
			}
		}

		_resultScanner.close();

		return tmpString;
	}

	public String getRow(HTable _table, String key) throws IOException {

		Get get = new Get(key.getBytes());
		Result result = _table.get(get);

		String tmpString = null;

		String stri = new String(result.getRow());
		if (stri.equals(key)) {
			return tmpString;
		}
		tmpString = key = new String(result.getRow());
		KeyValue[] kv = result.raw();
		for (int i = 0; i < kv.length; i++) {
			String fieldName = new String(kv[i].getQualifier());
			String fieldValue = new String(kv[i].getValue());
			System.out.println(fieldName + "  -> " + fieldValue);
		}

		return tmpString;
	}

	long timesp = 1471252903762L;

	public String getRow_time(HTable _table) throws IOException {

		Scan _scan = new Scan();
		// _scan.setCaching(num);
		String tmpString = null;
		// _scan.setBatch(1);
		if (key != "") {
			_scan.setStartRow(key.getBytes());
		}
		_scan.setTimeRange(timesp, timesp);

		// Filter filter = new PageFilter(num); // scan.setFilter(filter);
		// _scan.setFilter(filter);
		ResultScanner _resultScanner = _table.getScanner(_scan);

		for (Result result : _resultScanner) {
			String stri = new String(result.getRow());
			if (stri.equals(key)) {
				continue;
			}
			tmpString = key = new String(result.getRow());
			KeyValue[] kv = result.raw();
			for (int i = 0; i < kv.length; i++) {
				String fieldName = new String(kv[i].getQualifier());
				String fieldValue = new String(kv[i].getValue());
				System.out.println(fieldName + "  -> " + fieldValue);
			}
		}

		_resultScanner.close();

		return tmpString;
	}

	/**
	 * 添加一条数据
	 * 
	 * @throws IOException
	 */
	public void add(String tableName, Put put) throws IOException {
		HTable hTable = new HTable(hbaseConfig, tableName);
		try {
			hTable.put(put);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			hTable.close();
		}

	}

	/**
	 * 创建一张表
	 * 
	 * @throws IOException
	 */
	public void createTable(String tablename) throws IOException {
		HBaseAdmin admin = new HBaseAdmin(hbaseConfig);
		if (admin.tableExists(tablename)) {
			System.out.println("table Exists!!!");
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(tablename);
			tableDesc.addFamily(new HColumnDescriptor("name:"));
			admin.createTable(tableDesc);
			System.out.println("create table ok.");
		}
		admin.close();
	}

	/**
	 * 删除一张表
	 * 
	 * @throws IOException
	 */
	public void dropTable(String tablename) throws IOException {
		HBaseAdmin admin = new HBaseAdmin(hbaseConfig);
		admin.disableTable(tablename);
		admin.deleteTable(tablename);
		System.out.println("drop table ok.");
		admin.close();
	}

}
