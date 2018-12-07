package org.enzymes;

import java.util.*;

import sim.Dataset;
import sim.DatasetBuffer;
import sim.MLModel;
import sim.MLModelEval;
import sim.MLModelXValid;
import sim.ProgressMeter;
import structools.mlmodels.*;
import structools.structure.InterfaceEncoding;
import structools.structure.ResidueBLOMAPEncoding;
import structools.structure.ResidueEncoding;
import structools.structure.ResiduePropertyEncoding;

/**
 * An object to store all information related to the Request
 * being made by the user.
 * 
 * @author John Hawkins
 */

public class Request {
    // Length of time (in milliseconds) that a user's request and its results are stored in
    //    the system after it is last accessed : default is set to three days
    public static final long requestLifespan = 1000 * 60 * 60 * 24 * 3;
    
    // Used to find request objects given request IDs
    private static Hashtable<Integer, Request> requestTable = new Hashtable<Integer, Request>();    
    
    // Unique identifier of this request
    private final int id;
    // Time that this request was last accessed (from System.currentTimeMillis())
    private long timestamp;

    // Track the progress of the request
    ProgressMeter pm = new ProgressMeter();

	// the default number of seconds to wait between refreshes on the 'processing' page
    // this can be overwritten with setRefreshTime
    private int refreshTime = 10;
    // whether or not the results are ready
    private boolean resultsAvailable; 
    // the name of the client that sent the request
    private String userIdentifier; 
	// any error that may have occured during processing. null means all good.
	private String error;

    private boolean useEnsembleofXValidationModels = true; 
	
	/*
	 * ALL THE PARAMETERS OF THE MODEL
	 */
	private String modelname;

	private String modelParams;
	private String refseq;

	private String[] names;
	private String[] seqs;

	private String[] effics; 
	private String criticalPositions;
	private int[][] orgPossies;
	private int[][] alnPossies;
	private int modelParameter;
	private double modelParameter2;
	private double modelParameter3;
	private String modelType;

	private String encoding;

	/*
     * Parameters for the SVM model
     */
    private double epsilon = 0.000000000001;
    private double tolerance = 0.038;
    private double complexity = 0.05;
    private double gamma = 0.01;
    private int seed = 0;
	/*
     * Parameters for the SLP AND MLP model
     */
    private double eta = 0.01;
    private int rounds = 10000;
    
    
    MLModelEval totals;
    
    ProteinEnsembleModel model;

    MLModel finalModel;
    
	/*
	 * Create a new Request Object
	 * 
	 * With all the parameters of the model and the data set for training
	 */
	
	public Request(String identifier, String name, String[] na, String[] ss, String[] efcs, int[][] possies, String model, String enc, int modelParam, double modelParam2, double modelParam3) {
		modelname = name;
		names = na;
		seqs = ss;
		refseq = seqs[0];
		effics = efcs;
		orgPossies = possies;
		modelType = model;
		refseq = seqs[0];
		modelParameter = modelParam;
		modelParameter2 = modelParam2;
		modelParameter3 = modelParam3;

		encoding = enc;
        this.userIdentifier = identifier;
        
        // Generates new 7 digit random numbers until an unused one is found
        Random r = new Random(System.currentTimeMillis());
        int newID;
        do {
            newID = r.nextInt(9000000) + 1000000;
        } while (requestTable.contains(new Integer(newID)));
        requestTable.put(new Integer(newID), this);
        id = newID;
        resultsAvailable = false;
        this.touch();
	}
	
	/*
	 * Create a new Request Object
	 * 
	 * With all the parameters of the model and the data set for training
	 */
	
	public Request(String identifier, String name, String[] na, String[] ss, String[] efcs, String posList, String model, String enc, int modelParam, double modelParam2, double modelParam3) {
		modelname = name;
		names = na;
		seqs = ss;
		refseq = seqs[0];
		effics = efcs;
		criticalPositions = posList;
		String[] possies = criticalPositions.split("-");
		orgPossies = new int[possies.length][];
        for(int i =0; i<possies.length; i++) {
        	String[] temp = possies[i].split(",");
        	orgPossies[i] = new int[temp.length];
        	for(int t=0; t<temp.length; t++) {
        		orgPossies[i][t] = Integer.parseInt(temp[t]);
        	}
        }
		
		modelType = model;
		modelParameter = modelParam;
		modelParameter2 = modelParam2;
		modelParameter3 = modelParam3;

		encoding = enc;
        this.userIdentifier = identifier;
        
        // Generates new 7 digit random numbers until an unused one is found
        Random r = new Random(System.currentTimeMillis());
        int newID;
        do {
            newID = r.nextInt(9000000) + 1000000;
        } while (requestTable.contains(new Integer(newID)));
        requestTable.put(new Integer(newID), this);
        id = newID;
        resultsAvailable = false;
        this.touch();
	}
	
	/*
	 * X-Validate using the data of this request
	 * 
	 */
	public void xvalidate() {
		
		//this.percentageComplete = 0.0;
		
		int numPositions = orgPossies.length;
		ProteinDataset dataset;
        boolean mergeEncodings = true;
        ProteinSampleEncoder sampleEncoder;

        InterfaceEncoding[] encoders = new InterfaceEncoding[numPositions];
        
        /*
        System.err.print("Converting these positions: ");
        for(int i=0; i<orgPossies.length; i++) {
        	System.err.print("{ ");
        	for(int j=0; j<orgPossies[i].length; j++) {
        		System.err.print(orgPossies[i][j] + " ");
        	}
        	System.err.print("} ");
        }
    	System.err.print("\n");
        System.err.println("Against this sequence: ");
        System.err.println(seqs[0]);
        */
        
        alnPossies = ProteinDataset.convertBindingPositionsToAlignment(orgPossies, seqs[0]);
        dataset = new ProteinDataset( names, seqs, orgPossies, effics);

		if(encoding.equals("ORTHONORMAL") ) {
			for(int l=0; l<numPositions; l++) {
				encoders[l] = new InterfaceEncoding(new ResidueEncoding(), mergeEncodings);
			}
			
		} else if(encoding.equals("CHEMPROP") ) {
			for(int l=0; l<numPositions; l++) {
				encoders[l] = new InterfaceEncoding(new ResiduePropertyEncoding(), mergeEncodings);
			}
		} else {
			for(int l=0; l<numPositions; l++) {
				encoders[l] = new InterfaceEncoding(new ResidueBLOMAPEncoding(), mergeEncodings);
			}
		}
		sampleEncoder = new ProteinSampleEncoder(encoders, alnPossies, orgPossies, numPositions, seqs);
		
		DatasetBuffer[] dbufs = dataset.getDatasetBufferArray(numPositions);
		
		MLModel[] classmodels = new ProteinRegModel [dbufs.length];


		for (int m = 0; m < classmodels.length; m++) {

			if(modelType.length() > 4 && modelType.substring(0,5).equals("SVM-0")) {
				if(modelParameter2 > 0)
					gamma = modelParameter2;
				if(modelParameter3 > 0)
					complexity = modelParameter3;
				classmodels[m] = new ProteinSVMRegModel( sampleEncoder, 0 , gamma, complexity);
				modelParams = "G["+gamma+"] C["+complexity+"]";
			} else if(modelType.length() > 2 && modelType.substring(0,3).equals("SVM")) {
				if(modelParameter2 > 0)
					gamma = modelParameter2;
				if(modelParameter3 > 0)
					complexity = modelParameter3;
				classmodels[m] = new ProteinSVMRegModel( sampleEncoder, modelParameter, gamma, complexity );
				modelParams = "P["+modelParameter+"] C["+complexity+"]";
			} else if(modelType.length() > 1 && modelType.substring(0,2).equals("NN")) {
				classmodels[m] = new ProteinEfficiencySimpleModel( sampleEncoder, modelParameter );
				modelParams = "N["+modelParameter+"]";
			} else if(modelType.length() > 2 && modelType.substring(0,3).equals("SLP")) {

				if(modelParameter2 > 0)
					eta = modelParameter2;
				if(modelParameter3 > 0)
					rounds = (int) modelParameter3;
				classmodels[m] = new ProteinEfficiencyMLPModel( sampleEncoder, 0, eta, rounds, 0 );

				modelParams = "e["+modelParameter2+"] R["+modelParameter3+"]";
			} else if(modelType.length() > 2 && modelType.substring(0,3).equals("MLP")) {
				if(modelParameter2 > 0)
					eta = modelParameter2;
				if(modelParameter3 > 0)
					rounds = (int) modelParameter3;
				classmodels[m] = new ProteinEfficiencyMLPModel( sampleEncoder, modelParameter, eta, rounds, 0 );

				modelParams = "e["+modelParameter2+"] R["+modelParameter3+"]";
			} else if(modelType.length() > 4 && modelType.substring(0,5).equals("LinRM")) {

				classmodels[m] = new ProteinLinearRegModel( sampleEncoder );
				
			} else if(modelType.length() > 4 && modelType.substring(0,5).equals("GausP")) {

				classmodels[m] = new ProteinGaussianProcessModel( sampleEncoder );
				
			} else {
				classmodels[m] = new ProteinEfficiencySimpleModel( sampleEncoder );
			}
		}

		MLModelXValid crossValidation = new MLModelXValid (classmodels, dbufs, seed, pm);
		crossValidation.train();

		MLModelEval[] evals = (MLModelEval[]) crossValidation.test();
		totals= MLModelEval.merge(evals);

		model = new ProteinEnsembleModel(classmodels);

		if(modelType.length() > 4 && modelType.substring(0,5).equals("SVM-0")) {
			finalModel = new ProteinSVMRegModel( sampleEncoder, 0, gamma, complexity );
		} else if(modelType.length() > 2 && modelType.substring(0,3).equals("SVM")) {
			finalModel = new ProteinSVMRegModel( sampleEncoder, modelParameter, gamma, complexity  );
		} else if(modelType.length() > 1 && modelType.substring(0,2).equals("NN")) {
			finalModel = new ProteinEfficiencySimpleModel( sampleEncoder, modelParameter );
		} else if(modelType.length() > 2 && modelType.substring(0,3).equals("SLP")) {
			finalModel = new ProteinEfficiencyMLPModel( sampleEncoder, 0, eta, rounds, 0 );
		} else if(modelType.length() > 2 && modelType.substring(0,3).equals("MLP")) {
			finalModel = new ProteinEfficiencyMLPModel( sampleEncoder, modelParameter, eta, rounds, 0 );
		} else if(modelType.length() > 4 && modelType.substring(0,3).equals("LinRM")) {
			finalModel = new ProteinLinearRegModel( sampleEncoder );
		} else if(modelType.length() > 4 && modelType.substring(0,5).equals("GausP")) {
			finalModel = new ProteinGaussianProcessModel( sampleEncoder );
		} else {
			finalModel = new ProteinEfficiencySimpleModel( sampleEncoder );
		}
		
		
		finalModel.train(new Dataset(dbufs), null);
	
		setResultsAvailable();
		//this.percentageComplete = 100.0;
	}
	
	/*
	 * Test the given sequence
	 */
	public double test(String seq) {
		return test( seq, useEnsembleofXValidationModels);
	}
	
	/*
	 * Test the given sequence
	 */
	public double test(String seq, boolean useEnsemble) {
		ProteinSample tester = new ProteinSample("TEST", null, seq, null );
		//System.err.println("GENERATED SAMPLE =" + tester.getPdbID() + " : " + tester.getSequence() );
		double result;
		if(useEnsemble)
			result = model.test(tester)[0];
		else
			result = finalModel.test(tester)[0];

		return result;
	}
	
	/*
	 * Test the specified modifications to the ref seq

	public double test(char[] mods) {
		String testSeq = new String(refseq);
		ProteinSample tester = new ProteinSample("TEST", null, seq, "UNKNOWN" );
		double result = model.test(tester)[0];
		return result;
	}
		 */
	
    public synchronized static boolean isValidID(int id){
        return requestTable.contains(new Integer(id));
    }
    
    public synchronized static Request getRequest (int id){
        return (Request)requestTable.get(new Integer(id));
    }
    
    public synchronized static Vector<Request> getUserRequests (String identifier) {
    	Vector<Request> results = new Vector<Request>();
    	Enumeration<Request> enumer = requestTable.elements();
        while (enumer.hasMoreElements()){
            Request request = (Request) enumer.nextElement();
            if (request.getUserIdentifier().equals(identifier)) {
            	results.add(request);
            }
        }
        return results;
    }
    
    
    /**
     * Checks each request in the system for expired requests
     * and deletes them. This should be called regularly,
     * but there is no need to call it very frequently.
     *
     */
    public synchronized static void deleteExpired () {
        Enumeration<Request> enumer = requestTable.elements();
        while (enumer.hasMoreElements()){
            Request request = (Request) enumer.nextElement();
            if (System.currentTimeMillis() - request.timestamp> requestLifespan) {
                request.abandon();
            }
        }
    }
    
    public synchronized static int getNumberActive (){
        return requestTable.size();
    }
    
    public synchronized static Enumeration<Request> getTableEnum (){
        return requestTable.elements();
    }
    
    public int getID(){
        return id;
    }
    
    public String getUserIdentifier (){
        return userIdentifier;
    }
    
	public String getModelname() {
		return modelname;
	}
	
	public String getModelType() {
		return modelType;
	}
    public String getEncoding() {
		return encoding;
	}
    public String getCriticalPositions() {
		return criticalPositions;
	}
    
	public int[][] getOriginalSeqPositions() {
		return orgPossies;
	}

	public int[][] getAlnSeqPosistions() {
		return alnPossies;
	}
	
	public String[] getSeqs() {
		return seqs;
	}
	
	public String getRefseq() {
		return refseq;
	}

    public double getPercentageComplete() {
		return pm.getPercentageProcessed();
	}
   
    public void setRefreshTime (int refreshTime){
        this.refreshTime = refreshTime;
    }
    
    public int getRefreshTime (){
        return refreshTime;
    }
    
    public long getLastAccessTime (){
        return timestamp;
    }
    
    /**
     * Signals the availability of the results. Because a request is designed
     * to be used by a signal request (ie no re-use), it wouldn't make sense
     * to be able to change this to false from true.
     *
     */
    public void setResultsAvailable (){
        resultsAvailable = true;
    }
    
    /**
     * Whether or not processing is complete and results are available.
     * Ok, so "is results available" doesn't make sense, but "are" is the
     * same verb in a different form - I think adhering to naming conventions
     * is more important in this context than grammatical accuracy. (While
     * we're on the topic of grammar, the first sentence above is not a complete
     * sentence)
     */
    public boolean isResultsAvailable (){
        return error == null && resultsAvailable;
    }
    
    /**
     * Notify that the request has been accessed, so that it doesn't
     * time out.
     *
     */
    public void touch(){
        timestamp = System.currentTimeMillis();
    }
    
    /**
     * Removes the request from the system
     *
     */
    public void abandon() {
        requestTable.remove(new Integer(id));
    }
    
	/**
	 * Set the error message. This doesn't stop the other methods
	 * from being accessed, but relies on accessors of this class checking
	 * that getError isn't null before accessing it...
	 * Also, erroneous requests are NOT deleted any sooner (though this
	 * could easily be changed in deleteExpired). This is so that users
	 * who bookmark the page and check back later will still be able to
	 * view the error message.
	 * @param error the error
	 */
	public synchronized void setError (String error){
	    this.error = error;
	}
	
	public synchronized String getError (){
	    return error;
	}
	
	
	public double calculateCC() {
		
		Vector outputs = totals.getOutputs();
		
		int num = outputs.size();
		double[] targets = new double[num];
		double[] preds = new double[num];

		double sumOfTargs = 0.0;
		double sumOfTargsSQRD = 0.0;
		double sumOfPreds = 0.0;
		double sumOfPredsSQRD = 0.0;
		double sumOfTargsTimesPreds = 0.0;
		
		for(int i=0; i<num; i++) {
			Object[] contents = (Object[]) outputs.get(i);
			preds[i] = ((double[]) contents[0])[0];
			ProteinSample test = (ProteinSample) contents[1];
			targets[i] = Double.parseDouble( (String) test.getKey() );
			
			sumOfTargs += targets[i];
			sumOfPreds += preds[i];
			sumOfTargsSQRD += Math.pow(targets[i], 2);
			sumOfPredsSQRD += Math.pow(preds[i], 2);
			sumOfTargsTimesPreds += ( targets[i] * preds[i] );
		}

		double ccNumerator = num * sumOfTargsTimesPreds - (sumOfTargs * sumOfPreds);
		double ccDenominator1 = Math.sqrt( (num * sumOfTargsSQRD ) - (sumOfTargs*sumOfTargs) );
		double ccDenominator2 = Math.sqrt( (num * sumOfPredsSQRD ) - (sumOfPreds*sumOfPreds) );
		
		double ccDenominator = ccDenominator1 * ccDenominator2;

		double cc = ccNumerator / ccDenominator;

		return cc;
	}

	public double calculateMSE() {
		
		Vector outputs = totals.getOutputs();
		
		int num = outputs.size();
		double[] targets = new double[num];
		double[] preds = new double[num];

		double mse = 0.0;
		
		for(int i=0; i<num; i++) {
			Object[] contents = (Object[]) outputs.get(i);
			preds[i] = ((double[]) contents[0])[0];
			ProteinSample test = (ProteinSample) contents[1];
			targets[i] = Double.parseDouble( (String) test.getKey() );
			mse += Math.pow(targets[i]-preds[i], 2);

	        //System.err.println("TARGET: " + targets[i] + " PRED: " + preds[i]);
		}

		return (mse/num);
	}

	public Prediction[] getPredictions() {
		Vector outputs = totals.getOutputs();
		int num = outputs.size();
		Prediction[] preds  = new Prediction[num];
		for(int i=0; i<num; i++) {
			Object[] contents = (Object[]) outputs.get(i);
			double pred = ((double[]) contents[0])[0];
			ProteinSample test = (ProteinSample) contents[1];
			double target = Double.parseDouble( (String) test.getKey() );

			preds[i] = new Prediction(test.getPdbID(), test.getSequence(), target, pred);
		}
		
		return preds;
	}

	public String getModelParams() {
		return modelParams;
	}
}
