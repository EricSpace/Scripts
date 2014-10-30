<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<jsp:useBean id="jb" scope="page" class="org.liufeng.weixin.connDB.getAllPersons">
</jsp:useBean>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>用户列表</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
   	  <table>
       <tr>
        <th>用户Id</th>
        <th>用户名</th>
       </tr>
       </table>
       
     <%ArrayList al = new org.liufeng.weixin.connDB.getAllPersons().Query();   
      for (int i=0;i<al.size();i++){
      org.liufeng.course.entity.Person b = (org.liufeng.course.entity.Person)al.get(i); %>
       <tr>
        <td><%=b.getId() %></td>
        <td><%=b.getName() %></td>
       </tr>
       </table>
       <%} %>
  </body>
</html>
