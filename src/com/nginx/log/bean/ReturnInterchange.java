package com.nginx.log.bean;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.ResultScanner;

public class ReturnInterchange {
	public ReturnInterchange(ResultScanner resultScanner,HTablePool hTablePool){
		this.resultScanner=resultScanner;
		this.hTablePool=hTablePool;
	}
	private ResultScanner resultScanner;
	private HTablePool hTablePool;
	
	public ResultScanner getresultScanner() {
		return this.resultScanner;
	}
//	public void setresultScanner(ResultScanner resultScanner) {
//		this.resultScanner = resultScanner;
//	}
	public HTablePool gethTablePool() {
		return hTablePool;
	}
//	public void sethTablePool(HTablePool hTablePool) {
//		this.hTablePool = hTablePool;
//	}
}
