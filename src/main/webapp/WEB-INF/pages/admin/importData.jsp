<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<body>
	<section class="bg_white clearfix">
		<div class="body clearfix mt20">

			<div class="alert alert-success">
				<p id="err_nameTable" style="color: red"></p>
				<p id="err_file" style="color: red"></p>
				<p id="err_formatFile" style="color: red"></p>
				<p id="err_data" style="color: red">${err_data}</p>
				<p id="error_table" style="color: red">${error_table}</p>
				<p id="error_file" style="color: red">${error_file}</p>
				<p id="success" style="color: green">${success}</p>
				<p id="fail" style="color: red">${fail}</p>
			</div>
			<form method="POST" action="${pageContext.request.contextPath}/importData" enctype="multipart/form-data" 
			modelAttribute="dataImportBean" id="formImport">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">IMPORT DATA</h3>
				</div>
				<div class="panel-body">
					<table class="table-bordered profile_regist">
						<tr>
							<th>Choose Table
								<font class="red">*</font>
							</th>
							<td>
								<select id="nameTable" name="nameTable" class="form-control" required>
									<option value="" selected="selected"></option>
									<option value="01">BOOKS</option>
									<option value="02">BOOKDETAILS</option>
									<option value="03">CATEGORIES</option>
									<option value="04">AUTHORS</option>
									<option value="05">PUBLISHERS</option>
									<option value="06">USERS</option>
									<option value="07">PERMISSIONS</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>Import File
								<font class="red">*</font>
							</th>
							<td>
								<div class="form-inline">
								<input type="file" id="fileImport" name="fileImport" class="file" />
								<div class="input-group col-xs-12">
								  <input type="text" id="fileName" name="fileName" class="form-control" disabled placeholder="" />
								  <span class="input-group-btn">
									<button class="browse btn btn-primary" type="button"><i class="glyphicon glyphicon-search"></i> Choose File</button>
								  </span>
								</div>
							  </div>
							</td>
						</tr>
						
					</table>
				</div>
			</div>
			<div id="sub_btn">
				<input type="submit" value="Back" class="btn-forwardscreen" onclick="goBack()">
				<input type="submit" value="Import Data" class="btn-forwardscreen" id="btnImport">
			</div>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			<br>
			<c:if test="${!empty listError}">
			<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">ERROR LIST</h3>
			</div>
			<div class="panel-body">
				<table class="table-bordered profile_info">
					<tr>
						<th> Line Number</th>
						<th> Column</th>
						<th> Error</th>
					</tr>
					<c:forEach items="${listError}" var="error">
					<tr>
						<td>${error.numberLine}</td>
						<td>${error.column}</td>
						<td>${error.error}</td>
					</tr>
					</c:forEach>
				</table>
			</div>
			</div>
			</c:if>
			</form>
		</div>
	</section>
	
</body>

</html>
