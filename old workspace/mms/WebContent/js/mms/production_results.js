var pdtId;
var startDlg;
var finishDlg;
var autoFinishDlg;
var close;
var numberRegex = /^[+-]?\d+(\.\d+)?([eE][+-]?\d+)?$/;

$(document).ready(function() {
	if(ppId)
		$("#" + ppId).addClass("selected-row");
	startDlg = $("#start-dlg").dialog({
		autoOpen: false,
		top: 40,
		height: "auto",
		modal: true,
		buttons: {
			Cancel: function() {
				startDlg.dialog( "close" );
	    	}
	  	},
	  	open: function() {
	  		close = false;
	  		var counter = 3;
			var interval = setInterval(function() {
				if(!close) {
				    $("#start-dlg-countdown").html(counter);
				    if (counter == 0) {
				        autoStart();
				        clearInterval(interval);
				    }
				    --counter;
				}
				else clearInterval(interval);			    
			}, 1000);
	  	},
	  	close: function() {
	  		close = true;
			$("#barcode").focus();
	  	}
	});
	
	finishDlg = $("#finish-dlg").dialog({
		autoOpen: false,
		top: 40,
		height: "auto",
		modal: true,
		buttons: {
			Cancel: function() {
				finishDlg.dialog( "close" );
				$("#barcode").focus();
	    	}
	  	}
	});
	
	autoFinishDlg = $("#autoFinish-dlg").dialog({
		autoOpen: false,
		top: 40,
		height: "auto",
		modal: true,
		buttons: {
			Cancel: function() {
				autoFinishDlg.dialog( "close" );
	    	}
	  	},
	  	open: function() {
	  		close = false;
	  		var counter = 3;
			var interval = setInterval(function() {
				if(!close) {
				    $("#finish-dlg-countdown").html(counter);
				    if (counter == 0) {
				        autoFinish();
				        clearInterval(interval);
				    }
				    --counter;
				}
				else clearInterval(interval);			    
			}, 1000);
	  	},
	  	close: function() {
	  		close = true;
			$("#barcode").focus();
	  	}
	});
	
	snDlg = $("#sn-dlg").dialog({
		autoOpen: false,
		top: 40,
		height: "auto",
		modal: true,
		buttons: {
			Ok: function() {
				updateSn();
			},
			Cancel: function() {
				snDlg.dialog( "close" );
				$("#barcode").focus();
	    	}
	  	}
	});
	
	Date.prototype.toDateInputValue = (function() {
	    var local = new Date(this);
	    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
	    return local.toJSON().slice(0,10);
	});
	
	$("#pp-row tr").click(function() {
		if(ppId)
			$("#" + ppId).removeClass("selected-row");
		ppId = $(this).attr("id");
		$("#" + ppId).addClass("selected-row");
		var status = $(this).children().eq(4).html();
		if(status == "")
			startEnable();
		else if(status == "Working")
			finishEnable();
		else done();
		refreshProduction();
		$("#barcode").focus();
	});
	$("#pdt-row tr").click(function() {
		pdtClick($(this).attr("id"));
	});
	
	$("#start-btn").click(function() {
		operate("start");
		$("#barcode").focus();
	});
	$("#finish-btn").click(function() {
		operate("finish");
		$("#barcode").focus();
	});
	
	$("#barcode").bind("keypress", function(e) {
		var code = e.keyCode || e.which;
		 if(code == 13) {
			 var v = $(this).val()
			 barcodeTrigger(v);
		 }
	});
	
	$("#sn-sn").bind("keypress", function(e) {
		var code = e.keyCode || e.which;
		 if(code == 13) {
			 updateSn();
		 }
	});
	
	$("#ok").click(function() {
		finishResult("OK");
	});
	
	$("#ng").click(function() {
		finishResult("NG");
	});

	$("#reject").click(function() {
		finishResult("REJECT");
	});
	
	setInterval(_refresh, 10000);
});

function pdtClick(id) {
	if(pdtId)
		$("#pdt" + pdtId).removeClass("selected-row");
	
	pdtId = id.substring(3, id.length);
	$("#" + id).addClass("selected-row");
	if($("#" + ppId).children().eq(0).html() == "Plating") {
		$("#sn-barcode").html($("#" + id).children().eq(1).html());
		alert("#" + id + "      " + $("#" + id).children().eq(0).html());
		$("#sn-sn").val($("#" + id).children().eq(0).html());
		snDlg.dialog("open");
	}
	else $("#barcode").focus();
}

function barcodeTrigger(barcode) {
	$.ajax({
		type:"GET",
		url:"rest/productionWs/trigger/" + $("#mpId").val() + "/" + barcode,
		success:function(result) {
			if(result.length > 0) {
				var cols = result.split("\t");
				if(cols.length > 1) {
					var e = $("#pdt-row");
					e.html("");
					pdtId = cols[0];
					$("#" + cols[1]).click();
					if(cols[4] == "") {
						if(cols[6] == "-1") {
							$("#" + cols[1]).children().eq(4).html("Working");
							$("#" + cols[1]).children().eq(4).addClass("mp-status-production");
							$("#" + cols[1]).children().eq(5).html(cols[5]);
						}
						
						$("#start-dlg-process").html($("#" + ppId).children().eq(0).html());
						$("#start-dlg-sn").html(cols[2]);
						$("#start-dlg-barcode").html(cols[3]);
						$("#start-dlg").dialog("open");
					}
					else {
						$("#finish-dlg-process").html($("#" + ppId).children().eq(0).html());
						$("#finish-dlg-sn").html(cols[2]);
						$("#finish-dlg-barcode").html(cols[3]);
						$("#finish-dlg").dialog("open");
					}
				}
				else alert(result);
			}
			$("#barcode").val("");
		}
	});
}

function _refresh() {
	$(".refreshing").css("display", "");
	$.ajax({
		type:"GET",
		url:"rest/productionWs/refresh?mpId=" + $("#mpId").val(),
		success:function(result) {
			if(result.length > 0) {
				var rows = result.split("\n");
				for(var i=0; i<rows.length; i++) {
					var cols = rows[i].split("\t");
					var pps = $("#" + cols[0]).children();
					$(pps).eq(1).html(cols[3]);
					$(pps).eq(2).html(cols[4]);
					$(pps).eq(3).html(cols[5]);
					//alert(rows[i]);
					if(cols[6] == "") {
						$(pps).eq(4).html("");
						$(pps).eq(5).html("");
						$(pps).eq(6).html("");
					}
					else if(cols[7] == "") {
						$(pps).eq(4).html("Working");
						$(pps).eq(4).addClass("mp-status-production");
						$(pps).eq(5).html(cols[6]);
						$(pps).eq(6).html("");
					}
					else {
						$(pps).eq(4).html("Finished");
						$(pps).eq(4).addClass("mp-status-finished");
						$(pps).eq(5).html(cols[6]);
						$(pps).eq(6).html(cols[7]);
					}
				}
				$("#" + ppId).click();
			}
			else alert("Refresh without result");
			$(".refreshing").css("display", "none");
		}
	});
}

function autoStart() {
	$.ajax({
		type:"POST",
		url:"rest/productionWs/trigger/autoStart/" + pdtId,
		success:function(result) {
			$("#pdt" + pdtId).children().eq(2).html(result);
			$("#pdt" + pdtId).addClass("selected-row");
		}
	});
	
	startDlg.dialog("close");
}

function autoFinish() {
	$.ajax({
		type:"POST",
		url:"rest/productionWs/trigger/autoFinish/" + pdtId,
		success:function(result) {
			var s = result.split("\t");
			
			$("#" + ppId).removeClass("selected-row");
			var e = $("#" + s[0]);
			$(e).children().eq(4).html("Finished");
			$(e).children().eq(4).removeClass(".mp-status-production");
			$(e).children().eq(4).removeClass(".mp-status-finished");
			$(e).children().eq(6).html(s[1]);
			$(e).click();
		}
	});
	
	autoFinishDlg.dialog("close");
}

function finishResult(result) {
	$.ajax({
		type:"POST",
		url:"rest/productionWs/trigger/finishResult/" + pdtId + "/" + result,
		success:function(data) {
			var s = data.split("\t");
			var e = $("#pdt" + pdtId);
			var c4 = $(e).children().eq(4);
			$(e).children().eq(3).html(s[0]);
			$(c4).html(s[1]);
			if(s[1] == "OK")
				$(c4).addClass("ok");
			else if(s[1] == "NG")
				$(c4).addClass("ng");
			else if(s[1] == "REJECT")
				$(c4).addClass("reject");
			$(e).addClass("selected-row");
			
			var pp = $("#" + s[2]);
			$(pp).children().eq(2).html(s[3]);
			$(pp).children().eq(3).html(s[4]);
			
			var pqty = $(pp).children().eq(1).html();
			if(parseInt(pqty) <= parseInt(s[3])) {
				$("#start-dlg-process").html($("#" + ppId).children().eq(0).html());
				autoFinishDlg.dialog("open");
			}
			
			$("#barcode").val("");
		}
	});
	
	finishDlg.dialog("close");
}

function refreshProduction() {
	$.ajax({
		type:"GET",
		url:"rest/productionWs",
		data: {
			"ppId": ppId
		},
		success:function(result) {
			var e = $("#pdt-row");
			e.html("");
			if(result.length > 0) {
				var rows = result.split("\n");
				for(var i=0; i<rows.length; i++) {
					var cols = rows[i].split("\t");
					
					var c = "";
					if(cols[7] == "OK")
						c = " class='ok'";
					else if(cols[7] == "NG")
						c = " class='ng'";
					else if(cols[7] == "REJECT")
						c = " class='reject'";
					else cols[7] == "";
					e.append("<tr id='pdt" + cols[0] + "'><td>" + cols[2] + "</td><td>" + cols[3] + "</td><td>" + cols[4] + "</td><td>" + cols[5] + "</td><td" + c + ">" + cols[7] + "</td></tr>");
					$("#pdt" + cols[0]).click(function () {
						pdtClick("pdt" + cols[0]);
					});
				}
			}
		}
	});
}

function operate(type) {
	var process = $("#" + ppId).children().eq(0).html();
	if(confirm("Do you want to " + type + " process " + process + "?")) {
		$.ajax({
			type:"POST",
			url:"rest/productionWs/" + type + "/" + ppId,
			data: {
				"ppId": ppId
			},
			success:function(result) {
				if(type == "start") { 
					finishEnable();
					$("#" + ppId).children().eq(5).html(result);
					$("#" + ppId).children().eq(4).html("Working");
					$("#" + ppId).children().eq(4).addClass("mp-status-production");
				}
				else if(type == "finish") {
					done();
					$("#" + ppId).children().eq(6).html(result);
					$("#" + ppId).children().eq(4).html("Finished");
					$("#" + ppId).children().eq(4).addClass("mp-status-finished");
				}
			}
		});
	}
}

function updateSn() {
	var sn = $("#sn-sn").val();
	
	$.ajax({
		type:"POST",
		url:"rest/productionWs/sn/" + pdtId + "/" + sn,
		success:function(result) {
			$("#pdt" + pdtId).children().eq(0).html(result);
			$("#pdt" + pdtId).click(function() {
				pdtClick("pdt" + pdtId);
			});
			snDlg.dialog( "close" );
			$("#barcode").focus();
		}
	});
}

function startEnable() {
	$("#start-btn").prop("disabled", false);
	$("#finish-btn").prop("disabled", true);
}

function finishEnable() {
	$("#start-btn").prop("disabled", true);
	$("#finish-btn").prop("disabled", false);
}

function done() {
	$("#start-btn").prop("disabled", true);
	$("#finish-btn").prop("disabled", true);
}