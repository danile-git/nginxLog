package com.nginx.log.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimerUtil {
	private static HashMap<ITask, TaskData> timerTaskMap = new HashMap<ITask, TaskData>();

	public static HashMap<ITask, TaskData> getTaskMap() {
		return timerTaskMap;
	}

	/**
	 * 定时任务
	 * */
	public void tick(int delay, int tick, final ITask task) {

		final Timer timer = new Timer();
		timerTaskMap.put(task, new TaskData(delay, tick, timer));
		timer.schedule(new TimerTask() {
			public void run() {
			//	Long second = (new Date().getTime()-task.getCurrentDate().getTime()) / 1000;// 秒
				//System.out.println(second);
			//	if (second >= seconds) {
					task.task(timer);
			//		task.restDate();
			//	}
			}
		}, delay, tick);// 设定指定的时间delaytime

	}
}
