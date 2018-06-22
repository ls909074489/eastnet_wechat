package com.eastnet.wechat.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eastnet.wechat.pojo.AccessToken;
import com.eastnet.wechat.pojo.Button;
import com.eastnet.wechat.pojo.CommonButton;
import com.eastnet.wechat.pojo.ComplexButton;
import com.eastnet.wechat.pojo.Menu;
import com.eastnet.wechat.pojo.ViewButton;
import com.eastnet.wechat.utils.WeixinUtil;

public class CreateMenuServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public CreateMenuServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 第三方用户唯一凭证  
        String appId = "wx5caef5720ded3a0b";  
        // 第三方用户唯一凭证密钥  
        String appSecret = "1f0bcb6a2e9f19db234eccd5a14cd910";  
  
        // 调用接口获取access_token  
        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);  
  
        if (null != at) {  
            // 调用接口创建菜单  
            int result = WeixinUtil.createMenu(getMenu(), at.getToken());  
  
            // 判断菜单创建结果  
            if (0 == result){
            	response.setContentType("text/html;charset=UTF-8");  
            	PrintWriter pw = response.getWriter();  
	            pw.println("菜单创建成功！");  
	            pw.flush();     
            }else{
            	response.setContentType("text/html;charset=UTF-8");  
            	PrintWriter pw = response.getWriter();  
	            pw.println("菜单创建失败，错误码：" + result);  
	            pw.flush();     
            }   
        }
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	/** 
     * 组装菜单数据 
     *  
     * @return 
     */  
    private static Menu getMenu() {  
        CommonButton btn11 = new CommonButton();  
        btn11.setName("个人信息查看");  
        btn11.setType("click");  
        btn11.setKey("stuInfoView");  
  
        CommonButton btn12 = new CommonButton();  
        btn12.setName("个人信息修改");  
        btn12.setType("click");  
        btn12.setKey("stuInfoEdit"); 
  
        CommonButton btn21 = new CommonButton();  
        btn21.setName("行程查看");  
        btn21.setType("click");  
        btn21.setKey("stuTravelView");  
  
        CommonButton btn22 = new CommonButton();  
        btn22.setName("行程添加");  
        btn22.setType("click");  
        btn22.setKey("stuTravelAdd");  
  
        CommonButton btn23 = new CommonButton();  
        btn23.setName("行程修改");  
        btn23.setType("click");  
        btn23.setKey("stuTravelEdit");  
  
        CommonButton btn31 = new CommonButton();  
        btn31.setName("操作说明");  
        btn31.setType("click");  
        btn31.setKey("help");  
  
        CommonButton btn32 = new CommonButton();  
        btn32.setName("呼叫管理员");  
        btn32.setType("click");  
        btn32.setKey("callAdmin");  
  
        CommonButton btn33 = new CommonButton();  
        btn33.setName("意见反馈");  
        btn33.setType("click");  
        btn33.setKey("suggestions");  
  
        ComplexButton mainBtn1 = new ComplexButton();  
        mainBtn1.setName("个人信息");  
        mainBtn1.setSub_button(new Button[] { btn11, btn12});  
  
        ComplexButton mainBtn2 = new ComplexButton();  
        mainBtn2.setName("行程");  
        mainBtn2.setSub_button(new Button[] { btn21, btn22, btn23});  
  
        ComplexButton mainBtn3 = new ComplexButton();  
        mainBtn3.setName("帮助");  
        mainBtn3.setSub_button(new Button[] { btn31, btn32, btn33 });  
  
        /** 
         * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项<br> 
         *  
         * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br> 
         * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：<br> 
         * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 }); 
         */  
        Menu menu = new Menu();  
        menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });  
  
        return menu;  
    }  
}
