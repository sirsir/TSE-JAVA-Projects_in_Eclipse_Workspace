<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Specification Master</title>
	<link rel="stylesheet" href="/mms/css/html5reset.css">
	<link rel="stylesheet" href="/mms/css/jquery-ui.css">
	<link rel="stylesheet" href="/mms/css/style.css">
	<script src="/mms/js/jquery-2.1.1.min.js"></script>
	<script src="/mms/js/jquery-ui.js"></script>
	<script src="/mms/js/util.js"></script>
	<script src="/mms/js/mms/specificationmaster.js"></script>
	<style>
		.dlg-panel label {
			width: 150px;
			text-align: right;
		}
		input[type=text]:disabled {
   			 background: #dddddd;
		}
	</style>
</head>
<body>
<jsp:include page="header.jsp"/>
<div id="content" class="full-content">
	<section class="toolbar">
		<div>
			<button id="add-btn" class="button">Add</button><button id="update-btn" class="button">Update</button><button id="delete-btn" class="button">Delete</button>
		</div>
	</section>
	<section class="data">
		<table>
			<thead>
				<tr><td>No</td><td>Specification Name</td><td>Attribute</td><td colspan="10">Part</td></tr>
			</thead>
			<tbody id="row">
				<c:forEach items="${model.specLst}" var="specLst">
					<tr id="${specLst.specId}">
						<td>${specLst.specId}</td>
						<td>${specLst.name}</td>
						<td id="a${specLst.attribute}">${model.nameAttrLst[specLst.attribute]}</td>
						<td>${specLst.part0}</td>
						<td>${specLst.part1}</td>
						<td>${specLst.part2}</td>
						<td>${specLst.part3}</td>
						<td>${specLst.part4}</td>
						<td>${specLst.part5}</td>
						<td>${specLst.part6}</td>
						<td>${specLst.part7}</td>
						<td>${specLst.part8}</td>
						<td>${specLst.part9}</td>
				</c:forEach>
			</tbody>
		</table>
	</section>
</div>

<div id="dialog">
	<form id="add-form" class="dlg-panel">
		<input type="hidden" id="specId">
		<div class="input-row">
			<label for="name">Specification Name:</label><input type="text" id="name">
		</div>
		<div class="input-row">
			<label for="attrId">Attribute:</label><select id="attrId">
			<c:forEach var="i" begin="0" end="4">
			  <option value="${i}">${model.nameAttrLst[i]}</option>
			</c:forEach>
			</select>
		</div>
		<div id="part" style="display:none">
			<c:forEach var="j" begin="0" end="9">
			<div class="input-row">
				<label for="code">Part ${j}:</label><input type="text" id="part${j}">			
			</div>
			</c:forEach>
		</div>
	</form>
</div>
</body>
</html>