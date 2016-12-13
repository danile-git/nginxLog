package com.nginx.log.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.nginx.log.bean.PropertiesType;
import com.nginx.log.service.HBaseService;
import com.nginx.log.service.KakfaService;
import com.nginx.log.util.*;

import kafka.consumer.ConsumerIterator;

import org.apache.storm.spout.SpoutOutputCollector;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;

import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

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
	 * ͬ������ʧ�ܵ����ݵ�kafka��Ϣ����
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
			System.out.println("ͬ��������....");
			for (String key : databaseHashMap.keySet()) {
				collector.emit(new Values(databaseHashMap.get(key), key));
				// if (hBaseService.delete_tmp(key,
				// HBaseService.TEMP_HBASE_TABLE_NAME)) {
				// String vaueString = databaseHashMap.get(key);
				// kafka.send(producer, FlumeKafkaTopology.TOPIC, vaueString);
				// }
			}

			System.out.println("ͬ�����.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("�޷�ͬ������...");
		}
	}

	// @Override
	// public void ack(Object msgId) {
	// // ����storm��Ϣ���е�ԭ��,���������ݴ���ʱ ��һ������Ż᷵�أ����������һ������
	// // ����ʱ��ʱ�䳬��topology.message.timeout.secs
	// // ��һ�����ݿ��ܻᳬʱ��storm����Ϊfail�����Դ˴�����ʹ��
	// //log.info("-----remove ---------------->" + msgId);
	// }

}
