<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Ballllllllll</title>
	<link rel="stylesheet" href="/mms/css/html5reset.css">
	<link rel="stylesheet" href="/mms/css/jquery-ui.css">
	<link rel="stylesheet" href="/mms/css/style.css">
	<script src="/mms/js/jquery-2.1.1.min.js"></script>
	<script src="/mms/js/jquery-ui.js"></script>
	<script src="/mms/js/util.js"></script>
	<script src="/mms/js/mms/arrival.js"></script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div id="content" class="full-content">
	<section class="toolbar">
		<div class="filter">
			<label for="start">Arrival Date</label>
			<input id="start" type="date" value="${model.startDate.dateString}"> ~ <input id="end" type="date" value="${model.endDate.dateString}"><img id="find" src="images/refresh.png">
		</div>
		<div>
			<button id="add-btn" class="button">Add</button><button id="update-btn" class="button" disabled>Update</button><button id="delete-btn" class="button" disabled>Delete</button>
		</div>
	</section>
	<section class="data">
		<table>
			<thead>
				<tr><td>Date</td><td>Arrival No.</td><td>Product Name</td><td>Planned Qty</td><td>Arrival Qty</td></tr>
			</thead>
			<tbody id="row">
				<c:forEach items="${model.arrivalLst}" var="arrival">
					<tr id="${arrival.arrId}">
						<td>${arrival.arrDate.dateString}</td>
						<td>${arrival.no}</td>
						<td id="p${arrival.prdId}">${arrival.prdName}</td>
						<td class="td-number">${arrival.planQty}</td>
						<td class="td-number"><c:if test="${arrival.arrQty > 0}">${arrival.arrQty}</c:if></td>
				</c:forEach>
			</tbody>
		</table>
	</section>
</div>

<div id="dialog">
	<form id="add-form" class="dlg-panel">
		<input type="hidden" id="arrId">
		<div class="input-row">
			<label for="date">Date:</label><input type="date" id="date">
		</div>
		<div class="input-row">
			<label for="no">Arrival No:</label><input type="text" id="no" disabled>
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
			<label for="arrivalQty">Arrival Qty:</label><input type="number" id="arrivalQty">
		</div>
	</form>
</div>
</body>
</html>