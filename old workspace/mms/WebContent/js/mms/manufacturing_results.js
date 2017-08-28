var mpId;
var numberRegex = /^[+-]?\d+(\.\d+)?([eE][+-]?\d+)?$/;

$(document).ready(function() {
	Date.prototype.toDateInputValue = (function() {
	    var local = new Date(this);
	    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
	    return local.toJSON().slice(0,10);
	});
	
	$("#find").click(function() {
		window.location = "manufacturing_results.htm?start=" + $("#start").val() + "&end=" + $("#end").val();
	});
	
	$("#row tr").click(function() {
		if(mpId)
			$("#" + mpId).removeClass("selected-row");
		mpId = $(this).attr("id");
		$("#" + mpId).addClass("selected-row");
		var status = $(this).children().eq(9).html();
		if(status == "" || status == "Break")
			startEnable();
		else if(status == "In production")
			finishEnable();
		else done();
	});
	
	$("#row tr").dblclick(function() {
		window.location = "production_results.htm?mpId=" + mpId;
	});
	
	$("#start-btn").click(function() {
		var s = $("#" + mpId).children().eq(9).html();
		if(s == "Break")
			operate("continue");
		else operate("start");
	});
	$("#finish-btn").click(function() {
		operate("finish");
	});
	$("#break-btn").click(function() {
		operate("break");
	});
	$("#restart-btn").click(function() {
		operate("restart");
	});
	$("#cancel-btn").click(function() {
		operate("cancel");
	});
});

function operate(type) {
	var planNo = $("#" + mpId).children().eq(1).html();
	if(confirm("Do you want to " + type + " Plan No. " + planNo + "?")) {
		$.ajax({
			type:"POST",
			url:"rest/manufacturingWs/" + type + "/" + mpId,
			data: {
				"mpId": mpId
			},
			success:function(result) {
				switch(type) {
					case "start": 
					case "restart": 
						finishEnable();
						$("#" + mpId).children().eq(7).html(result);
						$("#" + mpId).children().eq(9).html("In production");
						$("#" + mpId).children().eq(9).addClass("mp-status-production");
						break;
					case "continue": 
						finishEnable();
						$("#" + mpId).children().eq(9).html("In production");
						$("#" + mpId).children().eq(9).addClass("mp-status-production");
						break;
					case "break": 
						startEnable();
						$("#" + mpId).children().eq(9).html("Break");
						$("#" + mpId).children().eq(9).addClass("mp-status-break");
						break;
					case "finish":
						done();
						$("#" + mpId).children().eq(8).html(result);
						$("#" + mpId).children().eq(9).html("Finished");
						$("#" + mpId).children().eq(9).addClass("mp-status-finished");
						break;
					case "cancel":
						done();
						$("#" + mpId).children().eq(9).html("Canceled");
						$("#" + mpId).children().eq(9).addClass("mp-status-canceled");
						break;
				}
			}
		});
	}
}

function startEnable() {
	$("#start-btn").prop("disabled", false);
	$("#finish-btn").prop("disabled", true);
	$("#break-btn").prop("disabled", true);
	$("#restart-btn").prop("disabled", true);
	$("#cancel-btn").prop("disabled", true);
}

function finishEnable() {
	$("#start-btn").prop("disabled", true);
	$("#finish-btn").prop("disabled", false);
	$("#break-btn").prop("disabled", false);
	$("#restart-btn").prop("disabled", false);
	$("#cancel-btn").prop("disabled", false);
}

function done() {
	$("#start-btn").prop("disabled", true);
	$("#finish-btn").prop("disabled", true);
	$("#break-btn").prop("disabled", true);
	$("#restart-btn").prop("disabled", true);
	$("#cancel-btn").prop("disabled", true);
}