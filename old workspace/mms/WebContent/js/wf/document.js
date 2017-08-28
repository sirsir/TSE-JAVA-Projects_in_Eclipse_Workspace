function showFiles(key, view, uId) {
	$.ajax({
		type:"POST",
		url:"/cog/wf/document",
		data: {
			"type": "treeView",
			"key": key,
			"view": view,
			"uId" : uId
		},
		success:function(xml) {
			if($(xml).find('item').length == 0) {
				$("#file-container").html("");
				return;
			}
			var html = "<table id='table' width='100%'><thead><tr><th></th><th>Name</th><th>Version</th><th>Size</th><th>Date</th><th>Owner</th><th>Action</th></tr></thead><tbody id='row'>";
		
			$(xml).find('item').each(function(i,o) {
					//alert("success");
				docId = $(this).attr("docId");
				var catId = $(this).attr("catId");
				var fileName=$(this).attr("fileName");
				var size=$(this).attr("size");
				var desc=$(this).attr("desc");
				if(desc == 'null')
					desc = "&nbsp;";
				var version=$(this).attr("version");
				var release=$(this).attr("release");
				next = parseInt(release)+1;
				var expire=$(this).attr("expire");
				var sgId=$(this).attr("sgId");
				var parentSgId=$(this).attr("parentSgId");
				var wfName=$(this).attr("wfName");
				var wfDesc=$(this).attr("wfDesc");
				var owner=$(this).attr("owner");
				var create=$(this).attr("create");
				var approved=$(this).attr("approved");
				var lastRelease = $(this).attr("lastRelease");
				var expired = $(this).attr("setExp");
				
				var suffix = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length);
				html += "<tr id='r" + docId + "'><td class='col0'><img src='/cog/images/file_type/" + suffix + ".png' onError='this.onerror=null;this.src=\"/cog/images/file_type/unknown.png\";' /></td><td class='col1'>" + fileName + "<br><p style='color:rgb(140,140,140); font-style: italic;'>" + desc + "</p></td>";
				if(version == 'null')
					html += "<td class='col2'></td>";
				else if(catId <= 0)
					html += "<td class='col2' style='vertical-align: top'>v" + version + "</td>"; 
				else  html += "<td class='col2' style='vertical-align: top'>v" + version + "&nbsp;&nbsp;&nbsp;#" + release + "</td>";
				
				html += "<td class='col3' style='vertical-align: top'>" + size + "<td class='col4' style='vertical-align: top'>create: " + create + "<br>expire: " + expire + "</td><td class='col5' style='vertical-align: top'>" + owner + "</td>" + 
					"<td class='col6'>";
				
				
				html +="<img id='w"+docId+"' title='";
				if(lastRelease == 'n' || expired != 0){
					if(expired != 0)
						html +="This file has been requested to remove' src='/cog/images/wf/warning2.png'";
					else html +="This file is not latest release' src='/cog/images/wf/warning3.png'"; 
					html +="style='display:;'>";
				}
				else html +="'src='/cog/images/wf/warning2.png' style='display:none;'>";
				if(expired == 0)
					html +="<img src='/cog/images/wf/remove.png' id='rm"+docId+"' fileName='"+fileName+"' class='action reqRemove' title='Request to remove this file'>";
				if(approved == 'n' && expired == 0)
					html+="<img src='/cog/images/wf/dwn_grey.png' id='m" + docId + "' title='This file is not approved yet'><!--<img src='/cog/images/wf/remove.png' id='rm"+docId+"' fileName='"+fileName+"' class='action reqRemove' title='Request to remove this file'>--!>";
				else if(expired == 0){
					if(lastRelease == 'n')
						html+="<img src='/cog/images/wf/dwn.png' id='m" + docId + "' nlast='y' class='action file' title='Download this file'>";
					else html+="<img src='/cog/images/wf/dwn.png' id='m" + docId + "' class='action file' title='Download this file'>";
				}
				
				
				if(catId > 0 && approved != 'n' && lastRelease !='n' && expired == 0)
					html += "<img src='/cog/images/wf/new_release.png' id='m" + docId + "' class='action new' title='Upload new release' next='"+next+"'>";
				
				if(catId > 0 && release > 1)
						html += "<img src='/cog/images/wf/show_release.png' id='t_" + docId + "_" + parentSgId + "_" + release + "' class='action show' title='Show old release'>";
				
				html +="<span id='msg"+docId+"' docExp='"+expired+"' style='display:none;'></span>";
				html += "</td></tr>";
					  
				html += "<tr id='n" + docId + "' style='display:none;background-color: #fbfbfb'><td colspan='7' class='release-form'>";
				
				html += "</td></tr>";
				
				html += "<tr id='s" + docId + "' style='display:none;background-color: #fbfbfb'><td colspan='7'><div>";
				html += "<table width='100%'><thead><tr><th class='col1'>Name</th><th>Version</th><th>Size</th><th>Date</th><th>Owner</th><th>Action</th></tr></thead><tbody id='rt" + docId + "'></tbody></table></div></td></tr>";
			});
			html += "</tbody></table>";
			$("#file-container").html(html);
			
			$(".file").click(function() {
				var nlast = $(this).attr("nlast");
				var confirm_dwn = true;
				if(nlast != null)
					confirm_dwn = confirm("This file is not latest release.\nDo you want to download this file?");
				if(confirm_dwn){
					var docId_dwn = $(this).attr("id");
					docId_dwn = docId_dwn.substring(1, docId_dwn.length);
					window.location = "/cog/wf/viewDocument?docId="+docId_dwn;
				}
			});
			
			$(".reqRemove").click(function() {
				if(confirm("Do you want to remove file '"+$(this).attr("fileName")+"'?")){
					var docId = $(this).attr("id");
					docId = docId.substring(2, docId.length);
					$(this).css("display","none");
					$("#w"+docId).attr("title","This file has been requested to remove");
					$("#w"+docId).css("display","");
					$("#msg"+docId).html("Your remove request was sent.");
					$("#msg"+docId).css("display","");
					$("#s"+docId).css("display","none");
					$("#m"+docId).css("display","none");
					
					$.ajax({
						type:"POST",
						url:"/cog/wf/document",
						data: {
							"type": "reqRemove",
							"docId": docId
						},
						success:function(xml) {
							
						}
					});
				}
			});
			
			
			$(".new").click(function() {
				var id = $(this).attr("id");
				var nextR = $(this).attr("next");
				id = id.substring(1, id.length);
				
				$(".release-form").empty();
				var html = "<form name='newRelease' id='newRelease' enctype='multipart/form-data'>" +
					"<div class='comment'><div>Release #" + nextR + "</div><div><textarea id='comment' name='text' placeholder='Comment'></textarea></div></div>" +
					"<div id='dragAndDropFiles' class='uploadArea' fileItem='' changed=''>" +
						"<h1>Click / Drop Documents Here</h1>" +
						"<p><input id='file' type='file' name='file' style='display:none'/></p>" +
						"<input type='hidden' id='docId' value='" + id + "'>" +
						"<input type='hidden' id='next' value='" + nextR + "'>" +
						"<div class='progressBar'><div class='status'></div></div>" +
					"</div>" +
					"<div id='submitRelease'><input type='submit' class='button small green' name='submitHandler' id='submitHandler' value='Upload'/></div>" +
					"</form>";
				

				$("#n" + id).children().eq(0).html(html);
				
				
				
				$(".uploadArea").click(function() {
				var f = $(this).attr('fileItem');
				var c = $(this).attr('changed');
				var d = $("#file").css("display");
					if(d == "none" && f  < 1 && c < 1) {
						$("#file").css("display", "");
						$("#file").click();
					}
					else {
						$(this).attr('changed','0');
						$("#file").css("display", "none");
					}
				});
				initMultiUploader(config);
				
				$("#s" + id).css("display", "none");
				var d = $("#n" + id).css("display");
				if(d == 'none')
					$("#n" + id).css("display", "");
				else $("#n" + id).css("display", "none");
			});
			
			$(".show").click(function() {
				var id = $(this).attr("id");
				var s = id.split("_");
				
				$("#n" + s[1]).css("display", "none");
				var st = $("#s" + s[1]);
				
				var d = $(st).css("display");
				if(d == 'none')
					$(st).css("display", "");
				else {
					$(st).css("display", "none");
					return;
				}
				$.ajax({
					type:"POST",
					url:"/cog/wf/document",
					data: {
						"type": "oldRelease",
						"docId": s[1],
						"parentSgId": s[2],
						"maxRelease": s[3]
					},
					success:function(xml) {
						var html = "";
						$(xml).find('item').each(function(i,o) {
							var docId = $(this).attr("docId");
							var fileName=$(this).attr("fileName");
							var size=$(this).attr("size");
							var desc=$(this).attr("desc");
							if(desc == 'null')
								desc = "&nbsp;";
							var version=$(this).attr("version");
							var release=$(this).attr("release");
							var expire=$(this).attr("expire");
							var owner=$(this).attr("owner");
							var create=$(this).attr("create");
							var expired = $(this).attr("setExp");
							
							html += "<tr><td class='col1'>" + fileName + "<br><p style='color:rgb(140,140,140); font-style: italic;'>" + desc + "</p></td><td class='col2' style='vertical-align: top'>v" + version + "&nbsp;&nbsp;&nbsp;#" + release + "</td>" +
								"<td class='col3' style='vertical-align: top'>" + size  +"</td><td class='col4'>create: " + create + "<br>expire: " + expire + "</td><td class='col5' style='vertical-align: top'>" + owner + "</td><td>";
							if(expired < 2){
								if(expired < 1)
									html+="<img src='/cog/images/wf/remove.png' id='rm"+docId+"' lastestId='"+s[1]+"' fileName='"+fileName+"' release='"+release+"' maxrelease='"+s[3]+"' class='action reqRemoveOldR' title='Request to remove this file'><img src='/cog/images/wf/dwn.png' id='m" + docId + "' class='action file' title='Download this file'>";
								else html+= "<img src='/cog/images/wf/warning2.png' id='w"+docId+"' title='This file has been requested to remove'>";
							}
							else html+= "<span style='font-style: italic; font-weight: bold; color: red;'>Removed</span>";
							
							html+="</td></tr>";
						});
						
						$($("#rt" + s[1])).html(html);
						
						
						$(".file").click(function() {
							var confirm_dwn = confirm("This file is not latest release.\nDo you want to download this file?");
							if(confirm_dwn){
								var docId_dwn = $(this).attr("id");
								docId_dwn = docId_dwn.substring(1, docId_dwn.length);
								window.location = "/cog/wf/viewDocument?docId="+docId_dwn;
							}
							window.location = "/cog/wf/viewDocument?docId="+docId_dwn;
						});
						
						$(".reqRemoveOldR").click(function() {
							var release = $(this).attr("release");
							if(confirm("Do you want to remove file '"+$(this).attr("fileName")+" Release: "+release+" '?")){
								var docId = $(this).attr("id");
								docId = docId.substring(2, docId.length);
								var lastest_docId = $(this).attr("lastestId");
								var maxRelease = $(this).attr("maxrelease")-1;
								var order = maxRelease - release;
								
								var oldReleaseTable = $("#rt"+lastest_docId).children().eq(order);
								oldReleaseTable.children().eq(5).html("<img title='This file has been requested to remove' src='/cog/images/wf/warning2.png' >Your remove requset was sent");
								
								$.ajax({
									type:"POST",
									url:"/cog/wf/document",
									data: {
										"type": "reqRemove",
										"docId": docId
									},
									success:function(xml) {
										
									}
								});
							}
						});
						
					}
				});
			});
		}
	});
}

//Add NewRelease
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
			document.getElementById("dragAndDropFiles").addEventListener("drop", this._dropImg, false);
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
		if(self.all.length == 0)
			$("#dragAndDropFiles h1").css("display", "");
		else $("#dragAndDropFiles h1").css("display", "none");
		$("#dragAndDropFiles").attr("fileItem", self.all.length);
		for(var i = 0; i<self.all.length; i++){
			var name=self.all[i].name;
			var type=name.substring(name.lastIndexOf(".")+1, name.length);
			
			$("#dragAndDropFiles").append('<div class="dfiles">'+self.all[i].name+'<img id="f' + i + '" src="../../images/delete.png"/></div></div>');
			
			document.getElementById("f"+i).addEventListener("click", this._cancel, false);
		}
	};
	
	multiUploader.prototype._cancel = function(evt){
		var n=this.id.split("f");
		var name=self.all[n[1]].name;
		if(confirm("Do you want to cancel file '"+name+"'?")) { 
			$("#dragAndDropFiles").attr("changed", "1");
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
	};
	
	multiUploader.prototype._uploader = function(data) {
		$.ajax({
			type:"POST",
			url:this.config.uploadUrl,
			data:data,
			cache: false,
			contentType: false,
			processData: false,
			success:function(xml){
				$("#comment").val("");
				$(".release-form").empty();
				this.all = [];
				self.all = [];
				$(".dfiles").remove();
				$("#dragAndDropFiles h1").css("display", "");
				
				var view = $("#viewBy").val();
				var nodeKey = $("#tree").fancytree("getActiveNode");
				window.parent.location = "/cog/wf/document/document.htm?view="+view+"&folderId=" + nodeKey.key;
			}
		});
	};
	
	multiUploader.prototype._startUpload = function(){
		var data = new FormData();
		var docId = $("#docId").val();
		var next = $("#next").val();
		var comment = $("#comment").val();
		var uId = $("#uId").html();
		
		data.append("docId", docId);
		data.append("next", next);
		data.append("comment", comment);
		data.append("uId", uId);
		if(this.all.length > 0){
			for(var k=0; k<1; k++){
				var file = this.all[k];
				data.append(file.name, file);
			}
		}
		this._uploader(data);
	};
	
	String.prototype._unique = function(){
		return this.replace(/[a-zA-Z]/g, function(c){
     	   return String.fromCharCode((c <= "Z" ? 90 : 122) >= (c = c.charCodeAt(0) + 13) ? c : c - 26);
    	});
	};

	this._init();
}


function initMultiUploader() {
	new multiUploader(config);
}

//Add NewDoc 
function _multiUploader(config) {
	  
	this.config = config;
	this.items = "";
	this.all = [];
	this.msgImg = null;
	var self = this;
	
	_multiUploader.prototype._init = function(){
		if (window.File && 
			window.FileReader && 
			window.FileList && 
			window.Blob) {		
			 document.getElementById("_dragAndDropFiles").addEventListener("drop", this._dropImg, false);
			 document.getElementById("_file").addEventListener("change", this._read, false);
			 document.getElementById(this.config.dragArea).addEventListener("dragover", function(e){ e.stopPropagation(); e.preventDefault(); }, false);
			 document.getElementById(this.config.dragArea).addEventListener("drop", this._dropFiles, false);
			 document.getElementById(this.config.form).addEventListener("submit", this._submit, false);
		} else console.log("Browser supports failed");
	};
	
	_multiUploader.prototype._submit = function(e){
		e.stopPropagation();
		e.preventDefault();
		self._startUpload();
	};
	
	_multiUploader.prototype._preview = function(){
		$("._dfiles").remove();
		if(self.all.length == 0)
			$("#_dragAndDropFiles h1").css("display", "");
		else $("#_dragAndDropFiles h1").css("display", "none");
		for(var i = 0; i<self.all.length; i++){
			var name=self.all[i].name;
			var type=name.substring(name.lastIndexOf(".")+1, name.length);
			$("#_dragAndDropFiles").append('<div class="_dfiles">'+self.all[i].name+'<img id="f' + i + '" src="../../images/delete.png"/></div></div>');
			document.getElementById("f"+i).addEventListener("click", this._cancel, false);
		}
	};
	
	_multiUploader.prototype._cancel = function(evt){
		var n=this.id.split("f");
		var name=self.all[n[1]].name;
		if(confirm("Do you want to cancel file '"+name+"'?")) { 
			$("#_dragAndDropFiles").attr("changed", "1");
			self.all.splice(n[1],1);
			self._preview();
		}
	};

	_multiUploader.prototype._read = function(evt){
		if(evt.target.files){
			self._distinct(evt.target.files);
			self._preview();
		} 
		else console.log("Failed file reading");
	};
	
	_multiUploader.prototype._validate = function(format){
		var arr = this.config.support.split(",");
		return arr.indexOf(format);
	};
	
	_multiUploader.prototype._dropFiles = function(e){
		e.stopPropagation();
		e.preventDefault();
		self._distinct(e.dataTransfer.files);
		self._preview();
	};
	
	_multiUploader.prototype._dropImg = function(evt){
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
		
	_multiUploader.prototype._distinct = function(files){
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
	
	_multiUploader.prototype._uploader = function(data){
		$.ajax({
			type:"POST",
			url:this.config.uploadUrl,
			data:data,
			cache: false,
			contentType: false,
			processData: false,
			success:function(xml){
				if($(xml).find('item').length < 1) {
					var view = $("#viewBy").val();
					var nodeKey = $("#tree").fancytree("getActiveNode");
					window.parent.location = "/cog/wf/document/document.htm?view="+view+"&folderId=" + nodeKey.key+"_"+$("#categories option:selected").text().replace(" ","_");
				} else {
				var msg = "Add Document Error: Duplicate file in directory\nCategory : "+$("#categories option:selected").text()+"\nDuplicate Item:\n";
				var idx = 1;
				$(xml).find('item').each(function(i,o) {
					msg += idx+". "+$(this).html()+"\n";
					idx = idx+1;
				});
					alert(msg);
				}
			}	
		});
	};
	
	_multiUploader.prototype._startUpload = function(){
		var data = new FormData();
		var fileName = $("#fileName").val();
		var description = $("#description").val();
		var nodeKey = $("#tree").fancytree("getActiveNode");
		var sgId = $("#"+nodeKey.key).attr("sgid");
		var catId = $("#categories").val();
		var version = $("#version").val();
		var expire = $("#expire").val();
		var reviewer = $("#reviewer").val();
		var approver = $("#approver").val();
		var share = $("#share").val();
		var uId = $("#uId").html();
		
		
		if(fileName.length == 0 || catId == 0 || version.length == 0 || expire.length == 0 || reviewer.length == 0 || approver.length == 0) {
			alert('Please fill all data.');
			return;
		}
		
		if(this.all.length == 0) {
			alert('Please provide document files');
			return
		}
			
		
		data.append("type", "add");
		data.append("fileName", fileName);
		data.append("description", description);
		data.append("uId", uId);
		data.append("sgId", sgId);
		data.append("catId", catId);
		data.append("version", version);
		data.append("expire", expire);
		data.append("reviewer", reviewer);
		data.append("approver", approver);
		data.append("share", share);
		if(this.all.length > 0){
			for(var k=0; k<this.all.length; k++){
				var file = this.all[k];
				//this._uploader(file,0);
				data.append(file.name, file);
			}
		}
		this._uploader(data);
	};
	
	String.prototype._unique = function(){
		return this.replace(/[a-zA-Z]/g, function(c){
     	   return String.fromCharCode((c <= "Z" ? 90 : 122) >= (c = c.charCodeAt(0) + 13) ? c : c - 26);
    	});
	};

	this._init();
}

function _initMultiUploader(){
	new _multiUploader(config_addDoc);
}