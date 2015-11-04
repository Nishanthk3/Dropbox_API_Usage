<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html lang="en">
<head>
<title>EditOnFly Dropbox Text Editor</title>
<meta charset="utf-8">
<link rel="shortcut icon" href="../images/editonfly_favicon.ico">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
<script type="text/javascript" src="../js/script.js"></script>
<style type="text/css">
 .onmouseover{
 cursor: pointer;
 onmouseover:"";
 }
 .center {
    margin: auto;
    width: 60%;
    border:1.5px solid #000000;
    padding: 10px;
}
}
</style>
<script>

	var name = null;
	var path = new Array();
	var isDir = new Array();
	var fileOpened = false;
	var filesMetadata = null;
	var dict = [];
	$(document).ready(function() {
		$("#btn_sign_out").hide();
/* 	    $('#dialog_link').click(function () {
	        $('#dialog').dialog('open');
	        return false;
	    }); */
	    
		/* $('#dialog').dialog(
				{ autoOpen: false}); */
	});

	$(function() {
		name = "${name}";
		if (name.length > 0) {
			$("#btn_sign_on").text("${name}");
			$("#btn_sign_on").attr("disabled", true);
			$("#btn_sign_out").show();
		}
	});

</script>
</head>
<body>
	<div align="center" style = "width:100%">
		<h2 class="onmouseover" onclick ="location.href='main';">Edit It!</h2>
			<button class="onmouseover" type="button" id="btn_sign_on" onclick="signIn();">Sign
				In To Dropbox Account</button>
			<button class="onmouseover" type="button" id="btn_sign_out" onclick="signOut();">Sign
				Out</button>
	</div>
	<br />
<%-- 	<div id="dialog" title="Basic dialog">
	  <p>This</p>
		<form:select path="country">
		    <form:options items="${directoryInfo}" />
		</form:select>
	</div>   --%>	
    <div class = "center" align="center" style = "width:60%">
		<!-- <input id="accountId" type="button" onclick="accountInfoCall();" value="AccountInfo" /> -->

		<form>
			<h5>File Name:<input id=fileId type="text">
			<input class="onmouseover" title="Opens the given filename i.e. present in your account" type="button" onclick="getFileCall();" value="Open" />
			<input class="onmouseover" title="Persists the data present in the below textarea to your account" type="button" onclick="saveFile();" value="Save" />
			<input class="onmouseover" title="Persists the data present in the below textarea to your account" type="button" onclick="saveAsFile();" value="Save As" />
			<!-- <input id="dialog_link" class="onmouseover" title="Opens the files and folders in your account" type="button" onclick="getAll();" value="OpenAllFiles" /> --></h5> 
		</form>
		<!-- <input class="onmouseover" title="Clears the below textarea" type="button" onclick="clearText();" value="Clear" /> -->
		<div>
			<textarea rows="10" cols="100" id="textarea">Text Area to update a file, if required.</textarea>
		</div>
		
		<form id="add_file_form" method="post" enctype="multipart/form-data">
			<!-- File input -->
			<h5>
			File to upload:<input class="onmouseover" name="file" id="add_file" type="file" >
			Name: <input type="text" id="add_file_name" name="add_file_name" />
			<input title="Uploads the file to your account" class="onmouseover" type="button" onclick="uploadFormData();" value="Upload" />
			</h5>
		</form>		
	
	<h6>Note:Tool-tips available when hovered on the button</h6>
	</div>
	
	 <div align="center" style = "width:100%">
	 <h5 class="onmouseover" onclick ="location.href='about';"><u>About</u></h5>
	 </div>
</body>
</html>