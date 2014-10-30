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
 * �˵���������
 * 
 * @author liufeng
 * @date 2013-08-08
 */
public class MenuManager {
	private static Logger log = LoggerFactory.getLogger(MenuManager.class);

	public static void main(String[] args) {
		// �������û�Ψһƾ֤
		String appId = "wx35e20298e9252a7a";
		// �������û�Ψһƾ֤��Կ
		String appSecret = "d203aa92ab81ea5ea0641e399858f52d";

		// ���ýӿڻ�ȡaccess_token
		AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);
		String jsonMenu = JSONObject.fromObject(getMenu()).toString();
//		String j1 = jsonMenu.toString();
//		System.out.println("jsonMenu:"+jsonMenu);
//		System.out.println("*****************************");
//		System.out.println("j1:"+j1);


		if (null != at) {
			// ���ýӿڴ����˵�
			int result = WeixinUtil.createMenu(getMenu(), at.getToken());

			// �жϲ˵��������
			if (0 == result)
				log.info("�˵������ɹ���");
			else
				log.info("�˵�����ʧ�ܣ������룺" + result);
		}

	}

	/**
	 * ��װ�˵�����
	 * 
	 * @return
	 */
	private static Menu getMenu() {
		CommonButton btn11 = new CommonButton();
		btn11.setName("������ѯ");
		btn11.setType("click");
		btn11.setKey("11");

		CommonButton btn12 = new CommonButton();
		btn12.setName("������ѯ");
		btn12.setType("click");
		btn12.setKey("12");

		CommonButton btn13 = new CommonButton();
		btn13.setName("��ʷ�ϵĽ���");
		btn13.setType("click");
		btn13.setKey("13");
		
		ViewButton btn14 = new ViewButton();
		btn14.setName("��ȡ��ע�б�");
		btn14.setType("view");
//		btn14.setUrl("http://www.baidu.com/");
		btn14.setUrl("http://connwx.aliapp.com/Jsp/showAllSubscribers.jsp");
		
		
		CommonButton btn21 = new CommonButton();
		btn21.setName("̩���ſ�");
		btn21.setType("click");
		btn21.setKey("21");
		
		CommonButton btn22 = new CommonButton();
		btn22.setName("ҵ������");
		btn22.setType("click");
		btn22.setKey("22");
		
		CommonButton btn23 = new CommonButton();
		btn23.setName("�Ŷӷ��");
		btn23.setType("click");
		btn23.setKey("23");
		
		CommonButton btn24 = new CommonButton();
		btn24.setName("��ϵ����");
		btn24.setType("click");
		btn24.setKey("24");
		
		CommonButton btn31 = new CommonButton();
		btn31.setName("���ɽ��");
		btn31.setType("click");
		btn31.setKey("31");
		
		CommonButton btn32 = new CommonButton();
		btn32.setName("��������");
		btn32.setType("click");
		btn32.setKey("32");
		
		CommonButton btn33 = new CommonButton();
		btn33.setName("��ʦ�ּ�");
		btn33.setType("click");
		btn33.setKey("33");
		
		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("��������");
		mainBtn1.setSub_button(new CommonButton[] { btn11, btn12, btn13, btn14});

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("̩�����");
		mainBtn2.setSub_button(new CommonButton[] { btn21, btn22, btn23, btn24});

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("̩���ӵ�");
		mainBtn3.setSub_button(new CommonButton[] { btn31, btn32, btn33 });

		/**
		 * ���ǹ��ں�xiaoqrobotĿǰ�Ĳ˵��ṹ��ÿ��һ���˵����ж����˵���<br>
		 * 
		 * ��ĳ��һ���˵���û�ж����˵��������menu����ζ����أ�<br>
		 * ���磬������һ���˵����"��������"����ֱ����"��ĬЦ��"����ômenuӦ���������壺<br>
		 * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
		 */
		Menu menu = new Menu();
//		menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });
		menu.setButton(new Button[] { mainBtn2, mainBtn3, mainBtn1 });

		return menu;
	}
}