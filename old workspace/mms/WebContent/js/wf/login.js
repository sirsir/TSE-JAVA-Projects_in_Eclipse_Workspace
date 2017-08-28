

function userEnter(e) {
	if(e.keyCode == 13 || e.which == 13) {
		var v = document.getElementById("user").value;
		v = v.trim();
		if(v.length > 0) {
			document.getElementById("password").focus();
		}
        return false;
    }
    return true;
}

function passwordEnter(e) {
	if(e.keyCode == 13 || e.which == 13) {
		var v = document.getElementById("password").value;
		v = v.trim();
		if(v.length > 0) {
			login('login');
		}
        return false;
    }
    return true;
}

function clearPic() {
	document.getElementById("picture-frame").style.display = "none";
}
function clearMsg() {
	document.getElementById("msg").innerHTML = "";
}

function force() {
	var uri = document.getElementById("uri");
	var url = "login?type=forceAdmin&uri=" + uri.value;
	var httpReq = new XMLHttpRequest();
	httpReq.open("get", url, true);
	httpReq.onreadystatechange=function() {
	  	if(httpReq.readyState==4) {
	  		var xml=httpReq.responseXML;
	  		var xmlObj=xml.documentElement;
			var items=xmlObj.getElementsByTagName("item");
			var uri0 = items[0].getAttribute("uri");
			window.location = uri0;
	  	}
	};
	httpReq.send();
}

function login(type) {
	var user = document.getElementById("user");
	var uri = document.getElementById("uri");
	var url = "login?type=" + type + "&user=" + user.value + "&uri=" + uri.value;
	if(type == "login") {
		var password = document.getElementById("password").value;
		url += "&password=" + password;
	}
	var httpReq = new XMLHttpRequest();
	httpReq.open("get", url, true);
	httpReq.onreadystatechange=function() {
	  	if(httpReq.readyState==4) {
	  		var xml=httpReq.responseXML;
	  		var xmlObj=xml.documentElement;
			var items=xmlObj.getElementsByTagName("item");
			var name=items[0].getAttribute("name");
			var value=items[0].getAttribute("value");
			var uri0 = items[0].getAttribute("uri");
			if(name == 'authorized') {
				var msg = document.getElementById("msg");
				if(value == "f") {
					msg.innerHTML="Wrong Username/Password!";
					document.getElementById("uri").value = uri0;
					user.focus();
					user.select();
				}
				else {
					if(uri0 == "")
						uri0 = "/cog/wf/home.htm";
					window.location = uri0;
				}
			}
			else if(value != "") {
				document.getElementById("picture").src = "/cog/images/user/" + value;
				document.getElementById("picture-frame").style.display = "block";
				document.getElementById("uri").value = uri0;
			}
	  	}
	};
	httpReq.send();
}