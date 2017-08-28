<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Manufacturing Plan and Results</title>
	<link rel="stylesheet" href="/mms/css/html5reset.css">
	<link rel="stylesheet" href="/mms/css/jquery-ui.css">
	<link rel="stylesheet" href="/mms/css/style.css">
	<style>
		.mp-status-production {color: purple}
		.mp-status-finished {color: green}
		.mp-status-break {color: blue}
		.mp-status-canceled {color: red}
	</style>
	<script src="/mms/js/jquery-2.1.1.min.js"></script>
	<script src="/mms/js/jquery-ui.js"></script>
	<script src="/mms/js/util.js"></script>
	<script src="/mms/js/mms/manufacturing_results.js"></script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div id="content" class="full-content">
	<section class="toolbar">
		<div class="filter">
			<label for="start">Manufacturing Date</label>
			<input id="start" type="date" value="${model.startDate.dateString}"> ~ <input id="end" type="date" value="${model.endDate.dateString}"><img id="find" src="images/refresh.png">
		</div>
		<div>
			<button id="start-btn" class="button" disabled>Start</button><button id="finish-btn" class="button" disabled>Finish</button><button id="break-btn" class="button" disabled>Break</button><button id="restart-btn" class="button" disabled>Restart</button><button id="cancel-btn" class="button" disabled>Cancel</button>
		</div>
	</section>
	<section class="data">
		<table>
			<thead>
				<tr><td>Date</td><td>Plan No.</td><td>Product Name</td><td>Planned Qty.</td><td>Result Qty.</td><td>Plan to Start</td><td>Plan to Finish</td><td>Start Time</td><td>Finish Time</td><td>Status</td><td>Result</td></tr>
			</thead>
			<tbody id="row">
				<c:forEach items="${model.manufacturingPlanLst}" var="manufacturingPlan">
					<tr id="${manufacturingPlan.mpId}">
						<td>${manufacturingPlan.mpDate.dateString}</td>
						<td>${manufacturingPlan.no}</td>
						<td id="p${manufacturingPlan.prdId}">${manufacturingPlan.prdName}</td>
						<td class="td-number">${manufacturingPlan.qty}</td>
						<td></td>
						<td>${manufacturingPlan.startPlan.fullDateTimeString}</td>
						<td>${manufacturingPlan.finishPlan.fullDateTimeString}</td>
						<td>${manufacturingPlan.startActual.fullDateTimeString}</td>
						<td>${manufacturingPlan.finishActual.fullDateTimeString}</td>
						<c:choose>
							<c:when test="${manufacturingPlan.statusName == 'In production'}"><c:set var="c" value="mp-status-production"/></c:when>
							<c:when test="${manufacturingPlan.statusName == 'Finished'}"><c:set var="c" value="mp-status-finished"/></c:when>
							<c:when test="${manufacturingPlan.statusName == 'Break'}"><c:set var="c" value="mp-status-break"/></c:when>
							<c:when test="${manufacturingPlan.statusName == 'Canceled'}"><c:set var="c" value="mp-status-canceled"/></c:when>
						</c:choose>
						<td class="${c}">${manufacturingPlan.statusName}</td>
						<td></td>
				</c:forEach>
			</tbody>
		</table>
	</section>
</div>

<!-- <div id="start-dialog">
	<form id="add-form" class="dlg-panel">
		<input type="hidden" id="mpId">
		<div class="input-row">
			<label for="date">Date:</label><input type="date" id="date" disabled>
			<label for="no">Plan No:</label><input type="text" id="no" disabled>
		</div>
		<div class="input-row">
			<label for="prdId">Product:</label><input type="text" id="no" disabled>
			<label for="planQty">Planned Qty:</label><input type="number" id="planQty">
		</div>
	</form>
</div> -->
</body>
</html>