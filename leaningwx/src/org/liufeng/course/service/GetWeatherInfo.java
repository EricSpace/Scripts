package org.liufeng.course.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.appengine.api.fetchurl.FetchUrlService;
import com.alibaba.appengine.api.fetchurl.FetchUrlServiceFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetWeatherInfo {
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
			body = fetchUrlService.get(url);
			if(body!=null){				
				buffer.append(body);
			}			
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
		
		return (null == buffer) ? null : buffer.toString();
	}
	
	/**
	 * 从html中抽取出历史上的今天信息
	 * 
	 * @param html
	 * @return
	 */
	private static String extract(String html) {
		StringBuffer buffer = new StringBuffer();
		// 日期标签：区分是昨天还是今天
		String dateTag = getMonthDay(0);
		Pattern p = Pattern.compile("(.*)(<china dn=\"day\">)(.*?)(</china>)");
		Matcher m = p.matcher(html); 
		
		if(m.matches()){
			buffer.append(m.group(3));
		}else{
			buffer.append(html);
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
	 */
	@SuppressWarnings("static-access")
	public static String getJasonObject(String obj){
		String result = null;
		JSONObject dataJson=new JSONObject().fromObject(obj);
		JSONArray dataArray = new JSONArray().fromObject(dataJson);
//		System.out.println("size is "+dataArray.size());
		
		for(int i=0;i<dataArray.size();i++){
			JSONObject data = dataArray.getJSONObject(i); //取到包含weatherinfo层的信息			
			JSONArray nodes = JSONArray.fromObject(data.get("weatherinfo")); //取到weatherinfo层下的信息
//			System.out.println("data:"+data);
//			System.out.println("nodes:"+nodes);
//			System.out.println("aabb::"+data.getString("weatherinfo"));	
//			System.out.println("nodes.size"+nodes.size());
			for(int j=0;j<nodes.size();j++){
				JSONObject node = nodes.getJSONObject(j);
				result = "城市："+node.getString("city")+" ，温度："+node.getString("temp")+"度";		
			}		
			
		}
				
		return result;
	}
	
	public static String getCityTemp(String cityCode){
		String url = "http://www.weather.com.cn/data/sk/"+cityCode+".html";
//		String url = "http://www.weather.com.cn/data/cityinfo/"+cityCode+".html";
		String result = null;
//		String html = httpRequest(url);
		try{
			String html = fetchUrl(url);
			String parseHtml = extract(html);
			result = getJasonObject(parseHtml);
		}catch(Exception e){
			result = e.getMessage();
		}
		
//		System.out.println(result);		
		return result;
	}

	/**
	 * 通过main在本地测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		String cityId = "101280601";
		System.out.println(getCityTemp(cityId));
	}

}
