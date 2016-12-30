package com.nginx.log.bean;

import java.io.Serializable;

public enum RichClickEnum implements Serializable {
//	public static final byte[] QUALIFER_REMOTE_ADDR = "remote_addr".getBytes();
//
//	//public static final byte[] QUALIFER_TIME_LOCAL = "time_local".getBytes();
//	public static final byte[] QUALIFER_REQUEST_TIME = "timezone".getBytes();
//	public static final byte[] QUALIFER_REQUEST_TIMEZONE = "timezone".getBytes();
//	
//	
//	public static final byte[] QUALIFER_REQUEST_MSEC = "msec".getBytes();
//
////	public static final byte[] QUALIFER_REQUEST = "request".getBytes();
//	public static final byte[] QUALIFER_REQUEST_TYPE = "type".getBytes();
//	public static final byte[] QUALIFER_REQUEST_SOURCE = "request_source".getBytes();
//	public static final byte[] QUALIFER_REQUEST_HTTP_VERSION = "http_version".getBytes();
//	
//	public static final byte[] QUALIFER_REQUEST_STATUS = "status".getBytes();
//	
//	public static final byte[] QUALIFER_RESPONSE_BODY_BYTES_SENT = "body_bytes_sent".getBytes();
//	
//	public static final byte[] QUALIFER_HTTP_REFERER = "http_referer".getBytes();
//	//设备信息
//	public static final byte[] QUALIFER_HTTP_USER_AGENT = "http_user_agent".getBytes();
//	//响应时间
//	public static final byte[] QUALIFER_RESPONSE_TIME = "response_time".getBytes();
//
//	public static final byte[] QUALIFER_UPSTREAM_ADDR = "upstream_addr".getBytes();
//	GET /hm.//jpg?callback=nofunctionback&screen=360*640&model= Galaxy Nexus Build/ICL53F&system=Android 4.0.2
//&browser= AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30
//&x=206&y=2525&time=1478847210067&tag=start&element=IMG-0-2412 HTTP/1.0
	//{"body_bytes_sent":"175","request_time":"0.000",
	//"request":"GET \/richclick HTTP\/1.1","msec":"1476946713.992",
	//"time_local":"20\/Oct\/2016:14:58:33 +0800",
	//"http_user_agent":"curl\/7.19.7 (x86_64-redhat-linux-gnu) libcurl\/7.19.7 NSS\/3.14.0.0 zlib\/1.2.3 libidn\/1.18 libssh2\/1.4.2",
	//"remote_addr":"127.0.0.1","status":"404"}\
	/**
	 * 判断是否需要转换
	 * */
	COVERED("covered"),
	REMOTE_ADDR("remote_addr"),REQUEST_TIME("request_time"),REQUEST_TIMEZONE("request_timezone"),
	REQUEST_MSEC("msec"),REQUEST_TYPE("request_type"),REQUEST_SOURCE("request"),TIME_LOCAL("time_local"),
	REQUEST_HTTPVERSION("request_httpVersion"),REQUEST_STATUS("status"),RESPONSE_BODY_BYTES_SENT("body_bytes_sent"),
	HTTP_REFERER("http_referer"),HTTP_USER_AGENT("http_user_agent"),RESPONSE_TIME("response_time"),
	UPSTREAM_ADDR("upstream_addr"),URL_CALLBACK("CALLBACK"),URL_SCREEN("screen"),
	URL_MODEL("model"),URL_SYSTEM("system"),URL_BROWSER("browser")
	,URL_X("x"),URL_Y("y"),URL_TIME("time")
	,URL_TAG("tag"),URL_ELEMENT("element"),
	VID("vid"),SID("sid"),GOODS_NAME("goods_name")
	,SPECIAL("special"),REACHTIME("reachtime"),LEAVETIME("leavetime")
	,LASTMODTIME("lastmodtime"),CENTERIP("centerip"),URLS("urls")
	,DOP("dop"),DON("don"),VCNT("vcnt"),
	IDX("idx");
	;
	private String value;
	private RichClickEnum(String value){
		this.value=value;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.value;
	}
	
	
}
