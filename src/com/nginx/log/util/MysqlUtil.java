package com.nginx.log.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MysqlUtil implements Serializable {
	private static final String DBDRIVER = "com.mysql.jdbc.Driver";
	// private static final String DBURL = "jdbc:mysql://master:3306/nginx";//
	// admin是你的数据库名
	private static final String DBURL = "jdbc:mysql://master1:3306/d_test_0918";// admin是你的数据库名
	private static final String DBUSER = "admin";
	private static final String DBPASS = "";
	private static final String TABLE_NAME = "nginx_log";
	private static Connection con = null;

	public ResultSet executeQuery(String sql) throws SQLException, UnsupportedEncodingException {
		Statement statement = connection().createStatement();
		return statement.executeQuery(sql);
	}

	public boolean executeUpdate(String sql) throws SQLException, UnsupportedEncodingException {
		Statement statement = connection().createStatement();
		boolean flg = statement.executeUpdate(sql) > 0 ? true : false;
		this.con.close();
		return flg;
	}

//	public void executeInsert() throws SQLException {
//		Connection conn = DriverManager.getConnection(DBURL + "?rewriteBatchedStatements=true", DBUSER, DBPASS);
//		conn.setAutoCommit(false);	
//		PreparedStatement pstmt = conn.prepareStatement("insert into loadtest (id, data) values (?, ?)");
//		pstmt.clearBatch();
//		for (int i = 1; i <= 1000; i++) {
//			pstmt.setInt(1, i);
//			pstmt.setString(2, "");
//			pstmt.addBatch();
//		}
//		pstmt.executeBatch();
//		conn.commit();
//		conn.close();
//	}

	private Connection connection() throws SQLException {
		if (this.con == null || this.con.isClosed()) {
			try {
				Class.forName(DBDRIVER);
				this.con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
				// con.setAutoCommit(false);
			} catch (Exception e) {
				this.con = null;
				System.out.println("连接数据库出现错误:" + e.getMessage());
			}
		}
		return this.con;

	}

}
