<%@page import="bbs.BbsDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
	String userID = (String) session.getAttribute("userID");
%>    
<jsp:useBean id="bbs" class="bbs.Bbs" scope="page"></jsp:useBean>
<jsp:setProperty property="*" name="bbs"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP 게시판 웹 사이트</title>
</head>
<body>
<%
	BbsDAO bbsDAO = new BbsDAO();
	bbs.setUserID(userID);
	
	int res = bbsDAO.write(bbs);
	out.println(res);
	switch(res){
		case 1 : response.sendRedirect("bbs.jsp");	break;
		default : response.sendRedirect("write.jsp"); break;	
	}
%>
</body>
</html>