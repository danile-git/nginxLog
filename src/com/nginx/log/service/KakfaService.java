package com.nginx.log.service;

import java.util.Properties;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import com.nginx.log.bean.Kafka;
import com.nginx.log.bean.PropertiesType;
import com.nginx.log.util.KAFKAUtil;

public class KakfaService {

	String [] propertys={
			PropertiesType.KAFKA_ZOOKEEPER_SESSION_TIMEOUT_MS,
			PropertiesType.KAFKA_ZOOKEEPER_SYNC_TIME_MS,
			PropertiesType.KAFKA_AUTO_COMMIT_INTERVAL_MS,
			PropertiesType.KAFKA_AUTO_OFFSET_RESET,
			PropertiesType.KAFKA_SERIALIZER_CLASS,
			PropertiesType.KAFKA_REBALANCE_MAX_RETRIES,
			PropertiesType.KAFKA_REBALANCE_BACKOFF_MS,
			PropertiesType.KAFKA_BOOTSTRAP_SERVERS
	};
	public ConsumerIterator<String, String> init() {
		KAFKAUtil kUtil = new KAFKAUtil();
		Kafka kafka=initConfig();
		KafkaStream<String, String> kafkaStream = kUtil.receive(kafka.getProperties(), kafka.getZookeeper(), kafka.getGroupId(), kafka.getTopic());
		return kafkaStream.iterator();
	}
	zookeeperService zkService = new zookeeperService();
	private Kafka initConfig(){
		
		Kafka kafka=new Kafka();
		Properties _properties = new Properties();
		for (int i = 0; i < propertys.length; i++) {
			_properties.put(propertys[i],zkService.getConf(propertys[i]));
		}
		kafka.setProperties(_properties);
		kafka.setGroupId(zkService.getConf(PropertiesType.KAFKA_GROUPID));
		kafka.setTopic(zkService.getConf(PropertiesType.KAFKA_TOPIC));
		kafka.setZookeeper(zkService.getConf(PropertiesType.ZOOKEEPER_QUORUM));
		System.out.println("kafka setting :"+kafka.toString());
		return kafka;
	}
}
