
<script type="text/javascript">

	function refreshList()     { 
		var urlString = "AjaxBuildModel?action=list";
		//ajaxRequest(urlString, "modelist");
		ajaxRequestWithResize(urlString, "modelist", "viewmodels-wrap", 32, 38);
		setTimeout('refreshList()',1000);
	}
	refreshList();

</script>

<div id='modelist'>

</div>