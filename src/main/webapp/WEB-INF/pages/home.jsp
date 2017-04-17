<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Spring Security Example</title>
    </head>
    <body>
        <form:form action="${pageContext.request.contextPath}/appLogout" method="POST">
           User : ${pageContext.request.userPrincipal.name} | 
           <input type="submit" value="Logout"/>
        </form:form>   
    </body>
</html> 
