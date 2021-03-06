var mpId;
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
	  		$("#date").focus();
	  	},
	  	close: function() {
	  		$("#add-form")[0].reset()
	  	}
	});
	
	Date.prototype.toDateInputValue = (function() {
	    var local = new Date(this);
	    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
	    return local.toJSON().slice(0,10);
	});
	
	$("#find").click(function() {
		window.location = "manufacturing_plan.htm?start=" + $("#start").val() + "&end=" + $("#end").val();
	});
	
	$("#row tr").click(function() {
		selectRow($(this).attr("id"));
	});
	
	$("#add-btn").click(function() {
		btnPress = "add";
		var today = new Date();
		var dcode = dateCode(today);
		$.get("rest/dateCodeWs?type=manufacturing&dcode=" + dcode, function(xml) {
			$("#no").val($(xml).find("dateCode").text());
		})
		.done(function() {
			$("#date").val(today.toDateInputValue());
			openDlg("add");
			$("#prdId").focus();
		});
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
	if(mpId)
		$("#" + mpId).removeClass("selected-row");
	mpId = id;
	$("#" + mpId).addClass("selected-row");
	$("#update-btn").prop("disabled", false);
	$("#delete-btn").prop("disabled", false);
}

function openDlg(type) {
	if(type == "add") {
		$(".ui-widget-header").css("background", "#9ECFDE");
		addDlg.dialog("option", "title", "Add manufacturing plan");
		addDlg.dialog("open");
	}
	else if(type == "update") {
		$(".ui-widget-header").css("background", "#DDFA9D");
		addDlg.dialog("option", "title", "Update manufacturing plan");
		addDlg.dialog("open");
	}
}

function add() {
	var date = $("#date").val();
	var no = $("#no").val();
	var prdId = $("#prdId").val();
	var prdName = $("#prdId option:selected").text();
	var planQty = $("#planQty").val();
	var startPlan = $("#startPlan").val() + " " + $("#startTime").val();
	var finishPlan = $("#finishPlan").val() + " " + $("#finishTime").val();
	
	if(prdId == 0) {
		alert("Please select product.");
		return;
	}
	else if(planQty.length == 0) {
		alert("Please fill Plan Qty data.");
		return;
	}
	$.ajax({
		type:"PUT",
		url:"rest/manufacturingWs",
		data: {
			"date": date,
			"no": no,
			"prdId": prdId,
			"planQty": planQty,
			"startPlan": startPlan,
			"finishPlan": finishPlan
		},
		success:function(result) {
			if(numberRegex.test(result)) {
				$("#row").append("<tr id='" + result + "'><td>" + date + "</td><td>" + no + "</td><td id='p" + prdId + "'>" + 
						prdName + "</td><td class='td-number'>" + planQty + "</td><td>" + startPlan + "</td><td>" + finishPlan + "</td><td></td><td></td></tr>");
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
	var e = $("#" + mpId);
	$("#mpId").val(mpId);
	$("#date").val($(e).children().eq(0).html());
	$("#no").val($(e).children().eq(1).html());
	
	var prdName = $(e).children().eq(2).html();
	$("select option").filter(function() {
	    //may want to use $.trim in here
	    return $(this).text() == prdName; 
	}).prop('selected', true);
	
	$("#planQty").val($(e).children().eq(3).html());
	
	var sp = $(e).children().eq(4).html().split(" ");
	$("#startPlan").val(sp[0]);
	$("#startTime").val(sp[1]);
	
	var fp = $(e).children().eq(5).html().split(" ");
	$("#finishPlan").val(fp[0]);
	$("#finishTime").val(fp[1]);
	
	openDlg("update");
}

function update() {
	var date = $("#date").val();
	var prdId = $("#prdId").val();
	var prdName = $("#prdId option:selected").text();
	var planQty = $("#planQty").val();
	var startPlan = $("#startPlan").val() + " " + $("#startTime").val();
	var finishPlan = $("#finishPlan").val() + " " + $("#finishTime").val();
	
	if(prdId == 0) {
		alert("Please select product.");
		return;
	}
	else if(planQty.length == 0) {
		alert("Please fill Plan Qty data.");
		return;
	}
	$.ajax({
		type:"POST",
		url:"rest/manufacturingWs",
		data: {
			"mpId": mpId,
			"date": date,
			"prdId": prdId,
			"planQty": planQty,
			"startPlan": startPlan,
			"finishPlan": finishPlan
		},
		success:function(result) {
			if(result == "true") {
				var e = $("#" + mpId);
				$(e).children().eq(0).html(date);
				$(e).children().eq(2).html(prdName);
				$(e).children().eq(3).html(planQty);
				$(e).children().eq(4).html(startPlan);
				$(e).children().eq(5).html(finishPlan);
				addDlg.dialog("close");
			}
			else alert("Cannot update Manufacturing Plan No. " + $("#no").val() + "!");
		}
	});
}

function del() {
	var no = $("#" + mpId).children().eq(1).html();
	if(confirm("Do you want to delete Manufacturing Plan No. " + no + "?")) {
		$.ajax({
			type:"DELETE",
			url:"rest/manufacturingWs",
			data: {
				"mpId": mpId
			},
			success:function(result) {
				if(result == "true") {
					$("#" + mpId).remove();
					addDlg.dialog("close");
				}
				else alert("Cannot remove Manufacturing Plan No. " + no + "!");
			}
		});
	}
}