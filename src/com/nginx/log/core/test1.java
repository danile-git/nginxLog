package com.nginx.log.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudera.com.fasterxml.jackson.core.type.TypeReference;
import com.cloudera.com.fasterxml.jackson.databind.ObjectMapper;
import com.nginx.log.Interface.IZKLock;
import com.nginx.log.bean.NginxJSON;
import com.nginx.log.bean.PropertiesType;
import com.nginx.log.bean.Type;
import com.nginx.log.service.RichClickService;
import com.nginx.log.util.Config;
import com.nginx.log.util.HDFSUtil;
import com.nginx.log.util.IOUtil;
import com.nginx.log.util.MysqlUtil;
import com.nginx.log.util.ZookeeperLock;

public class test1<T> {

	private final static Logger logger = LoggerFactory.getLogger(test1.class);

	static int indx = 0;
	static int rows = 1000;
	static String sql = "SELECT  yt_visitlog.sid,yt_visitlog.vid,yt_visitlog.goods_name,yt_visitlog.special,yt_visitlog.reachtime,yt_visitlog.leavetime,yt_visitlog.lastmodtime,yt_visitlog.centerIp,yt_visitlog.urls,yt_visitlog.dop,yt_visitlog.don,yt_visitlog.vcnt,yt_visitlog.idx FROM `yt_visitlog`  limit %d,1000";

	public static void main(String[] arg) throws IllegalArgumentException, IOException, SQLException {
		ZookeeperLock zkl=new ZookeeperLock();
		//zkl.test();
	//	zkl.test();
		//System.out.println(PropertiesType.FLUMEKAFKATOPOLOGY);
//		final MysqlUtil mysql = new MysqlUtil();
//		ResultSet rrr = mysql.executeQuery("select count(1) count from yt_visitlog", 1);
//		rrr.next();
//		int total = rrr.getInt("count");
//		System.out.println(total);
//		int i = 0;
//		double pages = total % rows > 0 ? (total / rows) + 1 : total / rows;
//		System.out.println(pages);
//		while (i <= pages) {
//			Thread thread = new Thread(new FetchFromMySQL(i,rows));
//			i++;
//			thread.start();
//		}
		// String sql =
		// "SELECT  yt_visitlog.sid,yt_visitlog.vid,yt_visitlog.goods_name,yt_visitlog.special,yt_visitlog.reachtime,yt_visitlog.leavetime,yt_visitlog.lastmodtime,yt_visitlog.centerIp,yt_visitlog.urls,yt_visitlog.dop,yt_visitlog.don,yt_visitlog.vcnt,yt_visitlog.idx FROM `yt_visitlog`  limit 0,1000";
		// MysqlUtil mysql = new MysqlUtil();
		// ResultSet result = mysql.executeQuery(sql, 1000);
		// int i=1;
		// while(result.next()){
		//
		// System.out.println(result.getString("sid"));
		// System.out.println(i++);
		// }
		//
		//
		// String
		// json="{\"body_bytes_sent\":\"175\",\"x\":\"444\",\"request_time\":\"0.000\"}";

		// System.out.println( RichClickService.convertJson(json).toKeyValue());
		// JSONObject jObject = JSONObject.fromObject(json);
		// NginxJSON JSON = (NginxJSON) JSONObject.toBean(jObject,
		// NginxJSON.class);

		// String json = "{\"name\":\"zitong\", \"age\":\"26\"}";
		// Map<String, String> map = new HashMap<String, String>();
		// ObjectMapper mapper = new ObjectMapper();

		// try{
		// map = mapper.readValue(json, new
		// TypeReference<HashMap<String,String>>(){});
		// System.out.println(map);
		// }catch(Exception e){
		// e.printStackTrace();
		// }

		// getConf(test.class);
		// HDFSUtil hdfs = new HDFSUtil();
		// FileSystem fssytem = hdfs.initFileSystem();
		// FSDataOutputStream fd = fssytem.create(new
		// Path("/storm_output/111"));
		// fd.write("rrr".getBytes());
		// fd.flush();
		// fd.close();
	}

	Config config = new Config();

	public void initTest() {
		try {
			InputStream inputStream = IOUtil.fileInputStream("/core.xml");
			byte[] confbyte = IOUtil.readStream(inputStream);
			HashMap<String, String> sssHashMap = config.loadConfig(IOUtil.byteTOInputStream(confbyte));
			for (String key : sssHashMap.keySet()) {
				logger.info("---------key : " + key + " value : " + sssHashMap.get(key));
			}
			logger.info("--------------" + confbyte.toString());
			inputStream.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("----------");

	}

	public Class<?> getConf(Class<?> type) {
		Type converType = Type.None;
		try {
			converType = Type.valueOf(type.getSimpleName());
		} catch (Exception e) {
			converType = Type.None;
		}

		switch (converType) {
		case Integer:
			System.out.println(converType);
			break;
		case String:
			System.out.println(converType);
			break;
		default:
			System.out.println("other");
			break;
		}
		return type;
	}
}
