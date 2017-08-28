var wcdate;
var btnPress;
var addDlg;
var numberRegex = /^[+-]?\d+(\.\d+)?([eE][+-]?\d+)?$/;

$(document).ready(function() {
	addDlg = $("#dialog").dialog({
		autoOpen: false,
		top: 40,
		width: 540,
		height: "auto",
		modal: true,
		buttons: {
			Ok: function() {
				update();
			},
	    	Cancel: function() {
	      		addDlg.dialog( "close" );
	    	}
	  	},
	  	open: function() {
	  		$("#startWorkingTime").focus();
	  	},
	  	close: function() {
	  		$("#update-form")[0].reset()
	  	}
	});
	
	$("#row tr").click(function() {
		selectRow($(this).attr("id"));
	});

	
	$("#update-btn").click(function() {
		perpareUpdate();
	});
	
	$("#defalt-btn").click(function() {
		setInputDefault();
	});
	
	$('.monthYearPicker').datepicker({
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true,
		dateFormat: 'MM yy'
	}).focus(function() {
		var thisCalendar = $(this);
		$('.ui-datepicker-calendar').detach();
		$('.ui-datepicker-close').click(function() {
			var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
			var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
			window.location = "workcalendar.htm?month=" + month + "&year=" + year;
		});
	});
	
	$("#nextMonth").click(function() {
		var my = $("#monthYear").val();
		var mtype = "next";
		
		window.location = "workcalendar.htm?monthYear=" + my + "&mtype=" + mtype;
	});
	
	$("#previousMonth").click(function() {
		var my = $("#monthYear").val();
		var mtype = "previous";
		
		window.location = "workcalendar.htm?monthYear=" + my + "&mtype=" + mtype;
	});
	
	
	
	 $("#holiday").change(function(){
		 if($("#holiday").val() == 1){
				$(".time").prop('disabled', true);
				$(".time").val("");
			}else{
				$("#startWorkingTime").prop('disabled', false);
				$("#endWorkingTime").prop('disabled', false);
				$("#startNonWorkingTime1").prop('disabled', false);
				$("#endNonWorkingTime1").prop('disabled', false);	
		}
	  });
	 
	 $("#startNonWorkingTime1").change(function(){
		 if($("#startNonWorkingTime1").val()!= ""){
			 	$("#startNonWorkingTime2").prop('disabled', false);
				$("#endNonWorkingTime2").prop('disabled', false);	
			}else{
				$("#startNonWorkingTime2").prop('disabled', true);
				$("#endNonWorkingTime2").prop('disabled', true);
				$("#startNonWorkingTime2").val("");
				$("#endNonWorkingTime2").val("");
				$("#startNonWorkingTime3").prop('disabled', true);
				$("#endNonWorkingTime3").prop('disabled', true);	
				$("#startNonWorkingTime3").val("");
				$("#endNonWorkingTime3").val("");
				$("#startNonWorkingTime4").prop('disabled', true);
				$("#endNonWorkingTime4").prop('disabled', true);
				$("#startNonWorkingTime4").val("");
				$("#endNonWorkingTime4").val("");
				$("#startNonWorkingTime5").prop('disabled', true);
				$("#endNonWorkingTime5").prop('disabled', true);
				$("#startNonWorkingTime5").val("");
				$("#endNonWorkingTime5").val("");
			}
	  });
	 
	 $("#startNonWorkingTime2").change(function(){
		 if($("#startNonWorkingTime2").val()!= ""){
			 	$("#startNonWorkingTime3").prop('disabled', false);
				$("#endNonWorkingTime3").prop('disabled', false);	
			}else{
				$("#startNonWorkingTime3").prop('disabled', true);
				$("#endNonWorkingTime3").prop('disabled', true);	
				$("#startNonWorkingTime3").val("");
				$("#endNonWorkingTime3").val("");
				$("#startNonWorkingTime4").prop('disabled', true);
				$("#endNonWorkingTime4").prop('disabled', true);
				$("#startNonWorkingTime4").val("");
				$("#endNonWorkingTime4").val("");
				$("#startNonWorkingTime5").prop('disabled', true);
				$("#endNonWorkingTime5").prop('disabled', true);
				$("#startNonWorkingTime5").val("");
				$("#endNonWorkingTime5").val("");
		}
	  });
	 $("#startNonWorkingTime3").change(function(){
		 if($("#startNonWorkingTime3").val()!= ""){
			 	$("#startNonWorkingTime4").prop('disabled', false);
				$("#endNonWorkingTime4").prop('disabled', false);	
			}else{
				$("#startNonWorkingTime4").prop('disabled', true);
				$("#endNonWorkingTime4").prop('disabled', true);
				$("#startNonWorkingTime4").val("");
				$("#endNonWorkingTime4").val("");
				$("#startNonWorkingTime5").prop('disabled', true);
				$("#endNonWorkingTime5").prop('disabled', true);
				$("#startNonWorkingTime5").val("");
				$("#endNonWorkingTime5").val("");
		}
	  });
	 $("#startNonWorkingTime4").change(function(){
		 if($("#startNonWorkingTime4").val()!= ""){
			 	$("#startNonWorkingTime5").prop('disabled', false);
				$("#endNonWorkingTime5").prop('disabled', false);	
			}else{
				$("#startNonWorkingTime5").prop('disabled', true);
				$("#endNonWorkingTime5").prop('disabled', true);
				$("#startNonWorkingTime5").val("");
				$("#endNonWorkingTime5").val("");
		}
	  });
});

function selectRow(id) {
	if(wcdate)
		$("#" + wcdate).removeClass("selected-row");
	wcdate = id;
	$("#" + wcdate).addClass("selected-row");
	$("#wcDate").val(wcdate);
}


function openDlg(type) {
	if(type == "update") {
		$(".ui-widget-header").css("background", "#DDFA9D");
		addDlg.dialog("option", "title", "Update Work Calendar");
		addDlg.dialog("open");
	}
}


function perpareUpdate() {
	var e = $("#" + wcdate);
	$("#wcDate").val(wcdate);
	$("#startWorkingTime").val($(e).children().eq(2).html());
	$("#endWorkingTime").val($(e).children().eq(3).html());
	$("#startNonWorkingTime1").val($(e).children().eq(4).html());
	$("#endNonWorkingTime1").val($(e).children().eq(5).html());
	$("#startNonWorkingTime2").val($(e).children().eq(6).html());
	$("#endNonWorkingTime2").val($(e).children().eq(7).html());
	$("#startNonWorkingTime3").val($(e).children().eq(8).html());
	$("#endNonWorkingTime3").val($(e).children().eq(9).html());
	$("#startNonWorkingTime4").val($(e).children().eq(10).html());
	$("#endNonWorkingTime4").val($(e).children().eq(11).html());
	$("#startNonWorkingTime5").val($(e).children().eq(12).html());
	$("#endNonWorkingTime5").val($(e).children().eq(13).html());
	var holiday = $("#h-"+wcdate).val();
	$("select option").filter(function() {
	    //may want to use $.trim in here
	    return $(this).val() == holiday; 
	}).prop('selected', true);

	if(holiday == 1){
		$(".time").prop('disabled', true);
		$(".time").val("");
	}else{
		$(".time").prop('disabled', true);
		$("#startWorkingTime").prop('disabled', false);
		$("#endWorkingTime").prop('disabled', false);
		$("#startNonWorkingTime1").prop('disabled', false);
		$("#endNonWorkingTime1").prop('disabled', false);	
		if($("#startNonWorkingTime1").val()!=""){
			$("#startNonWorkingTime2").prop('disabled', false);
			$("#endNonWorkingTime2").prop('disabled', false);	
		}
		if($("#startNonWorkingTime2").val()!=""){
			$("#startNonWorkingTime3").prop('disabled', false);
			$("#endNonWorkingTime3").prop('disabled', false);	
		}
		if($("#startNonWorkingTime3").val()!=""){
			$("#startNonWorkingTime4").prop('disabled', false);
			$("#endNonWorkingTime4").prop('disabled', false);	
		}
		if($("#startNonWorkingTime4").val()!=""){
			$("#startNonWorkingTime5").prop('disabled', false);
			$("#endNonWorkingTime5").prop('disabled', false);	
		}
	}
	
	openDlg("update");
}

function update() {
	var date = $("#wcDate").val();
	var startWorkingTime = $("#startWorkingTime").val();
	var endWorkingTime = $("#endWorkingTime").val();
	var holiday = $("#holiday").val();
	var startNonWorkingTime1 = $("#startNonWorkingTime1").val();
	var endNonWorkingTime1 = $("#endNonWorkingTime1").val();
	var startNonWorkingTime2 = $("#startNonWorkingTime2").val();
	var endNonWorkingTime2 = $("#endNonWorkingTime2").val();
	var startNonWorkingTime3 = $("#startNonWorkingTime3").val();
	var endNonWorkingTime3 = $("#endNonWorkingTime3").val();
	var startNonWorkingTime4 = $("#startNonWorkingTime4").val();
	var endNonWorkingTime4 = $("#endNonWorkingTime4").val();
	var startNonWorkingTime5= $("#startNonWorkingTime5").val();
	var endNonWorkingTime5 = $("#endNonWorkingTime5").val();
	if((startNonWorkingTime1!=""&&endNonWorkingTime1=="")||(startNonWorkingTime1==""&&endNonWorkingTime1!="")){
		alert("Please fill Start and End non working Time 1 ");
		return;
	}
	if((startNonWorkingTime2!=""&&endNonWorkingTime2=="")||(startNonWorkingTime2==""&&endNonWorkingTime2!="")){
		alert("Please fill Start and End non working Time 2 ");
		return;
	}
	if((startNonWorkingTime3!=""&&endNonWorkingTime3=="")||(startNonWorkingTime3==""&&endNonWorkingTime3!="")){
		alert("Please fill Start and End non working Time 3 ");
		return;
	}
	if((startNonWorkingTime4!=""&&endNonWorkingTime4=="")||(startNonWorkingTime4==""&&endNonWorkingTime4!="")){
		alert("Please fill Start and End non working Time 4 ");
		return;
	}
	if((startNonWorkingTime5!=""&&endNonWorkingTime5=="")||(startNonWorkingTime5==""&&endNonWorkingTime5!="")){
		alert("Please fill Start and End non working Time 5 ");
		return;
	}
	$.ajax({
		type:"POST",
		url:"rest/workcalendarWs",
		data: {
			"date": date,
			"startWorkingTime": startWorkingTime,
			"endWorkingTime": endWorkingTime,
			"holiday": holiday,
			"startNonWorkingTime1": startNonWorkingTime1,
			"endNonWorkingTime1": endNonWorkingTime1,
			"startNonWorkingTime2": startNonWorkingTime2,
			"endNonWorkingTime2": endNonWorkingTime2,
			"startNonWorkingTime3": startNonWorkingTime3,
			"endNonWorkingTime3": endNonWorkingTime3,
			"startNonWorkingTime4": startNonWorkingTime4,
			"endNonWorkingTime4": endNonWorkingTime4,
			"startNonWorkingTime5": startNonWorkingTime5,
			"endNonWorkingTime5": endNonWorkingTime5
		},
		success:function(result) {
			if(result != null){
				tlst = result.split("/t");
				var e = $("#" + date);
				$(e).children().eq(0).html(tlst[0]);
				if(holiday == 0)
					{$(e).children().eq(1).html("<img src='images/checkbox_no.png'/><input type='hidden' id='h-"+date+"' value='0'>");}
				else
					{$(e).children().eq(1).html("<img src='images/checkbox.png'/><input type='hidden' id='h-"+date+"' value='1'>");}
				$(e).children().eq(2).html($.trim(tlst[2]));
				$(e).children().eq(3).html($.trim(tlst[3]));
				$(e).children().eq(4).html($.trim(tlst[4]));
				$(e).children().eq(5).html($.trim(tlst[5]));
				$(e).children().eq(6).html($.trim(tlst[6]));
				$(e).children().eq(7).html($.trim(tlst[7]));
				$(e).children().eq(8).html($.trim(tlst[8]));
				$(e).children().eq(9).html($.trim(tlst[9]));
				$(e).children().eq(10).html($.trim(tlst[10]));
				$(e).children().eq(11).html($.trim(tlst[11]));
				$(e).children().eq(12).html($.trim(tlst[12]));
				$(e).children().eq(13).html($.trim(tlst[13]));
				$("#" + date).click(function() {
					selectRow(date);
				});
				$("#" + date).click();
				addDlg.dialog("close");
			}
			else alert("Cannot update WC Date. " + $("#wcDate").val() + "!");
		}
	});
}

function setInputDefault() {
	var date = $("#wcDate").val();
		
	$.ajax({
		type:"PUT",
		url:"rest/workcalendarWs",
		data: {
			"date": date
		},
		success:function(result) {
			if(result != null) {
				tlst = result.split("/t");
				var e = $("#" + date);
				$(e).children().eq(0).html(tlst[0]);
				$(e).children().eq(1).html("<img src='images/checkbox_no.png'/><input type='hidden' id='h-"+date+"' value='0'>");
				$(e).children().eq(2).html($.trim(tlst[2]));
				$(e).children().eq(3).html($.trim(tlst[3]));
				$(e).children().eq(4).html($.trim(tlst[4]));
				$(e).children().eq(5).html($.trim(tlst[5]));
				$(e).children().eq(6).html($.trim(tlst[6]));
				$(e).children().eq(7).html($.trim(tlst[7]));
				$(e).children().eq(8).html($.trim(tlst[8]));
				$(e).children().eq(9).html($.trim(tlst[9]));
				$(e).children().eq(10).html($.trim(tlst[10]));
				$(e).children().eq(11).html($.trim(tlst[11]));
				$(e).children().eq(12).html($.trim(tlst[12]));
				$(e).children().eq(13).html($.trim(tlst[13]));
				$("#" + date).click(function() {
					selectRow(date);
				});
				$("#" + date).click();
				addDlg.dialog("close");
			}
			else alert("Cannot update Default WC Date. " + $("#wcDate").val() + "!");
		}
	});
}

