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
	<link rel="stylesheet" href="/mms/css/style.css">
	<style>
		.mp-status-production {color: purple}
		.mp-status-finished {color: green}
		.mp-data thead td {
			background: gray;
			color: white;
			cursor: default;
		}
		.mp-data tbody td {
			background: #fafafa;
			cursor: default;
		}
		.big-btn {
			width: 80px;
			height: 40px;
			font-size: 18px;
			font-weight: bold;
			margin-left: 10px;
		}
		.ok {color: green;}
		.ng {color: orange;}
		.reject {color: red;}
		.refreshing {
			font-size: 14;
			font-style: italic;
			color: green;
		}
	</style>
	<script src="/mms/js/jquery-2.1.1.min.js"></script>
	<script src="/mms/js/jquery-ui.js"></script>
	<script src="/mms/js/util.js"></script>
	<script src="/mms/js/mms/production_results.js"></script>
	<script>
		var ppId = ${model.ppId};
	</script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div id="content" class="full-content">
	<input type="hidden" id="mpId" value="${model.manufacturingPlan.mpId}">
	<section>
		<table class="mp-data">
			<thead>
				<tr><td>Manufacturing Date</td><td>Plan No.</td><td>Product Name</td></tr>
			</thead>
			<tbody>
				<tr>
					<td>${model.manufacturingPlan.mpDate.dateString}</td>
					<td id="mp-no">${model.manufacturingPlan.no}</td>
					<td>${model.manufacturingPlan.prdName}</td>
				</tr>
			</tbody>
		</table>
	</section>
	<section class="toolbar" style="margin-top: 10px">
		<div>
			<button id="start-btn" class="button" disabled>Start</button><button id="finish-btn" class="button" disabled>Finish</button><span class="refreshing" style="display:none">Refreshing</span>
		</div>
	</section>
	<section class="data">
		<div  style="height:4px"></div>
		<table>
			<thead>
				<tr><td>Process</td><td>Plan Qty.</td><td>Result Qty.</td><td>Defective Qty.</td><td>Status</td><td>Start time</td><td>Finish time</td><td>Result</td></tr>
			</thead>
			<tbody id="pp-row">
				<c:forEach items="${model.pdtPrcLst}" var="pdtPrc">
					<tr id="${pdtPrc.ppId}">
						<td>${pdtPrc.prcName}</td>
						<td class="td-number">${pdtPrc.planQty}</td>
						<td class="td-number">${pdtPrc.resultQty}</td>
						<td class="td-number">${pdtPrc.defectiveQty}</td>
						
						<c:choose>
							<c:when test="${pdtPrc.startActual == null}"><td></td></c:when>
							<c:when test="${pdtPrc.finishActual == null}"><td class="mp-status-production">Working</td></c:when>
							<c:otherwise><td class="mp-status-finished">Finished</td></c:otherwise>
						</c:choose>
						<td>${pdtPrc.startActual.fullDateTimeString}</td>
						<td>${pdtPrc.finishActual.fullDateTimeString}</td>
						<td></td>
				</c:forEach>
			</tbody>
		</table>
	</section>
	<section class="toolbar" style="margin-top: 10px">
		<div>
			<label for="barcode">Barcode/SN</label><input type="text" id="barcode">
		</div>
	</section>
	<section class="data">
		<div  style="height:4px"></div>
		<table>
			<thead>
				<tr><td>Serial No.</td><td>Barcode Label No.</td><td>Start time</td><td>Finish time</td><td>Result</td></tr>
			</thead>
			<tbody id="pdt-row">
				<c:forEach items="${model.productionLst}" var="pdt">
					<tr id="pdt${pdt.pdtId}">
						<td>${pdt.serialNo}</td>
						<td>${pdt.barcode}</td>
						<td>${pdt.startActual.fullDateTimeString}</td>
						<td>${pdt.finishActual.fullDateTimeString}</td>
						<c:choose>
							<c:when test="${pdt.resultName == 'OK'}"><td class="ok">${pdt.resultName}</td></c:when>
							<c:when test="${pdt.resultName == 'NG'}"><td class="ng">${pdt.resultName}</td></c:when>
							<c:when test="${pdt.resultName == 'REJECT'}"><td class="reject">${pdt.resultName}</td></c:when>
							<c:otherwise><td></td></c:otherwise>
						</c:choose>
						
				</c:forEach>
			</tbody>
		</table>
	</section>
</div>

<div id="start-dlg" title="Auto Start">
	<br>
	<div class="input-row">
		<label for="date">Process: </label>&nbsp;<label id="start-dlg-process" style="font-weight:bold"></label>
	</div>
	<br>
	<div class="input-row">
		<label for="no">Serial No:</label>&nbsp;<label id="start-dlg-sn" style="font-weight:bold"></label>
	</div>
	<br>
	<div class="input-row">
		<label for="no">Barcode:</label>&nbsp;<label id="start-dlg-barcode" style="font-weight:bold"></label>
	</div>
	<br>
	<div class="input-row">
		<label for="no">This unit will be start in&nbsp;&nbsp;</label><span id="start-dlg-countdown" class="countdown">4</span>&nbsp;seconds.
	</div>
</div>

<div id="finish-dlg" title="Finish Result">
	<br>
	<div class="input-row">
		<label for="date">Process: </label>&nbsp;<label id="finish-dlg-process" style="font-weight:bold"></label>
	</div>
	<br>
	<div class="input-row">
		<label for="no">Serial No:</label>&nbsp;<label id="finish-dlg-sn" style="font-weight:bold"></label>
	</div>
	<br>
	<div class="input-row">
		<label for="no">Barcode:</label>&nbsp;<label id="finish-dlg-barcode" style="font-weight:bold"></label>
	</div>
	<br>
	<div class="input-row">
		<button id="ok" class="big-btn ok">OK</button><button id="ng" class="big-btn ng">NG</button><button id="reject" class="big-btn reject">REJECT</button>
	</div>
</div>

<div id="autoFinish-dlg" title="Auto Finish">
	<br>
	<div class="input-row">
		<label for="date">Process: </label>&nbsp;<label id="start-dlg-process" style="font-weight:bold"></label>
	</div>
	<div class="input-row">
		<label for="no">This unit will be finish in&nbsp;&nbsp;</label><span id="finish-dlg-countdown" class="countdown">4</span>&nbsp;seconds.
	</div>
</div>

<div id="sn-dlg" title="Serial No. Registration">
	<br>
	<div class="input-row">
		<label for="date">Barcode: </label>&nbsp;<label id="sn-barcode" style="font-weight:bold"></label>
	</div>
	<br>
	<div class="input-row">
		<label for="date">S/N: </label>&nbsp;<input type="text" id="sn-sn">
	</div>
</div>
</body>
</html>