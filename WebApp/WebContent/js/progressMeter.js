
var intervalID; // interval ID
var progressUpdateURL; // This will be set before each request

// Initiate and stop the AJAX polling for updating the progress
function startProgressMeter(strURL) {
	alert("HERE");
	progressUpdateURL = strURL;
	intervalID = window.setInterval('updateProgressMeter()',1500); 
}
function stopProgressMeter()  { window.clearInterval(intervalID); }


// Make an AJAX call to the server and update the progress meter
function updateProgressMeter()     {    
	alert("AND HERE");     
		var req = CreateXmlHttpObject(); // function to get xmlhttp object
	     	if (req) {
			req.onreadystatechange = function() {
	      			if (req.readyState == 4) { //data is retrieved from server
	       				if (req.status == 200) { // which represents ok status 
	       					// get progress from the XML node and set progress bar width and innerHTML
	       					var progress = document.getElementById('progressBar');
	       					var level=request.responseXML.getElementsByTagName('PROGRESS')[0].firstChild;
	       					progress.style.width = progress.innerHTML = level.nodeValue + '%';
	       					if(level==100) {
	       						stopProgressMeter();
	       					}
	      				} else { 
	         				alert("There was a problem while using XMLHTTP:\n");
	      				}
	      			}            
	      		}        
	    	req.open("GET", progressUpdateURL, true); //open url using get method
	    	req.send(null);//send the results
		}
}