package com.nginx.log.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.nginx.log.bean.PropertiesType;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

public class KAFKAUtil {

	/**
	 * 发送消息
	 * */
	public void send(Producer<String, String> producer, String topic, String msg) {
		KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, msg);
		producer.send(data);
	}

	private ProducerConfig initProducerConfig() {
		// 设置配置属性
		Properties props = new Properties();
		props.put(PropertiesType.KAFKA_METADATA_BROKER_LIST, PropertiesType.KAFKA_SERVERS);
		props.put(PropertiesType.KAFKA_SERIALIZER_CLASS,PropertiesType.KAFKA_SERIALIZER_STRINGENCODER);
		props.put(PropertiesType.KAFKA_KEY_SERIALIZER_CLASS,PropertiesType.KAFKA_SERIALIZER_STRINGENCODER);
		props.put("producer.type", "async");
		ProducerConfig config = new ProducerConfig(props);
		return config;
	}

	public Producer<String, String> getProducer() {
		Producer<String, String> producer = new Producer<String, String>(initProducerConfig());
		return producer;
	}

	/**
	 * 接收消息流
	 * */
	public KafkaStream<String, String> receive(Properties _properties, String _zookeepers, String _groupId, String topic) {
		Properties properties = getProperties(_properties, _zookeepers, _groupId);
		ConsumerConnector consumerConnector = initConsumerConfig(properties);
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(1));
		StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
		StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
		Map<String, List<KafkaStream<String, String>>> consumerMap = consumerConnector.createMessageStreams(
				topicCountMap, keyDecoder, valueDecoder);
		return consumerMap.get(topic).get(0);

	}

	private Properties getProperties(Properties _properties, String _zookeepers, String _groupId) {

		if (_properties == null) {
			_properties = new Properties();
		}
		if (!_properties.containsKey(PropertiesType.KAFKA_ZOOKEEPER_CONNECT))
			_properties.put(PropertiesType.KAFKA_ZOOKEEPER_CONNECT, _zookeepers);
		if (!_properties.containsKey(PropertiesType.KAFKA_GROUP_ID))
			_properties.put(PropertiesType.KAFKA_GROUP_ID, _groupId);
		return _properties;
	}

	private ConsumerConnector initConsumerConfig(Properties properties) {
		ConsumerConfig consumerConfig = new ConsumerConfig(properties);
		Consumer consumer=null;

		return kafka.consumer.Consumer.createJavaConsumerConnector(consumerConfig);
	}

}
