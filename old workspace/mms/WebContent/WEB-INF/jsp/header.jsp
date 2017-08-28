<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="/mms/css/nav.css">
	<link rel="stylesheet" href="/mms/css/content.css">
	<!-- <link rel="stylesheet" href="/mms/css/default.css" /> -->
	<link rel="stylesheet" href="/mms/css/responsive/responsive.menu.css" />
	
	<script src="/mms/js/jquery.sidr.min.js"></script>
	<script src="/mms/js/responsive-menu/modernizr.custom.js"></script>
	<script src="/mms/js/responsive-menu/jquery.dlmenu.js"></script>
	
	<script>
		$(document).ready(function() {
			$("#left-menu-toggle").sidr();
			$( '#dl-menu' ).dlmenu({
				animationClasses : { classin : 'dl-animate-in-2', classout : 'dl-animate-out-2' }
			});
			
			$("#left-menu-toggle").click(function() {
				var w = $("#sidr").css("left");
				var display = false;
				if(w == "0px") {
					display = true;
					$("#content").attr("class", "full-content");
				}
				else $("#content").attr("class", "half-content");
			});
			
			$("#search").keypress(function(event) {
				if(event.which == 13 || event.keyCode == 13) {
					event.preventDefault();
					v = $(this).val();
					v = v.trim();
					if(v.length > 0)
						window.location = "/mms/search.htm?text=" + v;
				}
			});
		});
	</script>
</head>
<body>
	<header>
		
		<a href = "/mms/home.htm" style="text-decoration: none;"><div id="prog-name" class="nav-item">mms</div></a>
		<div class="nav-item">
			<input type="text" id="search" placeholder="search" value="${model.text}">
		</div>
		<div style="clear:both"></div>
	</header>
	<nav id="left-sidebar">
		<div id="sidr">
			<!-- Your content -->
			<ul>
				<li><a href="/mms/ball_test.htm">Ball Testtttttt</a></li>
				<li><a href="/mms/arrival.htm">Arrival Plan</a></li>
				<li><a href="/mms/shipping.htm">Shipping Plan</a></li>
				<li><a href="/mms/manufacturing_plan.htm">Manufacturing Plan</a></li>
				<li><a href="/mms/manufacturing_results.htm">Manufacturing Results</a></li>
				<li><a href="/mms/production_results.htm">Production Results</a></li>
				<li>
					<ul>Admin
						<li>&nbsp;&nbsp;<a href="/mms/user_master.htm">User Master</a></li>
						<li>&nbsp;&nbsp;<a href="/mms/product_master.htm">Product Master</a></li>
						<li>&nbsp;&nbsp;<a href="/mms/specification_master.htm">Specification Master</a></li>
						<li>&nbsp;&nbsp;<a href="/mms/work_calendar.htm">Work Calendar</a></li>
					</ul>
				</li>
	  		</ul>
		</div>
		<!-- <div id="link-history"> -->
		<div id="link-bar">
			<a id="left-menu-toggle" href="#sidr"><img src="/mms/images/menu.png"></a>

			<c:set var="len" value="${fn:length(model.links)}"/>
        	<ul class="link" style="display:inline-block">
        		<c:forEach items="${model.links}" var="link" varStatus="status">
        			<c:choose>
       					<c:when test="${status.index == 0 && len == 1}">
        					<li class="link-current" style="z-index:<c:out value="${99-status.index}"/>"><a href="${link.url}"><img src="/mms/images/home.png"></a></li>
        				</c:when>
        				<c:when test="${status.index == 0}">
        					<li class="link" style="z-index:<c:out value="${99-status.index}"/>"><a href="${link.url}"><img src="/mms/images/home.png"></a></li>
        				</c:when>
        				<c:when test="${fn:length(model.links) == (status.index+1)}">
        					<li class="link-current" style="z-index:<c:out value="${99-status.index}"/>"><a href="${link.url}"><c:out value="${link.name}"/></a></li>
        				</c:when>
        				<c:otherwise>
        					<li class="link-middle" style="z-index:<c:out value="${99-status.index}"/>"><a href="${link.url}"><c:out value="${link.name}"/></a></li>
        				</c:otherwise>
        			</c:choose>
        			
					
        		</c:forEach>
			</ul>
		</div>
	</nav>
	
	<div id="header-gap"></div><div id="nav-gap"></div>
</body>
</html>
