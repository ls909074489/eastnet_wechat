package com.eastnet.wechat.service;

import java.io.Writer;
import java.util.Date;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import com.eastnet.wechat.message.resp.MusicMessage;
import com.eastnet.wechat.message.resp.NewsMessage;
import com.eastnet.wechat.message.resp.TextMessage;
import com.eastnet.wechat.utils.DBCPConnection;
import com.eastnet.wechat.utils.MessageUtil;
import com.eastnet.wechat.utils.OpenDBConnection;
import com.eastnet.wechat.utils.OperatorUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 解耦，使控制层与业务逻辑层分离开来，主要处理请求，响应
 * @author pengsong
 * @date 2016.01.19
 */
public class EastnetService {
	private static OperatorUtil operatorUtil;
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		//默认返回的文本消息类容
		String respContent = "请求处理异常，请稍后尝试！";
		String fromUserName="";
		String toUserName ="";
		String msgType ="";
		try {
			//xml请求解析
			Map<String,String> requestMap = MessageUtil.pareXml(request);
			
			//发送方账号（open_id）
			fromUserName = requestMap.get("FromUserName");
			//公众账号
			toUserName = requestMap.get("ToUserName");
			//消息类型
			msgType = requestMap.get("MsgType");
//			String eventType = requestMap.get("Event");
			String fromContent=requestMap.get("Content");
			String userName="";
			if((MessageUtil.REQ_MESSSAGE_TYPE_EVENT).equals(msgType)){
				// 事件类型
				String eventType = requestMap.get("Event");
				if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					String eventKey = requestMap.get("EventKey");
					if ("stuInfoEdit".equals(eventKey)) {//个人信息修改
						respContent=new OperatorUtil().editStuInfo(fromUserName);
					}else if("stuInfoView".equals(eventKey)){
						respContent=new OperatorUtil().viewStuInfo(fromUserName);
					}else if("stuTravelView".equals(eventKey)){//行程查看
						respContent=new OperatorUtil().viewTravel(fromUserName);
					}else if("stuTravelAdd".equals(eventKey)){//行程添加
						respContent=new OperatorUtil().addTravel(fromUserName);
					}else if("stuTravelEdit".equals(eventKey)){//行程修改
						respContent=new OperatorUtil().editTravel(fromUserName);
					}else if("help".equals(eventKey)){//操作说明
						respContent="绑定账号:请回复  用户名绑定+用户名,例:用户名绑定fangw";
					}else if("callAdmin".equals(eventKey)){//呼叫管理员
						respContent="已通知管理员，稍后管理员会与您联系";
					}else if("suggestions".equals(eventKey)){//意见反馈
						respContent="意见反馈被点击";
					}else{
						respContent="请求失败";
					}
				}
			}
			//订阅
			String eventTypeSub = requestMap.get("Event");
			if((MessageUtil.EVENT_TYPE_SUBSCRIBE).equals(eventTypeSub)){
				respContent = "景程教育欢迎您的到来！ \n 回复\"用户名绑定\"+登录用户名  如:用户名绑定fangw  可完成账号绑定！\n 只有绑定账号后才可以实现接下来的操作";
			}
			//event
//			if(eventType.equals(MessageUtil.EVENT_TYPE_CLICK)){
//				String EventKey=requestMap.get("EventKey");
//				if("stuInfoEdit".equals(EventKey)){
//					respContent=new OperatorUtil().editTravel(fromUserName);
////					respMessage=("<xml><ToUserName><![CDATA["+requestMap.get("FromUserName")+
////							"]]></ToUserName>"+"<FromUserName><![CDATA["+requestMap.get("ToUserName")
////							+"]]></FromUserName><CreateTime>"+System.currentTimeMillis()+"</CreateTime><MsgType><![CDATA[event]]></MsgType><Content><![CDATA["+respContent+"]]></Content></xml>");
////					return respMessage;
//				}
//			}
			if(fromContent.contains("用户名绑定")){
				userName=fromContent.substring(5).trim();
				respContent=new OperatorUtil().bindAccount(fromUserName,userName);
			}
			if(fromContent.contains("解除绑定")){
				userName=fromContent.substring(4).trim();
				if("oS-GywW5Aljk6V5v1JGDiUAOMdX0".equals(fromUserName)){
					respContent=new OperatorUtil().unBindAccount(userName);
				}else{
					respContent="您不具备管理员权限";
				}
				
			}
			if("行程查看".equals(fromContent)){
				respContent=new OperatorUtil().viewTravel(fromUserName);
			}
			if("行程添加".equals(fromContent)){
				respContent=new OperatorUtil().addTravel(fromUserName);
			}
			if("行程修改".equals(fromContent)){
				respContent=new OperatorUtil().editTravel(fromUserName);
			}
			if("帮助".equals(fromContent)){
				respContent="绑定账号:请回复  用户名绑定+用户名,例:用户名绑定fangw\n行程查看:请回复  行程查看\n行程添加:请回复  行程添加\n行程修改:请回复  行程修改\n";
			}
			//回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(toUserName);
			textMessage.setFromUserName(fromUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);
			StringBuffer sb=new StringBuffer();
			//文本消息
			/*if(msgType.equals(MessageUtil.REQ_MESSSAGE_TYPE_TEXT)){
				Connection conn=new DBCPConnection().getConnection();
				if(conn==null){
					respContent="连接数据库失败";
				}else{
					int count =0;
					String sql="select * from crm_student_info";
			        PreparedStatement ps=conn.prepareStatement(sql);
			        ResultSet rs=ps.executeQuery();
		            while (rs.next()) {
		            	if(count>6){
		            		break;
		            	}
		                String name=rs.getString("exam_time");
		                sb.append(name).append("/n");
		                count++;
		            }
				}
				respContent=sb.toString();
				//respContent = new OpenDBConnection().selectData("select * from crm_student_info");
			}
			//图片消息
			else if(msgType.equals(MessageUtil.REQ_MESSSAGE_TYPE_IMAGE)){
				respContent = "您发送的是图片消息！";
			}
			//地理位置
			else if(msgType.equals(MessageUtil.REQ_MESSSAGE_TYPE_LOCATION)){
				respContent = "您发送的是地理位置消息！";
			}
			//链接消息
			else if(msgType.equals(MessageUtil.REQ_MESSSAGE_TYPE_LINK)){
				respContent = "您发送的是链接消息！";
			}
			//音频消息
			else if(msgType.equals(MessageUtil.REQ_MESSSAGE_TYPE_VOICE)){
				respContent = "您发送的是音频消息！";
			}
			//事件推送
			else if(msgType.equals(MessageUtil.REQ_MESSSAGE_TYPE_EVENT)){
				//事件类型
				String eventType = requestMap.get("Event");
				//订阅
				if(eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)){
					respContent = "谢谢关注！";
				}
				//取消订阅
				else if(eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)){
					//
					System.out.println("取消订阅");
				}
				else if(eventType.equals(MessageUtil.EVENT_TYPE_CLICK)){
					//自定义菜单消息处理
					System.out.println("自定义菜单消息处理");
				}
			}
			textMessage.setContent(respContent);*/
			respMessage=("<xml><ToUserName><![CDATA["+requestMap.get("FromUserName")+
					"]]></ToUserName>"+"<FromUserName><![CDATA["+requestMap.get("ToUserName")
					+"]]></FromUserName><CreateTime>"+System.currentTimeMillis()+"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA["+respContent+"]]></Content></xml>");
//			if("".equals(respContent)||(respContent==null)){
//				respMessage=("<xml><ToUserName><![CDATA["+requestMap.get("FromUserName")+
//						"]]></ToUserName>"+"<FromUserName><![CDATA["+requestMap.get("ToUserName")
//						+"]]></FromUserName><CreateTime>"+System.currentTimeMillis()+"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[查无此人]]></Content></xml>");
//			}else{
//				respMessage=("<xml><ToUserName><![CDATA["+requestMap.get("FromUserName")+
//						"]]></ToUserName>"+"<FromUserName><![CDATA["+requestMap.get("ToUserName")
//						+"]]></FromUserName><CreateTime>"+System.currentTimeMillis()+"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA["+respContent+"]]></Content></xml>");
//			}
			
			//respMessage = MessageUtil.textMessageToXml(textMessage);
		} catch (Exception e) {
			respMessage=("<xml><ToUserName><![CDATA["+fromUserName+
					"]]></ToUserName>"+"<FromUserName><![CDATA["+toUserName
					+"]]></FromUserName><CreateTime>"+System.currentTimeMillis()+"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA["+respContent+"]]></Content></xml>");
		}
		return respMessage;
	}
}
