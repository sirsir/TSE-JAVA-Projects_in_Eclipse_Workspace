<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Work Calendar</title>
	<link rel="stylesheet" href="/mms/css/html5reset.css">
	<link rel="stylesheet" href="/mms/css/jquery-ui.css">
	<link rel="stylesheet" href="/mms/css/style.css">
	<link rel="stylesheet" href="/mms/css/jquery-clockpicker.css">
	<style>
		table td {
	   		text-align: center;
		}
		.time {
			width: 60px;
			text-align: center;
		}
		.dlg-panel label {
			width: 170px;
			text-align: right;
		}
		input[type=text]:disabled {
   			 background: #dddddd;
		}
		#monthYear {
		 	text-align: center;
		 	width: 100px;
		 	border-width: 1px;
		 	-webkit-border-radius:3px;
			-moz-border-radius:3px;
			border-radius:3px;
			background-color: rgba(240, 240, 240, 0.6);
		 }
		#monthYear:hover {
		 	background-color: rgba(173,255,47, 0.4);
		 }
	</style>
	<script src="/mms/js/jquery-2.1.1.min.js"></script>
	<script src="/mms/js/jquery-clockpicker.js"></script>
	<script src="/mms/js/jquery-ui.js"></script>
	<script src="/mms/js/util.js"></script>
	<script src="/mms/js/mms/workcalendar.js"></script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div id="content" class="full-content">
	<section class="toolbar">
		<div>
		    <img id="previousMonth"src="images/left.png" title="Previous month">
			<input id="monthYear" name="myDate" class="monthYearPicker" value="${model.monthYear}" title="Change month"/>
			<img id="nextMonth" src="images/right.png" title="Next month">
			<button id="defalt-btn" class="button">Input Default</button><button id="update-btn" class="button">Update</button>
		</div>
	</section>
	<section class="data">
		<table>
			<thead>
				<tr>
					<td rowspan="2">Day</td>
					<td rowspan="2">Holiday</td>
					<td colspan="2">Working time</td>
					<td colspan="2">Non-working Time 1</td>
					<td colspan="2">Non-working Time 2</td>
					<td colspan="2">Non-working Time 3</td>
					<td colspan="2">Non-working Time 4</td>
					<td colspan="2">Non-working Time 5</td>
				</tr>
				<tr>
					<td>Start</td><td>End</td>
					<td>Start</td><td>End</td>
					<td>Start</td><td>End</td>
					<td>Start</td><td>End</td>
					<td>Start</td><td>End</td>
					<td>Start</td><td>End</td>
				</tr>
			</thead>
			<tbody id="row">
				<c:forEach items="${model.taWclst}" var="wclst">
					<c:choose>
						<c:when test="${wclst.holiday != 0}">
							<c:set var="holiday" value='<img src="images/checkbox.png"/>'/>
						</c:when>
						<c:otherwise>
							<c:set var="holiday" value='<img src="images/checkbox_no.png"/>'/>
						</c:otherwise>
					</c:choose>
					<tr id="${wclst.wcdate}">
						<td>${wclst.wcday}</td>
						<td>${holiday}
							<input type="hidden" id="h-${wclst.wcdate}" value="${wclst.holiday}">
						</td>
						<td>${wclst.startWorkingTime}</td>
						<td>${wclst.endWorkingTime}</td>
						<td>${wclst.startNonWorkingTime1}</td>
						<td>${wclst.endNonWorkingTime1}</td>
						<td>${wclst.startNonWorkingTime2}</td>
						<td>${wclst.endNonWorkingTime2}</td>
						<td>${wclst.startNonWorkingTime3}</td>
						<td>${wclst.endNonWorkingTime3}</td>
						<td>${wclst.startNonWorkingTime4}</td>
						<td>${wclst.endNonWorkingTime4}</td>
						<td>${wclst.startNonWorkingTime5}</td>
						<td>${wclst.endNonWorkingTime5}</td>
				</c:forEach>
			</tbody>
		</table>
	</section>
</div>

<div id="dialog">
	<form id="update-form" class="dlg-panel">
		<input type="hidden" id="wcDate">
		<div class="input-row">
			<label for="holiday">Holiday :</label>
			<select id="holiday">
			  <option value="0">No</option>
			  <option value="1">Yes</option>
			</select>
		</div>
		<div class="input-row">
			<label for="startWorkingTime">Start Working Time:</label>
			<input type="text" id="startWorkingTime" class="text ui-widget-content ui-corner-all time">
			<label for="endWorkingTime">End Working Time:</label>
			<input type="text" id="endWorkingTime" class="text ui-widget-content ui-corner-all time">
		</div>	
		<div class="input-row">
			<label for="startNonWorkingTime1">Start Non Working Time 1:</label>
			<input type="text" id="startNonWorkingTime1" class="text ui-widget-content ui-corner-all time">
			<label for="endNonWorkingTime1">End Non Working Time 1:</label>
			<input type="text" id="endNonWorkingTime1" class="text ui-widget-content ui-corner-all time">
		</div>		
		<div class="input-row">
			<label for="startNonWorkingTime2">Start Non Working Time 2:</label>
			<input type="text" id="startNonWorkingTime2" class="text ui-widget-content ui-corner-all time">
			<label for="endNonWorkingTime2">End Non Working Time 2:</label>
			<input type="text" id="endNonWorkingTime2" class="text ui-widget-content ui-corner-all time">
		</div>	
		
		<div class="input-row">
			<label for="startNonWorkingTime3">Start Non Working Time 3:</label>
			<input type="text" id="startNonWorkingTime3" class="text ui-widget-content ui-corner-all time">
			<label for="endNonWorkingTime3">End Non Working Time 3:</label>
			<input type="text" id="endNonWorkingTime3" class="text ui-widget-content ui-corner-all time">
		</div>		
		<div class="input-row">
			<label for="startNonWorkingTime4">Start Non Working Time 4:</label>
			<input type="text" id="startNonWorkingTime4" class="text ui-widget-content ui-corner-all time">
			<label for="endNonWorkingTime4">End Non Working Time 4:</label>
			<input type="text" id="endNonWorkingTime4" class="text ui-widget-content ui-corner-all time">
		</div>	
		<div class="input-row">
			<label for="startNonWorkingTime5">Start Non Working Time 5:</label>
			<input type="text" id="startNonWorkingTime5" class="text ui-widget-content ui-corner-all time">
			<label for="endNonWorkingTime5">End Non Working Time 5:</label>
			<input type="text" id="endNonWorkingTime5" class="text ui-widget-content ui-corner-all time">
		</div>	
	</form>
</div>
<script>
	$('#startWorkingTime').clockpicker({
		placement: 'bottom',
		align: 'left',
		autoclose: true,
		'default': 'now'
	});
	$('#endWorkingTime').clockpicker({
		placement: 'bottom',
		align: 'left',
		autoclose: true,
		'default': 'now'
	});
	$('#startNonWorkingTime1').clockpicker({
		placement: 'bottom',
		align: 'left',
		autoclose: true,
		'default': 'now'
	});
	$('#endNonWorkingTime1').clockpicker({
		placement: 'bottom',
		align: 'left',
		autoclose: true,
		'default': 'now'
	});
	$('#startNonWorkingTime2').clockpicker({
		placement: 'bottom',
		align: 'left',
		autoclose: true,
		'default': 'now'
	});
	$('#endNonWorkingTime2').clockpicker({
		placement: 'bottom',
		align: 'left',
		autoclose: true,
		'default': 'now'
	});
	$('#startNonWorkingTime3').clockpicker({
		placement: 'bottom',
		align: 'left',
		autoclose: true,
		'default': 'now'
	});
	$('#endNonWorkingTime3').clockpicker({
		placement: 'bottom',
		align: 'left',
		autoclose: true,
		'default': 'now'
	});
	$('#startNonWorkingTime4').clockpicker({
		placement: 'bottom',
		align: 'left',
		autoclose: true,
		'default': 'now'
	});
	$('#endNonWorkingTime4').clockpicker({
		placement: 'bottom',
		align: 'left',
		autoclose: true,
		'default': 'now'
	});
	$('#startNonWorkingTime5').clockpicker({
		placement: 'bottom',
		align: 'left',
		autoclose: true,
		'default': 'now'
	});
	$('#endNonWorkingTime5').clockpicker({
		placement: 'bottom',
		align: 'left',
		autoclose: true,
		'default': 'now'
	});
</script>
</body>
</html>