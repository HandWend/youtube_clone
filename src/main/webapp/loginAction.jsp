<%@page import="user.UserDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
%>    
<jsp:useBean id="user" class="user.User" scope="page"></jsp:useBean>
<jsp:setProperty property="userID" name="user"/>
<jsp:setProperty property="userPassword" name="user"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP 게시판 웹 사이트</title>
</head>
<body>
<%
	UserDAO userDAO = new UserDAO();
	int res = userDAO.login(user.getUserID(), user.getUserPassword());
	out.println(res);
	switch(res){
		case 1 : 
			session.setAttribute("userID", user.getUserID());
			response.sendRedirect("main.jsp");	break;
		default : response.sendRedirect("login.jsp"); break;	
	}
%>
</body>
</html>