<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!-- List book 
 * phan.van.hieu@framgia.com
 * 17/05/2017
 -->

<section class="pb50">
	<div class="body clearfix mt20 manageUser">
		<div id="resultSearch">
			<div class="row">
				<div class="col-lg-12">
					<div class="alert alert-success">
						<p id="err_dateborrow" style="color: red">${err_dateborrow}</p>
						<p id="err_datereturn" style="color: red">${err_datereturn}</p>
						<p id="err_date" style="color: red">${err_date}</p>
						<p id="success" style="color: green">${success}</p>
						<p id="fail" style="color: red">${fail}</p>
					</div>
					<form role="form" id="searchForm"
						action="${pageContext.request.contextPath}/borrowBook/request"
						method="post">
						<div class="panel panel-default">
							<div class="panel-heading">Book Cart</div>
							<!-- /.panel-heading -->
							<div class="panel-body">
								<div class="dataTable_wrapper">
									<div class="col-sm-6">
										<label>Day Intends To Borrow</label> <input
											class="form-control css-required birthday_picker"
											id="dateBorrow" name="dateBorrow" type="text"
											placeholder="please input text" required="required">
									</div>
									<div class="col-sm-6">
										<label>Day Intends To Return</label> <input
											class="form-control css-required birthday_picker"
											id="dateReturn" name="dateReturn" type="text"
											placeholder="please input text" required="required">
									</div>
									<div class="clearfix" style="padding: 10px;"></div>
									<table class="table table-striped table-bordered table-hover"
										id="dataTables-result" width="100%">
										<thead>
											<tr>
												<th>Options</th>
												<th>Name Book</th>
												<th>Publisher Name</th>
												<th>Author Name</th>
												<th>Category Name</th>

											</tr>
										</thead>
										<tbody>
											<c:forEach var="pr" items="${sessionScope.cart}">
												<input hidden="true" type="text" id="bookid" name="bookId"
													value="${pr.bookId }" />
												<tr>
													<td><a
														href="${pageContext.request.contextPath}/borrowBook/delete/${pr.bookId}">Remove</a></td>
													<td><label id="nameBook">${pr.nameBook }</label></td>
													<td><label id="publisherName">${pr.publisherName }</label></td>
													<td><label id="authorName">${pr.authorName }</label></td>
													<td><label id="categoryName">${pr.categoryName }</label></td>
												</tr>
											</c:forEach>

										</tbody>
									</table>
									<div class="clearfix"></div>
									<div id="sub_btn">
										<input type="submit" value="Borrow Book"
											class="btn-forwardscreen" id="btnImport">
									</div>
								</div>
							</div>
						</div>
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
					</form>
				</div>
			</div>
		</div>
	</div>
</section>