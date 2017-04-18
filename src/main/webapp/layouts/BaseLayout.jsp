<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ja">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="description" content="">
<meta name="author" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- JavaScript -->
<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap-datepicker.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/init.js"></script>

<!-- Css -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/datepicker.css">

<title><tiles:insertAttribute name="title" ignore="true" /></title>
</head>

<body>

    <header>
        <section class="bg_red clearfix">
            <h1 id="logo" style="font-weight: bold;">FRAMGIA LIBRARY WEB</h1>
            <p id="logout">
                <%-- <s:property value="#session.universityNmSsn"/>　
                <button type="button" class="btn btn-default btn-xs" onclick="location.href=
                '${pageContext.request.contextPath}/logout'">logout</button> --%>
                
                <form:form action="${pageContext.request.contextPath}/appLogout" method="POST">
                ${pageContext.request.userPrincipal.name} | 
                <input type="submit" value="Logout"/>
                </form:form>
            </p>
        </section>
    </header>
        
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed"
                    data-toggle="collapse" data-target="#site-menu">
                </button>
                <p class="navbar-brand"><tiles:insertAttribute name="screenname" /></p>
            </div>
            <div class="collapse navbar-collapse" id="site-menu">
                <tiles:insertAttribute name="menu" />
            </div>
        </div>
    </nav>
    
    <tiles:insertAttribute name="body" />

    <footer>
        <div id="footerbody">
            <div id="footermenu">
                <p>&copy; FRAMGIA LIBRARY LTD. All rights reserved.</p>
            </div>
        </div>
    </footer>

</body>

</html>
