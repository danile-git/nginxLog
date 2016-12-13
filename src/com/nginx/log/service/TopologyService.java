package com.nginx.log.service;

import com.nginx.log.bean.Booleans;
import com.nginx.log.bean.PropertiesType;
import com.nginx.log.bean.TopologyPro;

public class TopologyService {

	String[] propertys = {
			PropertiesType.STORM_SPOUT_SIZE,
			PropertiesType.STORM_BOLT_SIZE,
			PropertiesType.STORM_WORK_SIZE,
			PropertiesType.STORM_NUM_ACKERS, 
			PropertiesType.STORM_DISTRIBUTE,
			PropertiesType.STORM_SEEDS 
		};
	zookeeperService zkService = new zookeeperService();
	public TopologyPro init() {
		TopologyPro topologyPro = new TopologyPro();
		topologyPro.setStorm_seeds(zkService.getConf(propertys[5]));
		String distribute = zkService.getConf(propertys[4]).toLowerCase();
		Booleans booleans = distribute.equals(Booleans.False.getValue()) ? Booleans.False : distribute
				.equals(Booleans.True.getValue()) ? Booleans.True : Booleans.None;
		topologyPro.setStorm_distribute(booleans);
		topologyPro.setStorm_num_ackers(Integer.parseInt(zkService.getConf(propertys[3])));
		topologyPro.setStorm_work_size(Integer.parseInt(zkService.getConf(propertys[2])));
		topologyPro.setStorm_bolt_size(Integer.parseInt(zkService.getConf(propertys[1])));
		topologyPro.setStorm_spout_size(Integer.parseInt(zkService.getConf(propertys[0])));
		return topologyPro;
	}
}
