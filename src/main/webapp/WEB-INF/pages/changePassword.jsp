<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="container">
	<div class="row vertical-offset-100">
		<div class="col-md-4 col-md-offset-4">
			<div class="panel panel-default">
				<div class="top-login">
					<div class="error-login">${messageChangePass}</div>
				</div>
				<div class="panel-heading">
					<center>
						<h3 class="panel-title">Please input password</h3>
					</center>
				</div>
				<div class="panel-body">
					<spring:url value="/resetPassword" var="searchActionUrl" />
					<form name="ChangePasswordForm" action="${searchActionUrl}" method="POST">
						<fieldset>
							<c:choose>
								<c:when test="${not empty token}">
									<input type="hidden" value="${token}"
										name="token" id="token">
									<input type="hidden" value="${idUser}" name="idUser"
										id="idUser">
									<input type="hidden" name="currencePass"
										id="currencePass">
								</c:when>
								<c:otherwise>
									<input class="form-control" type="password"
										placeholder="Current Password" name="currencePass"
										id="currencePass">
									<input type="hidden" value="${idUser}" name="idUser"
										id="idUser">
									<input type="hidden" value="${token}"
										name="token" id="token">
								</c:otherwise>
							</c:choose>
							<div class="form-group">
								<input class="form-control" placeholder="New password" id="newPassword"
									name="newPassword" type="password">
							</div>
							<div class="form-group">
								<input class="form-control" placeholder="Password confirmation" id="confirmPassword"
									name="confirmPassword" type="password">
							</div>
							<center>
								<input class="btn-forwardscreen" name="submit" type="submit"
									value="Save">
							</center>
						</fieldset>
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
