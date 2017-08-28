<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>mms - Home</title>
	<link rel="stylesheet" href="/mms/css/html5reset.css">
	<link rel="stylesheet" href="/mms/css/wf/search.css">
	<script src="/mms/js/jquery-2.1.1.min.js"></script>
</head>
<body>
<jsp:include page="./header.jsp"/>
<div id="content" class="full-content">
	<div id="result">
		<c:choose>
			<c:when test="${model.summary == null}">
				<section class="header">Your search - ${model.text} - did not match any documents.</section>
				<section class="detail">
					<div id="suggestion">Suggestions:</div>
					<ul>
						<li>Make sure that all words are spelled correctly.</li>
						<li>Try different keywords.</li>
						<li>Try more general keywords.</li>		
					</ul>
				</section>
			</c:when>
			<c:otherwise>
				<section class="header">${model.summary}</section>
				<section class="detail">
					<c:forEach items="${model.searchResultLst}" var="result">
						<article>
							<div>
								<%-- <c:choose>
									<c:when test="${result.table == 'message'}"><a href="/mms/workflow/stage.htm?sgId=${result.sgId}#msg-${result.id}">${result.subject}</a></c:when>
									<c:otherwise><a href="/mms/wf/workflow/stage.htm?sgId=${result.sgId}">${result.subject}</a></c:otherwise>
								</c:choose> --%>
								
							</div>
							<div>${result.flow}</div>
							<c:if test="${result.text != 'null'}"><div>${result.text}</div></c:if>
							<div>${result.name}&nbsp;&nbsp;${result.dateTime}</div>
						</article>
					</c:forEach>
				</section>
			</c:otherwise>
		</c:choose>
	</div>
</div>
</body>
</html>
