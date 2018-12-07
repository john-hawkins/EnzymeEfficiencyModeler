
$(function(){

	// Contact Form ANIMATION

	$('a.contact').toggle(function(){
		
		//remove the wrapper first if it exists
		$('#contact-wrap').remove();
		
		//add class to highlight link
		$(this).addClass("current");
		
		//create the wrapper
		$('#content').prepend('<div id="contact-wrap"></div>');
		
		//load contact form into the wrapper
		$.ajax({
		  url: "contact-form.jsp",
		  cache: false,
		  success: function(html){
		    $("#contact-wrap").append(html);
			//we need to validate the form
			//$('#contact').validate();
		  }
		});
		
		//slide the wrapper into view
		$('#contact-wrap').hide().slideDown('fast');
		
	}, function(){
		$(this).removeClass("current");
		$('#contact-wrap').slideUp();
	});
	
	// Search Form ANIMATION

	$('a.buildmodel').toggle(function(){
		
		//remove the wrapper first if it exists
		$('#buildmodel-wrap').remove();
		
		//add class to highlight link
		$(this).addClass("current");
		
		//create the wrapper
		$('#content').prepend('<div id="buildmodel-wrap"></div>');
		
		//load contact form into the wrapper
		$.ajax({
		  url: "buildmodel-form.jsp",
		  cache: false,
		  success: function(html){
		    $("#buildmodel-wrap").append(html);
			//we need to validate the form
			//$('#contact').validate();
		  }
		});
		
		//slide the wrapper into view
		$('#buildmodel-wrap').hide().slideDown('fast');
		
	}, function(){
		$(this).removeClass("current");
		$('#buildmodel-wrap').slideUp();
	});
	
	
	
	// View the list of models that have been created

	$('a.viewmodels').toggle(function(){
		
		//remove the wrapper first if it exists
		$('#viewmodels-wrap').remove();
		
		//add class to highlight link
		$(this).addClass("current");
		
		//create the wrapper
		$('#content').prepend('<div id="viewmodels-wrap"></div>');
		
		//load contact form into the wrapper
		$.ajax({ // url: "AjaxBuildModel?action=list",
		  url: "viewmodels.jsp",
		  cache: false,
		  success: function(html){
		    $("#viewmodels-wrap").append(html);
			//we need to validate the form
			//$('#contact').validate();
		  }
		});
		
		//slide the wrapper into view
		$('#viewmodels-wrap').hide().slideDown('fast');
		
	}, function(){
		$(this).removeClass("current");
		$('#viewmodels-wrap').slideUp();
	});
	
	
	// View the list of models that have been created
	$('a.testmodel').toggle(function(){
		
		//remove the wrapper first if it exists
		$('#testmodel-wrap').remove();
		
		//add class to highlight link
		$(this).addClass("current");
		
		//create the wrapper
		$('#content').prepend('<div id="testmodel-wrap"></div>');
		
		//load contact form into the wrapper
		$.ajax({ // url: "AjaxBuildModel?action=list",
		  url: "testmodel.jsp",
		  cache: false,
		  success: function(html){
		    $("#testmodel-wrap").append(html);
			//we need to validate the form
			//$('#contact').validate();
		  }
		});
		
		//slide the wrapper into view
		$('#testmodel-wrap').hide().slideDown('fast');
		
	}, function(){
		$(this).removeClass("current");
		$('#testmodel-wrap').slideUp();
	});
	
});

