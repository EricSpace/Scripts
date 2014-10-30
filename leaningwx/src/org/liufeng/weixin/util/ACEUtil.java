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
			
			// 读取返回结果
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
	
	
	// 获取access_token的接口地址（GET） 限200（次/天）
		public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

		/**
		 * 获取access_token
		 * 
		 * @param appid 凭证
		 * @param appsecret 密钥
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
				
				// 读取返回结果
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
					// 获取token失败
					throw new RuntimeException("获取token失败 errcode:{} errmsg:{}"+dataJson.getInt("errcode")+dataJson.getString("errmsg"));
				}				
			}
			
			return accessToken;
		}

}
