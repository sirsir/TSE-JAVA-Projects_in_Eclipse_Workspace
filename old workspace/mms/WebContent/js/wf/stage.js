function setMsgType(idx) {
	var o=document.getElementById("msgtId_"+msgtId);
	var osrc=o.src;
	var oidx=osrc.lastIndexOf("/");
	o.src=osrc.substring(0,oidx) + "/" + osrc.substring(oidx+3, osrc.length);
	var n=document.getElementById("msgtId_"+idx);
	var nsrc=n.src;
	var nidx=nsrc.lastIndexOf("/");
	n.src=nsrc.substring(0,nidx) + "/s_" + nsrc.substring(nidx+1, nsrc.length);
	msgtId=idx;
}

function addSubworkflow(idx) {
	var n = $("#name-" + idx).val();
	if(n.length > 0) {
		var wftId = $("#wftId-" + idx).val();
		if(wftId) {
			var desc = $("#desc-" + idx).val();
			var uId = $("#uId").val();
			var depId = $("#depId").val();
			$.ajax({
				type:"post",
				url:"../postData",
				data: {"type": "newWorkflow",
					"name": n,
					"description": desc,
					"uId": uId,
					"depId": depId,
					"wftId": wftId,
					"sgId": sgId
				},
				success:function(xml) {
					/*
					 * <tr>
										<td>${workflow.name}</td>
										<td>
											<c:forEach items="${workflow.childrenStages}" var="stage">
												<a href="stage.htm?wfName=${workflow.name}&sgId=${stage.sgId}" target="_parent"><div class="substage ${stage.status.name}">${stage.stageTemplate.name}</div></a>
											</c:forEach>
										</td>
									</tr>
								
					 */
					var html = "";
					var n;
					$(xml).find('item').each(function(i,o){
						n=$(this).attr("name");
						var i=$(this).attr("sgId");
						var s=$(this).attr("status");
						var t=$(this).attr("templateName");
						html += "<a href='stage.htm?wfName=" + n + "&sgId=" + i + "' target='_parent'><div class='substage " + s + "'>" + t + "</div></a>";
					});
					html = "<tr><td>" + n + "</td><td>" + html + "</td></tr>";
					
					$("#workflow-" + idx).html(html + $("#workflow-" + idx).html());
					$("#s-" + idx + "Box").css("display", "none");
				}
			});
		}
		else alert("Please select workflow template.");
	}
	else alert("Please fill Name.");
}

function multiUploader(config) {
  
	this.config = config;
	this.items = "";
	this.all = [];
	this.msgImg = null;
	var self = this;
	
	multiUploader.prototype._init = function(){
		if (window.File && 
			window.FileReader && 
			window.FileList && 
			window.Blob) {		
			 //document.getElementById("dragAndDropFiles").addEventListener("click", function() {document.getElementById("msgImg").click();}, false);
			 //document.getElementById("dragAndDropImg").addEventListener("dragover", function() {alert('a');return false;}, false);
			 document.getElementById("dragAndDropFiles").addEventListener("drop", this._dropImg, false);
			 
			 //var inputId = $("#"+this.config.form).find("input[type='file']").eq(0).attr("id");
			 document.getElementById("file").addEventListener("change", this._read, false);
			 document.getElementById(this.config.dragArea).addEventListener("dragover", function(e){ e.stopPropagation(); e.preventDefault(); }, false);
			 document.getElementById(this.config.dragArea).addEventListener("drop", this._dropFiles, false);
			 document.getElementById(this.config.form).addEventListener("submit", this._submit, false);
		} else console.log("Browser supports failed");
	};
	
	multiUploader.prototype._submit = function(e){
		e.stopPropagation();
		e.preventDefault();
		self._startUpload();
	};
	
	multiUploader.prototype._preview = function(){
		$(".dfiles").remove();
		$("#dragAndDropFiles h1").css("display", "none");
		//var uId = "";
		for(var i = 0; i<self.all.length; i++){
			//uId = self.all[i].name._unique();
			//var sampleIcon = '<img src="../../images/file_type/unknown.png" />';
			var name=self.all[i].name;
			var type=name.substring(name.lastIndexOf(".")+1, name.length);
			/*
			if(type=="jpg"||type=="png"||type=="bmp"||type=="jpeg"||type=="gif")
				sampleIcon = '<img src="../../images/file_type/image.png" />';
			else if(type=="rar"||type=="zip"||type=="tar")
				sampleIcon = '<img src="../../images/file_type/zip.png" />';
			else if(type=="doc"||type=="docx"||type=="sxw")
				sampleIcon = '<img src="../../images/file_type/word.png" />';
			else if(type=="xls"||type=="xlsx"||type=="sxc")
				sampleIcon = '<img src="../../images/file_type/excel.png" />';
			else if(type=="ppt"||type=="pptx"||type=="sxd")
				sampleIcon = '<img src="../../images/file_type/powerpoint.png" />';
			else if(type=="pdf")
				sampleIcon = '<img src="../../images/file_type/pdf.png" />';
			else if(type=="txt")
				sampleIcon = '<img src="../../images/file_type/text.png" />';
			else if(type=="html"||type=="htm"||type=="asp"||type=="jsp"||type=="php")
				sampleIcon = '<img src="../../images/file_type/html.png" />';
			else if(type=="xml")
				sampleIcon = '<img src="../../images/file_type/xml.png" />';
			else if(type=="css")
				sampleIcon = '<img src="../../images/file_type/css.png" />';
			else if(type=="js")
				sampleIcon = '<img src="../../images/file_type/js.png" />';
			else if(type=="java")
				sampleIcon = '<img src="../../images/file_type/java.png" />';
			else if(type=="class")
				sampleIcon = '<img src="../../images/file_type/class.png" />';
			else if(type=="jar")
				sampleIcon = '<img src="../../images/file_type/jar.png" />';
			$("#dragAndDropFiles").append('<div class="dfiles">'+sampleIcon+self.all[i].name+'<img id="f' + i + '" src="../../images/bin.png"/></div></div>');
			*/
			$("#dragAndDropFiles").append('<div class="dfiles">'+self.all[i].name+'<img id="f' + i + '" src="../../images/delete.png"/></div></div>');
			
			document.getElementById("f"+i).addEventListener("click", this._cancel, false);
		}
	};
	
	multiUploader.prototype._cancel = function(evt){
		var n=this.id.split("f");
		var name=self.all[n[1]].name;
		if(confirm("Do you want to cancel file '"+name+"'?")) { 
			self.all.splice(n[1],1);
			self._preview();
		}
	};

	multiUploader.prototype._read = function(evt){
		if(evt.target.files){
			self._distinct(evt.target.files);
			self._preview();
		} 
		else console.log("Failed file reading");
	};
	
	multiUploader.prototype._validate = function(format){
		var arr = this.config.support.split(",");
		return arr.indexOf(format);
	};
	
	multiUploader.prototype._dropFiles = function(e){
		e.stopPropagation();
		e.preventDefault();
		self._distinct(e.dataTransfer.files);
		self._preview();
	};
	
	/*multiUploader.prototype._readImg = function(evt){
		if(evt.target.files){
			var f=evt.target.files[0];
			self.msgImg=f;
			
			if (!f.type.match('image.*')) {
		        return;
		      }
			
		      var reader = new FileReader();

		      // Closure to capture the file information.
		      reader.onload = (function(theFile) {
		        return function(e) {
		          // Render thumbnail.
		        	var img=document.getElementById('dragAndDropImg');
		        	img.style.background="url('" + e.target.result + "') center";
		        	img.style.backgroundSize="140px 140px";
		        };
		      })(f);

		      // Read in the image file as a data URL.
		      reader.readAsDataURL(f);
		} 
		else console.log("Failed file reading");
	};*/
	
	multiUploader.prototype._dropImg = function(evt){
		evt.stopPropagation();
		evt.preventDefault();
		var f=evt.dataTransfer.files[0];
		self.msgImg=f;
		if (!f.type.match('image.*')) {
	        return;
	      }
		
	      var reader = new FileReader();

	      // Closure to capture the file information.
	      reader.onload = (function(theFile) {
	        return function(e) {
	          // Render thumbnail.
	        	var img=document.getElementById('dragAndDropImg');
	        	img.style.background="url('" + e.target.result + "') center";
	        	img.style.backgroundSize="140px 140px";
	        };
	      })(f);

	      // Read in the image file as a data URL.
	      reader.readAsDataURL(f);
	      return false;
	};
		
	multiUploader.prototype._distinct = function(files){
		if(self.all.length==0) {
			for(var i=0; i<files.length; i++) {
				self.all.push(files[i]);
			}
		}
		else {
			for(var i=0; i<files.length; i++) {
				var dup=false;
				for(var j=0; j<self.all.length; j++){
					if(files[i].name == self.all[j].name){
						dup=true;
						break;
					}
				}
				if(!dup)
					self.all.push(files[i]); 
			}
		}
	};
	
	multiUploader.prototype._uploader = function(data){
		
		//$(".dfiles[rel='"+ids+"']").find(".progress").show();
		
		$.ajax({
			type:"POST",
			url:this.config.uploadUrl,
			data:data,
			cache: false,
			contentType: false,
			processData: false,
			success:function(xml){
				var html = "";
				$(xml).find('msg').each(function(i,o) {
					var p=$(this).attr("userPicture");
					var s=$(this).attr("subject");
					var t=$(this).attr("text");
					html += "<article><div class='msgBox'>";
					html += "<img src='../../images/user/" + p + "' class='rounded50'/>";
					html += "<div class='arrow_box'>";
					html += "<div class='subject'>" + s + "</div>";
					html += "<div class='msg'>" + t + "</div>";
					html += "<div class='msgDoc'>";
					$(this).find('doc').each(function(i,o) {
						html += $(this).attr("name");
					});
					html += "</div></div></div></article>";
					
				});
				html += $("#msg-section").html();
				$("#msg-section").html(html);
				
				$("#subject").val("");
				$("#text").val("");
				$("#postBox").css("display", "none");
				this.all = [];
				self.all = [];
				$(".dfiles").remove();
				$("#dragAndDropFiles h1").css("display", "");
				//var dhtml = "<h1>Click / Drop Documents Here</h1><p><input id='file' type='file' name='file' multiple style='display:none'/></p><div class='progressBar'><div class='status'></div></div>";
				//$("#dragAndDropFiles").html(dhtml);
				
			}
		});
	};
	
	multiUploader.prototype._startUpload = function(){
		var data = new FormData();
		var subject = $("#subject").val();
		var text = $("#text").val();
		var uId = $("#uId").val();
		var userPicture = $("#userPicture").val();
		
		data.append("subject", subject);
		data.append("text", text);
		data.append("sgId", sgId);
		data.append("msgtId", msgtId);
		data.append("uId", uId);
		data.append("userPicture", userPicture);
		if(this.all.length > 0){
			for(var k=0; k<this.all.length; k++){
				var file = this.all[k];
				//this._uploader(file,0);
				data.append(file.name, file);
			}
		}
//		if(this.msgImg != null)
//			data.append(this.msgImg.name, this.msgImg);
		this._uploader(data);
	};
	
	String.prototype._unique = function(){
		return this.replace(/[a-zA-Z]/g, function(c){
     	   return String.fromCharCode((c <= "Z" ? 90 : 122) >= (c = c.charCodeAt(0) + 13) ? c : c - 26);
    	});
	};

	this._init();
}

function initMultiUploader(){
	new multiUploader(config);
}