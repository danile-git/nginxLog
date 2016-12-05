package com.nginx.log.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import parquet.pig.summary.Summary.Final;

public class DelayTimerUtil {

private	static HashMap<ITask, TaskData> delayTimerTaskMap = new HashMap<ITask, TaskData>();

	public static HashMap<ITask, TaskData> getTaskMap() {
		return delayTimerTaskMap;
	}

	/**
	 * 定时任务
	 * */
	public void tick(final int seconds, final ITask task) {

		final Timer timer = new Timer();
		delayTimerTaskMap.put(task, new TaskData(0, 1000, timer));
		timer.schedule(new TimerTask() {
			public void run() {
				Long second = (new Date().getTime()-task.getCurrentDate().getTime()) / 1000;// 秒
				//System.out.println(second);
				if (second >= seconds) {
					task.task(timer);
					task.restDate();
				}
			}
		}, 0, 1000);// 设定指定的时间delaytime

	}
}
