<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta name="viewport"
content=
" 
height = [pixel_value | device-height] ,
width = [pixel_value | device-width ] ,
initial-scale = 1.0,
minimum-scale = 0.5 ,
maximum-scale = 3.0 ,
user-scalable = [yes|no]
" 
/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=IUTF-8">
<title>登录界面</title>
 <style type="text/css" media="screen">
                .center{
                        position:absolute;
                        width: 100px;
                        height: 100px;
                        left: 50%;
                        top: 50%;
                        margin: -60px 0px 0px -60px;                        
                }
        </style>
</head>
<body>

<div class="center">
<form action="checkLogin.jsp" method="post">
<table border="0" style="width:100%">
<tr>
<td><nobr>用户:</nobr></td>
<td style="width:70%"><input type="text" name="userid" ></td>
</tr>
<tr>
<td><nobr>密码:</nobr></td>
<td style="width:70%"><input type="password" name="password" ></td>
</tr>
<tr>
<td></td>
<td align="left">
<input type="submit" value="登陆">
<input type="Reset" value="重置"></td>  
</tr>
</table>
</form>
</div>

</body>
</html>