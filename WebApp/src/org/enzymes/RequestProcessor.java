package org.enzymes;

import java.util.*;
import javax.servlet.ServletContext;

public class RequestProcessor implements Runnable {

	// The Request that is to be processed
	protected Request request;
	
	/**
	 * Contructor method.
	 */
	public RequestProcessor (Request request){
		this.request = request;
	}
	
	/**
	 * A method that is called at server initialisation, so common actions
	 * such as loading model data only occur once. This should be overridden
	 * by subclasses (Java won't allow static abstract classes).
	 *
	 */
	public static void init (ServletContext servletContext){
	    System.out.println ("Loading model data...");
	    String projectPath = servletContext.getRealPath("projects") + System.getProperty("file.separator");
		//theModel = new Model( new String[] {projectPath + modelName} );
		System.out.println ("Finished Loading model data...");
		//System.out.println (theModel.toString());
	}
	
	
	/**
	 * This method that executes all processing required to generate
	 * the model and the results. 
	 * It will be started by the main servlet, and will run concurrently.
	 *
	 */
	public void run () {
		request.xvalidate();
		setFinished ();
	}
	
	/**
	 * Signal the completion of all required processing. This may need to be
	 * modified if multiple processes are used.
	 * 
	 * This must be called AFTER the results bean has been set
	 *
	 */
	protected void setFinished (){
	    request.setResultsAvailable();
	}

}
