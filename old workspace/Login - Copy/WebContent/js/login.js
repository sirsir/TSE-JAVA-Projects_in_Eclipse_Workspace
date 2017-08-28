function clearPic() {
	document.getElementById("picture-frame").style.display = "none";
}
function clearMsg() {
	document.getElementById("msg").innerHTML = "";
}
function login(type) {
	var user = document.getElementById("user");
	var url = "login?type=" + type + "&user=" + user.value;
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
			if(name == 'authorized') {
				var msg = document.getElementById("msg");
				if(value == "f") {
					msg.innerHTML="Wrong Username/Password!";
					user.focus();
					user.select();
				}
				else msg.innerHTML="Welcome " + value;
			}
			else if(value != "") {
				document.getElementById("picture").src = "images/user/" + value;
				document.getElementById("picture-frame").style.display = "block";
			}
	  	}
	};
	httpReq.send();
}