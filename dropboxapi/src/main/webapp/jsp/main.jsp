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
<script type="text/javascript" src="https://www.dropbox.com/static/api/2/dropins.js" id="dropboxjs" data-app-key="bfss2zuuacudsen"></script>
<style type="text/css">
 .onmouseover{
 cursor: pointer;
 onmouseover:"";
 }
 .center {
    margin: auto;
    width: 60%;
    border:1.5px solid #000000;
    padding: 0px;
}
}
</style>
<script>

	var name = "";
	var fileOpened = false;
	var fileName = "";
	
	$(document).ready(function() {
		$("#btn_sign_out").hide();
		$("#link").hide();
	});

	$(function() {
		name = "${name}";
		if (name.length > 0) {
			document.getElementById('container').appendChild(button);
			$("#btn_sign_on").text("${name}");
			$("#btn_sign_on").attr("disabled", true);
			$("#btn_sign_out").show();
		}
	});
	var url = '';
    var button = Dropbox.createChooseButton({
        success: function(files) {
            var linkTag = document.getElementById('link');
            linkTag.href = files[0].link;
            linkTag.textContent = files[0].link;
            url = linkTag;
            getFileName(url);
        },
        linkType: 'direct'
    });
    $(function() {
		if (url.length > 0) {
			getFileName(url);
		}
	});
    
</script>
</head>
<body>
	<div style = "width:100%; height:40px;float:left; box-sizing:border-box">
		<div id="editonfly-home" align="center" style = "width:48%;height:40px; float:left">
			<h3 class="onmouseover" onclick ="location.href='main';">EditOnFly</h3>
		</div>
		<div id="editonfly-sign" align="center" style = "width:52%;float:right;height:40px">
			<div>
				<button class="onmouseover" type="button" id="btn_sign_on" onclick="signIn();">Dropbox Log In</button>
				<button class="onmouseover" type="button" id="btn_sign_out" onclick="signOut();">Log Out</button>
			</div>
		</div>
	</div>
	
	<br />

    <div align="center">
    
		<br/>
		<div id="container"></div>
   		<a id="link" style = "width:100%"></a>
		<h5>
			<input class="onmouseover" title="Persists the data present in the below textarea to your account" type="button" onclick="saveFile();" value="Save" />
			<input class="onmouseover" title="Persists the data present in the below textarea to your account" type="button" onclick="saveAsFile();" value="Save As" />
		</h5> 

		<textarea rows="20" cols="120" id="textarea">Text Area to update a file, if required.</textarea>
		
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