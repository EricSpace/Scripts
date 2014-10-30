package com.liufeng.course.test;

import com.alibaba.appengine.api.fetchurl.FetchUrlService;
import com.alibaba.appengine.api.fetchurl.FetchUrlServiceFactory;

public class FetchUrl {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FetchUrlService fetchUrlService = FetchUrlServiceFactory.getFetchUrlService();
		 final String url = "http://www.weather.com.cn/data/sk/101270101.html";
	     String body = fetchUrlService.get(url);
	     System.out.println("body is"+body);

	}
	
	public static String getRequest(){
		FetchUrlService fetchUrlService = FetchUrlServiceFactory.getFetchUrlService();
		final String url = "http://www.weather.com.cn/data/sk/101270101.html";
	    String body = fetchUrlService.get(url);
	    return body;
	}

}
