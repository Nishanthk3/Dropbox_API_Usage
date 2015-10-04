/**
 * 
 */
	function clearText()
	{
		$('#textarea').val("");
	}
	
	function signOut() {
		$.ajax({
			type : 'GET',
			url : "signOut.html",
			statusCode: {
			    404: function() {
			      alert( "Not Found" );
			    },
				401: function() {
				      alert( "Unauthorized - Access Denied" );
		    	}
			},
			success : function(data) {
				$("#btn_sign_out").hide();
				$("#btn_sign_on").removeAttr('disabled');
				$("#btn_sign_on").text("Sign In To Dropbox Account");
				$('#textarea').val("");
				$('#fileId').val("");
				$('#add_file_name').val("");
				$('#add_file').val("");
				fileOpened = false;
				
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				//alert("Status: " + textStatus);
				//alert("Error: " + errorThrown);
			}
		});
	}
	
 	function signIn() {
		$.ajax({
			type : 'GET',
			url : "signIn.html",
			statusCode: {
			    404: function() {
			      alert( "Not Found" );
			    },
				401: function() {
				      alert( "Unauthorized - Access Denied" );
		    	}
			},
			success : function(data) {
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				//alert("Status: " + textStatus);
				//alert("Error: " + errorThrown);
			}
		});
	} 

	function accountInfoCall() {
		$.ajax({
			type : 'GET',
			url : "accountInfo.html",
			statusCode: {
			    404: function() {
			      alert( "Not Found" );
			    },
				401: function() {
				      alert( "Unauthorized - Access Denied" );
		    	}
			},
			success : function(text) {
				text = text.replace(/\r?\n/g, '\n');
				$('#textarea').val(text);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				//alert("Status: " + textStatus);
				//alert("Error: " + errorThrown);
			}
		});
	}

	function getFileCall() {
		var fileName = $('#fileId').val();
		if(fileName.length > 0)
		{
			$.ajax({
				type : 'GET',
				url : "files.html?fileName=" + fileName,
				statusCode: {
				    404: function() {
				      alert( "File Not Found" );
				    },
					401: function() {
					      alert( "Unauthorized - Access Denied" );
			    	}
				},
				success : function(text) {
					if(text == "Not Signed In")
					{
						alert("Please sign into your dropbox account");
					}
					else
					{
						fileOpened = true;
						text = text.replace(/\r?\n/g, '\n');
						$('#textarea').val(text);
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					//alert("Status: " + textStatus);
					//alert("Error: " + errorThrown);
				} 
			});
		}
		else
		{
			alert("Please enter the fileName");
		}
	}

	/*     function saveFile() {
	 var fileName = $('#fileId').val();
	 var fileContent = $('#textarea').val();
	 fileContent = fileContent.replace(/\r?\n/g, '\n');
	 $.ajax({
	 type : 'GET',
	 url : "updateFile.html?fileContent="+fileContent+"&fileName="+fileName,
	 success : function(text) {
	 alert(text)
	 },
	 error: function(XMLHttpRequest, textStatus, errorThrown) { 
	 alert("Status: " + textStatus); alert("Error: " + errorThrown); 
	 }  
	 });
	 } */
	function saveFile() {
		var fileName = $('#fileId').val();
		var fileContent = $('#textarea').val();
		fileContent = fileContent.replace(/\r?\n/g, '\n');
		if(fileOpened == false)
		{
			alert("Open the file to save it");
		}
		else if(fileName.length > 0)
		{
			$.ajax({
				type : 'POST',
				url : "updateFile.html?fileName=" + fileName,
				data : {
					fileContent : fileContent
				},
				statusCode: {
				    404: function() {
				      alert( "File Not Found" );
				    },
					401: function() {
					      alert( "Unauthorized - Access Denied" );
			    	}
				},
				success : function(text) {
					if(text == "Not Signed In")
					{
						alert("Please sign into your dropbox account");
					}
					else if(text == "Updated Successfully")
					{
						alert("File Updated Successfully");
						$('#textarea').val("");
						$('#fileId').val("");
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					//alert("Status: " + textStatus);
					//alert("Error: " + errorThrown);
				}
			});
		}
		else
		{
			alert("Please open the file by typing the file name and click open");
		}
	}

	function saveAsFile() {
		var fileName;
		if(fileOpened == true)
		{
			fileName = prompt("Please enter file name","");
		}
		var fileContent = $('#textarea').val();
		fileContent = fileContent.replace(/\r?\n/g, '\n');
		if(fileOpened == false)
		{
			alert("Open the file to save it");
		}
		else if(fileName.length > 0)
		{
			$.ajax({
				type : 'POST',
				url : "updateFile.html?fileName=" + fileName,
				data : {
					fileContent : fileContent
				},
				statusCode: {
				    404: function() {
				      alert( "File Not Found" );
				    },
					401: function() {
					      alert( "Unauthorized - Access Denied" );
			    	}
				},
				success : function(text) {
					if(text == "Not Signed In")
					{
						alert("Please sign into your dropbox account");
					}
					else if(text == "Updated Successfully")
					{
						alert("File Updated Successfully");
						$('#textarea').val("");
						$('#fileId').val("");
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					//alert("Status: " + textStatus);
					//alert("Error: " + errorThrown);
				}
			});
		}
		else
		{
			alert("Please open the file by typing the file name and click open");
		}
	}

	function uploadFormData() {
		if ($('#add_file').val().length === 0) {
			alert("Need to choose a file to upload");
		} else if ($('#add_file_name').val().length > 0) {
			var fileName = $('#add_file_name').val();
			var oMyForm = new FormData();
			oMyForm.append("file", $('#add_file')[0].files[0]);

			$.ajax({
				type : 'POST',
				url : "add.html?fileName=" + fileName,
				data : oMyForm,
				dataType : 'text',
				statusCode: {
				    404: function() {
				      alert( "File Not Found" );
				    },
					401: function() {
					      alert( "Unauthorized - Access Denied" );
			    	}
				},
				processData : false,
				contentType : false,
				success : function(text) {
					if(text == "Not Signed In")
					{
						alert("Please sign into your dropbox account");
					}
					else if(text == "Uploaded Successfully")
					{
						alert("File Uploaded Successfully");
						$('#add_file').val("");
						$('#add_file_name').val("");
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					//alert("Status: " + textStatus);
					//alert("Error: " + errorThrown);
				}
			});
		} else {
			alert("Please give a name to the file");
		}
	}
	
