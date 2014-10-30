package org.liufeng.weixin.connDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.liufeng.course.entity.Person;

public class getAllPersons {
	
	private String name;
//	String driver = "com.mysql.jdbc.Driver";
//	String url = "jdbc:mysql://rdsbanf6bbanf6b.mysql.rds.aliyuncs.com:3306/r2q5pmhch1gpttkp";
//	String user = "r2q5pmhch1gpttkp";
//	String password = "141242";
	
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/first";
	String user = "root";
	String password = "Welcome1";
	ArrayList al = new ArrayList();
	
	public ArrayList Query()
	{	
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url,user,password);
			if(!conn.isClosed()){
//				System.out.println("succeed to connect database.");
				Statement st = conn.createStatement();
				String sql = "select * from person";
				ResultSet rs = st.executeQuery(sql);
				
				
				Person p = new Person();
						
				while(rs.next()){	
					p.setId(rs.getLong("Id"));
					p.setName(rs.getString("Name"));
					
					al.add(p);
				}
				rs.close();
				conn.close();
			}
		}catch(Exception e){			
			e.printStackTrace(); 
		}
		return al;
	}
	


}
