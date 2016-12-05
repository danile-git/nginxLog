package com.nginx.log.util;

import java.util.Timer;

public class TaskData {
	private int delay;
	private int tick;
	private Timer timer;
	
	public TaskData(){
		
	}
	public TaskData(int delay,int tick,Timer timer){
		this.delay=delay;
		this.tick=tick;
		this.timer=timer;
	}
	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getTick() {
		return tick;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}


}
