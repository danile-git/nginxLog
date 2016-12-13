package com.nginx.log.core;

import java.util.Arrays;

import org.slf4j.*;

import com.nginx.log.bean.*;
import com.nginx.log.service.TopologyService;
import com.nginx.log.service.zookeeperService;

 


import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;

public class FlumeKafkaTopology {
	private final static Logger logger = LoggerFactory.getLogger(FlumeKafkaTopology.class);

	public static void main(String[] _arguments) throws Exception {

		try {
			zookeeperService zkService = new zookeeperService();
			zkService.init();
			zkService._toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return;
			// TODO: handle exception
		}
		TopologyBuilder topologyBuilder = new TopologyBuilder();
		TopologyPro topologyPro = new TopologyService().init();
		topologyBuilder.setSpout(PropertiesType.KAFKA_SPOUT_ID, new LogSpout(), topologyPro.getStorm_spout_size());
		topologyBuilder.setBolt(PropertiesType.LOGBACKUPSAVE_ID, new LogBoltBackup(), topologyPro.getStorm_bolt_size())
				.shuffleGrouping(PropertiesType.KAFKA_SPOUT_ID);
		StormTopology stormTopology = topologyBuilder.createTopology();
		final Config conf = new Config();
		conf.setNumAckers(topologyPro.getStorm_num_ackers());
		switch (topologyPro.getStorm_distribute()) {
		case True:
			conf.put(Config.TOPOLOGY_CLASSPATH, PropertiesType.FLUMEKAFKATOPOLOGY);// 在监控中，掉线提交的方法入口
			conf.put(Config.NIMBUS_SEEDS,Arrays.asList(topologyPro.getStorm_seeds().split(",")));
			conf.setNumWorkers(topologyPro.getStorm_work_size());
			StormSubmitter.submitTopologyWithProgressBar(PropertiesType.FLUMEKAFKATOPOLOGY, conf, stormTopology);
			break;
		case False:
			conf.setMaxTaskParallelism(3);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology(PropertiesType.FLUMEKAFKATOPOLOGY, conf, stormTopology);
			break;
		default:
			logger.error("hava no setting the storm.distribute type");
			break;
		}
	}

}
