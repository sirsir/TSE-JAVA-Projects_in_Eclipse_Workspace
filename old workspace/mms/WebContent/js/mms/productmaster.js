var prdId;
var prcId;
var btnPress;
var addPrdDlg;
var numberRegex = /^[+-]?\d+(\.\d+)?([eE][+-]?\d+)?$/;

$(document).ready(function() {
	addPrdDlg = $("#dialog-product").dialog({
		autoOpen: false,
		top: 40,
		height: "auto",
		modal: true,
		buttons: {
			OK: function() {
				if(btnPress == "add")
					add();
				else if(btnPress == "update")
					prdUpdate();
			},
	    	Cancel: function() {
	      		addPrdDlg.dialog( "close" );
	    	}
	  	},
	  	open: function() {
	  		$("#code").focus();
	  	},
	  	close: function() {
	  		$("#add-form")[0].reset()
	  	}
	});
	
	addPrcDlg = $("#dialog-process").dialog({
		autoOpen: false,
		top: 40,
		height: "auto",
		modal: true,
		buttons: {
			OK: function() {
				prcUpdate();
			},
	    	Cancel: function() {
	      		addPrcDlg.dialog( "close" );
	    	}
	  	},
	  	open: function() {
	  		$("#prcContent").focus();
	  	},
	  	close: function() {
	  		$("#add-form-process")[0].reset()
	  	}
	});
	
	copyPrdDlg = $("#dialog-product-copy").dialog({
		autoOpen: false,
		top: 40,
		height: "auto",
		modal: true,
		buttons: {
			Copy: function() {
				copyProduct();
			},
	    	Cancel: function() {
	    		copyPrdDlg.dialog( "close" );
	    	}
	  	},
	  	open: function() {
	  		$("#copyCodePrd").focus();
	  	},
	  	close: function() {
	  		$("#add-form-copy")[0].reset()
	  	}
	});
	
	$("#row tr").click(function() {
		selectRow($(this).attr("id"));
		selectPrcRow();
	});
	
	$("#row-process tr").click(function() {
		popupProcessUpdate($(this).attr("id"));
	});
	
	$("#add-btn").click(function() {
		btnPress = "add";
		openPrdDlg("add");
		$("#code").focus();

	});
	
	$("#copy-btn").click(function() {
		$("#copyCodeProductTable").html($("#"+prdId).children().eq(0).html());
		$("#copyNameProductTable").html($("#"+prdId).children().eq(1).html());
		$(".ui-widget-header").css("background", "#9ECFDE");
		copyPrdDlg.dialog("option", "title", "Copy");
		copyPrdDlg.dialog( "open" );
	});
	
	$("#update-btn").click(function() {
		btnPress = "update";
		popupProductUpdate();
	});
	
	$("#delete-btn").click(function() {
		btnPress = "delete";
		del();
	});
});

function selectRow(id) {
	if(prdId)
		$("#" + prdId).removeClass("selected-row");
	prdId = id;
	$("#" + prdId).addClass("selected-row");
	$("#update-btn").prop("disabled", false);
	$("#delete-btn").prop("disabled", false);
}


function openPrdDlg(type) {
	if(type == "add") {
		$(".ui-widget-header").css("background", "#9ECFDE");
		addPrdDlg.dialog("option", "title", "Add Product");
		addPrdDlg.dialog("open");
	}
	else if(type == "update") {
		$(".ui-widget-header").css("background", "#DDFA9D");
		addPrdDlg.dialog("option", "title", "Update Product");
		addPrdDlg.dialog("open");
	}
}

function selectPrcRow() {
	$('#nameProcess').html($("#"+prdId).children().eq(1).html());
	$.ajax({
		type:"PUT",
		url:"rest/productmasterWs/process",
		data: {
			"prdId": prdId
		},
		success:function(result) {
			if(result != null) {
				var nlst = result.split("/n");
				$('#row-process').html("");
				for(i = 1; i< nlst.length-1;i++){
					tlst = nlst[i].split("/t");
					var tmp = "<tr id=p"+tlst[0]+">";
					for(j = 1 ;j < tlst.length ; j++){
						tmp += "<td>"+tlst[j] +"</td>";
					}
					tmp += "</tr>";
					$('#row-process').append(tmp);
				}
				
				$("#row-process tr").click(function() {
					popupProcessUpdate($(this).attr("id"));
				});
			}
			
		}
	});
}


function add() {
	var code = $("#code").val();
	var name = $("#name").val();
	var scatId = $("#scatId").val();
	var scatName = $("#scatId option:selected").text();
	
	if(scatId == 0) {
		alert("Please select Type.");
		return;
	}
	
	$.ajax({
		type:"PUT",
		url:"rest/productmasterWs",
		data: {
			"code": code,
			"name": name,
			"scatId": scatId
		},
		success:function(result) {
			if(result != null) {
				var nlst = result.split("/n");
				$("#row").append("<tr id='" + nlst[0] + "'><td>" + code + "</td><td>" + name + "</td><td id='r" + scatId + "'>" + 
						scatName + "</td></tr>");
				$("#" + nlst[0]).click(function() {
					selectRow(nlst[0]);
					selectPrcRow();
				});
				$("#" + nlst[0]).click();
				$('#row-process').html("");
				for(i = 1; i< nlst.length-1;i++){
					tlst = nlst[i].split("/t");
					var tmp = "<tr id=p"+tlst[0]+">";
					for(j = 1 ;j < tlst.length ; j++){
						tmp += "<td>"+tlst[j] +"</td>";
					}
					tmp += "</tr>";
					$('#row-process').append(tmp);
				}
				
				$("#row-process tr").click(function() {
					popupProcessUpdate($(this).attr("id"));
				});
				addPrdDlg.dialog("close");
			}
			else alert("Wrong format value!");
		}
	});
}

function popupProductUpdate() {
	var e = $("#" + prdId);
	$("#prdId").val(prdId);
	$("#code").val($(e).children().eq(0).html());
	$("#name").val($(e).children().eq(1).html());
	var scatName = $(e).children().eq(2).html();
	$("select option").filter(function() {
	    //may want to use $.trim in here
	    return $(this).text() == scatName; 
	}).prop('selected', true);
	
	openPrdDlg("update");
}

function popupProcessUpdate(id){
	prcId = id.substring(1);
	var e = $("#" + id);
	$("#prcId").val(id);
	$("#prcContent").val($(e).children().eq(1).html());
	$("#standardTime").val($(e).children().eq(2).html());
	$("#manPower").val($(e).children().eq(3).html());
	$("#unitCount").val($(e).children().eq(4).html());
	$("#printLabel").val($(e).children().eq(5).html());
	$("#unitSize").val($(e).children().eq(6).html());
	
	$(".ui-widget-header").css("background", "#DDFA9D");
	addPrcDlg.dialog("option", "title", "Update Process " + $(e).children().eq(0).html());
	addPrcDlg.dialog("open");
}
function copyProduct(){
	var copyCodePrd = $("#copyCodePrd").val();
	var copyNamePrd = $("#copyNamePrd").val();
	$.ajax({
		type:"POST",
		url:"rest/productmasterWs/copyProduct",
		data: {
			"prdId": prdId,
			"copyCodePrd": copyCodePrd,
			"copyNamePrd": copyNamePrd
		},
		success:function(result) {
			if(result != null) {
				var nlst = result.split("/n");
				var prdlst = nlst[0].split("/t");
				
				$("#row").append("<tr id='" + prdlst[0] + "'><td>" + prdlst[1] + "</td><td>" + prdlst[2] + "</td><td id='r" + prdlst[3] + "'>" + 
						prdlst[4] + "</td></tr>");
				$("#" + prdlst[0]).click(function() {
					selectRow(prdlst[0]);
					selectPrcRow();
				});
				$("#" + prdlst[0]).click();
				
				$('#row-process').html("");
				for(i = 1; i< nlst.length-1;i++){
					tlst = nlst[i].split("/t");
					var tmp = "<tr id=p"+tlst[0]+">";
					for(j = 1 ;j < tlst.length ; j++){
						tmp += "<td>"+tlst[j] +"</td>";
					}
					tmp += "</tr>";
					$('#row-process').append(tmp);
				}
				
				$("#row-process tr").click(function() {
					popupProcessUpdate($(this).attr("id"));
				});
				copyPrdDlg.dialog("close");
			}
			else alert("Wrong format value!");
		}
	});
}

function prdUpdate() {
	var prdId = $("#prdId").val();
	var code = $("#code").val();
	var name = $("#name").val();
	var scatName = $("#scatId option:selected").text();
	var scatId = $("#scatId").val();
	if(scatId == 0) {
		alert("Please select Type.");
		return;
	}
	
	$.ajax({
		type:"POST",
		url:"rest/productmasterWs/product/",
		data: {
			"prdId": prdId,
			"code": code,
			"name": name,
			"scatId": scatId
		},
		success:function(result) {
			if(result == "true") {
				var e = $("#" + prdId);
				$(e).children().eq(0).html(code);
				$(e).children().eq(1).html(name);
				$(e).children().eq(2).html(scatName);
				$('#nameProcess').html($(e).children().eq(1).html());
				addPrdDlg.dialog("close");
			}
			else alert("Cannot update Product Code. " + $("#code").val() + "!");
		}
	});
}

function prcUpdate() {
	
	var prcContent = $("#prcContent").val();
	var standardTime = $("#standardTime").val();
	var manPower = $("#manPower").val();
	var unitCount = $("#unitCount").val();
	var printLabel = $("#printLabel").val();
	var unitSize = $("#unitSize").val();
	var specName = $("#specId option:selected").text();
	var specId = $("#specId").val();
	$.ajax({
		type:"POST",
		url:"rest/productmasterWs/process/",
		data: {
			"prdId": prdId,
			"prcId": prcId,
			"prcContent": prcContent,
			"standardTime": standardTime,
			"manPower": manPower,
			"unitCount": unitCount,
			"printLabel": printLabel,
			"unitSize": unitSize,
			"specId" : specId
		},
		success:function(result) {
			if(result == "true") {
				var e = $("#p" + prcId);
				$(e).children().eq(1).html(prcContent);
				$(e).children().eq(2).html(standardTime);
				$(e).children().eq(3).html(manPower);
				$(e).children().eq(4).html(unitCount);
				$(e).children().eq(5).html(printLabel);
				$(e).children().eq(6).html(unitSize);
				$(e).children().eq(7).html(specId);
				addPrcDlg.dialog("close");
			}
			else alert("Cannot update Process Code. " + $("#code").val() + "!");
		}
	});
	
}

function del() {
	var code = $("#" + prdId).children().eq(0).html();
	if(confirm("Do you want to delete Product Code. " + code + "?")) {
		$.ajax({
			type:"DELETE",
			url:"rest/productmasterWs/product/",
			data: {
				"prdId": prdId
			},
			success:function(result) {
				if(result == "true") {
					$("#" + prdId).remove();
					$('#row-process').html("");
					$('#nameProcess').html("");
					addPrdDlg.dialog("close");
				}
				else alert("Cannot remove Product Code. " + code + "!");
			}
		});
	}
}