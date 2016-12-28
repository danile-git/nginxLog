package com.nginx.log.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import com.nginx.log.bean.PropertiesType;
import com.nginx.log.bean.RichClick;
import com.nginx.log.bean.RichClickEnum;
import com.nginx.log.service.HBaseService;
import com.nginx.log.service.KakfaService;
import com.nginx.log.service.RichClickService;
import com.nginx.log.service.ZookeeperLockService;
import com.nginx.log.util.*;

import kafka.consumer.ConsumerIterator;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class LogSpout extends BaseRichSpout {

	boolean sysnc = false;
	SpoutOutputCollector collector;
	public static final String DECLARE_FIELD = "line";
	ConsumerIterator<String, String> consumerIterator;
	HBaseService hBaseService = new HBaseService();
	int index = 0;
	private static   final Logger logger = Logger.getLogger(LogSpout.class);
	MysqlUtil mysql = null;
	ZookeeperLockService zkLock = null;
    
	@Override
	public void nextTuple() {
		// logger.info("-------------------KAFKA-Spout Tuple------------------------");
		switch (PropertiesType.SOURCE_TYPE) {
		case HDFS:
			break;
		case KAFKA:
			readKafka();
			break;
		case MYSQL:
			readMySQL();
			break;
		}
	}

	private synchronized  void collectorEmit(String msg) {
		///UUID msgId = UUID.randomUUID();
		//collector.emit(new Values(msg, msgId.toString()));
		collector.emit(new Values(msg));
	}

	private synchronized  void collectorEmit(RichClick msg) {
		//UUID msgId = UUID.randomUUID();
		collector.emit(new Values(msg));
	}

	@Override
	public void open(Map map, TopologyContext context, SpoutOutputCollector collector) {
		switch (PropertiesType.SOURCE_TYPE) {
		case MYSQL:
			zkLock = new ZookeeperLockService();
			index = getIndex();
			if (index % 2 == 0) {
				mysql = new MysqlUtil("cccc");
			} else {
				mysql = new MysqlUtil("dddd");
			}
		
			break;
		case KAFKA:
			consumerIterator = new KakfaService().init();
			break;
		default:
			break;
		}
		this.collector = collector;
		// sysncData();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declare) {
		declare.declare(new Fields(DECLARE_FIELD));
	}

	private void readKafka() {
		// System.out.println("read------");
		if (consumerIterator.hasNext() && HBaseUtil.IS_CONNECTION()) {
			String msg = consumerIterator.next().message();
			collectorEmit(msg);
		}
	}

	static String sql = "SELECT  sid,vid,goods_name,special,reachtime,leavetime,lastmodtime,centerIp,urls,dop,don,vcnt,idx FROM `yt_visitlog`  limit %d,%d";

	private void readMySQL() {
		if (index > FlumeKafkaTopology.pages)
			return;
		logger.info(this.toString() + "   index: " + index+"  limit "+index * FlumeKafkaTopology.rows+"  allPage"+FlumeKafkaTopology.pages);
		String sql2 = String.format(sql, index * FlumeKafkaTopology.rows, FlumeKafkaTopology.rows);
		ResultSet result = null;
		try {
			result = mysql.executeQuery(sql2, FlumeKafkaTopology.rows);
			while (result.next()) {
				collectorEmit(RichClickService.convertMySQL(result));
			}
			result.close();
			index=getIndex();
		} catch (Exception e) {
			if (result != null)
				try {
					result.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
//		try {
//			Thread.sleep(5);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
	
	int getIndex(){
		return Integer.parseInt(zkLock.dolock(this.toString()).toString());
	}

//	/**
//	 * 同步处理失败的数据到kafka消息队列
//	 * */
//	private void sysncData() {
//
//		if (!sysnc) {
//			sysnc = true;
//		} else {
//			return;
//		}
//		KAFKAUtil kafka = new KAFKAUtil();
//		// Producer<String, String> producer = kafka.getProducer();
//
//		try {
//			HashMap<String, String> databaseHashMap = hBaseService.scan(HBaseService.TEMP_HBASE_TABLE_NAME);
//			System.out.println("同步数据中....");
//			for (String key : databaseHashMap.keySet()) {
//				collector.emit(new Values(databaseHashMap.get(key), key));
//				// if (hBaseService.delete_tmp(key,
//				// HBaseService.TEMP_HBASE_TABLE_NAME)) {
//				// String vaueString = databaseHashMap.get(key);
//				// kafka.send(producer, FlumeKafkaTopology.TOPIC, vaueString);
//				// }
//			}
//
//			System.out.println("同步完毕.");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("无法同步数据...");
//		}
//	}

	// @Override
	// public void ack(Object msgId) {
	// // 由于storm消息队列的原因,当下条数据处理时 上一条结果才会返回，所以如果下一条数据
	// // 进入时，时间超过topology.message.timeout.secs
	// // 上一条数据可能会超时，storm会标记为fail，所以此处放弃使用
	// //log.info("-----remove ---------------->" + msgId);
	// }

}
