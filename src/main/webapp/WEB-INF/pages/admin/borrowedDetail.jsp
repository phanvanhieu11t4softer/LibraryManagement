<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!-- Management users 
 * vu.thi.tran.van@framgia.com
 * 18/04/2017
 -->
<section class="bg_white clearfix messageError">
	<div class="body clearfix mt20 manageUser" id="messageContainer">
	</div>
</section>
<label id="mgsSuccess" class = "hidden_elem"><spring:message code='update_success' text='' /></label>
<label id="mgsError" class = "hidden_elem"><spring:message code='update_error' text='' /></label>
<spring:url value="/managementBorrowed/update" var="searchActionUrl" />
<spring:url value="/managementUsers/update" var="userActionUrl" />
<form:form id="updateForm" class="form-horizontal" method="post"
			action="${userActionUrl}" modelAttribute="borrowedInfo">

	<section class="bg_white clearfix">
		<div class="body clearfix mt20 manageUser">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">User information</h3>
				</div>
				<div class="panel-body">
					<div class="form-group form-group-lg">
						<div class="col-sm-4">
							<label>Username: ${borrowedInfo.userInfo.userName}</label>
						</div>
						<div class="col-sm-4">
							<label>Full name: ${borrowedInfo.userInfo.name}</label>
						</div>
						<div class="col-sm-4">
							<label>Gender: ${borrowedInfo.userInfo.sex} - Birthday:
								${borrowedInfo.userInfo.birthDate}</label>
						</div>

						<div class="clearfix" style="padding: 10px;"></div>
						<div class="col-sm-4">
							<label>Email: ${borrowedInfo.userInfo.email}</label>
						</div>
						<div class="col-sm-4">
							<label>Phone number: ${borrowedInfo.userInfo.phone}</label>
						</div>
						<div class="col-sm-4">
							<label>Address: ${borrowedInfo.userInfo.address}</label>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>

	<section class="bg_white clearfix">
		<div class="body clearfix mt20 manageUser">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Borrowed information</h3>
				</div>
				<div class="panel-body">
					<div class="form-group form-group-lg">
						<div class="col-sm-4">
							<label>Borrowed code: ${borrowedInfo.borrowedCode}</label>
						</div>
						<div class="col-sm-4">
							<label>Intend date payment:
								${borrowedInfo.dIntendArrived}</label>
						</div>
						<div class="col-sm-4">
							<label>Intend date borrowed :
								${borrowedInfo.dIntendBorrowed}</label>
						</div>

						<div class="clearfix" style="padding: 10px;"></div>
						<c:choose>
							<c:when test="${(borrowedInfo.status == 'Request') || (borrowedInfo.status == 'Approve') || (borrowedInfo.status == 'Borrowed')}">
						
								<div class="col-sm-4">
									<div class="detail-borrowed-left">
										<label>Status:</label>
									</div>
									<div class="detail-borrowed-right">
										<form:select
											path="status" id="status"
											name="status" class="form-control css-required"
											style="display: inline; width: 65%;" disabled="true">
											<c:choose>
												<c:when test="${borrowedInfo.status == 'Request'}">
													<form:option value="1" selected="selected">Request</form:option>
													<form:option value="2">Approve</form:option>
													<form:option value="3">Cancel</form:option>
													<form:option value="4">Borrowed</form:option>
													<form:option value="5">Finish</form:option>
												</c:when>
												<c:when test="${borrowedInfo.status == 'Approve'}">
													<form:option value="1" disabled="true">Request</form:option>
													<form:option value="2" selected="selected">Approve</form:option>
													<form:option value="3">Cancel</form:option>
													<form:option value="4">Borrowed</form:option>
													<form:option value="5">Finish</form:option>
												</c:when>
												<c:otherwise>
													<form:option value="1" disabled="true">Request</form:option>
													<form:option value="2" disabled="true">Approve</form:option>
													<form:option value="3" disabled="true">Cancel</form:option>
													<form:option value="4" selected="selected">Borrowed</form:option>
													<form:option value="5">Finish</form:option>
												</c:otherwise>
											</c:choose>
											
										</form:select>
									</div>
								</div>
								
								<div class="col-sm-4">
								
									<c:choose>
										<c:when test="${empty borrowedInfo.dateBorrrowed }">
											<div class="detail-borrowed-left">
												<label>Actual date borrowed:</label>
											</div>
											<div class="detail-borrowed-right">
												<form:input path="dateBorrrowed" name="dateBorrrowed" id="dateBorrrowed" 
													class="form-control css-required"
													 style="display: inline; width: 65%;" disabled="true"/>
											</div>
										</c:when>
										<c:otherwise>
											<label>Actual date borrowed: ${borrowedInfo.dateBorrrowed}</label>
										</c:otherwise>
									</c:choose>
								</div>
								<div class="col-sm-4">
									<div class="detail-borrowed-left">
										<label>Actual date borrowed:</label>
									</div>
									<div class="detail-borrowed-right">
										<form:input path="dateArrived" name="dateArrived" id="dateArrived" 
											class="form-control css-required"
											 style="display: inline; width: 65%;" disabled="true"/>
									</div>
								</div>
						
							</c:when>
							<c:otherwise>
								<div class="col-sm-4">
									<label>Status: ${borrowedInfo.status}</label>
								</div>
								<div class="col-sm-4">
									<label>Actual date borrowed:
										${borrowedInfo.dateBorrrowed}</label>
								</div>
								<div class="col-sm-4">
									<label>Actual date payment: ${borrowedInfo.dateArrived}</label>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</section>

	<section class="bg_white clearfix">
		<div class="body clearfix mt20 manageUser">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Book information</h3>
				</div>
				<div class="panel-body">
					<div class="form-group form-group-lg">
						<table class="table table-striped table-bordered table-hover"
							id="dataTables-result">
							<thead>
								<tr>
									<th>Book code</th>
									<th>Book name</th>
									<th>Categories</th>
									<th>Publisher</th>
									<th>Book status</th>
									<th>Page number</th>
									<th>Price</th>
									<th>Status</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="borrowedDetail"
									items="${borrowedInfo.borrowedDetail}" varStatus="counter">
									<c:choose>
										<c:when test="${counter.count % 2 == 0}">
											<c:set var="rowStyle" scope="page" value="gradeX odd" />
										</c:when>
										<c:otherwise>
											<c:set var="rowStyle" scope="page" value="gradeX even" />
										</c:otherwise>
									</c:choose>
									<tr class="${rowStyle}">
										<td>${borrowedDetail.bookInfo.bookCode}</td>
										<td>${borrowedDetail.bookInfo.name}</td>
										<td>${borrowedDetail.bookInfo.categoriesName}</td>
										<td>${borrowedDetail.bookInfo.publishersName}</td>
										<td>${borrowedDetail.bookInfo.statusBook}</td>
										<td><fmt:formatNumber type="number" maxFractionDigits="3"
												value="${borrowedDetail.bookInfo.numberPage}" /></td>
										<td><fmt:formatNumber
												value="${borrowedDetail.bookInfo.price}" type="currency" />
										</td>
										<td>${borrowedDetail.status}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</section>
	<div id="sub_btn">
		<a href="/SpringSecurity/home" class="btn btn-detail">
			<input type="button" value="BACK HOME" class="btn-forwardscreen"></a>
			
		<c:if test="${(borrowedInfo.status == 'Request') || (borrowedInfo.status == 'Approve') || (borrowedInfo.status == 'Borrowed')}">
			<button onclick="clickBtnEdit()" id="editbtn" type="button"
			class="btnScrUser btn-detail detail_borrowed btn-defaul">EDIT</button>
			
			<button id="savebtn" type="submit" class="hidden_elem btnScrUser btn-detail detail_borrowed btn-defaul">SAVE</button>
			<button onclick="clickBtnCancel()" id="cancelbtn" type="button"
				class="hidden_elem detail_borrowed btn-detail btnScrUser btn-defaul">CANCEL</button>
		</c:if>
	</div>
	<input type="hidden" name="${_csrf.parameterName}"
		value="${_csrf.token}" />
</form:form>