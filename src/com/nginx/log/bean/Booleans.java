package com.nginx.log.bean;

public enum Booleans {
	False("false"), True("true"), None("none");

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.value;
	}

	private String value;

	private Booleans(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
