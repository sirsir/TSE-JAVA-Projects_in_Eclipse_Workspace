var selectedTr=-1;
var twfId=-1;
function refreshDepartments() {
	var coId=document.getElementById("customer").value;
	var url="../requestItems?type=department&coId=" + coId;
	xmlhttp.open("get", url, true);
	xmlhttp.onreadystatechange=function() {
		if(xmlhttp.readyState==4) {
			var xml=xmlhttp.responseXML;
			var xmlObj=xml.documentElement;
			var items=xmlObj.getElementsByTagName("item");
			var sel = clearItems("department");
			for(var i=0; i<items.length; i++) {
				var option=document.createElement('option');
				option.name=unescape(items[i].getAttribute('name'));
				option.value=items[i].getAttribute('value');
				option.innerHTML=items[i].getAttribute('name');
				sel.appendChild(option);
			}
		}
	};
	xmlhttp.send();
}

function clearItems(type) {
	var sel=document.getElementById(type);
	while(sel.childNodes.length >= 1) {
		sel.removeChild(sel.firstChild);
	}			
	return sel;
}

function refreshTemplate() {
	selectedTr=-1;
	twfId=-1;
	var templateWorkflows=clearItems("templateWorkflows");
	/*var table=document.createElement("table");
	table.style.width="100%";
	table.style.border="0";
	table.style.align="center";
	table.style.cellpadding="0";
	table.style.cellspacing="0";*/
	var catId=document.getElementById("category").value;
	
	/*var td0=document.createElement("td");
	td0.className="col";
	td0.width="220px";
	td0.innerHTML="&nbsp;Name";
	var td1=document.createElement("td");
	td1.className="col";
	td1.width="220px";
	td1.innerHTML="&nbsp;Category";
	var td2=document.createElement("td");
	td2.className="col";
	td2.width="220px";
	td2.innerHTML="&nbsp;Comment";
	
	var tr0=document.createElement("tr");
	tr0.style.height="24px";
	tr0.appendChild(td0);
	tr0.appendChild(td1);
	tr0.appendChild(td2);
	table.appendChild(tr0);*/
	
	var url="../requestItems?type=templateWorkflow&catId=" + catId;
	xmlhttp.open("get", url, true);
	xmlhttp.onreadystatechange=function() {
		if(xmlhttp.readyState==4) {
			var xml=xmlhttp.responseXML;
			var xmlObj=xml.documentElement;
			var items=xmlObj.getElementsByTagName("item");
			
			for(var i=0; i<items.length; i++) {
				var s=unescape(items[i].getAttribute('name')).split("_-");
				var td3=document.createElement("td");
				td3.className="row";
				td3.innerHTML=s[0];
				var td4=document.createElement("td");
				td4.className="row";
				td4.innerHTML=s[1];
				/*var td5=document.createElement("td");
				td5.className="row";
				td5.innerHTML=s[2];*/
				
				var tr1=document.createElement("tr");
				tr1.id=i;
				tr1.onmouseover=function() {
					if(selectedTr != this.id)
						this.bgColor="#d0dafd";
				};
				tr1.onclick=function() {
					selectedTr=this.id;
					
					/*var div=document.getElementById("templateWorkflows");
					var trs=div.getElementsByTagName("tr");
					for(var i=0; i<trs.length; i++) {
						if((trs[i].id%2)==1)
							trs[i].bgColor="#fbfbfb";
						else trs[i].bgColor="#ffffff";
					}*/
					var div=document.getElementById("templateWorkflows");
					var trs=div.getElementsByTagName("tr");
					for(var i=0; i<trs.length; i++) {
						trs[i].bgColor="#e8edff";
					}
					this.bgColor="#ff8c00";
					twfId=items[this.id].getAttribute('value');
					document.getElementById('previewFrame').src="preview.htm?twfId=" + twfId;
					
				};
				
				tr1.bgColor="#e8edff";
				tr1.onmouseout=function() {
					if(selectedTr != this.id)
						this.bgColor="#e8edff";
				};
				/*if((i%2) == 1){
					tr1.bgColor="#fbfbfb";
					tr1.onmouseout=function() {
						if(selectedTr != this.id)
							this.bgColor="#fbfbfb";
					};
				}
				else {
					tr1.onmouseout=function() {
						if(selectedTr != this.id)
							this.bgColor="#ffffff";
					};
				}*/
				tr1.style="height:24px";
				tr1.appendChild(td3);
				tr1.appendChild(td4);
				//tr1.appendChild(td5);
				templateWorkflows.appendChild(tr1);
			}
		}
	};
	xmlhttp.send();
	//templateWorkflows.appendChild(table);
}

/*function _preview() {
	if(document.getElementById("preview").checked && twfId >= 0) {
		document.getElementById('previewFrame').src="preview.htm?twfId=" + twfId;
		document.getElementById("previewFrame").hidden=false;
	}
	else document.getElementById("previewFrame").hidden=true;
}*/

function _submit() {
	if(twfId < 1) {
		alert("Please select a workflow template.");
		return;
	}
	document.getElementById("twfId").value=twfId;
	//document.form.submit();
	/* var name=document.getElementById("projectName").value;
	var description=document.getElementById("description").value;
	var depId=document.getElementById("department").value;
	window.location="project.htm?type=new&twfId="+twfId+"&name="+name+"&description="+description+"&depId="+depId; */
	
}