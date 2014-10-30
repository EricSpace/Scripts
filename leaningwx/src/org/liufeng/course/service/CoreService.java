package org.liufeng.course.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.liufeng.course.message.resp.Article;
import org.liufeng.course.message.resp.NewsMessage;
import org.liufeng.course.message.resp.TextMessage;
import org.liufeng.course.util.MessageUtil;

import org.liufeng.weixin.connDB.GetCityCode;
import org.liufeng.weixin.connDB.connectDB;
import org.liufeng.weixin.pojo.Subscriber;
import org.liufeng.course.service.TodayInHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liufeng.course.test.FetchUrl;

/**
 * ���ķ�����
 * 
 * @author liufeng
 * @date 2013-07-25
 */
public class CoreService {
	
	private static Logger log = LoggerFactory.getLogger(CoreService.class);
	
	/**
	 * ����΢�ŷ���������
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;

		try {
			// xml�������
			
			Map<String, String> requestMap = MessageUtil.parseXml(request);
//
//			// ���ͷ��ʺţ�open_id��
			String fromUserName = requestMap.get("FromUserName");
//			// �����ʺ�
			String toUserName = requestMap.get("ToUserName");
//			// ��Ϣ����
			String msgType = requestMap.get("MsgType");
//			//�¼�����
			String eventType = requestMap.get("Event");
			
			//�¼�����
			if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)){
				String eventKey = requestMap.get("EventKey");
				if("subscribe".equals(eventType)){
					TextMessage tMessage = new TextMessage();
					tMessage.setToUserName(fromUserName);
					tMessage.setFromUserName(toUserName);
					tMessage.setCreateTime(new Date().getTime());
					tMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
					tMessage.setFuncFlag(0);
					
					StringBuffer conMsg = new StringBuffer(); 
					conMsg.append("��л����ע̩����ʦ����������ƽ̨�����ǽ��߳�Ϊ������");
					tMessage.setContent(conMsg.toString());					
					respMessage = MessageUtil.textMessageToXml(tMessage);		
				}				
				if("unsubscribe".equals(eventType)){
					//��ʵ��ȡ����ע��Ͳ��ܸ��û��ٷ�����Ϣ�ˣ�����Ĵ���û������
					TextMessage tMessage = new TextMessage();
					tMessage.setToUserName(fromUserName);
					tMessage.setFromUserName(toUserName);
					tMessage.setCreateTime(new Date().getTime());
					tMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
					tMessage.setFuncFlag(0);
					
					StringBuffer conMsg = new StringBuffer(); 
					conMsg.append("����ȡ����ע��΢�Ŷ��ĺţ���ӭ�ٴι�ע��������ı��������");
					conMsg.append(emoji(0x1F6B2)).append(emoji(0x1F60D));
					tMessage.setContent(conMsg.toString());					
					respMessage = MessageUtil.textMessageToXml(tMessage);		
				}
				if("CLICK".equals(eventType)){
					//��ʷ�ϵĽ���
					if("13".equals(eventKey)){			
						TextMessage tMessage1 = new TextMessage();
						tMessage1.setToUserName(fromUserName);
						tMessage1.setFromUserName(toUserName);
						tMessage1.setCreateTime(new Date().getTime());
						tMessage1.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
						tMessage1.setFuncFlag(0);
						
						try{
							String result = TodayInHistoryService.getTodayInHistoryInfo();
							tMessage1.setContent(result);
						}catch(Exception e){						
							tMessage1.setContent(e.getMessage());
						}
						
						respMessage = MessageUtil.textMessageToXml(tMessage1);
					}
					//��ȡ�û����ڳ��е�������Ϣ
					if("11".equals(eventKey)){			
						TextMessage tMessage = new TextMessage();
						tMessage.setToUserName(fromUserName);
						tMessage.setFromUserName(toUserName);
						tMessage.setCreateTime(new Date().getTime());
						tMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
						tMessage.setFuncFlag(0);
						
						StringBuffer conMsg = new StringBuffer(); 
						Subscriber subscriber = null;
						String temp = null;
						String cityCode = null;
						String cityName = null;
						try{
							subscriber = GetUserInfo.getUserInfo(fromUserName);
							cityName = subscriber.getCity();
							cityCode = GetCityCode.getCityCode(cityName);
							temp = GetWeatherInfo.getCityTemp(cityCode);
						}catch(Exception e){
							temp = e.getMessage();
						}

						conMsg.append(temp);
						tMessage.setContent(conMsg.toString());
						
						respMessage = MessageUtil.textMessageToXml(tMessage);	
					}
					//������������ϵ��ʽ
					if("24".equals(eventKey)){		
						NewsMessage newsMessage = new NewsMessage();
						newsMessage.setToUserName(fromUserName);
						newsMessage.setFromUserName(toUserName);
						newsMessage.setCreateTime(new Date().getTime());
						newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
						newsMessage.setFuncFlag(0);
						
						List<Article> articleList = new ArrayList<Article>();
						Article article = new Article();
						article.setTitle("�Ĵ�̩����ʦ������");
						article.setDescription("��ӭ����ע");
						article.setPicUrl("http://mmbiz.qpic.cn/mmbiz/Ptict2vtia7SKCyZdlWleuKsoicDlyzpAGWA66JQYdiaOsEARA7ZQ2tZhVDBNVUcicZQYMWzxhToC9WWIqn0BlUNR6g/0");
						article.setUrl("http://mp.weixin.qq.com/s?__biz=MzA4NjUyNTAxNw==&mid=200691814&idx=1&sn=56e8c6ab840a0165259017c36d7468bb#rd");
						articleList.add(article);
						// ����ͼ����Ϣ����
						newsMessage.setArticleCount(articleList.size());
						// ����ͼ����Ϣ������ͼ�ļ���
						newsMessage.setArticles(articleList);
						// ��ͼ����Ϣ����ת����xml�ַ���
						respMessage = MessageUtil.newsMessageToXml(newsMessage);						
					}
				}
				
			}
			
			//ͼƬ��Ϣ
			if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)){
				
				TextMessage tMessage = new TextMessage();
				tMessage.setToUserName(fromUserName);
				tMessage.setFromUserName(toUserName);
				tMessage.setCreateTime(new Date().getTime());
				tMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
				tMessage.setFuncFlag(0);
				
				StringBuffer conMsg = new StringBuffer(); 
				conMsg.append("�㷢�͵���ͼƬӴ���ٺ٣�");
				tMessage.setContent(conMsg.toString());
				
				respMessage = MessageUtil.textMessageToXml(tMessage);			
				
			}
			// �ı���Ϣ
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				// �����û����͵��ı���Ϣ����
				String content = requestMap.get("Content");
//				String content = "AA";
//				System.out.println("here1");

				// ����ͼ����Ϣ
				NewsMessage newsMessage = new NewsMessage();
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);

				List<Article> articleList = new ArrayList<Article>();
				// ��ͼ����Ϣ
				if ("1".equals(content)) {
					Article article = new Article();
					article.setTitle("���ռ��");
					article.setDescription("ѧ�������������Ի�");
					article.setPicUrl("http://mmbiz.qpic.cn/mmbiz/8xsFvSN3lwPsnQsdBm7Pqm4SCHgiacxL7nUFNicFwP9icSJic13ia5pbm6nWxAvlahES3U0o1MmsNpD8dfgPiacGGEBg/0");
					article.setUrl("http://mp.weixin.qq.com/s?__biz=MzA4NjUyNTAxNw==&mid=200618401&idx=1&sn=6583d29f3b18b54a22c144b009a7af97#rd");
					articleList.add(article);
					// ����ͼ����Ϣ����
					newsMessage.setArticleCount(articleList.size());
					// ����ͼ����Ϣ������ͼ�ļ���
					newsMessage.setArticles(articleList);
					// ��ͼ����Ϣ����ת����xml�ַ���
					respMessage = MessageUtil.newsMessageToXml(newsMessage);
				}
				// ��ͼ����Ϣ---����ͼƬ
				else if ("2".equals(content)) {
					Article article = new Article();
					article.setTitle("���ռ��");
					// ͼ����Ϣ�п���ʹ��QQ���顢���ű���
					article.setDescription("����ͼƬ");
					// ��ͼƬ��Ϊ��
					article.setPicUrl("");
					article.setUrl("http://mp.weixin.qq.com/s?__biz=MzA4NjUyNTAxNw==&mid=200618401&idx=1&sn=6583d29f3b18b54a22c144b009a7af97#rd");
					articleList.add(article);
					newsMessage.setArticleCount(articleList.size());
					newsMessage.setArticles(articleList);
					respMessage = MessageUtil.newsMessageToXml(newsMessage);
				}

				// 2048��Ϸ
				else if ("001".equals(content)) {
					Article article1 = new Article();
					article1.setTitle("2048С��Ϸ");
					article1.setDescription("��ս2048");
					// ��ͼƬ��Ϊ��
					article1.setPicUrl("http://img10.ptpcp.com/v2/thumb/jpg/YWQwYSwwLDAsNCwzLDEsLTEsMSw=/u/www.ptbus.com/uploads/allimg/1404/15/1624-1404151A254C3.jpg");
					article1.setUrl("http://go2048.com/?wechat");				

					articleList.add(article1);

					newsMessage.setArticleCount(articleList.size());
					newsMessage.setArticles(articleList);
					respMessage = MessageUtil.newsMessageToXml(newsMessage);
				}
				//��ʾ��ʷ�ϵĽ���
				else if("12".equals(content)){
	
					TextMessage tMessage1 = new TextMessage();
					tMessage1.setToUserName(fromUserName);
					tMessage1.setFromUserName(toUserName);
					tMessage1.setCreateTime(new Date().getTime());
					tMessage1.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
					tMessage1.setFuncFlag(0);
					
					try{
						String result = TodayInHistoryService.getTodayInHistoryInfo();
						tMessage1.setContent(result);
					}catch(Exception e){						
						tMessage1.setContent(e.getMessage());
					}
					
					respMessage = MessageUtil.textMessageToXml(tMessage1);						
				}		
				else if("7".equals(content)){

					TextMessage tMessage2 = new TextMessage();
					tMessage2.setToUserName(fromUserName);
					tMessage2.setFromUserName(toUserName);
					tMessage2.setCreateTime(new Date().getTime());
					tMessage2.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
					tMessage2.setFuncFlag(0);
					
					connectDB connDB = new connectDB(); 
					String names = connDB.getName();
					StringBuffer conMsg2 = new StringBuffer(); 
					conMsg2.append(names);
					tMessage2.setContent(conMsg2.toString());
					
					respMessage = MessageUtil.textMessageToXml(tMessage2);	
					
				}
				else if("TQ".equals(content.toUpperCase())){

					TextMessage tMessage2 = new TextMessage();
					tMessage2.setToUserName(fromUserName);
					tMessage2.setFromUserName(toUserName);
					tMessage2.setCreateTime(new Date().getTime());
					tMessage2.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
					tMessage2.setFuncFlag(0);
					
					StringBuffer conMsg2 = new StringBuffer(); 
					String temp = null;
					String cityCode = null;
					try{
						cityCode = GetCityCode.getCityCode("�ɶ�");
						temp = GetWeatherInfo.getCityTemp(cityCode);
					}catch(Exception e){
						temp = e.getMessage();
					}

					conMsg2.append(temp);
					tMessage2.setContent(conMsg2.toString());
					
					respMessage = MessageUtil.textMessageToXml(tMessage2);	
					
				}
				else if("CS".equals(content.toUpperCase())){

					TextMessage tMessage2 = new TextMessage();
					tMessage2.setToUserName(fromUserName);
					tMessage2.setFromUserName(toUserName);
					tMessage2.setCreateTime(new Date().getTime());
					tMessage2.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
					tMessage2.setFuncFlag(0);
					
					StringBuffer conMsg2 = new StringBuffer(); 
					String temp = null;
					@SuppressWarnings("unused")
					String cityCode = null;
					try{
						temp = FetchUrl.getRequest();
//						temp = GetWeatherInfo.getCityTemp(cityCode);
					}catch(Exception e){
						temp = e.getMessage();
					}

					conMsg2.append(temp);
					tMessage2.setContent(conMsg2.toString());
					
					respMessage = MessageUtil.textMessageToXml(tMessage2);	
					
				}
				else if("8".equals(content)){

					TextMessage tMessage3 = new TextMessage();
					tMessage3.setToUserName(fromUserName);
					tMessage3.setFromUserName(toUserName);
					tMessage3.setCreateTime(new Date().getTime());
					tMessage3.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
					tMessage3.setFuncFlag(0);
					
					StringBuffer conMsg3 = new StringBuffer(); 
					conMsg3.append("��ӭ����<a href=\"http://www.baidu.com\">�ٶ���ҳ</a>").append("\n");
					tMessage3.setContent(conMsg3.toString());		
							
					respMessage = MessageUtil.textMessageToXml(tMessage3);	
					
				}
				else if("9".equals(content)){

					TextMessage tMessage5 = new TextMessage();
					tMessage5.setToUserName(fromUserName);
					tMessage5.setFromUserName(toUserName);
					tMessage5.setCreateTime(new Date().getTime());
					tMessage5.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
					tMessage5.setFuncFlag(0);
					
					StringBuffer conMsg5 = new StringBuffer(); 
					conMsg5.append("��ӭ����<a href=\"http://connwx.aliapp.com/Jsp/login.jsp\">��¼ҳ��</a>").append("\n");
					tMessage5.setContent(conMsg5.toString());		
							
					respMessage = MessageUtil.textMessageToXml(tMessage5);	
					
				}
				else if("TASK".equals(content.toUpperCase())){

					TextMessage tMessage5 = new TextMessage();
					tMessage5.setToUserName(fromUserName);
					tMessage5.setFromUserName(toUserName);
					tMessage5.setCreateTime(new Date().getTime());
					tMessage5.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
					tMessage5.setFuncFlag(0);
					
					StringBuffer conMsg5 = new StringBuffer(); 
					conMsg5.append("��ӭ����<a href=\"http://development.taskmagr.divshot.io/\">����ҳ��</a>").append("\n");
					tMessage5.setContent(conMsg5.toString());		
							
					respMessage = MessageUtil.textMessageToXml(tMessage5);	
					
				}				
				else if("11".equals(content)){
//					String initialUrl = "<a href="+"\\https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8888888888888888&redirect_uri=http://mascot.duapp.com/oauth2.php?userid=OPENID&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect\\"+">��֤</a>";
//					String initialUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8888888888888888&redirect_uri=http://mascot.duapp.com/oauth2.php?userid=OPENID&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
//					String rdUrl = initialUrl.replace("OPENID", fromUserName);
					
					
					TextMessage tMessage5 = new TextMessage();
					tMessage5.setToUserName(fromUserName);
					tMessage5.setFromUserName(toUserName);
					tMessage5.setCreateTime(new Date().getTime());
					tMessage5.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
					tMessage5.setFuncFlag(0);
					
					StringBuffer conMsg5 = new StringBuffer(); 
					conMsg5.append("��ӭ����<a href=\"http://www.sczjls.cn/about.asp?tid=190/\">��Ƹҳ��</a>").append("\n");
//					conMsg5.append("��ӭ").append(rdUrl).append("\n");
					tMessage5.setContent(conMsg5.toString());		
							
					respMessage = MessageUtil.textMessageToXml(tMessage5);	
					
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();	
			throw new RuntimeException(e.getMessage());
		}

		return respMessage;
	}

	/**
	 * emoji����ת��(hex -> utf-16)
	 * 
	 * @param hexEmoji
	 * @return
	 */
	public static String emoji(int hexEmoji) {
		return String.valueOf(Character.toChars(hexEmoji));
	}
}