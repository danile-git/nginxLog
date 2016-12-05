package com.nginx.log.bean;

public enum TopologyType {
	spout("spout"), bolt("bolt");
	
	private String value;
	private TopologyType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
