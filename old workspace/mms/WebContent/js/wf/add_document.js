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
		$("#dragAndDropFiles h1").css("display", "none");
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
		$.ajax({
			type:"POST",
			url:this.config.uploadUrl,
			data:data,
			cache: false,
			contentType: false,
			processData: false,
			success:function(xml){
				window.parent.location = "/cog/wf/workflow/stage.htm?sgId=" + $("#sgId").val();
			}
		});
	};
	
	multiUploader.prototype._startUpload = function(){
		var data = new FormData();
		var fileName = $("#fileName").val();
		var description = $("#description").val();
		var sgId = $("#sgId").val();
		var catId = $("#categories").val();
		var version = $("#version").val();
		var uId = $("#uId").val();
		var reviewer = $("#reviewer").val();
		var approver = $("#approver").val();
		var share = $("#share").val();
		
		
		if(fileName.length == 0 || catId == 0 || version.length == 0 || reviewer.length == 0 || approver.length == 0) {
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
		data.append("sgId", sgId);
		data.append("catId", catId);
		data.append("version", version);
		data.append("uId", uId);
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

function initMultiUploader(){
	new multiUploader(config);
}