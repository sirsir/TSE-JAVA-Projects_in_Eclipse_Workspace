var preview=false;

function refreshOwner() {
	var tIds="";
	for(var i=0; i<teams.length; i++) {
		if(teams[i].checked == 'true') {
			tIds += teams[i].id + ",";
		}
	}
	
	
	$.ajax({
		type:"get",
		url:"../requestItems",
		data: {"type": "owner",
			"tIds": tIds
		},
		success:function(xml) {
			var sel = clearItems("owner");
			$(xml).find('item').each(function(i,o){
				var option=document.createElement('option');
				option.name=unescape($(this).attr("name"));
				option.value=unescape($(this).attr("value"));
				if(selectedNo != null && option.value == stages[selectedNo].owner)
					option.selected = true;
				option.innerHTML=$(this).attr("name");
				sel.appendChild(option);
			});
		}
	});
}

function addNewStage() {
  var no=stages.length;
  var stage=new Stage(no);
  selectedNo=no;
  drawStages();
  
  document.getElementById("stageNo").firstChild.nodeValue="Stage " + (no+1);
  document.getElementById("statusType").options[0].selected=true;
  var url="../postData?type=addStage&wftId="+wftId+"&no="+stages.length+"&name=undefined&duration=7&owner=1&stId=2";
  
  //var url="../requestItems?type=addStage&twfId="+twfId+"&no="+stages.length+"&name=noname&duration=7&owner="+owner+"&stId="+stId;
  var httpReq = new XMLHttpRequest();
  httpReq.open("get", url, true);
  httpReq.onreadystatechange=function() {
	  	if(httpReq.readyState==4) {
			var xml=httpReq.responseXML;
			var xmlObj=xml.documentElement;
			var items=xmlObj.getElementsByTagName("item");
			
			for(var i=0; i<items.length; i++) {
				stage.id=items[i].getAttribute("value");
				setStatusType();
				setOwner();
				
				break;
			}
		}
	};
	httpReq.send();
	  
	//  var owner=document.getElementById("owner").value;
	//  var stId=document.getElementById("statusType").value;
}
		
function update(table,input,id) {
	var value = "";
	if(input != null)
		value=input.value;
	value=value.trim();
	
	if(input.name=="duration" && (value==0 || value.length==0)) {
		alert("Please fill out " + input.name + " that has a value more than 0.");
		return;
	}
	if(input.name=="stId") {
		var tmp=value.split("-");
		value=tmp[0];
	}
	if(input.required && value.length == 0) {
		alert("Please fill out " + input.name + " data.");
		input.focus();
		return;
	}
	
	var url;
	value = replaceAll(value,"\n", "\\\\n");
	/*if(table == "w")
		url="../postData?type=update&table=workflow_template&field="+input.name+"&value="+value+"&key=wftId&id="+id;
	else url="../postData?type=update&table=stage_template&field="+input.name+"&value="+value+"&key=sgtId&id="+id;
	xmlhttp.open("get", url, true);
	xmlhttp.onreadystatechange=function() {
		if(xmlhttp.readyState==4) {
			var xml=xmlhttp.responseXML;
			var xmlObj=xml.documentElement;
			var items=xmlObj.getElementsByTagName("item");
			for(var i=0; i<items.length; i++) {
				if(items[i].getAttribute("name") != "success")
					alert(msg);
				break;
			}
		}
	};
	xmlhttp.send();*/
	var tab = "stage_template";
	var key = "sgtId";
	if(table == "w") {
		tab = "workflow_template";
		key = "wftId";
	}
	
	$.ajax({
		type:"get",
		url:"../postData",
		data: {"type": "update",
			"table": tab,
			"field": input.name,
			"value": value,
			"key": key,
			"id": id
		},
		success:function() {
			if(table == "s") {
				if(input.name == 'name')
					stages[selectedNo].name = value;
				drawStages();
			}
		}
	});
}

function deleteStage() {
	if(stages.length == 1) {
		alert("Cannot delete the last stage.");
		return;
	}
	
	if(confirm("Do you want to delete Stage "+(selectedNo+1)+"?")) {
		$.ajax({
			type:"POST",
			url:"../postData",
			data: {"type": "delStage",
				"sgtId": stages[selectedNo].id
			},
			success:function() {
				stages.splice(selectedNo, 1);
				var len=stages.length;
				if(selectedNo >= len)
					selectedNo=len-1;
				else {
					for(var i=selectedNo; i<len; i++) {
					stages[i].no=i;
							stages[i].y=(i*64)+4;
					};
				}

				if(selectedNo == 0)
					stages[selectedNo].x=4;
				drawStages();
				refreshCustomField();
				refreshSubWorkFlow();
			}
		});
	}
}

function drawStages() {
  // Clear the canvas.
  context.clearRect(0, 0, canvas.width, canvas.height);

  // Go through all the circles.
  for(var i=0; i<stages.length; i++) {
    var stage=stages[i];
    if(i > 0) {
      var p=stage.predecessor;
      if(p.length > 0) {
    	var pStage=stages[p-1];
    	stage.x=pStage.x+pStage.width;
      }
      else {
    	var pStage=stages[i-1];
    	stage.x=pStage.x+pStage.width-50;
      }
      
      var pStage=stages[i-1];
  	  var beginX=(stage.x+stage.width)-(pStage.x+pStage.width);
  	  if(beginX > 0) { 
  		beginX/=2;
  		beginX+=pStage.x+pStage.width;
  		
    	var beginY=stage.y-(pStage.height/2)-1;
    	var endX=pStage.x+(pStage.width);
    	context.beginPath();
    	context.moveTo(beginX,stage.y-1);
        context.lineTo(beginX,beginY);
        context.lineTo(endX,beginY);
        context.lineWidth = 2;
        context.strokeStyle = "green";
        context.stroke();
  	  }
  	
      var r=stage.returnTo;
      //alert(stage.name + " " + i + " " + r);
      if(r != 0 && r.length > 0) {
	    var tStage=stages[r-1];
  	    var endX=tStage.x+(tStage.width/2);
  	    var endY=tStage.y+tStage.height;
  	    if(stages[r].predecessor.length == 0)
  		  endX-=25;
  	    var beginY=stage.y+(stage.height/2);
  	    
  	    context.beginPath();
        context.moveTo(stage.x,beginY);
        context.lineTo(endX,beginY);
        context.lineTo(endX,endY);
        context.lineWidth=2;
        context.strokeStyle="#b22222";
        context.stroke();
      }
    }
    
    context.globalAlpha=0.85;    
    context.beginPath();
    context.rect(stage.x, stage.y, stage.width, stage.height);
    
    if(stage.returnStage == 0)
    	context.fillStyle="#A8F999";   
    else context.fillStyle="#FAD892";
    context.fill();    
    context.beginPath();
    
    if(stage.no==selectedNo) {
      context.strokeStyle="#6BADE2";
      context.lineWidth=3;
      refreshForm(i);
    }
    else {
      context.strokeStyle="gray";
      context.lineWidth=1;
    }
    context.strokeRect(stage.x, stage.y, stage.width, stage.height);
    
    drawText((i+1),stage.x+4,stage.y+4);
    drawText(stage.name,stage.x+24,stage.y+4);
    drawText(stage.statusName,stage.x+4,stage.y+22);
    drawText(stage.duration+" days",stage.x+136,stage.y+22);
    drawText(stage.ownerName,stage.x+4,stage.y+42);
   
    if(!preview) {
	    if(i==stages.length-1) {
	      var img = document.getElementById("addStage");
	      context.drawImage(img, stage.x+stage.width-18, stage.y+2);
	      img = document.getElementById("delStage");
	      context.drawImage(img, stage.x+stage.width-18, stage.y+22);
	    }
	    else {
	      var img = document.getElementById("delStage");
	      context.drawImage(img, stage.x+stage.width-18, stage.y+4);
	    }
    }
  }
}

function drawText(text,x,y) {
  context.beginPath();
  context.font="12px Verdana,sans-serif,Arial";
  context.textBaseline="top";
  context.fillStyle="black";
  context.fillText(text,x,y);
}

function refreshCustomField() {
  var cf=document.getElementById("customField");
  while(cf.firstChild) {
    cf.removeChild(cf.firstChild);
  }
 
  var legend=document.createElement("legend");
  if(preview)
	  legend.innerHTML="Custom Field";
  else legend.innerHTML="";
  cf.appendChild(legend);
  
  var customFields=stages[selectedNo].customFields;
  for(var j=0; j<customFields.length; j++) {
    var customField=customFields[j];
    var div=document.createElement("div");
    div.id=j;
    var type=customField.fieldType;
    if(type != "") {
	  var fieldName=customField.fieldName;
	  var label=document.createElement("label");
	  label.id="lb_"+j;
      label.htmlFor=fieldName;
      label.innerHTML=fieldName;
      div.appendChild(label);
      
	  if(type=="b") {
		var input=document.createElement("select");
		input.id=fieldName;
		input.name=fieldName;
		for(var i=0; i<customField.items.length; i++) {
		  var item=customField.items[i];
		  var opt=document.createElement("option");
		  opt.value=item;
		  opt.innerHTML=item;
		  input.appendChild(opt);
		}
		div.appendChild(input);
		
		if(preview)
			input.disabled=true;
		else {
			var img=createDelCustomField(j,fieldName);
			div.appendChild(img);			
		}
		  
		var br=document.createElement("br");
		div.appendChild(br);
	  }
	  else if(type=="c" || type=="d") {
    	for(var i=0; i<customField.items.length; i++) {
    	  var item=customField.items[i];
		  var radio=document.createElement("input");
		  if(type=="c")
			  radio.type="radio";
		  else radio.type="checkbox";
		  radio.name=fieldName;
		  radio.id=item;
		  radio.value=item;
		  if(preview)
			radio.disabled=true;
		  div.appendChild(radio);
    
		  var rlabel=document.createElement("label");
		  rlabel.width="200px";
		  rlabel.htmlFor=item;
		  rlabel.innerHTML=item;
		  div.appendChild(rlabel);
		  
		  if(i == 0 && !preview) {
			  var img=createDelCustomField(j,fieldName);
			  div.appendChild(img);
		  }
			
		  var br=document.createElement("br");
		  div.appendChild(br);
			
		  if(i<customField.items.length-1) {
		    var label1=document.createElement("label");
			label1.innerHTML="";
			div.appendChild(label1);
		  }
        }

		var br=document.createElement("br");
		div.appendChild(br);
      }
      else {
        var input=null;
        if(type=="2")
          input=document.createElement("textarea");
        else {
          input=document.createElement("input");
          
          if(type=="0")
        	  input.type="text";
          else if(type=="1")
        	  input.type="number";
          else if(type=="3")
        	  input.type="date";
          else if(type=="4")
        	  input.type="time";
          else if(type=="5")
        	  input.type="datetimelocal";
          else if(type=="6"){
        	  input.type="range";
        	  input.min='<c:out value="${model.min}"/>';
              input.max='<c:out value="${model.max}"/>';
          }
          else if(type=="7")
        	  input.type="email";
          else if(type=="8")
        	  input.type="tel";
          else if(type=="9")
        	  input.type="url";
          else if(type=="a")
        	  input.type="color";
        } 
  
        input.id=fieldName;
        input.name=fieldName;
        div.appendChild(input);
        
        if(preview)
  			input.disabled=true;
        else {
        	var img=createDelCustomField(j,fieldName);
            div.appendChild(img);
        }
        
        var br=document.createElement("br");
        div.appendChild(br);
      }
	  cf.appendChild(div);
    }
  }
}

function clearItems(type) {
	var sel=document.getElementById(type);
	while(sel.childNodes.length >= 1) {
		sel.removeChild(sel.firstChild);
	}			
	return sel;
}

function refreshForm(idx) {
  var returnTo=clearItems("returnTo");
  for(var i=1; i<=selectedNo; i++) {
    var option=document.createElement('option');
    option.value=i;
    option.innerHTML=i;
    returnTo.appendChild(option);
  }
  
  var predecessor=clearItems("predecessor");
  var option0=document.createElement('option');
  option0.value="";
  option0.innerHTML="";
  predecessor.appendChild(option0);
  for(var i=1; i<=selectedNo; i++) {
    var option1=document.createElement('option');
    option1.value=i;
    option1.innerHTML=i;
    predecessor.appendChild(option1);
  }
  
  var stage=stages[idx];
  document.getElementById("stageNo").firstChild.nodeValue="Stage " + (idx+1);
  document.getElementById("stageName").value=stage.name;
  document.getElementById("duration").value=stage.duration;
  if(stage.link == 'null')
	  stage.link = '';
  document.getElementById("link").value=stage.link;
  
  setSelectedIndex("predecessor", stage.predecessor);
  setSelectedIndex("returnTo", stage.returnTo);
  setSelectedIndex("owner", stage.owner);
  setSelectedIndex1("statusType", stage.statusType, '-', 0);
}

function canvasClick(e) {
  //var dif=287-this.getBoundingClientRect().top;	
  var canvas=document.getElementById("drawingCanvas");
  var rect = canvas.getBoundingClientRect();
  var clickX=e.pageX - (document.body.scrollLeft + rect.left);
  var clickY=e.pageY - (document.body.scrollTop + rect.top);
  
  for(var i=stages.length-1; i>=0; i--) {
    var stage=stages[i];
    
    if(clickX >= stage.x && clickX <= (stage.x+stage.width) && clickY >= stage.y && clickY <= (stage.y+stage.height+10)) {
    	selectedNo=i;
    	
    	if(!preview) {
	    	if(selectedNo==(stages.length-1)) {
	    		if(clickX >= (stage.x+stage.width-20) && clickY <= (stage.y+22))
	    			addNewStage();
	    		else if(clickX >= (stage.x+stage.width-20) && clickY > (stage.y+24) && clickY < (stage.y+44))
	    			deleteStage();
	    	}
	    	else if(clickX >= (stage.x+stage.width-20) && clickY <= (stage.y+28))
	    		 deleteStage();
    	}
    	drawStages();
        refreshCustomField();
        refreshSubWorkFlow();
        setStatusType();
    	return;
    }
  }
}

function createDelCustomField(idx,fieldName) {
	var img=document.createElement("img");
	img.id="del_"+fieldName;
	img.src="/cog/images/delete.png";
	img.alt="Delete " + fieldName;
	img.onclick=function delCustomField() {
	  var name=document.getElementById("lb_"+idx);
	  if(confirm("Do you want to delete field '"+name.innerHTML+"'?")) {
	    var cf=document.getElementById("customField");
	    var node=document.getElementById(idx);
	    cf.removeChild(node);
	    stages[selectedNo].customFields.splice(idx,1);
	    
		$.ajax({
			type:"POST",
			url:"../postData",
			data: {"type": "delCustomField",
				"idx": idx,
				"sgtId": stages[selectedNo].id
			},
			done:function() {
				refreshCustomField();
			}
		});
	  }
	};
	return img;
}

function setStageName() {
	var name=document.getElementById("stageName");
    stages[selectedNo].name=name.value;
    drawStages();
}

function setPredecessor() {
  var name=document.getElementById("predecessor");
  stages[selectedNo].predecessor=name.value;
  drawStages();
}

function setReturnTo() {
  var name=document.getElementById("returnTo");
  stages[selectedNo].returnTo=name.value;
  drawStages();
}

function setDuration() {
	var name=document.getElementById("duration");
    stages[selectedNo].duration=name.value;
}

function setLink() {
	var name=document.getElementById("link");
    stages[selectedNo].link=name.value;
}

function setOwner() {
  var name=document.getElementById("owner");
  stages[selectedNo].owner=name.value;
  stages[selectedNo].ownerName=name.options[name.selectedIndex].innerHTML;
  drawStages();
}

function setStatusType() {
  var name=document.getElementById("statusType");
  var values=name.value.split("-");
  var stage=stages[selectedNo];
  stage.statusType=values[0];
  stage.returnStage=values[1];
  
  var sname=name.options[name.selectedIndex].innerHTML;
  var tmp=sname.split(" - ");
  stage.statusName=tmp[0].replace(" Stage","");
  
  if(values[1] > 0) {
	var returnTo=document.getElementById("returnTo");
	returnTo.disabled=false;
	stage.returnTo=returnTo.value;
	update('s',returnTo,stage.id);
  }
  else {
	var returnTo=document.getElementById("returnTo");
	returnTo.disabled=true;
	stage.returnTo="";
	returnTo.value="";
	update('s',returnTo,stage.id);
  }
//  drawStages();
}

function setSelectedIndex(b, v) {
  var s=document.getElementById(b);
  for(var i=0; i<s.options.length; i++) {
	if(s.options[i].value == v) {
	  s.options[i].selected=true;
	  return;
	}
  }
}

function setSelectedIndex1(b,v,splitor,idx) {
	var s=document.getElementById(b);
  for(var i=0; i<s.options.length; i++) {
	var values=s.options[i].value.split(splitor);
	if(values[idx] == v) {
	  s.options[i].selected=true;
	  return;
	}
  }
}

function handleEnter(table,input,id,e) {
	var charCode;
	if(e && e.which)
		charCode=e.which;
	else if(window.event) {
		e=window.event;
		charCode=e.keyCode;
	}
	if(charCode==13) {
		update(table,input,id);
	}
}

function setItems() {
	var items=document.getElementById("items");
	while(items.firstChild) {
		items.removeChild(items.firstChild);
	}
	
	var fieldType=document.getElementById("fieldType");
	if(fieldType.selectedIndex == 6) {
		var minLb=document.createElement("label");
		minLb.htmlFor="min";
		minLb.innerHTML="Min Value";
		items.appendChild(minLb);
		
		var minInput=document.createElement("input");
		minInput.id="min";
		minInput.name="min";
		minInput.type="number";
		items.appendChild(minInput);
		
		var maxLb=document.createElement("label");
		maxLb.htmlFor="max";
		maxLb.innerHTML="Max Value";
		items.appendChild(maxLb);
		
		var maxInput=document.createElement("input");
		maxInput.id="max";
		maxInput.name="max";
		maxInput.type="number";
		items.appendChild(maxInput);
	}
	else if(fieldType.selectedIndex == 11 || fieldType.selectedIndex == 12 || fieldType.selectedIndex == 13) {
		var label=document.createElement("label");
		label.htmlFor="itemArea";
		label.innerHTML="Items";
		items.appendChild(label);
		
		var itemArea=document.createElement("textarea");
		itemArea.id="itemArea";
		itemArea.name="itemArea";
		items.appendChild(itemArea);
	}
	
}

function addCustomField() {
	customFieldDlg = $("#custom-field-dlg").dialog({
		modal: true,
		buttons: {
	        "Submit": function() {
	        	submitCustomField();
	        	customFieldDlg.dialog("close");
	        },
	        Cancel: function() {
	        	customFieldDlg.dialog( "close" );
	        }
	      }
	});
}

function submitCustomField() {
	var ft = $("#fieldType").val();
	var fn = $("#fieldName").val();
	var min = "";
	var max = "";
	var itemArea = "";
	
	var cf=new CustomField();
	cf.fieldType=ft;
	cf.fieldName=fn;
	
	if(ft == 6) {
		min = $("#min").val();
		max = $("#max").val();
	}
	else if(ft == 'b' || ft == 'c' || ft == 'd') {
		itemArea = $("#itemArea").val();
		var lines = itemArea.split('\n');
		$.each(lines, function(){
		  var s = this.trim();
		  cf.items.push(s);
		});
	}
	stages[selectedNo].customFields.push(cf);
	
	$.ajax({
		type:"POST",
		url:"../postData",
		data: {"type": "addCustomField",
			"fieldType": ft,
			"fieldName": fn,
			"min": min,
			"max": max,
			"itemArea": itemArea,
			"selectedNo": $("#selectedNo").val(),
			"sgtId": stages[selectedNo].id
		},
		success:function() {
			refreshCustomField();
			customFieldDlg.dialog("close");
		}
	});
}

function setSubWorkflow() {
	$.ajax({
		type:"POST",
		url:"../requestItems",
		data: {
			"type": "templateWorkflow",
			"catId": $("#subwf-cat").val()
		},
		success:function(xml) {
			//clearItems("subwf-list");
			var html = "";
			$(xml).find('item').each(function(i,o){
				var n=unescape($(this).attr("name"));
				var v=$(this).attr("value");
				var s=n.split("_-");
				html += "<div class='sub-workflow-cb'><input type='checkbox' id='t" + v + "' value='" + v + "'>" + s[0] + "</input></div>";
				
			});
			$("#subwf-list").html(html);
			
		}
	});
}

function addSubWorkflow() {
	subWfDlg = $("#subwf-dlg").dialog({
		modal: true,
		buttons: {
	        "Submit": function() {
	        	submitSubWorkflow();
	        	subWfDlg.dialog("close");
	        },
	        Cancel: function() {
	        	subWfDlg.dialog( "close" );
	        }
	      }
	});
}

function submitSubWorkflow() {
	var ids = "";
	$(".subwf-cb").each(function() {
		var f = $(this).children().eq(0);
		if(f.prop("checked"))
			ids += f.val() + ",";
	});
	var sgtId = stages[selectedNo].id;
	if(ids.length > 0) {
		ids = ids.substring(0, ids.length-1);
		$.ajax({
			type:"POST",
			url:"../postData",
			data: {"type": "addSubWorkflow",
				"sgtId": sgtId,
				"ids": ids
			},
			success:function(xml) {
				adjustSubWorkflow(xml);
			}
		});
	}
}

function refreshSubWorkFlow() {
	$.ajax({
		type:"POST",
		url:"../requestItems",
		data: {"type": "subWorkflow",
			"sgtId": stages[selectedNo].id
		},
		success:function(xml) {
			adjustSubWorkflow(xml);
		}
	});
}

function adjustSubWorkflow(xml) {
	var txt = "";
	$(xml).find('item').each(function(i,o){
		var n=unescape($(this).attr("name"));
		var v=$(this).attr("value");
		txt += "<div class='subwf' id='dwft_" + v + "'>" + n + " &nbsp;<img src='../../images/delete.png' onclick='delSubWorkflow(" + v + ")'></div>";
	});
	$("#subworkflow").html(txt);
}

function delSubWorkflow(wftId) {
	var t = $("#dwft_" + wftId).html();
	var s = t.split(" ");
	var sgtId = stages[selectedNo].id;
	alert(sgtId + "/" + wftId);
	if(confirm("Do you want to delete sub-workflow '" + s[0] + "'?")) {
		$.ajax({
			type:"POST",
			url:"../postData",
			data: {"type": "delSubWorkflow",
				"sgtId": sgtId,
				"wftId": wftId
			},
			success:function() {
				$("#dwft_" + wftId).remove();
			}
		});
	}
}

function Stage(no) {	  
  this.x=(no*154)+4;
  this.y=(no*64)+4;
  this.width=200;
  this.height=60;
  this.color="green";
  this.isSelected=false;
  this.shift=0;
    
  this.id;
  this.twfId;
  this.no=no;
  this.name="";
  this.predecessor="";
  this.duration=7;
  this.owner;
  this.ownerName="";
  this.statusType;
  this.statusName;
  this.returnTo="";
  this.customFields=new Array();
  this.returnStage=0;
  this.link="";
  stages.push(this);
}

function CustomField(){
	this.fieldType;
	this.fieldName;
	this.items=new Array();
}

function Team(){
  this.id;
  this.checked;
}
