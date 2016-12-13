package com.nginx.log.bean;

public class TopologyPro {
	private int storm_spout_size;
	private int storm_bolt_size;
	private int storm_work_size;
	private int storm_num_ackers;
	private Booleans storm_distribute;
	private String storm_seeds;
	
	public int getStorm_spout_size() {
		return storm_spout_size;
	}
	public void setStorm_spout_size(int storm_spout_size) {
		this.storm_spout_size = storm_spout_size;
	}
	public int getStorm_bolt_size() {
		return storm_bolt_size;
	}
	public void setStorm_bolt_size(int storm_bolt_size) {
		this.storm_bolt_size = storm_bolt_size;
	}
	public int getStorm_work_size() {
		return storm_work_size;
	}
	public void setStorm_work_size(int storm_work_size) {
		this.storm_work_size = storm_work_size;
	}
	public int getStorm_num_ackers() {
		return storm_num_ackers;
	}
	public void setStorm_num_ackers(int storm_num_ackers) {
		this.storm_num_ackers = storm_num_ackers;
	}
	public String getStorm_seeds() {
		return storm_seeds;
	}
	public void setStorm_seeds(String storm_seeds) {
		this.storm_seeds = storm_seeds;
	}
	public Booleans getStorm_distribute() {
		return storm_distribute;
	}
	public void setStorm_distribute(Booleans storm_distribute) {
		this.storm_distribute = storm_distribute;
	}
	
}
