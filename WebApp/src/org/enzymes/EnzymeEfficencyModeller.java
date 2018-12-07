package org.enzymes;

import java.util.*;

import sim.DatasetBuffer;
import sim.MLModelEval;
import sim.MLModelXValid;

import structools.structure.*;
import structools.mlmodels.*;

public class EnzymeEfficencyModeller {

    /*
     * Parameters for the SVM model
     */
    private double epsilon = 0.000000000001;
    private double tolerance = 0.038;
    private double complexity = 1.0;
    private double gamma = 0.01;
    private int seed = 0;
	
    MLModelEval totals;
    
	public EnzymeEfficencyModeller(){
		
	}
	
	/*
	 * X Validate the given data with the spcified model and return the 
	 * correlation coefficient
	 */
	public void xvalidate(String[] names, String[] seqs, String[] effics, int[][] orgPossies, int kernel, String encoding) {
		double results = 0.00;

		int numPositions = orgPossies.length;
		ProteinDataset dataset;
        boolean mergeEncodings = true;
        ProteinSampleEncoder sampleEncoder;

        InterfaceEncoding[] encoders = new InterfaceEncoding[numPositions];
        
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
        
        int[][] positions = ProteinDataset.convertBindingPositionsToAlignment(orgPossies, seqs[0]);
        dataset = new ProteinDataset( names, seqs, orgPossies, effics);

		if(encoding.equals("ortho") ) {
			for(int l=0; l<numPositions; l++) {
				encoders[l] = new InterfaceEncoding(new ResidueEncoding(), mergeEncodings);
			}
			
		} else if(encoding.equals("chemprop") ) {
			for(int l=0; l<numPositions; l++) {
				encoders[l] = new InterfaceEncoding(new ResiduePropertyEncoding(), mergeEncodings);
			}
		} else {
			for(int l=0; l<numPositions; l++) {
				encoders[l] = new InterfaceEncoding(new ResidueBLOMAPEncoding(), mergeEncodings);
			}
		}
		
		sampleEncoder = new ProteinSampleEncoder(encoders, positions, orgPossies, numPositions, seqs);

		
		DatasetBuffer[] dbufs = dataset.getDatasetBufferArray(numPositions);
		
		ProteinSVMRegModel[] classmodels = new ProteinSVMRegModel[dbufs.length];


		for (int m = 0; m < classmodels.length; m++) {
			classmodels[m] = new ProteinSVMRegModel( sampleEncoder, kernel, gamma, complexity );
		}

		MLModelXValid crossValidation = new MLModelXValid (classmodels, dbufs, seed);
		crossValidation.train();

		MLModelEval[] evals = (MLModelEval[]) crossValidation.test();
		totals= MLModelEval.merge(evals);

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

	        System.err.println("TARGET: " + targets[i] + " PRED: " + preds[i]);
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
	
}
