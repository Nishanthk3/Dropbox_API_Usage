/**
 *  JavaScript Functions 
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
				$("#btn_sign_on").text("Dropbox Log In");
				$('#textarea').val("");
				$('#fileId').val("");
				$('#add_file_name').val("");
				$('#add_file').val("");
				name = "";
				var element = document.getElementById("container");
				element.parentNode.removeChild(element);
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
				openUrl(data);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				//alert("Status: " + textStatus);
				//alert("Error: " + errorThrown);
			}
		});
	} 
 	function openUrl(data)
 	{
 		location.href = data;
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
				url : "file.html?fileName=" + fileName,
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
	function getAll() {
		$.ajax({
			type : 'GET',
			url : "getFilesAndFolders.html",
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
					//var data = JSON.stringify(text);
					//filesMetadata = JSON.parse(data);
					$('#textarea').val(JSON.stringify(text));					
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				//alert("Status: " + textStatus);
				//alert("Error: " + errorThrown);
			} 
		});
	}

	function saveFile() {
		//var fileName = $('#fileId').val();
		if(name.length > 0)
		{
			if(fileOpened == false)
			{
				alert("Choose a file from dropbox to save it");
			}
			else if(fileName.toString().indexOf(".txt") > -1)
			{
				save();
			}
			else
			{
				if (confirm('Are you sure you want to save this? Save recommended only if it is .txt or if the file has proper text data')) {
					save();
				}
				else {
					fileName = null;
					$('#textarea').val("");
				}
			}
		}
		else{
			alert("Sign into dropbox");
		}
	}

	function save()
	{
		var fileContent = $('#textarea').val();
		fileContent = fileContent.replace(/\r?\n/g, '\n');
		if(fileName.length > 0)
		{
			$.ajax({
				type : 'POST',
				url : "updateFile.html?fileName=" + encodeURIComponent(fileName),
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
						alert("File : "+ fileName +", Updated Successfully");
						fileName = null;
						$('#textarea').val("");
						fileOpened = false;
						//$('#fileId').val("");
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					fileName = null;
					//alert("Status: " + textStatus);
					//alert("Error: " + errorThrown);
				}
			});
		}
	}
	function saveAsFile() {
		if(name.length > 0)
		{
			var fileName = null;
			if(fileOpened == true)
			{
				fileName = prompt("Enter file name","");
			}
			else
			{
				alert("Choose a file from dropbox to save it");
			}
			var fileContent = $('#textarea').val();
			fileContent = fileContent.replace(/\r?\n/g, '\n');
			if(fileName.length > 0)
			{
				$.ajax({
					type : 'POST',
					url : "updateFile.html?fileName=" + encodeURIComponent(fileName),
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
							fileOpened = false;
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
				alert("Please choose a file from dropbox to save it");
			}
		}
		else{
			alert("Sign into dropbox");
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
	
	function getFileName(url) {
		var res = url.toString().split("/");
		var fileNameSplitFromUrl = "";
		for(var i = 0;i<res.length ;i++)
		{
			if(i > 5)
			{
				fileNameSplitFromUrl += res[i]+"/";
			}
		}
		fileName = fileNameSplitFromUrl;
		getFile(fileNameSplitFromUrl);
	}

	function getFile(fileNameSplitFromUrl) {
		if(fileName.length > 0)
		{
			$.ajax({
				type : 'GET',
				url : "file.html?fileName=" + fileName,
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