<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
.vertical-offset-100 {
	padding-top: 150px;
}
</style>
</head>
<body onload='document.loginForm.username.focus();'>
	<div class="container">
		<div class="row vertical-offset-100">
			<div class="col-md-4 col-md-offset-4">
				<div class="panel panel-default">
					<div class="top-login">
						<div class="error-login"></div>
						<c:if test="${not empty error}">
							<font color="red"><div class="error-login">${error}</div></font>
						</c:if>
						<c:if test="${not empty msg}">
							<font color="red"><div class="logout-msg">${msg}</div></font>
						</c:if>
					</div>
					<div class="panel-heading">
						<center><h3 class="panel-title">Please sign in</h3></center>
					</div>
					<div class="panel-body">
						<form name='loginForm'
							action="<c:url value='/login' />" method='POST'>
							<fieldset>
								<div class="form-group">
									<input class="form-control" placeholder="User Name"
										name="username" type="text">
								</div>
								<div class="form-group">
									<input class="form-control" placeholder="Password"
										name="password" type="password" value="">
								</div>
								<div class="checkbox">
									<label>
								    <input type='checkbox' name="remember-me-param"/>Remember Me? <br/> 
								</div>
								<center><input class="btn-forwardscreen" name="submit" type="submit" value="Login""></center>
							</fieldset>
							<input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" />
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
