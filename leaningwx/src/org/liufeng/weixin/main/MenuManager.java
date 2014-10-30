package org.liufeng.weixin.main;

import net.sf.json.JSONObject;

import org.liufeng.weixin.pojo.AccessToken;
import org.liufeng.weixin.pojo.Button;
import org.liufeng.weixin.pojo.CommonButton;
import org.liufeng.weixin.pojo.ComplexButton;
import org.liufeng.weixin.pojo.Menu;
import org.liufeng.weixin.pojo.ViewButton;
import org.liufeng.weixin.util.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 菜单管理器类
 * 
 * @author liufeng
 * @date 2013-08-08
 */
public class MenuManager {
	private static Logger log = LoggerFactory.getLogger(MenuManager.class);

	public static void main(String[] args) {
		// 第三方用户唯一凭证
		String appId = "wx35e20298e9252a7a";
		// 第三方用户唯一凭证密钥
		String appSecret = "d203aa92ab81ea5ea0641e399858f52d";

		// 调用接口获取access_token
		AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);
		String jsonMenu = JSONObject.fromObject(getMenu()).toString();
//		String j1 = jsonMenu.toString();
//		System.out.println("jsonMenu:"+jsonMenu);
//		System.out.println("*****************************");
//		System.out.println("j1:"+j1);


		if (null != at) {
			// 调用接口创建菜单
			int result = WeixinUtil.createMenu(getMenu(), at.getToken());

			// 判断菜单创建结果
			if (0 == result)
				log.info("菜单创建成功！");
			else
				log.info("菜单创建失败，错误码：" + result);
		}

	}

	/**
	 * 组装菜单数据
	 * 
	 * @return
	 */
	private static Menu getMenu() {
		CommonButton btn11 = new CommonButton();
		btn11.setName("天气查询");
		btn11.setType("click");
		btn11.setKey("11");

		CommonButton btn12 = new CommonButton();
		btn12.setName("公交查询");
		btn12.setType("click");
		btn12.setKey("12");

		CommonButton btn13 = new CommonButton();
		btn13.setName("历史上的今天");
		btn13.setType("click");
		btn13.setKey("13");
		
		ViewButton btn14 = new ViewButton();
		btn14.setName("获取关注列表");
		btn14.setType("view");
//		btn14.setUrl("http://www.baidu.com/");
		btn14.setUrl("http://connwx.aliapp.com/Jsp/showAllSubscribers.jsp");
		
		
		CommonButton btn21 = new CommonButton();
		btn21.setName("泰常概况");
		btn21.setType("click");
		btn21.setKey("21");
		
		CommonButton btn22 = new CommonButton();
		btn22.setName("业绩荣誉");
		btn22.setType("click");
		btn22.setKey("22");
		
		CommonButton btn23 = new CommonButton();
		btn23.setName("团队风采");
		btn23.setType("click");
		btn23.setKey("23");
		
		CommonButton btn24 = new CommonButton();
		btn24.setName("联系我们");
		btn24.setType("click");
		btn24.setKey("24");
		
		CommonButton btn31 = new CommonButton();
		btn31.setName("法律解读");
		btn31.setType("click");
		btn31.setKey("31");
		
		CommonButton btn32 = new CommonButton();
		btn32.setName("案例分析");
		btn32.setType("click");
		btn32.setKey("32");
		
		CommonButton btn33 = new CommonButton();
		btn33.setName("律师手记");
		btn33.setType("click");
		btn33.setKey("33");
		
		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("生活助手");
		mainBtn1.setSub_button(new CommonButton[] { btn11, btn12, btn13, btn14});

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("泰常简介");
		mainBtn2.setSub_button(new CommonButton[] { btn21, btn22, btn23, btn24});

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("泰常视点");
		mainBtn3.setSub_button(new CommonButton[] { btn31, btn32, btn33 });

		/**
		 * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项<br>
		 * 
		 * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br>
		 * 比如，第三个一级菜单项不是"更多体验"，而直接是"幽默笑话"，那么menu应该这样定义：<br>
		 * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
		 */
		Menu menu = new Menu();
//		menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });
		menu.setButton(new Button[] { mainBtn2, mainBtn3, mainBtn1 });

		return menu;
	}
}