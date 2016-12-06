package com.nginx.log.core;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.text.DefaultEditorKit.InsertBreakAction;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

import com.nginx.log.bean.HBasePro;
import com.nginx.log.bean.InsertType;
import com.nginx.log.bean.NginxJSON;
import com.nginx.log.bean.PropertiesType;
import com.nginx.log.service.HBaseService;
import com.nginx.log.service.zookeeperService;
import com.nginx.log.util.ITask;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class LogBoltBackup extends BaseRichBolt implements ITask {
	HBaseService hbaseService =null;
//	HBasePro hbasePro;
	OutputCollector collector;
	Date currentTime;
	zookeeperService zkService=new zookeeperService();
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(LogBoltBackup.class);

	@Override
	public void execute(Tuple tuple) {
		backup(tuple);
	}

	@Override
	public void prepare(Map map, TopologyContext context, OutputCollector collector) {
		logger.info("status : prepare");
		//zkService.initTest();
		switch (PropertiesType.SAVE_TYPE) {
		case HBASE:
			hbaseService = new HBaseService();
			break;
		default:
			break;
		}
		this.collector = collector;
		currentTime = new Date();
		Task.addDelay(this);
	}

	HashMap<UUID, String> pendingHashMap = new HashMap<UUID, String>();

	private void backup(Tuple tuple) {
		currentTime = new Date();
		String line = tuple.getStringByField(LogSpout.DECLARE_FIELD);
		UUID key = UUID.fromString(tuple.getStringByField(PropertiesType.MESSAGE_ID));
		if (pendingHashMap.size() >=Integer.parseInt(zkService.getConf(PropertiesType.HBASE_BATCH_SIZE))) {
			switch (PropertiesType.SAVE_TYPE) {
			case HBASE:
				insert(InsertType.Bolt);
				break;
			default:
				break;
			}
		}
		pendingHashMap.put(key, line);
		// collector.emit(new Values(line, key.toString()));
	}

	private void insert(InsertType type) {
		if (pendingHashMap.size() > 0) {
			logger.info(String.format("Class: %s ,type: %s ,info: insert to HBase..size:%d", this.toString(),
					type.getValue(), pendingHashMap.size()));
			try {
				synchronized (this) {
					if (hbaseService.insert_tmp(pendingHashMap)) {
						pendingHashMap.clear();
					}
				}
				logger.info(String.format("Class: %s ,type: %s ,info: insert done..", this.toString(),
						type.getValue()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			}
		} else {
			logger.info(String.format("Class: %s ,type: %s ,info:nothing to do..size:%d", this.toString(),
					type.getValue(), pendingHashMap.size()));
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void task(Object sender) {
		// TODO Auto-generated method stub
//		logger.info("trigger the tick task..");
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
