var specId;
var btnPress;
var addDlg;
var numberRegex = /^[+-]?\d+(\.\d+)?([eE][+-]?\d+)?$/;

$(document).ready(function() {
	addDlg = $("#dialog").dialog({
		autoOpen: false,
		top: 40,
		width: 370,
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
	    		$('#part').hide();
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
		$('#part').hide();
		btnPress = "add";
		openDlg("add");
		$("#name").focus();

	});
	
	$("#update-btn").click(function() {
		btnPress = "update";
		perpareUpdate();
	});
	
	$("#delete-btn").click(function() {
		btnPress = "delete";
		del();
	});
	
	 $("#attrId").change(function(){
		 if($("#attrId").find(':selected').text() == "Button"||$("#attrId").find(':selected').text() =="Pull-down"){
			 $('#part').show();
			}else{
		     $('#part').hide();
		}
	  });
});

function selectRow(id) {
	if(specId)
		$("#" + specId).removeClass("selected-row");
	specId = id;
	$("#" + specId).addClass("selected-row");
	$("#update-btn").prop("disabled", false);
	$("#delete-btn").prop("disabled", false);
}


function openDlg(type) {
	if(type == "add") {
		$(".ui-widget-header").css("background", "#9ECFDE");
		addDlg.dialog("option", "title", "Add Specification Master");
		addDlg.dialog("open");
	}
	else if(type == "update") {
		$(".ui-widget-header").css("background", "#DDFA9D");
		addDlg.dialog("option", "title", "Update Specification Master");
		addDlg.dialog("open");
	}
}

function add() {
	var name = $("#name").val();
	var attrId = $("#attrId").val();
	var attrName = $("#attrId option:selected").text();
	var pastLst = [];
	
	for(i = 0; i < $('#part').children().length; i++) { 
		if($('#part'+i).val() != ""){
			pastLst.push($('#part'+i).val());	
		}
	}
	var part0 = "";
	var part1 = "";
	var part2 = "";
	var part3 = "";
	var part4 = "";
	var part5 = "";
	var part6 = "";
	var part7 = "";
	var part8 = "";
	var part9 = "";

	if (typeof pastLst[0]!== "undefined") {
		part0 = pastLst[0];
	}
	if (typeof pastLst[1]!= "undefined") {
		part1 = pastLst[1];
	}
	if (typeof pastLst[2]!= "undefined") {
		part2 = pastLst[2];
	}
	if (typeof pastLst[3]!= "undefined") {
		part3 = pastLst[3];
	}
	if (typeof pastLst[4]!= "undefined") {
		part4 = pastLst[4];
	}
	if (typeof pastLst[5]!= "undefined") {
		part5 = pastLst[5];
	}
	if (typeof pastLst[6]!= "undefined") {
		part6 = pastLst[6];
	}
	if (typeof pastLst[7]!= "undefined") {
		part7 = pastLst[7];
	}
	if (typeof pastLst[8]!= "undefined") {
		part8 = pastLst[8];
	}
	if (typeof pastLst[9]!= "undefined") {
		part9 = pastLst[9];
	}
	
	$.ajax({
		type:"PUT",
		url:"rest/specificationmasterWs",
		data: {
			"name": name,
			"attrId": attrId,
			"part0": part0,
			"part1": part1,
			"part2": part2,
			"part3": part3,
			"part4": part4,
			"part5": part5,
			"part6": part6,
			"part7": part7,
			"part8": part8,
			"part9": part9
		},
		success:function(result) {
			if(numberRegex.test(result)) {
				$("#row").append("<tr id='" + result + "'><td>" + result + "</td><td>" + name + "</td><td id=a"+attrId+">" + attrName + "</td><td>" 
						+part0 + "</td><td>" + part1 + "</td><td>" + part2 + "</td><td>" + part3 + "</td><td>" + part4 + "</td><td>" + part5 + "</td>" +
						"<td>" + part6 + "</td><td>" + part7 + "</td><td>" + part8 + "</td><td>" + part9 + "</td></tr>");
				$("#" + result).click(function() {
					selectRow(result);
				});
				$("#" + result).click();
				$('#part').hide();
				addDlg.dialog("close");
			}
			else alert("Wrong format value!");
		}
	});
}

function perpareUpdate() {
	var e = $("#" + specId);
	$("#specId").val(specId);
	$("#name").val($(e).children().eq(1).html());
	var attrName = $(e).children().eq(2).html();
	$("select option").filter(function() {
	    //may want to use $.trim in here
	    return $(this).text() == attrName; 
	}).prop('selected', true);
	if(attrName == "Button"|| attrName =="Pull-down"){
		$('#part').show();
	}else{
		$('#part').hide();
	}
	$("#part0").val($(e).children().eq(3).html());
	$("#part1").val($(e).children().eq(4).html());
	$("#part2").val($(e).children().eq(5).html());
	$("#part3").val($(e).children().eq(6).html());
	$("#part4").val($(e).children().eq(7).html());
	$("#part5").val($(e).children().eq(8).html());
	$("#part6").val($(e).children().eq(9).html());
	$("#part7").val($(e).children().eq(10).html());
	$("#part8").val($(e).children().eq(11).html());
	$("#part9").val($(e).children().eq(12).html());
	openDlg("update");
}

function update() {
	var specId = $("#specId").val();
	var name = $("#name").val();
	var attrId = $("#attrId").val();
	var attrName = $("#attrId option:selected").text();
	var pastLst = [];
	
	for(i = 0; i < $('#part').children().length; i++) { 
		if($('#part'+i).val() != ""){
			pastLst.push($('#part'+i).val());	
		}
	}
	var part0 = "";
	var part1 = "";
	var part2 = "";
	var part3 = "";
	var part4 = "";
	var part5 = "";
	var part6 = "";
	var part7 = "";
	var part8 = "";
	var part9 = "";

	if (typeof pastLst[0]!== "undefined") {
		part0 = pastLst[0];
	}
	if (typeof pastLst[1]!= "undefined") {
		part1 = pastLst[1];
	}
	if (typeof pastLst[2]!= "undefined") {
		part2 = pastLst[2];
	}
	if (typeof pastLst[3]!= "undefined") {
		part3 = pastLst[3];
	}
	if (typeof pastLst[4]!= "undefined") {
		part4 = pastLst[4];
	}
	if (typeof pastLst[5]!= "undefined") {
		part5 = pastLst[5];
	}
	if (typeof pastLst[6]!= "undefined") {
		part6 = pastLst[6];
	}
	if (typeof pastLst[7]!= "undefined") {
		part7 = pastLst[7];
	}
	if (typeof pastLst[8]!= "undefined") {
		part8 = pastLst[8];
	}
	if (typeof pastLst[9]!= "undefined") {
		part9 = pastLst[9];
	}
	
	
	$.ajax({
		type:"POST",
		url:"rest/specificationmasterWs",
		data: {
			"specId": specId,
			"name": name,
			"attrId": attrId,
			"part0": part0,
			"part1": part1,
			"part2": part2,
			"part3": part3,
			"part4": part4,
			"part5": part5,
			"part6": part6,
			"part7": part7,
			"part8": part8,
			"part9": part9
		},
		success:function(result) {
			if(result == "true") {
				var e = $("#" + usrId);
				$(e).children().eq(0).html(specId);
				$(e).children().eq(1).html(name);
				$(e).children().eq(2).html(attrName);
				$(e).children().eq(3).html(part0);
				$(e).children().eq(4).html(part1);
				$(e).children().eq(5).html(part2);
				$(e).children().eq(6).html(part3);
				$(e).children().eq(7).html(part4);
				$(e).children().eq(8).html(part5);
				$(e).children().eq(9).html(part6);
				$(e).children().eq(10).html(part7);
				$(e).children().eq(11).html(part8);
				$(e).children().eq(12).html(part9);
				addDlg.dialog("close");
			}
			else alert("Cannot update Specification Master Name. " + name + "!");
		}
	});
}

function del() {
	var name = $("#" + specId).children().eq(1).html();
	if(confirm("Do you want to delete Specification Master Name. " + name + "?")) {
		$.ajax({
			type:"DELETE",
			url:"rest/specificationmasterWs",
			data: {
				"specId": specId
			},
			success:function(result) {
				if(result == "true") {
					$("#" + specId).remove();
					addDlg.dialog("close");
				}
				else alert("Cannot remove Specification Master Name. " + name + "!");
			}
		});
	}
}