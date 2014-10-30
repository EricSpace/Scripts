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
	 * ��html�г�ȡ����ʷ�ϵĽ�����Ϣ
	 * 
	 * @param html
	 * @return
	 */
	private static String extract(String html) {
		StringBuffer buffer = new StringBuffer();
		// ���ڱ�ǩ�����������컹�ǽ���
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
	 *  ����Jason���ض���
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
			JSONObject data = dataArray.getJSONObject(i); //ȡ������weatherinfo�����Ϣ			
			JSONArray nodes = JSONArray.fromObject(data.get("weatherinfo")); //ȡ��weatherinfo���µ���Ϣ
//			System.out.println("data:"+data);
//			System.out.println("nodes:"+nodes);
//			System.out.println("aabb::"+data.getString("weatherinfo"));	
//			System.out.println("nodes.size"+nodes.size());
			for(int j=0;j<nodes.size();j++){
				JSONObject node = nodes.getJSONObject(j);
				result = "���У�"+node.getString("city")+" ���¶ȣ�"+node.getString("temp")+"��";		
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
	 * ͨ��main�ڱ��ز���
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		String cityId = "101280601";
		System.out.println(getCityTemp(cityId));
	}

}
