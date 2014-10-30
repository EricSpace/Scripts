package org.liufeng.weixin.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.liufeng.weixin.pojo.AccessToken;

import com.alibaba.appengine.api.fetchurl.FetchUrlService;
import com.alibaba.appengine.api.fetchurl.FetchUrlServiceFactory;

public class ACEUtil {
	
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
			
//			if(body!=null){				
//				buffer.append(body);
//			}			
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
		
		return (null == buffer) ? null : buffer.toString();
	}
	
	
	// ��ȡaccess_token�Ľӿڵ�ַ��GET�� ��200����/�죩
		public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

		/**
		 * ��ȡaccess_token
		 * 
		 * @param appid ƾ֤
		 * @param appsecret ��Կ
		 * @return
		 */
		public static AccessToken getAccessToken(String appid, String appsecret) {
			AccessToken accessToken = null;
			StringBuffer buffer = null;

			String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
			String resultUrl = fetchUrl(requestUrl);
			

			try {
				InputStream inputStream = new ByteArrayInputStream(resultUrl.getBytes());
				InputStreamReader inputStreamReader;
				inputStreamReader = new InputStreamReader(inputStream, "utf-8");
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				
				// ��ȡ���ؽ��
				String str = null;
				buffer = new StringBuffer();
				while ((str = bufferedReader.readLine()) != null) {
					buffer.append(str);
				}
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e1.getMessage());
			}
			
			JSONObject dataJson = new JSONObject().fromObject(buffer.toString());	

			
			if(null!=dataJson){
				try{
					accessToken = new AccessToken();
					accessToken.setToken(dataJson.getString("access_token"));
					accessToken.setExpiresIn(dataJson.getInt("expires_in"));
				}catch (JSONException e) {
					accessToken = null;
					// ��ȡtokenʧ��
					throw new RuntimeException("��ȡtokenʧ�� errcode:{} errmsg:{}"+dataJson.getInt("errcode")+dataJson.getString("errmsg"));
				}				
			}
			
			return accessToken;
		}

}
