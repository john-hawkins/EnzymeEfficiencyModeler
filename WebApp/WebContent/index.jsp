<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Enzyme Efficiency Modeler</title>

	<link rel="stylesheet" type="text/css" href="style.css"> 

	<link rel="shortcut icon" href="favicon.ico">
	<link rel="icon" type="image/ico" href="favicon.ico">

	<script src="js/jquery-1.3.1.min.js" type="text/javascript"></script>
	<script src="js/forms.js" type="text/javascript"></script>
	<script src="js/ajaxscript.js" type="text/javascript"></script>

</head>
<body>

<form id='masterForm' method="post" action="" NAME="masterForm">
<div class='hiddenFields'>

	<input type="hidden" name="action" value="" />
	<input type="hidden" name="id" value="" />

	<input type="hidden" name="modelname" value="" />
	<input type="hidden" name="model" value="" />
	<input type="hidden" name="encoding" value="" />
	
	<input type="hidden" name="sequences" value="" />
	<input type="hidden" name="positions" value="" />
	<input type="hidden" name="parameter1" value="" />
	<input type="hidden" name="parameter2" value="" />
	<input type="hidden" name="parameter3" value="" />
	<input type="hidden" name="efficiencies" value="" />
</div>
</form> 

<div id="wrapper">

<header id="header"> <!-- HTML5 header tag -->

	<div id="header">
			<h1><img src="images/Logo.png"></img> <a href="">Enzyme Efficiency Modeler</a></h1>
	</div>
</header>

<nav class="main floatL colSpan2"> <!-- HTML5 navigation tag -->


	<div id="navigation"> 

		<ul>
			<li><a href="" class="current">Home</a></li>
			<li><a href="#" class="buildmodel">Build Model</a></li>
			<li><a href="#" class="viewmodels">View Results</a></li>
			<li><a href="#" class="testmodel">Apply Model</a></li>
			<li><a href="">Datasets</a></li>
			<li><a href="">References</a></li>
			<li><a href="#" class="help" id="helpButton">Help</a></li>
		</ul>
		
	</div>	


</nav>


<section id="messagecontainer"> <!-- HTML5 section tag for the content 'section' -->

	<div id="messages">
	</div>	

</section>

<section id="contentcontainer"> <!-- HTML5 section tag for the content 'section' -->

<div id="content">

	<div id="results">
		
		<h2>Enzyme Efficiency Modeler</h2>
		
		<p>Use this application to build and test a model of enzyme efficiency.</p>
		
		<p></p>
	

	
	</div>		
	
</div>

</section>

<footer> <!-- HTML5 footer tag -->
	<div id="footer"> (c) 2010 John.C.Hawkins </div>
</footer>

</div>

</body>
</html>