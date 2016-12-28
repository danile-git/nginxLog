package com.nginx.log.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.LoggerFactory;

import com.cloudera.com.fasterxml.jackson.core.type.TypeReference;
import com.cloudera.com.fasterxml.jackson.databind.ObjectMapper;
import com.nginx.log.bean.InsertType;
import com.nginx.log.bean.PropertiesType;
import com.nginx.log.bean.RichClick;
import com.nginx.log.service.HBaseService;
import com.nginx.log.service.HBaseServiceUseEnum;
import com.nginx.log.service.RichClickService;
import com.nginx.log.service.zookeeperService;
import com.nginx.log.util.ITask;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

public class LogBoltBackup extends BaseRichBolt implements ITask {
	// HBaseService hbaseService =null;
	// HBasePro hbasePro;
	HBaseServiceUseEnum hbaseService;
	OutputCollector collector;
	Date currentTime;
	zookeeperService zkService = new zookeeperService();
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(LogBoltBackup.class);
	static int thread = 1;

	@Override
	public void execute(Tuple tuple) {
		backup(tuple);
//	String line = tuple.getStringByField(LogSpout.DECLARE_FIELD);
		//System.out.println("---------" + line);
	}

	@Override
	public void prepare(Map map, TopologyContext context, OutputCollector collector) {
		logger.info("status : prepare");
		// zkService.initTest();
		switch (PropertiesType.SAVE_TYPE) {
		case HBASE:
			hbaseService = new HBaseServiceUseEnum();
			// hbaseService = new HBaseService();
			break;
		default:
			break;
		}
		this.collector = collector;
		currentTime = new Date();
	
	}

	// HashMap<UUID, String> pending = new HashMap<UUID, String>();
	List<RichClick> pending = new ArrayList<RichClick>();

	private void backup(Tuple tuple) {
		
		Task.addDelay(this);
		currentTime = new Date();

		// UUID key =
		// UUID.fromString(tuple.getStringByField(PropertiesType.MESSAGE_ID));
		RichClick richclick = null;
		switch (PropertiesType.SOURCE_TYPE) {
		case MYSQL:
			richclick = (RichClick) tuple.getValueByField(LogSpout.DECLARE_FIELD);
			break;
		default:
			String line = tuple.getStringByField(LogSpout.DECLARE_FIELD);
			richclick = RichClickService.convertJson(line);
			break;
		}

		if (pending.size() >= Integer.parseInt(zkService.getConf(PropertiesType.HBASE_BATCH_SIZE))) {
			switch (PropertiesType.SAVE_TYPE) {
			case HBASE:
				insert(InsertType.Bolt);
				break;
			case MySQL:
				break;
			default:
				break;
			}
		}
		if (richclick != null)
			pending.add(richclick);
		// collector.emit(new Values(line, key.toString()));
	}

	private void insert(InsertType type) {
		if (pending.size() > 0) {
			logger.info(String.format("Class: %s ,type: %s ,info: insert to HBase..size:%d", this.toString(),
					type.getValue(), pending.size()));
			try {
				synchronized (this) {
					if (hbaseService.insert(pending)) {
						pending.clear();
					}
				}
				logger.info(String.format("Class: %s ,type: %s ,info: insert done..", this.toString(), type.getValue()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			}
		} else {
			logger.info(String.format("Class: %s ,type: %s ,info:nothing to do..size:%d", this.toString(),
					type.getValue(), pending.size()));
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void task(Object sender) {
		// TODO Auto-generated method stub
		// logger.info("trigger the tick task..");
		insert(InsertType.Task);
	}

	@Override
	public Date getCurrentDate() {
		// TODO Auto-generated method stub
		return currentTime;
	}

	@Override
	public void restDate() {
		// TODO Auto-generated method stub
		currentTime = new Date();
	}

}
