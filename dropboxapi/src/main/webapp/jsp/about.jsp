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
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
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
<script type="text/javascript">
$(document).ready(function() {
	$("#btn_sign_out").hide();
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
	
	<br/>
	<br/>
	<br/>
	<div class="center" style="width: 60%">
		<h4>EditOnFly ?</h4>
		<h5>Dropbox does not allow you to edit text files in the dropbox application itself. Whenever you want to edit your text file that is present in
		your dropbox account, you'll have to download it, edit it and then upload it back to your account.
		This web application EditOnFly will allow you to connect to your account and will provide you the functionality to edit text files and upload
		them back from the application itself.</h5>
	<br />
		<h4>Contact</h4>
		<h5>I'm Nishanth Reddy Kommidi, developer of EditOnFly application, and can be contacted through email. Id: nishanth.k3@gmail.com</h5>
	</div>
	<br />
</body>
</html>