package com.nginx.log.bean;

import java.util.Properties;

public class Kafka {
	private String groupId;
	private Properties properties;
	private String zookeeper;
	private String topic;
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	public String getZookeeper() {
		return zookeeper;
	}
	public void setZookeeper(String zookeeper) {
		this.zookeeper = zookeeper;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}

	
}
