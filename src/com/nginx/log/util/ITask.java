package com.nginx.log.util;

import java.util.Date;
import java.util.Timer;

public interface ITask {
	void task(Object sender);
	Date getCurrentDate();
	void restDate();
}
