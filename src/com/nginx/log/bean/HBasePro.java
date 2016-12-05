package com.nginx.log.bean;

public class HBasePro {
	//hbase 
	//public static final String HBASE_BATCH_SIZE = "hbase.batch.size";
	private int hbase_batch_size;
	private int hbase_batch_time;
	public int getHbase_batch_size() {
		return hbase_batch_size;
	}

	public void setHbase_batch_size(int hbase_batch_size) {
		this.hbase_batch_size = hbase_batch_size;
	}

	public int getHbase_batch_time() {
		return hbase_batch_time;
	}

	public void setHbase_batch_time(int hbase_batch_time) {
		this.hbase_batch_time = hbase_batch_time;
	}
	
}
