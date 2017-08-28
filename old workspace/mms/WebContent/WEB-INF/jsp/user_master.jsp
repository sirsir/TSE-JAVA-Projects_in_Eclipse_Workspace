<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>User Master</title>
	<link rel="stylesheet" href="/mms/css/html5reset.css">
	<link rel="stylesheet" href="/mms/css/jquery-ui.css">
	<link rel="stylesheet" href="/mms/css/style.css">
	<script src="/mms/js/jquery-2.1.1.min.js"></script>
	<script src="/mms/js/jquery-ui.js"></script>
	<script src="/mms/js/util.js"></script>
	<script src="/mms/js/mms/usermaster.js"></script>
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
				<tr><td>No</td><td>User Code</td><td>User Name</td><td>Password</td><td>Role</td></tr>
			</thead>
			<tbody id="row">
				<c:forEach items="${model.userLst}" var="userLst">
					<tr id="${userLst.usrId}">
						<td>${userLst.usrId}</td>
						<td>${userLst.code}</td>
						<td>${userLst.name}</td>
						<td>${userLst.password}</td>
						<td id="r${userLst.roId}">${userLst.roName}</td>
				</c:forEach>
			</tbody>
		</table>
	</section>
</div>

<div id="dialog">
	<form id="add-form" class="dlg-panel">
		<input type="hidden" id="usrId">
		<div class="input-row">
			<label for="code">User Code:</label><input type="text" id="code">
		</div>
		<div class="input-row">
			<label for="name">User Name:</label><input type="text" id="name" >
		</div>
		<div class="input-row">
			<label for="password">Password:</label><input type="text" id="password">
		</div>
		<div class="input-row">
			<label for="roId">Role:</label><select id="roId">
				<c:forEach items="${model.roleLst}" var="role">
					<option value="${role.roId}">${role.name}</option>
				</c:forEach>
			</select>
		</div>
	</form>
</div>
</body>
</html>