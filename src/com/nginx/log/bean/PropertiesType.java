package com.nginx.log.bean;

import java.io.Serializable;

import com.nginx.log.core.FlumeKafkaTopology;
import com.nginx.log.core.LogBoltBackup;
import com.nginx.log.core.LogBoltSave;
import com.nginx.log.core.LogSpout;

public class PropertiesType implements Serializable{
	//Local
	public static final String ZOOKEEPER_PATH = "zookeeper.path";
	/**
	 * 储存---->hbase mysql 或 hdfs
	 **/
	public static final SaveType SAVE_TYPE = SaveType.HBASE;
	/**
	 * 导入类型<---------kafka ,hdfs
	 */
	public static final SourceType SOURCE_TYPE = SourceType.KAFKA;

	public static final String LOGBOLTSAVE_ID = LogBoltSave.class.getSimpleName();
	public static final String FLUMEKAFKATOPOLOGY = FlumeKafkaTopology.class.getSimpleName();
	public static final String LOGBACKUPSAVE_ID = LogBoltBackup.class.getSimpleName();
	public static final String KAFKA_SPOUT_ID = LogSpout.class.getSimpleName();
	public static final String MESSAGE_ID = "MESSAGE_ID";
	
	//TIMER
	public static final String TASK_BATCH_TIME = "task.batch.time";
	
	// key kafka
	public static final String KAFKA_ZOOKEEPER_SESSION_TIMEOUT_MS= "zookeeper.session.timeout.ms";
	public static final String KAFKA_ZOOKEEPER_SYNC_TIME_MS = "zookeeper.sync.time.ms";
	public static final String KAFKA_AUTO_COMMIT_INTERVAL_MS ="auto.commit.interval.ms";
	public static final String KAFKA_AUTO_OFFSET_RESET = "auto.offset.reset";
	public static final String KAFKA_SERIALIZER_CLASS ="serializer.class";
	
	public static final String KAFKA_METADATA_BROKER_LIST ="metadata.broker.list";
	public static final String KAFKA_KEY_SERIALIZER_CLASS ="key.serializer.class";
	
	public static final String KAFKA_ZOOKEEPER_CONNECT ="zookeeper.connect";
	public static final String KAFKA_GROUP_ID ="group.id";
	
	public static final String KAFKA_REBALANCE_MAX_RETRIES ="rebalance.max.retries";
	public static final String KAFKA_REBALANCE_BACKOFF_MS ="rebalance.backoff.ms";
	public static final String KAFKA_BOOTSTRAP_SERVERS ="bootstrap.servers";
	

	public static final String KAFKA_TOPIC = "kafka.topic";// kafka话题
	public static final String KAFKA_GROUPID = "kafka.topic.group";// 消息组
	//zookeeper
	public static final String ZOOKEEPER_QUORUM = "zookeeper.quorum";
	
	//storm
	// NIMBUS的信息
	public static final String STORM_SPOUT_SIZE = "storm.spout.size";
	public static final String STORM_BOLT_SIZE = "storm.bolt.size";
	public static final String STORM_WORK_SIZE = "storm.work.size";
	public static final String STORM_NUM_ACKERS = "storm.num.ackers";
	public static final String STORM_SEEDS = "storm.seeds";
	public static final String STORM_DISTRIBUTE = "storm.distribute";
	public static final String STORM_NIMBUS = "storm.nimbus";
	public static String STORM_PORT = "9092";
	
	//hbase 
	public static final String HBASE_BATCH_SIZE = "hbase.batch.size";

	public static final int DEFAULT_HBASE_BATCH_SIZE = 1000;
	
	public static final String HBASE_ZOOKEEPER_PROPERTY_CLIENTPORT ="hbase.zookeeper.property.clientPort";
	public static final String HBASE_ZOOKEEPER_QUORUM ="hbase.zookeeper.quorum";
	
	//value 
	public static final String KAFKA_SERIALIZER_STRINGENCODER ="kafka.serializer.StringEncoder";
	public static final String KAFKA_AUTO_OFFSET_RESET_SMALLEST ="smallest";

	public static final String ZOOKEEPER_PORT="2181";
	public static final String ZOOKEEPER_SERVERS="master2,node1,node2,node3,node4";
	//public static final String ZOOKEEPER_SERVERS="slave1,slave2,slave3";
	public static final String ZOOKEEPER_SERVERS_WITH_PORT="master2:2181,node2:2181,node4:2181,node1:2181,node3:2181";
	
	public static final String KAFKA_SERVERS="master1:9092,master2:9092,node1:9092,node2:9092,node3:9092,node4:9092,node5:9092,node6:9092";
	
}
