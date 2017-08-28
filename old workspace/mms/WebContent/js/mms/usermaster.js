var usrId;
var btnPress;
var addDlg;
var numberRegex = /^[+-]?\d+(\.\d+)?([eE][+-]?\d+)?$/;

$(document).ready(function() {
	addDlg = $("#dialog").dialog({
		autoOpen: false,
		top: 40,
		height: "auto",
		modal: true,
		buttons: {
			Ok: function() {
				if(btnPress == "add")
					add();
				else if(btnPress == "update")
					update();
			},
	    	Cancel: function() {
	      		addDlg.dialog( "close" );
	    	}
	  	},
	  	open: function() {
	  		$("#code").focus();
	  	},
	  	close: function() {
	  		$("#add-form")[0].reset()
	  	}
	});
	
	$("#row tr").click(function() {
		selectRow($(this).attr("id"));
	});
	
	$("#add-btn").click(function() {
		btnPress = "add";
		openDlg("add");
		$("#code").focus();

	});
	
	$("#update-btn").click(function() {
		btnPress = "update";
		perpareUpdate();
	});
	
	$("#delete-btn").click(function() {
		btnPress = "delete";
		del();
	});
});

function selectRow(id) {
	if(usrId)
		$("#" + usrId).removeClass("selected-row");
	usrId = id;
	$("#" + usrId).addClass("selected-row");
	$("#update-btn").prop("disabled", false);
	$("#delete-btn").prop("disabled", false);
}


function openDlg(type) {
	if(type == "add") {
		$(".ui-widget-header").css("background", "#9ECFDE");
		addDlg.dialog("option", "title", "Add User Master");
		addDlg.dialog("open");
	}
	else if(type == "update") {
		$(".ui-widget-header").css("background", "#DDFA9D");
		addDlg.dialog("option", "title", "Update User Master");
		addDlg.dialog("open");
	}
}

function add() {
	var code = $("#code").val();
	var name = $("#name").val();
	var roId = $("#roId").val();
	var roName = $("#roId option:selected").text();
	var password = $("#password").val();
	
	if(roId == 0) {
		alert("Please select Role.");
		return;
	}
	
	$.ajax({
		type:"PUT",
		url:"rest/usermasterWs",
		data: {
			"code": code,
			"name": name,
			"password": password,
			"roId": roId
		},
		success:function(result) {
			if(numberRegex.test(result)) {
				$("#row").append("<tr id='" + result + "'><td>" + result + "</td><td>" + code + "</td><td>" + name + "</td><td>" 
						+password + "</td><td id='r" + roId + "'>" + 
						roName + "</td></tr>");
				$("#" + result).click(function() {
					selectRow(result);
				});
				$("#" + result).click();
				addDlg.dialog("close");
			}
			else alert("Wrong format value!");
		}
	});
}

function perpareUpdate() {
	var e = $("#" + usrId);
	$("#usrId").val(usrId);
	$("#code").val($(e).children().eq(1).html());
	$("#name").val($(e).children().eq(2).html());
	$("#password").val($(e).children().eq(3).html());
	var roName = $(e).children().eq(4).html();
	$("select option").filter(function() {
	    //may want to use $.trim in here
	    return $(this).text() == roName; 
	}).prop('selected', true);
	
	openDlg("update");
}

function update() {
	var usrId = $("#usrId").val();
	var code = $("#code").val();
	var name = $("#name").val();
	var roName = $("#roId option:selected").text();
	var password = $("#password").val();
	var roId = $("#roId").val();
	if(roId == 0) {
		alert("Please select role.");
		return;
	}
	
	$.ajax({
		type:"POST",
		url:"rest/usermasterWs",
		data: {
			"usrId": usrId,
			"code": code,
			"name": name,
			"password": password,
			"roId": roId
		},
		success:function(result) {
			if(result == "true") {
				var e = $("#" + usrId);
				$(e).children().eq(1).html(code);
				$(e).children().eq(2).html(name);
				$(e).children().eq(3).html(password);
				$(e).children().eq(4).html(roName);
				addDlg.dialog("close");
			}
			else alert("Cannot update User Master Code. " + $("#code").val() + "!");
		}
	});
}

function del() {
	var code = $("#" + usrId).children().eq(0).html();
	if(confirm("Do you want to delete User Master Code. " + code + "?")) {
		$.ajax({
			type:"DELETE",
			url:"rest/usermasterWs",
			data: {
				"usrId": usrId
			},
			success:function(result) {
				if(result == "true") {
					$("#" + usrId).remove();
					addDlg.dialog("close");
				}
				else alert("Cannot remove User Master Code. " + code + "!");
			}
		});
	}
}