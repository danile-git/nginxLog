package com.nginx.log.bean;

public enum GroupType {
	shuffleGrouping("shuffleGrouping");
	
	private String value;
	private GroupType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
