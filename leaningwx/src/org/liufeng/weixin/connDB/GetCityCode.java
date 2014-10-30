package org.liufeng.weixin.connDB;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.liufeng.course.service.GetWeatherInfo;

public class GetCityCode {
	
	private static Connection connectDB() throws Exception{
		
		String driver = "com.mysql.jdbc.Driver";
	
//		String hostip = "localhost:3306"; 
//		String DBname = "lawyer"; 
//		String user = "root"; 
//		String passwd = "Welcome1"; 
        
		String hostip = "rdsbanf6bbanf6b.mysql.rds.aliyuncs.com:3306"; 
        String DBname = "r2q5pmhch1gpttkp"; 
        String user = "r2q5pmhch1gpttkp"; 
        String passwd = "141242"; 
         
        //1.加载数据库驱动 
        Class.forName("com.mysql.jdbc.Driver").newInstance(); 
         
        //2.获取数据库的连接 
        Connection con=DriverManager.getConnection("jdbc:mysql://"+hostip +"/"+DBname+"?user="+user+"&password="+passwd+"&useUnicode=true&characterEncoding=utf-8"); 
		return con;
	}
	
	public static String getCityCode(String cityName) throws Exception{
		Connection conn = connectDB();
		String cityCode = null;
		try {
			if(!conn.isClosed()){
				String sql = "select * from city_info_ where city_name_=?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, cityName);
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()){
					cityCode = rs.getString("city_code_");	
					break;
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Error occured when query the city code!!!");		
		}
		return cityCode;
	}
	public static void main(String args[]) throws Exception{
		String cityName = "成都";
		String cityCode = getCityCode(cityName);
		System.out.println(cityCode);
		String temp = GetWeatherInfo.getCityTemp(cityCode);
		System.out.println(temp);
		
	}

}
