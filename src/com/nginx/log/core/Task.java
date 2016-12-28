package com.nginx.log.core;

import java.util.HashMap;

import com.nginx.log.bean.PropertiesType;
import com.nginx.log.service.zookeeperService;
import com.nginx.log.util.DelayTimerUtil;
import com.nginx.log.util.ITask;
import com.nginx.log.util.TaskData;
import com.nginx.log.util.TimerUtil;

public class Task {
	// static List<ITask> batchTasks = new ArrayList<ITask>();
	// ms
	private static int delay = 0, tick = 1000;
	//s 间隔时间 触发事件
	
	public Task() {
		getTaskMap();
	}

	private static HashMap<ITask, TaskData> getTaskMap() {
		return TimerUtil.getTaskMap();
	}
	private static HashMap<ITask, TaskData> getDelayTaskMap() {
		return DelayTimerUtil.getTaskMap();
	}

	public void stop() {
		for (ITask key : getTaskMap().keySet()) {
			getTaskMap().get(key).getTimer().cancel();
		}
	}

	public void stop(ITask task) {
		getTaskMap().get(task).getTimer().cancel();
	}

//	public static void start(ITask task) {
//		TaskData taskData = getTaskMap().get(task);
//		remove(task);
//		newTask(task, taskData.getDelay(), taskData.getTick());
//	}

	public static void add(ITask task) {
		if (!getTaskMap().containsKey(task)) {
			newTask(task, delay, tick);
		}
	}
	
	public static void addDelay(ITask task) {
		if (!getDelayTaskMap().containsKey(task)) {
			DelayTimerUtil delayTimerUtil=new DelayTimerUtil();
			zookeeperService zkService=new zookeeperService();
			delayTimerUtil.tick(Integer.parseInt(zkService.getConf(PropertiesType.TASK_BATCH_TIME)), task);
		}
	}

	public static void add(ITask task, int delay, int tick) {
		if (!getTaskMap().containsKey(task)) {
			newTask(task, delay, tick);
		}
	}

	private static void newTask(ITask task, int delay, int tick) {
		TimerUtil timerUtil = new TimerUtil();
		timerUtil.tick(delay, tick, task);
	}

	public static void remove(ITask task) {
		if (getTaskMap().containsKey(task)) {
			getTaskMap().get(task).getTimer().cancel();
			getTaskMap().remove(task);
		}
	}
	
	public static void removeDelay(ITask task) {
		if (getDelayTaskMap().containsKey(task)) {
			getDelayTaskMap().get(task).getTimer().cancel();
			getDelayTaskMap().remove(task);
		}
	}

	public void clear() {
		stop();
		getTaskMap().clear();
	}
}
