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

import com.alibaba.appengine.api.fetchurl.FetchUrlService;
import com.alibaba.appengine.api.fetchurl.FetchUrlServiceFactory;

/**
 * 历史上的今天查询服务
 * 
 * @author liufeng
 * @date 2013-10-16
 * 
 */
public class TodayInHistoryService {

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
			throw new RuntimeException("connect request time out!!!");
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
			
			InputStream inputStream = new ByteArrayInputStream(body.getBytes());
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			// 读取返回结果
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
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
		 StringBuffer buffer = null;  
//		 return "cccccc";

		 	try{
		 		 // 日期标签：区分是昨天还是今天  
		        String dateTag = getMonthDay(0);  
		  
		        Pattern p = Pattern.compile("(.*)(<div class=\"listren\">)(.*?)(</div>)(.*)");  
		        Matcher m = p.matcher(html);  
//		        if(m.matches()){
//		        	buffer = new StringBuffer(); 
//		        	buffer.append(m.group(3));
//		        }else{
//		        	buffer = new StringBuffer(); 
//		        	buffer.append("abcdef");
//		        }
		        
		        if (m.matches()) {  
		            buffer = new StringBuffer();  
		            if (m.group(3).contains(getMonthDay(-1)))  
		                dateTag = getMonthDay(-1);  
		  
		            // 拼装标题  
		            buffer.append("≡≡ ").append("历史上的").append(dateTag).append(" ≡≡").append("\n\n");  
		  
		            // 抽取需要的数据  
		            for (String info : m.group(3).split("  ")) {  
		                info = info.replace(dateTag, "").replace("（图）", "").replaceAll("&nbsp;&nbsp;", "").replaceAll("</?[^>]+>", "").trim();  
		                // 在每行末尾追加2个换行符  
		                if (!"".equals(info)) {  
		                    buffer.append(info).append("\n\n");  
		                }  
		            }  
		            
		        }  
		        
		 	}catch(Exception e){		
		 		e.printStackTrace();
		 		throw new RuntimeException(e.getMessage());
		 	}
	       
	        // 将buffer最后两个换行符移除并返回  
	        return (null == buffer) ? null : buffer.substring(0, buffer.lastIndexOf("\n\n"));
//	        return (null == buffer) ? null : buffer.toString();

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
	 * 封装历史上的今天查询方法，供外部调用
	 * 
	 * @return
	 */
	public static String getTodayInHistoryInfo() {
		// 获取网页源代码
//		String html = httpRequest("http://www.rijiben.com/");
		String html = fetchUrl("http://www.rijiben.com/");

//		System.out.println(html);
		// 从网页中抽取信息
		String result = extract(html);

		return result;
	}
		
	/**
	 * 计算采用utf-8编码方式时字符串所占字节数
	 * 
	 * @param content
	 * @return
	 */
	public static int getByteSize(String content) {
		int size = 0;
		if (null != content) {
			try {
				// 汉字采用utf-8编码时占3个字节
				size = content.getBytes("utf-8").length;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return size;
	}
	
	/**
	 * 通过main在本地测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String info = getTodayInHistoryInfo();
		System.out.println(info);
		System.out.println("----------------");
		System.out.println(getByteSize(info));
	}
}