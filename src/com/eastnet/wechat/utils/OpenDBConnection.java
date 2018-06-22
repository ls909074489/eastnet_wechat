package com.eastnet.wechat.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OpenDBConnection {
	/**
	 * 用于开启数据库连接
	 */
	public Connection OpenConnection(){
		Connection conn = null;
		try{
			Class.forName("com.mysql.jdbc.Driver") ;
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/数据库name", "username", "pwd");
			return conn;
			}catch(Exception e){
				e.printStackTrace();
				}
		return null ;
	}
	/**
	 * 用于释放数据库资源
	 * @param conn :表示数据库的链接
	 * @param stmt :表示sql语句执行的对象
	 * @param rst  :表示查询的结果集
	 */
	public void close(Connection conn,Statement stmt,ResultSet rst){
		if(rst!=null){
			try {
				rst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				rst=null;
			}
		}
		if(stmt != null){
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				stmt=null;
			}
		}
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				conn=null;
			}
		}
	}
	/**
	 * 用于查询数据
	 */
	public String selectData(String sql){
		int count=0;
		String result="";
		Connection conn = OpenConnection();
		if(conn==null){
			return "连接数据库失败";
		}
		StringBuffer sb=new StringBuffer();
		try {  
            Statement stmt = conn.createStatement();  
            ResultSet rs = stmt.executeQuery(sql);  
            while(rs.next()) {  
            	if(count>10){
            		break;
            	}
                //String name = rs.getString("user_id");  
                //String examTime = rs.getString("exam_time");  
                String examSchool = rs.getString("exam_school");  
                sb.append(examSchool);
                count++;
            }  
            result=sb.toString();  
        } catch (SQLException e) {  
        	result="无法查询";  
        }finally{
        	conn=null;  
        }
		return result;  
	}
	}
