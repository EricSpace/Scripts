package org.liufeng.weixin.connDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class connectDB {
	
	private String name;
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://rdsbanf6bbanf6b.mysql.rds.aliyuncs.com:3306/r2q5pmhch1gpttkp";
	String user = "r2q5pmhch1gpttkp";
	String password = "141242";
	
	{	
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url,user,password);
			if(!conn.isClosed()){
//				System.out.println("succeed to connect database.");
				Statement st = conn.createStatement();
				String sql = "select * from test";
				ResultSet rs = st.executeQuery(sql);		
						
				while(rs.next()){		
					if(this.getName()!=null){
						this.setName(this.getName()+" / "+rs.getString("Name"));						
					}else{
						this.setName(rs.getString("Name"));
					}
				}
				rs.close();
				conn.close();
			}
		}catch(Exception e){
			this.setName("数据库连接错误，无法获得数据！！！");
			e.printStackTrace(); 
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
