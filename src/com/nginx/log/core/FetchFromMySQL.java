package com.nginx.log.core;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.nginx.log.util.MysqlUtil;

public class FetchFromMySQL implements Runnable {
	int indx = 0;
	int rows = 1000;
	static String sql = "SELECT  yt_visitlog.sid,yt_visitlog.vid,yt_visitlog.goods_name,yt_visitlog.special,yt_visitlog.reachtime,yt_visitlog.leavetime,yt_visitlog.lastmodtime,yt_visitlog.centerIp,yt_visitlog.urls,yt_visitlog.dop,yt_visitlog.don,yt_visitlog.vcnt,yt_visitlog.idx FROM `yt_visitlog`  limit %d,1000";
	static final MysqlUtil mysql = new MysqlUtil();
	public FetchFromMySQL(int index,int rows){	
		this.indx=index*rows;
		this.rows=rows;
	}
	@Override
	public void run() {
		ResultSet result=null;
		try {
			String sql2=String.format(sql, indx);
			result = mysql.executeQuery(String.format(sql, indx), rows);
			result.close();
			System.out.println(sql2);
			System.out.println(indx);
			
			// int i=1;
			// while(result.next()){
			// System.out.println(result.getString("sid"));
			// System.out.println(i++);
			// }
		} catch (Exception e) {
			try {
				if(result!=null)
				result.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}}
		
	
