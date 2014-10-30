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
 * ��ʷ�ϵĽ����ѯ����
 * 
 * @author liufeng
 * @date 2013-10-16
 * 
 */
public class TodayInHistoryService {

	/**
	 * ����http get�����ȡ��ҳԴ����
	 * 
	 * @param requestUrl
	 * @return
	 */
	private static String httpRequest(String requestUrl) {
		StringBuffer buffer = null;

		try {
			// ��������
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
//			httpUrlConn.setConnectTimeout(100000);
//			httpUrlConn.setReadTimeout(100000);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setRequestMethod("GET");
			
			// ��ȡ������ 
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			// ��ȡ���ؽ��
			buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			// �ͷ���Դ
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
			
			// ��ȡ���ؽ��
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
	 * ��html�г�ȡ����ʷ�ϵĽ�����Ϣ
	 * 
	 * @param html
	 * @return
	 */
	private static String extract(String html) {
		 StringBuffer buffer = null;  
//		 return "cccccc";

		 	try{
		 		 // ���ڱ�ǩ�����������컹�ǽ���  
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
		  
		            // ƴװ����  
		            buffer.append("�ԡ� ").append("��ʷ�ϵ�").append(dateTag).append(" �ԡ�").append("\n\n");  
		  
		            // ��ȡ��Ҫ������  
		            for (String info : m.group(3).split("  ")) {  
		                info = info.replace(dateTag, "").replace("��ͼ��", "").replaceAll("&nbsp;&nbsp;", "").replaceAll("</?[^>]+>", "").trim();  
		                // ��ÿ��ĩβ׷��2�����з�  
		                if (!"".equals(info)) {  
		                    buffer.append(info).append("\n\n");  
		                }  
		            }  
		            
		        }  
		        
		 	}catch(Exception e){		
		 		e.printStackTrace();
		 		throw new RuntimeException(e.getMessage());
		 	}
	       
	        // ��buffer����������з��Ƴ�������  
	        return (null == buffer) ? null : buffer.substring(0, buffer.lastIndexOf("\n\n"));
//	        return (null == buffer) ? null : buffer.toString();

	}

	/**
	 * ��ȡǰ/��n������(M��d��)
	 * 
	 * @return
	 */
	private static String getMonthDay(int diff) {
		DateFormat df = new SimpleDateFormat("M��d��");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, diff);
		return df.format(c.getTime());
	}

	/**
	 * ��װ��ʷ�ϵĽ����ѯ���������ⲿ����
	 * 
	 * @return
	 */
	public static String getTodayInHistoryInfo() {
		// ��ȡ��ҳԴ����
//		String html = httpRequest("http://www.rijiben.com/");
		String html = fetchUrl("http://www.rijiben.com/");

//		System.out.println(html);
		// ����ҳ�г�ȡ��Ϣ
		String result = extract(html);

		return result;
	}
		
	/**
	 * �������utf-8���뷽ʽʱ�ַ�����ռ�ֽ���
	 * 
	 * @param content
	 * @return
	 */
	public static int getByteSize(String content) {
		int size = 0;
		if (null != content) {
			try {
				// ���ֲ���utf-8����ʱռ3���ֽ�
				size = content.getBytes("utf-8").length;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return size;
	}
	
	/**
	 * ͨ��main�ڱ��ز���
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