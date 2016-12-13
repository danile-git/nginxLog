package com.nginx.log.util;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import org.apache.storm.generated.ClusterSummary;
import org.apache.storm.generated.ErrorInfo;
import org.apache.storm.generated.TopologyInfo;
import org.apache.storm.generated.TopologySummary;
import org.apache.storm.utils.NimbusClient;
import org.apache.storm.utils.Utils;

public class StormUtil{
	// NIMBUS的信息
	public static String NIMBUS_HOST = "master2";
	public static String NIMBUS_PORT = "9092";
	static Map<String, String> conf;
	static {
		conf = Utils.readStormConfig();
		conf.put("nimbus.host", NIMBUS_HOST);
		conf.put("nimbus.port", NIMBUS_PORT);
	}

	public StormUtil() {

	}

	public StormUtil(String topologyName) {
		setTopologyName(topologyName);
	}

	/**
	 * 根据名字判断topology是否Alive
	 * */
	public boolean topologyIsAliveByName(String name) {
		NimbusClient client = NimbusClient.getConfiguredClient(conf);
		try {
			ClusterSummary summary = client.getClient().getClusterInfo();
			// summary.
			for (TopologySummary topologySummary : summary.get_topologies()) {
				if (topologySummary.get_name().equals(name)) {
					return true;
				}
			}
			return false;

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			client.close();
		}
	}

	/**
	 * 根据名字判断topology是否存在
	 * */
	public boolean topologyExistsByName(String name) {
		NimbusClient client = NimbusClient.getConfiguredClient(conf);
		try {
			ClusterSummary summary = client.getClient().getClusterInfo();
			for (TopologySummary topologySummary : summary.get_topologies()) {
				if (topologySummary.get_name().equals(name)) {
					return true;
				}
			}
			return false;

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			client.close();
		}
	}

	/**
	 * 根据id判断topology是否存在
	 * */
	public boolean topologyExistsById(String id) {
		NimbusClient client = NimbusClient.getConfiguredClient(conf);
		try {
			ClusterSummary summary = client.getClient().getClusterInfo();
			for (TopologySummary topologySummary : summary.get_topologies()) {
				if (topologySummary.get_id().equals(id)) {
					return true;
				}
			}
			return false;

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			client.close();
		}
	}

	/**
	 * 根据id得到topology
	 * */
	public TopologySummary getTopologyById(String id) {
		NimbusClient client = NimbusClient.getConfiguredClient(conf);
		try {
			ClusterSummary summary = client.getClient().getClusterInfo();
			for (TopologySummary topologySummary : summary.get_topologies()) {
				if (topologySummary.get_id().equals(id)) {
					return topologySummary;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			client.close();
		}
		return null;
	}

	/**
	 * 根据名字得到topology
	 * */
	public TopologySummary getTopologyByName(String name) {
		NimbusClient client = NimbusClient.getConfiguredClient(conf);
		try {
			ClusterSummary summary = client.getClient().getClusterInfo();
			for (TopologySummary topologySummary : summary.get_topologies()) {
				if (topologySummary.get_name().equals(name)) {
					return topologySummary;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			client.close();
		}
		return null;
	}

	/**
	 * 根据name得到topology_id
	 * */
	public String getTopologyIdByName(String name) {
		NimbusClient client = NimbusClient.getConfiguredClient(conf);
		try {
			ClusterSummary summary = client.getClient().getClusterInfo();
			for (TopologySummary topologySummary : summary.get_topologies()) {
				if (topologySummary.get_name().equals(name)) {
					return topologySummary.get_id();
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			client.close();
		}
		return null;
	}

	/**
	 * 全部topology
	 * */
	public List<TopologySummary> getTopologyList() {
		NimbusClient client = NimbusClient.getConfiguredClient(conf);
		try {
			return client.getClient().getClusterInfo().get_topologies();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			client.close();
		}

	}

	/**
	 * 返回某个拓扑的异常堆栈
	 * 
	 * @param topoName
	 * @return
	 */
	public String getTopoExpStackTrace(String topoName) {

		NimbusClient client = NimbusClient.getConfiguredClient(conf);
		StringBuilder error = new StringBuilder();
		try {

			ClusterSummary clusterSummary = client.getClient().getClusterInfo();
			List<TopologySummary> topoSummaryList = clusterSummary.get_topologies();
			for (TopologySummary topologySummary : topoSummaryList) {
				if (topologySummary.get_name().equals(topoName)) {
					TopologyInfo topologyInfo = client.getClient().getTopologyInfo(topologySummary.get_id());
					// 得到错误信息
					Set<String> errorKeySet = topologyInfo.get_errors().keySet();
					for (String errorKey : errorKeySet) {
						List<ErrorInfo> listErrorInfo = topologyInfo.get_errors().get(errorKey);
						for (ErrorInfo errorInfo : listErrorInfo) {
							// 发生异常的时间
							long expTime = (long) errorInfo.get_error_time_secs() * 1000;
							// 现在的时间
							long now = System.currentTimeMillis();

							// 由于获取的是全量的错误堆栈，我们可以设置一个范围来获取指定范围的错误，看情况而定
							// 如果超过5min，那么就不用记录了，因为5min检查一次
							if (now - expTime > 1000 * 60 * 5) {
								continue;
							}

							error.append(new Date(expTime) + "\n");
							error.append(errorInfo.get_error() + "\n");
						}
					}

					break;
				}
			}

			return error.toString().isEmpty() ? "none" : error.toString();
		} catch (Exception e) {
			return "-1";
		} finally {
			client.close();
		}
	}

//	@Override
//	public void task(Object sender) {
//		Timer timer = (Timer) sender;
//		if (getTopologyName() == null) {
//			System.err.println(String.format("InvalidArgumentException 未设置 topologyName 值为 :%s", getTopologyName()));
//			timer.cancel();
//		} else {
//			boolean isAlive = topologyIsAliveByName(getTopologyName());
//			System.out.println(isAlive);
//		}
//
//	}

	private String topologyName;

	public String getTopologyName() {
		return topologyName;
	}

	private void setTopologyName(String topologyName) {
		this.topologyName = topologyName;
	}
}
