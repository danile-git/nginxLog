package com.nginx.log.util;

import java.util.UUID;


//import backtype.storm.timer.schedule_recurring.this__1647;
import kafka.javaapi.producer.Producer;

public class TestRunnable extends Thread {

	KAFKAUtil kafka;
	Producer<String, String> producer ;
	String topic;
	UUID msg;
  public TestRunnable(KAFKAUtil kafkaUtil,Producer<String, String> producer,String topic,UUID msg) {
	  this.kafka=kafkaUtil;
	  this.producer=producer;
	  this.topic=topic;
	  this.msg=msg;
	// TODO Auto-generated constructor stub
}
	@Override
	public void run() {	
			kafka.send(producer, topic, msg.toString());
//			try {
//				hBaseService.insert_tmp(msg, msg.toString());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		//	System.out.println(msg);
			stop();
	}

}
