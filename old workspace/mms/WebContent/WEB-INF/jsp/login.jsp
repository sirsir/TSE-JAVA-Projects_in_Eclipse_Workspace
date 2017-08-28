<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0"> 
	<title>Login</title>

	<link rel="stylesheet" href="/mms/css/html5reset.css" media="all">
	<link rel="stylesheet" href="/mms/css/responsivegridsystem.css" media="all">
	<link rel="stylesheet" href="/mms/css/wf/login.css" media="all">
	
	<link href='http://fonts.googleapis.com/css?family=Orbitron' rel='stylesheet'>
	
	<style>
		@media only screen and (max-width: 480px) {
			#login-form { 
				margin-top: 0px;
			}
		}
		
		@font-face {
			font-family: gearbox;
			src: url(/mms/fonts/GearBox.ttf);
		}
		
		body {
			background: #f8f8f8 url(/mms/images/wf/bg.png);
			margin-left: 0;
		}
		#login-bar {
			position: absolute;
		    top: 40%;
		    width: 100%;
		    height: 64px;
		    padding: 20px 0 20px 0;
		    background: rgba(194,129,42,0.6);
			border-bottom-right-radius: 60px;
		    -webkit-border-bottom-right-radius: 60px;
			-moz-border-radius-bottomright: 60px;
		}
		#name {
			float: left;
			position: relative;
			margin-left: 14%;
			color: rgb(148,121,94);
			font-family: gearbox;
			font-size: 64px;
		}
		#login-form {
			float: left;
			position: relative;
			margin-top: -70px;
			/* margin-left: 14%; */
			padding: 20px 20px 10px 20px;
			background-color: rgba(128,128,128,0.4);
			border-radius: 5px;
			-webkit-border-radius: 5px;
			-moz-border-radius: 5px;
		}
		
		#login-header {
			vertical-align: top;
			color: rgb(148,121,94);
			font-family: 'Orbitron', sans-serif;
		}
		#login-entry {
			margin: 30px 0 34px 0;
		}
		.label {
			width: 76px;
			display: inline-block;
		}
		#sign-in {
			float: right;
		}
		#msg {
			float: left;
			position: relative;
			top: 25px;
			left: 20px;
			color: red;
			font-family: 'Orbitron', sans-serif;
		}
		#picture-frame {
			float: right;
			position: relative;
			margin-left: 14%;
			margin-top: -30px;
			display: none;
		}
		#picture {
			height: 50px;
			border-radius: 10px;
			-webkit-border-radius: 10px;
			-moz-border-radius: 10px;
		}
	</style>
	
	<script src="/mms/js/wf/login.js"></script>
</head>
<body>
	<input type="hidden" id="uri" value="${model.uri}"/>
	<div id="login-bar">
		<div id="name">
			mms
		</div>
		
		<div id="login-form">
			<div id="login-header">Login</div>
			<div id="picture-frame">
				<img src="" id="picture">
			</div>
			<div id="login-entry">
				<label for="user" class="label">Username:</label><input type="text" id="user" autofocus onblur="login('chkName')" onfocus="clearPic()" onkeypress="userEnter(event)" value="wnokkae"><br><br>
				<label for="password" class="label">Password:</label><input type="password" id="password" onfocus="clearMsg()" onkeypress="passwordEnter(event)" value="k250122">
			</div>
			<div id="login-footer">
				<input type="button" id="sign-in" value="Sign In" onclick="login('login')">
				<c:if test="${model.message !=  ''}">
					<input type="button" id="forceAdmin" value="Force" onclick="force()">
				</c:if>
			</div>
		</div>
		<div id="msg">${model.message}</div>
	</div>
</body>
</html>
