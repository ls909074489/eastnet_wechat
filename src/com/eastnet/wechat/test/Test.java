package com.eastnet.wechat.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.eastnet.wechat.utils.DBCPConnection;
import com.eastnet.wechat.utils.OpenDBConnection;

public class Test {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		StringBuffer sb=new StringBuffer();
		String selectStuSql="select * from crm_student where user_id='fangw'";
		Connection connStu= new DBCPConnection().getConnection();
		PreparedStatement psStu=connStu.prepareStatement(selectStuSql);
		ResultSet rsStu=psStu.executeQuery();
		if(rsStu.next()){
			sb.append("用户名:").append(rsStu.getString("user_id")).append("\t").append("\n");
			sb.append("姓名:").append(rsStu.getString("stu_name")).append("\t").append("\n");
			if("0".equals(rsStu.getString("stu_sex"))){
				sb.append("性别:").append("男").append("\t").append("\n");
			}else{
				sb.append("性别:").append("女").append("\t").append("\n");
			}
			sb.append("电话:").append(rsStu.getString("tel")).append("\t").append("\n");
			sb.append("生源地:").append(rsStu.getString("origin_area")).append("\t").append("\n");
			sb.append("父母电话:").append(rsStu.getString("parent_tel")).append("\t").append("\n");
			sb.append("身份证号:").append(rsStu.getString("idno")).append("\t").append("\n");
			sb.append("准考证号:").append(rsStu.getString("ticket_num")).append("\t").append("\n");
			if("1".equals(rsStu.getString("is_company"))){
				sb.append("是否父母陪同:").append("是").append("\n");
			}else{
				sb.append("是否父母陪同:").append("否").append("\n");
			}
			System.out.println(sb.toString());
		}

	}

}
