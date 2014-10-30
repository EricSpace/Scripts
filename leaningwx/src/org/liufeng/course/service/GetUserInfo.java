package org.liufeng.course.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.liufeng.weixin.connDB.GetCityCode;
import org.liufeng.weixin.pojo.AccessToken;
import org.liufeng.weixin.pojo.Subscriber;
import org.liufeng.weixin.util.ACEUtil;
import org.liufeng.weixin.util.WeixinUtil;

import com.alibaba.appengine.api.fetchurl.FetchUrlService;
import com.alibaba.appengine.api.fetchurl.FetchUrlServiceFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetUserInfo {
	/**
	 * 发起http get请求获取网页源代码
	 * 
	 * @param requestUrl
	 * @return
	 */
	private static String httpRequest(String requestUrl) {
		StringBuffer buffer = null;

		try {
			// 建立连接
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
//			httpUrlConn.setConnectTimeout(100000);
//			httpUrlConn.setReadTimeout(100000);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setRequestMethod("GET");

			// 获取输入流
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			// 读取返回结果
			buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			httpUrlConn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
//			throw new RuntimeException("connect request time out!!!");
		}
		return buffer.toString();
	}
	
	/**
	 * Connect to http source 
	 * @param url
	 * @return Http response
	 */
	public static String fetchUrl(String url){
		StringBuffer buffer = null;
		buffer = new StringBuffer();
		try{
			FetchUrlService fetchUrlService = FetchUrlServiceFactory.getFetchUrlService();
			String body = null;
			byte[] bytes = null;
			bytes = fetchUrlService.getBytes(url);
			body = new String(bytes,"utf-8");
			if(body!=null){				
				buffer.append(body);
			}			
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
		
		return (null == buffer) ? null : buffer.toString();
	}
	
	
	/**
	 * 获取前/后n天日期(M月d日)
	 * 
	 * @return
	 */
	private static String getMonthDay(int diff) {
		DateFormat df = new SimpleDateFormat("M月d日");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, diff);
		return df.format(c.getTime());
	}
	
	/**
	 *  解析Jason返回对象
	 * @param obj
	 * @return
	 * @throws Exception 
	 */
//	@SuppressWarnings({ "static-access", "null" })
	public static Subscriber getJasonObject(String obj) throws Exception{
		
		Subscriber subscriber = null;
		subscriber = new Subscriber();
		try{
			JSONObject dataJson=new JSONObject().fromObject(obj);	

			subscriber.setOpenid(dataJson.getString("openid"));
			subscriber.setSubscribe(dataJson.getInt("subscribe"));
			subscriber.setNickname(dataJson.getString("nickname"));
			subscriber.setSex(dataJson.getInt("sex"));
			subscriber.setLanguage(dataJson.getString("language"));
			subscriber.setCity(dataJson.getString("city"));
			subscriber.setProvince(dataJson.getString("province"));
			subscriber.setCountry(dataJson.getString("country"));
			subscriber.setHeadimgurl(dataJson.getString("headimgurl"));
			subscriber.setSubscribe_time(dataJson.getInt("subscribe_time"));
			subscriber.setRemark(dataJson.getString("remark"));		

		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
		

//		System.out.println(subscriber);				
		return subscriber;
	}
	
	public static Subscriber getUserInfo(String openId){
		Subscriber subscriber = null;
		String appid = "wx35e20298e9252a7a";
		String appsecret = "d203aa92ab81ea5ea0641e399858f52d";
		String user_info_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		
		try{
			AccessToken acessToken = ACEUtil.getAccessToken(appid, appsecret);
//			AccessToken acessToken = WeixinUtil.getAccessToken(appid, appsecret);
			String at = acessToken.getToken();
			
			String request_url = user_info_url.replace("ACCESS_TOKEN", at).replace("OPENID", openId);
			String result_jason = fetchUrl(request_url);

			if(null!=result_jason){
				subscriber = new Subscriber();
				subscriber = getJasonObject(result_jason);
			}		
			
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
		
		return subscriber;		
	}
	

	/**
	 * 通过main在本地测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx35e20298e9252a7a&secret=d203aa92ab81ea5ea0641e399858f52d";
		
//		String res = httpRequest(url);
//		System.out.println(res);
		
		String openid = "ohSnhsqZypFbUSyp765JVNB7_s08";
		String atJason = httpRequest(url);
		System.out.println("at:"+atJason);
		JSONObject dataJson = new JSONObject().fromObject(atJason);	
		System.out.println("dataJson:"+dataJson);
		String at = dataJson.getString("access_token");
		System.out.println("at:"+at);
		
		
		String infourl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		String infourl1 = infourl.replace("ACCESS_TOKEN", at).replace("OPENID", openid);
		
		String res = httpRequest(infourl1);
		System.out.println("res:"+res);
		
		JSONObject dataJson1 = new JSONObject().fromObject(res);	
		System.out.println("dataJson1:"+dataJson1);
 
		System.out.println("nickname:"+dataJson1.getString("nickname"));
		
		/*
		Subscriber s = new Subscriber();
		s =	getUserInfo("ohSnhsqZypFbUSyp765JVNB7_s08");
		System.out.println(s.getCity()+""+s.getSubscribe()+" "+s.getSex()+" "+s.getProvince());
		String cityName = s.getCity();
		String cityCode;
		try {
			cityCode = GetCityCode.getCityCode(cityName);
			System.out.println("cityCode="+cityCode);
			String temp = GetWeatherInfo.getCityTemp(cityCode);
			System.out.println("temp="+temp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	
	}

}
