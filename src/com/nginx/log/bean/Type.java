package com.nginx.log.bean;

public enum Type {
	Integer("Integer"), String("String"),None("None");
	private String value;

	private Type(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
