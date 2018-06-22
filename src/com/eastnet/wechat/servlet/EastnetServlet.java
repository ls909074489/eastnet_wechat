package com.eastnet.wechat.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.eastnet.wechat.service.EastnetService;
import com.eastnet.wechat.utils.SignUtil;
import com.sun.org.apache.regexp.internal.RE;




public class EastnetServlet extends HttpServlet {

	public EastnetServlet() {
		super();
	}


	public void destroy() {
		super.destroy(); 
		// Put your code here
	}
	/**
	 * 确认请求来自于微信服务器
	 */

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			System.out.println("接口测试开始！！！");
			//微信加密签名
			String signature = request.getParameter("signature");
			//时间戳
			String timestamp = request.getParameter("timestamp");
			//随机数
			String nonce = request.getParameter("nonce");
			//随机字符串
			String echostr = request.getParameter("echostr");
			
			PrintWriter out = response.getWriter();
			//通过校验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
			if(SignUtil.checkSignature(signature,timestamp,nonce)){
				out.print(echostr);
			}
			out.close();
			out = null;
//			response.encodeRedirectURL("success.jsp");
			
		
	}
	/**
	 * 处理微信服务器发来的消息
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//消息的接受、处理、响应
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		//调用核心业务类型接受消息、处理消息
		String respMessage = EastnetService.processRequest(request);
		
		//响应消息
		PrintWriter out = response.getWriter();
		out.print(respMessage);
		out.close();
		
		
	}

	public void init() throws ServletException {
		// Put your code here
	}
	
}
