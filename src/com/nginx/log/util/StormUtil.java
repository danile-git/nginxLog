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
	// NIMBUS����Ϣ
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
	 * ���������ж�topology�Ƿ�Alive
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
	 * ���������ж�topology�Ƿ����
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
	 * ����id�ж�topology�Ƿ����
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
	 * ����id�õ�topology
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
	 * �������ֵõ�topology
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
	 * ����name�õ�topology_id
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
	 * ȫ��topology
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
	 * ����ĳ�����˵��쳣��ջ
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
					// �õ�������Ϣ
					Set<String> errorKeySet = topologyInfo.get_errors().keySet();
					for (String errorKey : errorKeySet) {
						List<ErrorInfo> listErrorInfo = topologyInfo.get_errors().get(errorKey);
						for (ErrorInfo errorInfo : listErrorInfo) {
							// �����쳣��ʱ��
							long expTime = (long) errorInfo.get_error_time_secs() * 1000;
							// ���ڵ�ʱ��
							long now = System.currentTimeMillis();

							// ���ڻ�ȡ����ȫ���Ĵ����ջ�����ǿ�������һ����Χ����ȡָ����Χ�Ĵ��󣬿��������
							// �������5min����ô�Ͳ��ü�¼�ˣ���Ϊ5min���һ��
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
//			System.err.println(String.format("InvalidArgumentException δ���� topologyName ֵΪ :%s", getTopologyName()));
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
