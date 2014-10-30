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
 * 核心服务类
 * 
 * @author liufeng
 * @date 2013-07-25
 */
public class CoreService {
	
	private static Logger log = LoggerFactory.getLogger(CoreService.class);
	
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;

		try {
			// xml请求解析
			
			Map<String, String> requestMap = MessageUtil.parseXml(request);
//
//			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
//			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
//			// 消息类型
			String msgType = requestMap.get("MsgType");
//			//事件类型
			String eventType = requestMap.get("Event");
			
			//事件类型
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
					conMsg.append("感谢您关注泰常律师事务所公众平台，我们将竭诚为您服务");
					tMessage.setContent(conMsg.toString());					
					respMessage = MessageUtil.textMessageToXml(tMessage);		
				}				
				if("unsubscribe".equals(eventType)){
					//事实上取消关注后就不能给用户再发送消息了，下面的代码没有意义
					TextMessage tMessage = new TextMessage();
					tMessage.setToUserName(fromUserName);
					tMessage.setFromUserName(toUserName);
					tMessage.setCreateTime(new Date().getTime());
					tMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
					tMessage.setFuncFlag(0);
					
					StringBuffer conMsg = new StringBuffer(); 
					conMsg.append("您已取消关注本微信订阅号，欢迎再次关注并提出您的宝贵意见！");
					conMsg.append(emoji(0x1F6B2)).append(emoji(0x1F60D));
					tMessage.setContent(conMsg.toString());					
					respMessage = MessageUtil.textMessageToXml(tMessage);		
				}
				if("CLICK".equals(eventType)){
					//历史上的今天
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
					//获取用户所在城市的天气信息
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
					//发送事务所联系方式
					if("24".equals(eventKey)){		
						NewsMessage newsMessage = new NewsMessage();
						newsMessage.setToUserName(fromUserName);
						newsMessage.setFromUserName(toUserName);
						newsMessage.setCreateTime(new Date().getTime());
						newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
						newsMessage.setFuncFlag(0);
						
						List<Article> articleList = new ArrayList<Article>();
						Article article = new Article();
						article.setTitle("四川泰常律师事务所");
						article.setDescription("欢迎您关注");
						article.setPicUrl("http://mmbiz.qpic.cn/mmbiz/Ptict2vtia7SKCyZdlWleuKsoicDlyzpAGWA66JQYdiaOsEARA7ZQ2tZhVDBNVUcicZQYMWzxhToC9WWIqn0BlUNR6g/0");
						article.setUrl("http://mp.weixin.qq.com/s?__biz=MzA4NjUyNTAxNw==&mid=200691814&idx=1&sn=56e8c6ab840a0165259017c36d7468bb#rd");
						articleList.add(article);
						// 设置图文消息个数
						newsMessage.setArticleCount(articleList.size());
						// 设置图文消息包含的图文集合
						newsMessage.setArticles(articleList);
						// 将图文消息对象转换成xml字符串
						respMessage = MessageUtil.newsMessageToXml(newsMessage);						
					}
				}
				
			}
			
			//图片消息
			if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)){
				
				TextMessage tMessage = new TextMessage();
				tMessage.setToUserName(fromUserName);
				tMessage.setFromUserName(toUserName);
				tMessage.setCreateTime(new Date().getTime());
				tMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
				tMessage.setFuncFlag(0);
				
				StringBuffer conMsg = new StringBuffer(); 
				conMsg.append("你发送的是图片哟，嘿嘿！");
				tMessage.setContent(conMsg.toString());
				
				respMessage = MessageUtil.textMessageToXml(tMessage);			
				
			}
			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				// 接收用户发送的文本消息内容
				String content = requestMap.get("Content");
//				String content = "AA";
//				System.out.println("here1");

				// 创建图文消息
				NewsMessage newsMessage = new NewsMessage();
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);

				List<Article> articleList = new ArrayList<Article>();
				// 单图文消息
				if ("1".equals(content)) {
					Article article = new Article();
					article.setTitle("香港占中");
					article.setDescription("学生代表与征服对话");
					article.setPicUrl("http://mmbiz.qpic.cn/mmbiz/8xsFvSN3lwPsnQsdBm7Pqm4SCHgiacxL7nUFNicFwP9icSJic13ia5pbm6nWxAvlahES3U0o1MmsNpD8dfgPiacGGEBg/0");
					article.setUrl("http://mp.weixin.qq.com/s?__biz=MzA4NjUyNTAxNw==&mid=200618401&idx=1&sn=6583d29f3b18b54a22c144b009a7af97#rd");
					articleList.add(article);
					// 设置图文消息个数
					newsMessage.setArticleCount(articleList.size());
					// 设置图文消息包含的图文集合
					newsMessage.setArticles(articleList);
					// 将图文消息对象转换成xml字符串
					respMessage = MessageUtil.newsMessageToXml(newsMessage);
				}
				// 单图文消息---不含图片
				else if ("2".equals(content)) {
					Article article = new Article();
					article.setTitle("香港占中");
					// 图文消息中可以使用QQ表情、符号表情
					article.setDescription("不带图片");
					// 将图片置为空
					article.setPicUrl("");
					article.setUrl("http://mp.weixin.qq.com/s?__biz=MzA4NjUyNTAxNw==&mid=200618401&idx=1&sn=6583d29f3b18b54a22c144b009a7af97#rd");
					articleList.add(article);
					newsMessage.setArticleCount(articleList.size());
					newsMessage.setArticles(articleList);
					respMessage = MessageUtil.newsMessageToXml(newsMessage);
				}

				// 2048游戏
				else if ("001".equals(content)) {
					Article article1 = new Article();
					article1.setTitle("2048小游戏");
					article1.setDescription("挑战2048");
					// 将图片置为空
					article1.setPicUrl("http://img10.ptpcp.com/v2/thumb/jpg/YWQwYSwwLDAsNCwzLDEsLTEsMSw=/u/www.ptbus.com/uploads/allimg/1404/15/1624-1404151A254C3.jpg");
					article1.setUrl("http://go2048.com/?wechat");				

					articleList.add(article1);

					newsMessage.setArticleCount(articleList.size());
					newsMessage.setArticles(articleList);
					respMessage = MessageUtil.newsMessageToXml(newsMessage);
				}
				//显示历史上的今天
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
						cityCode = GetCityCode.getCityCode("成都");
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
					conMsg3.append("欢迎访问<a href=\"http://www.baidu.com\">百度主页</a>").append("\n");
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
					conMsg5.append("欢迎进入<a href=\"http://connwx.aliapp.com/Jsp/login.jsp\">登录页面</a>").append("\n");
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
					conMsg5.append("欢迎进入<a href=\"http://development.taskmagr.divshot.io/\">任务页面</a>").append("\n");
					tMessage5.setContent(conMsg5.toString());		
							
					respMessage = MessageUtil.textMessageToXml(tMessage5);	
					
				}				
				else if("11".equals(content)){
//					String initialUrl = "<a href="+"\\https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8888888888888888&redirect_uri=http://mascot.duapp.com/oauth2.php?userid=OPENID&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect\\"+">认证</a>";
//					String initialUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8888888888888888&redirect_uri=http://mascot.duapp.com/oauth2.php?userid=OPENID&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
//					String rdUrl = initialUrl.replace("OPENID", fromUserName);
					
					
					TextMessage tMessage5 = new TextMessage();
					tMessage5.setToUserName(fromUserName);
					tMessage5.setFromUserName(toUserName);
					tMessage5.setCreateTime(new Date().getTime());
					tMessage5.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
					tMessage5.setFuncFlag(0);
					
					StringBuffer conMsg5 = new StringBuffer(); 
					conMsg5.append("欢迎进入<a href=\"http://www.sczjls.cn/about.asp?tid=190/\">招聘页面</a>").append("\n");
//					conMsg5.append("欢迎").append(rdUrl).append("\n");
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
	 * emoji表情转换(hex -> utf-16)
	 * 
	 * @param hexEmoji
	 * @return
	 */
	public static String emoji(int hexEmoji) {
		return String.valueOf(Character.toChars(hexEmoji));
	}
}