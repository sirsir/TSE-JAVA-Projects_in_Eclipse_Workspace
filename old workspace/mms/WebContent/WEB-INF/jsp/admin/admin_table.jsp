<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>mms - <fmt:message key="${model.table}"/> List</title>
<meta name="HandheldFriendly" content="True">
<meta name="MobileOptimized" content="320">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="/mms/css/html5reset.css">
<link rel="stylesheet" href="/mms/css/responsivegridsystem.css">
<link rel="stylesheet" href="/mms/css/col.css">
<link rel="stylesheet" href="/mms/css/jquery.dataTables.css">
<script src="/mms/js/jquery-2.1.1.min.js"></script>
<script src="/mms/js/jquery.dataTables.min.js"></script>
<script type="text/javascript">

	$(document).ready(function() {
		$('#table').dataTable();
		$(".row").click(function() {
			var id = $(this).attr("id");
			var s = id.split("-");
			if(s[0] == "team_member"){
				window.location = "team_member_form.htm?tmId=" + s[1];
			} else window.location = "admin_form.htm?table=" + s[0] + "&id=" + s[1];
		});
	});
	
</script>

</head>
<body>
	<jsp:include page="../header.jsp" />
	<div id="content" class="full-content">

		<div class="section group">
			<div class="col span-3-of-3">
				<div style="margin-bottom: 10px;" id="txtNewSupplier">
					<a 
					<c:choose>
						<c:when test="${model.table == 'team_member'}">
								href="team_member_form.htm"
						</c:when>
						<c:otherwise>
							href="admin_form.htm?table=<c:out value="${model.table}"/>"
						</c:otherwise> 
					</c:choose>
					style="text-decoration: none; text-transform: capitalize;"><img
						alt="New <fmt:message key="${model.table}"/>" src="/mms/images/add.png">&nbsp;New
						<fmt:message key="${model.table}"/></a>
				</div>
			</div>
		</div>
		<div class="section group">
			<div class="col span-3-of-3">
				<table id="table" class="display" cellspacing="0" width="100%">
					<thead>
						<tr style="text-transform: capitalize;">
							<c:forEach items="${model.tableField}" var="tableField" varStatus="count">
								<td>
									<c:choose>
											<c:when test="${count.index == 0}">
												<c:out value="${tableField.fieldName}" />
												<input type="hidden" name="pk" value="${tableField.fieldName}"/>
											</c:when>
											<c:otherwise>
												<fmt:message key="${tableField.fieldName}"/>	
											</c:otherwise>
										</c:choose>		
								</td>
							</c:forEach>
						</tr>
					</thead>
					<tbody>
						
						<c:forEach items="${model.tableData}" var="tableData" >
							<tr class="row" id="<c:out value="${model.table}"/>-<c:out value="${tableData.fieldValue[0]}"/>">
								<c:set var="tbField" value="${model.tableField}"/>
								<c:forEach items="${tableData.fieldValue}" var="field" varStatus="status">
									<td>
										<c:choose>
											<c:when test="${tbField[status.index].fieldName == 'expired' }">
												<c:choose>
													<c:when test="${field == '0' }">
														<c:out value="No"/>
													</c:when>
													<c:otherwise>
														<c:out value="Yes"/>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<c:out value="${field}"/>
												<c:if test="${field == 'null' }">
												<c:out value ="*"/>
												</c:if>
											</c:otherwise>
										</c:choose>
									</td>
								</c:forEach>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<%-- <jsp:include page="footer.jsp"/> --%>
</body>
</html>
