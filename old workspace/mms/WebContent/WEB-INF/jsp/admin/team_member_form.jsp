<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>mms - team member</title>
	<meta name="HandheldFriendly" content="True">
	<meta name="MobileOptimized" content="320">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="/mms/css/html5reset.css">
	<link rel="stylesheet" href="/mms/css/jquery-ui.css">
	<link rel="stylesheet" href="/mms/css/responsivegridsystem.css">
	<link rel="stylesheet" href="/mms/css/col.css">
	<link rel="stylesheet" href="/mms/css/token-input-facebook.css">
	<style>
		.teamForm {
			padding: 10px;
			border : 1px solid black;
			border-radius: 5px;
		}
		.title {
			padding-top: 5px;
			padding-bottom: 5px;
		}
		.tableTitle {
			background: rgb(204, 204, 204);
			padding: 10px;
			border-radius: 5px;
			margin-bottom: 5px;
		}
		em{color: red;}
	</style>
	<script src="/mms/js/jquery-2.1.1.min.js"></script>
	<script src="/mms/js/jquery.tokeninput.min.js"></script>
	<script>
	$(document).ready(function() {
		var tmId = ${model.id};
		if(tmId != 0){
		$("#User").tokenInput("/mms/requestItems?type=userRef", {
				theme: "facebook",
				cache: true,
		        preventDuplicates: true,
		        tokenLimit : 1,
		        prePopulate: [{'id':$('#UserName').attr('userId'),'name': $('#userName').attr('userName')}]
		       });
		} else {
			$("#User").tokenInput("/mms/requestItems?type=userRef", {
				theme: "facebook",
				cache: true,
		        preventDuplicates: true,
		        tokenLimit : 1
		       });
		}
		$("#cancel").click(function() {
			window.history.back();
		});
	});
	</script>
</head>

<body>
	<jsp:include page="../header.jsp" />
	<div id="content" class="full-content">
		<div class="section group">
			<div class="col span-2-of-3">
				<!-- <div style="cursor:pointer;margin-bottom:10px;" id="txtNewSupplier">
				<img alt="New Component Supplier" src="images/add.png">&nbsp;New Supplier
			</div> -->
				<form class="teamForm">
					<div class="tableTitle">
						<h2 style="text-transform:capitalize;" >Team Member</h2>
					</div>
					<div>
						<table style="text-transform:capitalize;">
								<tr>
									<td class="title" style="width: 30%;">	
									TmId
									</td>
									<td>
									<c:choose>
										<c:when test="${model.teamMember.tmId != null}">
											<c:out value="${model.teamMember.tmId}" />
											<input type="hidden" name="tmId" value="${model.teamMember.tmId}" >
										</c:when>
										<c:otherwise>
											Auto Generate
										</c:otherwise>
									</c:choose>
									</td>
								</tr>
								<tr>
									<td class="title" style="width: 30%;">	
									Team
									</td>
									<td>
									<select class="selectMenu" name="tId">
										<c:forEach items="${model.team}" var="team">
												<option 
													<c:if test="${model.teamMember.TId == team.TId}">
														<c:out value="selected" />
													</c:if>
												value="<c:out value="${team.TId}"/>"><c:out value="${team.name}"/>
												</option>
										</c:forEach>
									</select>
									</td>
								</tr>
								<tr>
									<td class="title" style="width: 30%;">	
									User
									</td>
									<td>
									<input type="text" id="User" value="${model.teamMember.UId}" name="uId" >
									<input type="hidden" value="${model.teamMember.UId}" name="_uId" >
									<p id="userName" style="display: none;" userId="${model.teamMember.UId}" userName="<c:out value="${model.userName}"/>"></p>
									</td>
								</tr>
								<!-- <tr>
									<td class="title" style="width: 30%;">	
									Team Member Role
									</td>
									<td>
									
									</td>
								</tr>
								<tr>
									<td class="title" style="width: 30%;">	
									workMinute
									</td>
									<td>
									
									</td>
								</tr> -->
										<c:if test="${model.teamMember.tmId != null}">
								<tr>
									<td class="title" style="width: 30%;">	
									expired
									</td>
									<td>
									<select name="expired">
										<c:if test="${model.teamMember.expired == '0'}">
											<option value="1">Yes</option>
											<option value="0" selected>No</option>
										</c:if>
										<c:if test="${model.teamMember.expired == '1'}">
											<option value="1" selected>Yes</option>
											<option value="0">No</option>
										</c:if>
									</select>
									</td>
								</tr>
								</c:if>
								
						</table>
					</div>
					<hr />
					<div style="text-align: left; margin-top: -10px;">
						<c:choose>
							<c:when test="${model.id == 0}">
								<input type="submit" id="submit" name="submit" value="Add" />
							</c:when>
							<c:otherwise>
								<input type="submit" id="submit" name="submit" value="Update" />
							</c:otherwise>
						</c:choose>
						<input type="reset" id="cancel" name="cancel" value="cancel" />
					</div>
				</form>
			</div>
		</div>
	</div>

	<script src="/mms/js/jquery-ui.js"></script>
	<script>	
		$("#submit").button();
		$("#cancel").button();
	</script>

	<%-- <jsp:include page="footer.jsp"/> --%>

</body>
</html>
