<form id='buildmodelForm' method="post" action="" NAME="buildmodelForm" >
<div class='hiddenFields'>
</div>


<div class="clear">
		<label for="sequences" class="long">Multiple Sequence Alignment</label>
		<label for="blank" class=""> </label>
		<label for="blank" class=""> </label>
		<label for="blank" class=""> </label>
		<label for="blank" class=""> </label>
		<label for="efficiencies" class="right">Efficiencies</label>
</div>

	
<div class="clear">
		<textarea name="sequences" cols="" rows="10" id="sequences" wrap="OFF"></textarea>
		<textarea name="efficiencies" cols="" rows="10" id="efficiencies" wrap="OFF"></textarea>
</div>

	
<div class="clear">
		<label for="blank" class=""> </label>
</div>

<div class="clear">
	<label for="modelname" class="">Model Name</label>
	<input type="text" name="modelname" size="30" id="buildmodel-modelname">
</div>

<div class="clear">	
	<label for="encoding" class="">Encoding</label>
	<select name='encoding' id="encoding">
		<option value='ORTHONORMAL'>Orthonormal</option>
		<option value='CHEMPROP'>Chemical Properties</option>
		<option value='BLOMAP'>BLOMAP</option>
	</select>
	<label for="positions" class="righttext">Positions</label>
	<input type="text" name="positions" size="30" id="buildmodel-positions">
</div>

<div class="clear">	
	<label for="model" class="">Model Type</label>
	<select name='model' id="model">
		<option value='Simple'>Simple (Weighted Average)</option>
		<option value='NN'>Nearest Neighbour(s)</option>
		<option value='LinRM'>Linear Regression Model</option>
		<option value='GausP'>Gaussian Process Model</option>
		<option value='SVM-1'>SVR Polynomial Kernel</option>
		<option value='SVM-0'>SVR Gaussian Kernel</option>
		<option value='SLP'>Single Layer Perceptron</option>
		<option value='MLP'>Multi Layer Perceptron</option>
	</select>
</div>

<div class="clear">	


  <fieldset>
    <legend>Model Parameters</legend>
	<label for="parameter1" class="">Degree/Nodes</label>	
	<select name='parameter1' id="buildmodel-parameter1">
		<option value='1'>1</option>
		<option value='2'>2</option>
		<option value='3'>3</option>
		<option value='4'>4</option>
		<option value='5'>5</option>
		<option value='6'>6</option>
		<option value='7'>7</option>
		<option value='8'>8</option>
		<option value='9'>9</option>
	</select>
	<label for="parameter2" class="righttext">Gamma/Eta</label>	
	<input type="text" name="parameter2" size="8" id="buildmodel-parameter2">
	<label for="parameter3" class="righttext">Rounds</label>	
	<input type="text" name="parameter3" size="8" id="buildmodel-parameter3">
  </fieldset>

</div>

<div class="clear">	
For SVMs the parameter controls the order of the polynomial kernel, for Neural Networks it control the number of hidden nodes, and for Nearest Neighbour it controls the number of Neighbours that are used.
</div>

<div class="clear">
	
	<input type="button" name="submit" value="Run" class="submit button" onclick="submitBuildModelForm();return false;">

</div>

<div class="clear">
</div>

</form> 