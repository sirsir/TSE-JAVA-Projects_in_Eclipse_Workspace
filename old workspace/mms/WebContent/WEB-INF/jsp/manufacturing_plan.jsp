<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Registation Manufacturing Plan</title>
	<link rel="stylesheet" href="/mms/css/html5reset.css">
	<link rel="stylesheet" href="/mms/css/jquery-ui.css">
	<link rel="stylesheet" href="/mms/css/jquery-clockpicker.min.css">
	<link rel="stylesheet" href="/mms/css/style.css">
	<script src="/mms/js/jquery-2.1.1.min.js"></script>
	<script src="/mms/js/jquery-ui.js"></script>
	<script src="/mms/js/jquery-clockpicker.min.js"></script>
	<script src="/mms/js/util.js"></script>
	<script src="/mms/js/mms/manufacturing_plan.js"></script>
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
			<button id="add-btn" class="button">Add</button><button id="update-btn" class="button">Update</button><button id="delete-btn" class="button">Delete</button>
		</div>
	</section>
	<section class="data">
		<table>
			<thead>
				<tr><td>Date</td><td>Plan No.</td><td>Product Name</td><td>Planned Qty.</td><td>Plan to Start</td><td>Plan to Finish</td><td>Start Time</td><td>Finish Time</td></tr>
			</thead>
			<tbody id="row">
				<c:forEach items="${model.manufacturingPlanLst}" var="manufacturingPlan">
					<tr id="${manufacturingPlan.mpId}">
						<td>${manufacturingPlan.mpDate.dateString}</td>
						<td>${manufacturingPlan.no}</td>
						<td id="p${manufacturingPlan.prdId}">${manufacturingPlan.prdName}</td>
						<td class="td-number">${manufacturingPlan.qty}</td>
						<td>${manufacturingPlan.startPlan.fullDateTimeString}</td>
						<td>${manufacturingPlan.finishPlan.fullDateTimeString}</td>
						<td>${manufacturingPlan.startActual.fullDateTimeString}</td>
						<td>${manufacturingPlan.finishActual.fullDateTimeString}</td>
				</c:forEach>
			</tbody>
		</table>
	</section>
</div>

<div id="dialog">
	<form id="add-form" class="dlg-panel">
		<input type="hidden" id="mpId">
		<div class="input-row">
			<label for="date">Date:</label><input type="date" id="date">
		</div>
		<div class="input-row">
			<label for="no">Plan No:</label><input type="text" id="no" disabled>
		</div>
		<div class="input-row">
			<label for="prdId">Product:</label><select id="prdId">
				<c:forEach items="${model.productLst}" var="product">
					<option value="${product.prdId}">${product.name}</option>
				</c:forEach>
			</select>
		</div>
		<div class="input-row">
			<label for="planQty">Planned Qty:</label><input type="number" id="planQty">
		</div>
		<div class="input-row">
			<label for="startPlan">Plan to Start:</label><input type="date" id="startPlan" style="width:120px"><input type="text" id="startTime" class="dt-time">
		</div>
		<div class="input-row">
			<label for="finishPlan">Plan to Finish:</label><input type="date" id="finishPlan" style="width:120px"><input type="text" id="finishTime" class="dt-time">
		</div>
	</form>
</div>

<script>
	$('#startTime').clockpicker({
		placement: 'bottom',
		align: 'left',
		autoclose: true,
		'default': 'now'
		/* twelvehour: true */
	});
	$('#finishTime').clockpicker({
		placement: 'bottom',
		align: 'left',
		autoclose: true,
		'default': 'now'
	});
</script>
</body>
</html>