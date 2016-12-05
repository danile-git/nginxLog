package com.nginx.log.bean;

public enum InsertType {
	Task("task"), Bolt("enough");
	private String value;

	private InsertType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
