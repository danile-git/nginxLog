package com.nginx.log.bean;

import java.io.Serializable;

public class ZookeeperPro implements Serializable {
		private String zookeeper_quorum;
		private String zookeeper_path;
		private Integer session_time;
		public String getZookeeper_quorum() {
			return zookeeper_quorum;
		}
		public void setZookeeper_quorum(String zookeeper_quorum) {
			this.zookeeper_quorum = zookeeper_quorum;
		}
		public String getZookeeper_path() {
			return zookeeper_path;
		}
		public void setZookeeper_path(String zookeeper_path) {
			this.zookeeper_path = zookeeper_path;
		}
		public Integer getSession_time() {
			return session_time;
		}
		public void setSession_time(Integer session_time) {
			this.session_time = session_time;
		}
		
}
