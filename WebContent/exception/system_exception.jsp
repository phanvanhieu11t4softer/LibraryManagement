<%@ taglib prefix="s" uri="/struts-tags"%>
<body>
	<strong style="color: red; font-weight: bold"><s:property
			value="errorMessage" /></strong>
	<br />
	<br />
	<br />
	<br /> &nbsp;&nbsp;&nbsp;The system has been shutdown, please wait a
	moment!
	<%
 	Exception exception = (Exception) request.getAttribute("exception");
 	//exception.printStackTrace(response.getWriter());
 %>
</body>
