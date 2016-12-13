package com.nginx.log.core;

import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.nginx.log.bean.NginxJSON;
import com.nginx.log.bean.PropertiesType;
import com.nginx.log.service.HBaseService;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

public class LogBoltSave extends BaseRichBolt {

	//static Logger log = Logger.getLogger(LogBoltSave.class);
	HBaseService hbaseService = new HBaseService();;
	OutputCollector collector;
	@Override
	public void execute(Tuple tuple) {
		Save(tuple);
	}

	@Override
	public void prepare(Map map, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	private void Save(Tuple tuple) {
		System.out.println("save..");
		try {
			String line = tuple.getStringByField(LogSpout.DECLARE_FIELD);
			NginxJSON nginxJSON = new NginxJSON();
			JSONObject jObject = JSONObject.fromObject(line);
			nginxJSON = (NginxJSON) JSONObject.toBean(jObject, NginxJSON.class);
			nginxJSON.setOriginal(line);		
			switch (PropertiesType.SAVE_TYPE) {
			case HDFS:
				break;
			case MySQL:
				break;
			case HBASE:
				hbaseService.insert(nginxJSON);
				// // 删除同步库内的备份
				String lineId = tuple.getStringByField(PropertiesType.MESSAGE_ID);
				hbaseService.delete_tmp(lineId);
				break;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
	//	log.debug(e.getMessage());
		}

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub

	}

}
