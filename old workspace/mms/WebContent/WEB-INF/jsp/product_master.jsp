<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Product Master</title>
	<link rel="stylesheet" href="/mms/css/html5reset.css">
	<link rel="stylesheet" href="/mms/css/jquery-ui.css">
	<link rel="stylesheet" href="/mms/css/style.css">
	<script src="/mms/js/jquery-2.1.1.min.js"></script>
	<script src="/mms/js/jquery-ui.js"></script>
	<script src="/mms/js/util.js"></script>
	<script src="/mms/js/mms/productmaster.js"></script>
	<script>
	$(document).ready(function() {
		$("#${model.productId}").click();
		
	});
	
	
	</script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div id="content" class="full-content">
	<section class="toolbar">
		<label for="start">Product</label>
		<div>
			<button id="add-btn" class="button">Add</button><button id="copy-btn" class="button">Copy</button><button id="update-btn" class="button">Update</button><button id="delete-btn" class="button">Delete</button>
		</div>
	</section>
	<section class="data">
		<table>
			<thead>
				<tr><td>Product Code</td><td>Product Name</td><td>Type</td></tr>
			</thead>
			<tbody id="row">
				<c:forEach items="${model.productLst}" var="productLst">
					<tr id="${productLst.prdId}">
						<td>${productLst.code}</td>
						<td>${productLst.name}</td>
						<td id="r${productLst.scatId}">${productLst.scatName}</td>
				</c:forEach>
			</tbody>
		</table>
	</section>
	<br><br>
	<label for="start">Process <span id="nameProcess"></span></label>
	<section class="data">
		<table id="processTable">
			<thead>
				<tr><td>Process Name</td><td>Process Contents</td><td>Standard time(sec)</td><td>Manpower</td><td>Unit count</td><td>Print Label</td><td>Unit Size(cm2)</td>
				<td>Specification</td></tr>
			</thead>
			<tbody id="row-process">
				<c:forEach items="${model.ppsLst}" var="ppsLst">
					<tr id="p${ppsLst.prcId}">
						<td>${ppsLst.prdName}</td>
						<td>${ppsLst.content}</td>
						<td>${ppsLst.standardTime}</td>
						<td>${ppsLst.manPower}</td>
						<td>${ppsLst.unitCount}</td>
						<td>${ppsLst.printLabel}</td>
						<td>${ppsLst.unitSize}</td>
						<td id="r${ppsLst.specId}">${ppsLst.specName}</td>
				</c:forEach>
			</tbody>
		</table>
	</section>
</div>

<div id="dialog-product">
	<form id="add-form" class="dlg-panel">
		<input type="hidden" id="prdId">
		<div class="input-row">
			<label for="code">Product Code:</label><input type="text" id="code">
		</div>
		<div class="input-row">
			<label for="name">Product Name:</label><input type="text" id="name" >
		</div>
		<div class="input-row">
			<label for="scatId">Type:</label><select id="scatId">
				<c:forEach items="${model.scatLst}" var="scatLst">
					<option value="${scatLst.scatId}">${scatLst.name}</option>
				</c:forEach>
			</select>
		</div>
	</form>
</div>

<div id="dialog-process">
	<form id="add-form-process" class="dlg-panel">
		<input type="hidden" id="prcId">
		<div class="input-row">
			<label for="prdContent">Content:</label><input type="text" id="prcContent">
		</div>
		<div class="input-row">
			<label for="standardTime">Standard time(sec):</label><input type="number" id="standardTime" >
		</div>
		<div class="input-row">
			<label for="manPower">Manpower:</label><input type="number" id="manPower" >
		</div>
		<div class="input-row">
			<label for="unitCount">Unit count:</label><input type="number" id="unitCount" >
		</div>
		<div class="input-row">
			<label for="printlabel">Print Label:</label><input type="checkbox" id="printLabel" >
		</div>
		<div class="input-row">
			<label for="unitSize">Unit Size(cm2):</label><input type="number" id="unitSize" >
		</div>
		<div class="input-row">
			<label for="specId">Specification:</label><select id="specId">
				<c:forEach items="${model.specLst}" var="specLst">
					<option value="${specLst.specId}">${specLst.name}</option>
				</c:forEach>
			</select>
		</div>
	</form>
</div>

<div id="dialog-product-copy">
	<label for="header">- Copy Form</label><br>
	<div align="center">
		<table>
			<tr>
				<td id="copyCodeProductTable"></td>
				<td id="copyNameProductTable"></td>
			</tr>
		</table>
	</div>
	<br>
	<label for="header">- Copy To</label>
	<form id="add-form-copy" class="dlg-panel">
		<div class="input-row">
			<label for="copyCodePrd">Product Code:</label><input type="text" id="copyCodePrd">
		</div>
		<div class="input-row">
			<label for="copyNamePrd">Product Name:</label><input type="text" id="copyNamePrd" >
		</div>
	</form>
</div>

</body>
</html>