package org.enzymes;


/**
 * A class that contains all the results from a request, including the model
 * 
 * @author John Hawkins
 */
public class ResultsBean implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EnzymeEfficencyModeller modeller;
	
	public ResultsBean (){
		modeller = null;
	}
	
	/**
	 * Put the predicted sequence in here
	 * @param ps
	 */
	public synchronized void setModeller (EnzymeEfficencyModeller mod){
		modeller = mod;
	}
	
	
}

