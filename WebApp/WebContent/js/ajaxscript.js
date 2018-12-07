  /*
   * A generic function to return the XML HTTP object
   */
  function CreateXmlHttpObject() { 
	var xmlhttp=false;	
	try {
		xmlhttp=new XMLHttpRequest();//creates a new ajax object
	} catch(e) {		
		try {			
			xmlhttp= new ActiveXObject("Microsoft.XMLHTTP");//this is for IE browser
		} catch(e2) {
			try {
				req = new ActiveXObject("Msxml2.XMLHTTP");//this is for IE browser
			} catch(e3) {
				xmlhttp=false;//error creating object
			}
		}
	}
		 	
	return xmlhttp;
  }
	

  
  /*
   * Make an AJAX call to the specified URL and place the results in the 'Results' div
   */
  function GetFormResults(strURL)     {         
		var req = CreateXmlHttpObject(); // function to get xmlhttp object
	     	if (req) {
			req.onreadystatechange = function() {
	      			if (req.readyState == 4) { //data is retrieved from server
	       				if (req.status == 200) { // which represents OK status 
	         				document.getElementById('results').innerHTML=req.responseText;//put the results of the requests in or element
	      				} else { 
	         				alert("There was a problem while using XMLHTTP:\n");
	      				}
	      			}            
	      		}        
			document.getElementById('results').innerHTML="Running...";//Clear out previous results
	    	req.open("GET", strURL, true); //open url using get method
	    	req.send(null);//send the results
		}
  }
  
  // Make an AJAX call to the specified URL and place the results in the 'Message' div
  function GetFormMessage(strURL)     {         
		var req = CreateXmlHttpObject(); // function to get xmlhttp object
	     	if (req) {
			req.onreadystatechange = function() {
	      			if (req.readyState == 4) { //data is retrieved from server
	       				if (req.status == 200) { // which represents ok status                    
	         				document.getElementById('messages').innerHTML=req.responseText;//put the results of the requests in or element
	      				} else { 
	         				alert("There was a problem while using XMLHTTP:\n");
	      				}
	      			}            
	      		}        
	    		req.open("GET", strURL, true); //open url using get method
	    		req.send(null);//send the results
		}
  }
  
  // Make an AJAX call to the specified URL and place the results in the 'targetElement' div
  function ajaxRequest(strURL, targetElement)     {         
		var req = CreateXmlHttpObject(); // function to get xmlhttp object
	     	if (req) {
			req.onreadystatechange = function() {
	      			if (req.readyState == 4) { //data is retrieved from server
	       				if (req.status == 200) { // which represents ok status     
	       					//alert("RESULTS: " + req.responseText );
	         				document.getElementById(targetElement).innerHTML=req.responseText;
	      				} else { 
	         				alert("There was a problem while using XMLHTTP:\n");
	      				}
	      			}            
	      		}        
	    		req.open("GET", strURL, true); //open url using get method
	    		req.send(null);//send the results
		}
  }
  
  // Make an AJAX call to the specified URL and place the results in the 'targetElement' div
  function ajaxRequestWithResize(strURL, targetElement, resizeElement, baseSize, perElemSize)     {         
		var req = CreateXmlHttpObject(); // function to get xmlhttp object
	     	if (req) {
			req.onreadystatechange = function() {
	      			if (req.readyState == 4) { //data is retrieved from server
	       				if (req.status == 200) { // which represents ok status  
	       					var pattern = /<VAR>/;
	       					var patternEnd = /<\/VAR>/;
	       					var startLocation = req.responseText.search(pattern);
	       					var endLocation = req.responseText.search(patternEnd);
	       					//alert(req.responseText.substring(startLocation+5,endLocation));
	       					var numResults = req.responseText.substring(startLocation+5,endLocation);
	       					var newHeight = baseSize + (perElemSize * numResults);
	       					document.getElementById(resizeElement).style.height = newHeight +"px";
	         				document.getElementById(targetElement).innerHTML=req.responseText;
	      				} else { 
	         				alert("There was a problem while using XMLHTTP:\n");
	      				}
	      			}            
	      		} ;      
	    	req.open("GET", strURL, true); //open url using get method
	    	req.send(null);//send the results
		}
  }
  
  // Make an AJAX call to the specified URL with POST
  // and place the results in the 'targetElement' div
  function ajaxPOSTRequest(strURL, parameters, targetElement)     {         
		var req = CreateXmlHttpObject(); // function to get xmlhttp object
	     	if (req) {
			req.onreadystatechange = function() {
	      			if (req.readyState == 4) { //data is retrieved from server
	       				if (req.status == 200) { // which represents ok status     
	         				document.getElementById(targetElement).innerHTML=req.responseText;
	      				} else { 
	         				alert("There was a problem while using XMLHTTP:\n");
	      				}
	      			}            
	      		};        
	    	req.open('POST', strURL, true);
	    	req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	    	req.setRequestHeader("Content-length", parameters.length);
	    	req.setRequestHeader("Connection", "close");
	    	req.send(parameters);
		}
  }
  
  function submitModelTest() {
	  var urlString = "AjaxTestModel";
	  var numPositions = document.getElementById("numPositions").value;
	  var modelID = document.getElementById("modelID").value;
	  var useEnsemble = document.getElementById("useEnsemble").value;
	  var poststr = "action=test&numpos="+numPositions+ "&id="+modelID+ "&useEnsemble="+useEnsemble;
	  for(var p=0; p<numPositions; p++) {
		  var theName = "pos" + p;
		  var theVal = document.getElementById(theName).value;
		  poststr = poststr + "&" + theName + "=" + theVal;
	  }

	  ajaxPOSTRequest( urlString, poststr, "modelresults" );
	  return false;
  }
  
  function submitTestSeq() {
	  var urlString = "AjaxTestModel";
	  var numPositions = document.getElementById("numPositions").value;
	  var modelID = document.getElementById("modelID").value;
	  var useEnsemble = document.getElementById("useEnsemble").value;
	  var testSeq = document.getElementById("testSeq").value;
	  var poststr = "action=test-seq&numpos="+numPositions+ "&id="+modelID+ "&useEnsemble="+useEnsemble+ "&testSeq="+testSeq;

	  ajaxPOSTRequest( urlString, poststr, "modelresults" );
	  return false;
  }
  
  function submitMasterForm() {
	  //var urlString = "AjaxBuildModel?action=" +document.masterForm.action.value;
	  //urlString = urlString + "&model=" + document.masterForm.model.value;
	  //urlString = urlString + "&id=" + document.masterForm.id.value;
	  //urlString = urlString + "&encoding=" + document.masterForm.encoding.value;
	  //urlString = urlString + "&modelname=" + document.masterForm.modelname.value;
	  //urlString = urlString + "&sequences=" + document.masterForm.sequences.value;
	  //urlString = urlString + "&positions=" + document.masterForm.positions.value;
	  //urlString = urlString + "&parameter1=" + document.masterForm.parameter1.value;
	  //urlString = urlString + "&parameter2=" + document.masterForm.parameter2.value;
	  //urlString = urlString + "&parameter3=" + document.masterForm.parameter3.value;
	  //urlString = urlString + "&efficiencies=" + document.masterForm.efficiencies.value;
	  //GetFormResults( urlString );
	  //alert(urlString);
	  //return false;
	  
	  var urlString = "AjaxBuildModel";
	  var poststr = "action=" +document.masterForm.action.value;
	  poststr = poststr + "&model=" + document.masterForm.model.value;
	  poststr = poststr + "&id=" + document.masterForm.id.value;
	  poststr = poststr + "&encoding=" + document.masterForm.encoding.value;
	  poststr = poststr + "&modelname=" + document.masterForm.modelname.value;
	  poststr = poststr + "&sequences=" + document.masterForm.sequences.value;
	  poststr = poststr + "&positions=" + document.masterForm.positions.value;
	  poststr = poststr + "&parameter1=" + document.masterForm.parameter1.value;
	  poststr = poststr + "&parameter2=" + document.masterForm.parameter2.value;
	  poststr = poststr + "&parameter3=" + document.masterForm.parameter3.value;
	  poststr = poststr + "&efficiencies=" + document.masterForm.efficiencies.value;
	  ajaxPOSTRequest( urlString, poststr, "messages" );
	  return false;
  }
  
  function loadResults(requestID) {
		  var urlString = "AjaxBuildModel?action=view&id=" +requestID;
		  GetFormResults( urlString );
		  return false;
  }
  
  function submitBuildModelForm() {
	  document.masterForm.action.value = "build";
	  document.masterForm.model.value = document.buildmodelForm.model.value;
	  document.masterForm.encoding.value = document.buildmodelForm.encoding.value;
	  document.masterForm.modelname.value = document.buildmodelForm.modelname.value;
	  document.masterForm.sequences.value = document.buildmodelForm.sequences.value.replace(/\n/g, "~");
	  document.masterForm.positions.value = document.buildmodelForm.positions.value;
	  document.masterForm.parameter1.value = document.buildmodelForm.parameter1.value;
	  document.masterForm.parameter2.value = document.buildmodelForm.parameter2.value;
	  document.masterForm.parameter3.value = document.buildmodelForm.parameter3.value;
	  document.masterForm.efficiencies.value = document.buildmodelForm.efficiencies.value.replace(/\n/g, "~");
	  return submitMasterForm();
  }
  
  
  function submitViewForm() {
	  document.masterForm.id.value = document.viewForm.id.value;
	  return submitMasterForm();
  }
  
  