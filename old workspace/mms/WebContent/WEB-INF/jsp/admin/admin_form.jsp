<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>mms</title>
	<meta name="HandheldFriendly" content="True">
	<meta name="MobileOptimized" content="320">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="/mms/css/html5reset.css">
	<link rel="stylesheet" href="/mms/css/jquery-ui.css">
	<link rel="stylesheet" href="/mms/css/responsivegridsystem.css">
	<link rel="stylesheet" href="/mms/css/col.css">
	<link rel="stylesheet" href="/mms/css/form.css">
	<style>
		em{color: red;}
	</style>
	<script src="/mms/js/jquery-2.1.1.min.js"></script>
	<script>
		$(document).ready(function() {
			 $("#companyList").change(function() {
				var value = $(this).val();
				$.ajax({
					type:"POST",
					url:"/mms/requestItems",
					data: {
						"type": "department",
						"coId": value
					},
					success:function(xml) {
						var html = "";
						$(xml).find('item').each(function(i,o) {
							var n=unescape($(this).attr("name"));
							var v=$(this).attr("value");
							html += "<option value=" + v + ">" + n + "</option>";
						});
						$("#departmentList").html(html);
					}
				});
			});
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
				<form class="form-flat-blue" id="frmNewComponentSupplier">
					<div class="title">
						<h2 style="text-transform:capitalize;" ><fmt:message key="${modelForm.table}"/></h2>
						<input type="hidden" name="table" value="${modelForm.table}"/>
					</div>
					<div>
						<table style="text-transform:capitalize;">
							<c:forEach items="${modelForm.formMap}" var="formMap"
								varStatus="count">
								<tr>
									<td class="title" style="width: 30%;">
										<c:choose>
											<c:when test="${count.index == 0}">
												<c:out value="${formMap.fieldName}" />
												<input type="hidden" name="pk" value="${formMap.fieldName}" required/>
											</c:when>
											<c:otherwise>
												<fmt:message key="${formMap.fieldName}"/>
												<c:if test="${formMap.nullable == 'NO' }">
												<em>*</em>
												</c:if>	
											</c:otherwise>
										</c:choose>		
									</td>
									<td><c:choose>
											<c:when test="${count.index == 0}">
												<c:out value="${formMap.fieldValue}" />
												<input 
												<c:if test="${formMap.nullable == 'NO'}">
												<c:out value="required"/>
												</c:if> 
												type="hidden" name="${formMap.fieldName}" value="${formMap.fieldValue}"/>
												<c:if test="${formMap.fieldValue == null}">
												<c:out value="Auto Generate" />
												</c:if>
											</c:when>
											<c:when test="${formMap.fieldName == 'expired'}">
												<select class="selectMenu" style="width:60px;" name="${formMap.fieldName}">
													<c:if test="${formMap.fieldValue == '0'}">
														<option value="1">Yes</option>
														<option value="0" selected>No</option>
													</c:if>
													<c:if test="${formMap.fieldValue == '1'}">
														<option value="1" selected>Yes</option>
														<option value="0">No</option>
													</c:if>
												</select>
											</c:when>
											<c:when test="${formMap.dataType == 'select'}">
												<select 
												<c:if test="${formMap.fieldName == 'coId' }">
													id = "companyList"
												</c:if>
												<c:if test="${formMap.fieldName == 'depId' }">
													id = "departmentList"
												</c:if>
												class="selectMenu" name="${formMap.fieldName}">
													<c:forEach items="${formMap.list}" var="list">
													<option 
														<c:if test="${formMap.fieldValue == list.value}">
															<c:out value="selected" />
														</c:if>
													value="<c:out value="${list.key}"/>"><c:out value="${list.value}"/>
													</option>
													</c:forEach>
												</select>
											</c:when>
											<c:when test="${formMap.fieldName == 'password'}">
												<input
												<c:if test="${formMap.nullable == 'NO'}">
												<c:out value="required"/>
												</c:if> 
												class="textbox" type="text" name="${formMap.fieldName}"
													value="<c:out value="${formMap.fieldValue}"/>" />
											</c:when>
											<c:when  test="${formMap.fieldName == 'email'}">
												<input
												<c:if test="${formMap.nullable == 'NO'}">
												<c:out value="required"/>
												</c:if> 
												class="textbox" type="email" name="${formMap.fieldName}"
													value="<c:out value="${formMap.fieldValue}"/>" />
											</c:when>
											<c:when test="${formMap.dataType == 'text'}">
												<input
												<c:if test="${formMap.nullable == 'NO'}">
												<c:out value="required"/>
												</c:if> 	
												class="textbox" type="text" name="${formMap.fieldName}"
													value="<c:out value="${formMap.fieldValue}"/>" />
											</c:when>
											<c:when test="${formMap.dataType == 'number'}">
												<input
												<c:if test="${formMap.nullable == 'NO'}">
												<c:out value="required"/>
												</c:if> 
												class="textbox" type="text" name="${formMap.fieldName}"
													value="<c:out value="${formMap.fieldValue}"/>" />
											</c:when>
										</c:choose></td>
								</tr>
							</c:forEach>
						</table>
					</div>
					<hr />
					<div style="text-align: left; margin-top: -10px;">
						<c:choose>
							<c:when test="${modelForm.id == null}">
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
