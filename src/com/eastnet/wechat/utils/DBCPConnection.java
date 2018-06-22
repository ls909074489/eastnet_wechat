package com.eastnet.wechat.utils;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
public class DBCPConnection {
	public Connection getConnection(){
		Connection conn=null;
		try {
	        BasicDataSource ds = new BasicDataSource();
	        ds.setUsername("root");
	        ds.setPassword("root");
	        ds.setUrl("jdbc:mysql://localhost:3306/artexam?autoReconnect=true&useUnicode=true&characterEncoding=utf8");
	        ds.setDriverClassName("com.mysql.jdbc.Driver");
	        
	        ds.setMaxActive(20);
	        ds.setMaxIdle(10);
	        ds.setInitialSize(10);
	        ds.setMinIdle(2);
	        ds.setMaxWait(1000);
	
	        conn=ds.getConnection();
	        return conn;
      } catch (Exception e) {
          e.printStackTrace();
          return null;
      }
	}
}
