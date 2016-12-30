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
	// admin是你的数据库名?relaxAutoCommit=true&zeroDateTimeBehavior=convertToNull
	//private static String HOST = "localhost";
	private static String HOST="master1";
	private String DBURL = "jdbc:mysql://%s:3306/%s?relaxAutoCommit=true&zeroDateTimeBehavior=convertToNull";// admin是你的数据库名
	private static final String DBUSER = "root";
	private static final String DBPASS = "";
	private static final String TABLE_NAME = "yt_visitlog";
	private Connection con = null;
	private String database_name = "ddd";

	public MysqlUtil() {
		// setDatabase_name("dddd");
	}

	public MysqlUtil(String databaseName) {
		setDatabase_URL(databaseName);
	}

	public String getDatabase_URL() {
		return String.format(DBURL, HOST, database_name);
		// return url;
	}

	private void setDatabase_URL(String database_name) {
		this.database_name = database_name;
	}

	public ResultSet executeQuery(String sql, int fetchSize) throws SQLException, UnsupportedEncodingException {
		Statement statement = connection().createStatement();
		statement.setFetchSize(fetchSize);
		return statement.executeQuery(sql);
	}
	public ResultSet executeQuery(String sql) throws SQLException, UnsupportedEncodingException {
		Statement statement = connection().createStatement();
		statement.setFetchSize(1000);
		return statement.executeQuery(sql);
	}
	public boolean executeUpdate(String sql) throws SQLException, UnsupportedEncodingException {
		Statement statement = connection().createStatement();
		boolean flg = statement.executeUpdate(sql) > 0 ? true : false;
		close();
		return flg;
	}
	public boolean executeUpdateWithoutClose(String sql) throws SQLException, UnsupportedEncodingException {
		Statement statement = connection().createStatement();
		boolean flg = statement.executeUpdate(sql) > 0 ? true : false;
		return flg;
	}
	public void close(){
		try {
			if(this.con!=null)
			this.con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// public void executeInsert() throws SQLException {
	// Connection conn = DriverManager.getConnection(DBURL +
	// "?rewriteBatchedStatements=true", DBUSER, DBPASS);
	// conn.setAutoCommit(false);
	// PreparedStatement pstmt =
	// conn.prepareStatement("insert into loadtest (id, data) values (?, ?)");
	// pstmt.clearBatch();
	// for (int i = 1; i <= 1000; i++) {
	// pstmt.setInt(1, i);
	// pstmt.setString(2, "");
	// pstmt.addBatch();
	// }
	// pstmt.executeBatch();
	// conn.commit();
	// conn.close();
	// }

	private Connection connection() throws SQLException {
		if (this.con == null || this.con.isClosed()) {
			try {
				Class.forName(DBDRIVER);
				this.con = DriverManager.getConnection(this.getDatabase_URL(), DBUSER, DBPASS);
				// con.setAutoCommit(false);
			} catch (Exception e) {
				this.con = null;
				System.out.println("连接数据库出现错误:" + e.getMessage());
			}
		}
		return this.con;

	}

}
