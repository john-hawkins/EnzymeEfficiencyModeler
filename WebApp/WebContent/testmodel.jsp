
<script type="text/javascript">

	function refreshTestModelList()     { 
		var urlString = "AjaxTestModel?action=create-model-list&target=modelbox";
		ajaxRequest(urlString, "selectlist");
		setTimeout('refreshTestModelList()',10000);
	}
	refreshTestModelList();

</script>
<div id='selectlist'>

</div>
<br>

<div id='modelbox'>
Params for Model will go here
</div>

<div id='modelresults'>
</div>