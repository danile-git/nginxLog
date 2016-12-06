package com.nginx.log.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.nginx.log.bean.PropertiesType;
import com.nginx.log.service.HBaseService;
import com.nginx.log.service.KakfaService;
import com.nginx.log.util.*;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.producer.Producer;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class LogSpout extends BaseRichSpout {


	static boolean sysnc = false;
	SpoutOutputCollector collector;
	public static final String DECLARE_FIELD = "line";
	ConsumerIterator<String, String> consumerIterator;
	HBaseService hBaseService = new HBaseService();
	private static final  Logger logger = Logger.getLogger(LogSpout.class);

	@Override
	public void nextTuple() {
		//logger.info("-------------------KAFKA-Spout Tuple------------------------");
		switch (PropertiesType.SOURCE_TYPE) {
		case HDFS:
			break;
		case KAFKA:
			readKafka();
			break;
		}
	}

	private void collectorEmit(String msg) {
		UUID msgId = UUID.randomUUID();
		collector.emit(new Values(msg, msgId.toString()));
	}

	@Override
	public void open(Map map, TopologyContext context, SpoutOutputCollector collector) {
		 switch (PropertiesType.SOURCE_TYPE) {
		 case HDFS:
		 break;
		 case KAFKA:
			 consumerIterator =new KakfaService().init();
		 break;
		 }
		this.collector = collector;
		// sysncData();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declare) {
		declare.declare(new Fields(DECLARE_FIELD, PropertiesType.MESSAGE_ID));
	}

	private void readKafka() {
	//	 System.out.println("read------");
		if (consumerIterator.hasNext() && HBaseUtil.IS_CONNECTION()) {
			String msg = consumerIterator.next().message();
			collectorEmit(msg);
		}
	}

	/**
	 * 同步处理失败的数据到kafka消息队列
	 * */
	private void sysncData() {

		if (!sysnc) {
			sysnc = true;
		} else {
			return;
		}
		KAFKAUtil kafka = new KAFKAUtil();
		// Producer<String, String> producer = kafka.getProducer();

		try {
			HashMap<String, String> databaseHashMap = hBaseService.scan(HBaseService.TEMP_HBASE_TABLE_NAME);
			System.out.println("同步数据中....");
			for (String key : databaseHashMap.keySet()) {
				collector.emit(new Values(databaseHashMap.get(key), key));
				// if (hBaseService.delete_tmp(key,
				// HBaseService.TEMP_HBASE_TABLE_NAME)) {
				// String vaueString = databaseHashMap.get(key);
				// kafka.send(producer, FlumeKafkaTopology.TOPIC, vaueString);
				// }
			}

			System.out.println("同步完毕.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("无法同步数据...");
		}
	}

	// @Override
	// public void ack(Object msgId) {
	// // 由于storm消息队列的原因,当下条数据处理时 上一条结果才会返回，所以如果下一条数据
	// // 进入时，时间超过topology.message.timeout.secs
	// // 上一条数据可能会超时，storm会标记为fail，所以此处放弃使用
	// //log.info("-----remove ---------------->" + msgId);
	// }

}
