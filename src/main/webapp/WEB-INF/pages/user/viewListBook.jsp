<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!-- List book 
 * phan.van.hieu@framgia.com
 * 17/05/2017
 -->
<section class="bg_white clearfix messageError">
	<div class="body clearfix mt20 manageUser" id="messageContainer">
	</div>
</section>
<label id="mgsSuccess" class = "hidden_elem"><spring:message code='add_book_succes' text='' /></label>
<label id="mgsError" class = "hidden_elem"><spring:message code='add_book_fail' text='' /></label>
<section class="bg_white clearfix">
	<div class="body clearfix mt20 manageUser">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Search area</h3>
			</div>
			<div class="panel-body">
				<spring:url value="/listBook/search" var="searchActionUrl" />
				<form role="form" id="searchForm" action="${searchActionUrl}"
					method="post">
					<div class="form-group form-group-lg">
						<div class="col-sm-6">
							<label>Name Book: </label> <input class="form-control" id="txtName"
								name="txtName" type="text" placeholder="please input text">
						</div>
						<div class="col-sm-6">
							<label>Categories: </label>
							<select id="txtCategoryName"
								name="txtCategoryName" class="form-control">
								<option value="0" selected="selected"></option>
								<c:forEach items="${listCategory}" var="catLevel1">
									<c:if test="${catLevel1.catSubId==0 }">
										<optgroup label="${catLevel1.name}">
											<c:forEach items="${listCategory}" var="catLevel2">
												<c:if test="${catLevel2.catSubId==catLevel1.categoriesId }">
													<option value="${catLevel2.categoriesId}">${catLevel2.name}</option>
												</c:if>
											</c:forEach>
										</optgroup>
									</c:if>
								</c:forEach>
							</select>
						</div>
						<div class="clearfix"></div>
					</div>
					<div class="form-group form-group-lg">
						<div class="col-sm-6">
							<label>Name Publisher: </label> 
							<select id="txtPublisherName"
								name="txtPublisherName" class="form-control">
								<option value="0" selected="selected"></option>
								<c:forEach items="${publisherInfoList}" var="catLevel">
									<option value="${catLevel.publishersId}">${catLevel.publishersName}</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-sm-6">
							<label>Name Author: </label> 
							<select id="txtAuthorName"
								name="txtAuthorName" class="form-control">
								<option value="0" selected="selected"></option>
								<c:forEach items="${authorInfoList}" var="catLevel">
									<option value="${catLevel.authorsId}">${catLevel.authorsName}</option>
								</c:forEach>
							</select>
						</div>
						<div class="clearfix"></div>
					</div>
					<div id="sub_btn">
						<input type="button" onclick="submitClear()" value="Clear"
							class="btn btn-defaul"> <input type="submit"
							value="Search" class="btn">
					</div>
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
				</form>
			</div>
		</div>
	</div>
</section>
<section class="pb50">
	<div id="resultSearch" class="hidden_elem">
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-default">
					<div class="panel-heading">List result</div>
					<!-- /.panel-heading -->
					<div class="panel-body">
						<div class="dataTable_wrapper">
							<table class="table table-striped table-bordered table-hover"
								id="dataTables-result" width="100%">
								<thead>
									<tr>
										<th>Name Book</th>
										<th>Book Code</th>
										<th>Name Category</th>
										<th>Name Publishers</th>
										<th>Number Book</th>
										<th>Number Rest</th>
										<th>Name Author</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</section>
