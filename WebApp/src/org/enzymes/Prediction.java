package org.enzymes;

public class Prediction {
	
	String proteinName;
	String proteinSeq;
	double target;
	double pred;

	public Prediction(String proteinName, String proteinSeq, double target, double pred) {
		super();
		this.proteinName = proteinName;
		this.proteinSeq = proteinSeq;
		this.target = target;
		this.pred = pred;
	}

	public String getProteinName() {
		return proteinName;
	}

	public void setProteinName(String proteinName) {
		this.proteinName = proteinName;
	}

	public String getProteinSeq() {
		return proteinSeq;
	}

	public void setProteinSeq(String proteinSeq) {
		this.proteinSeq = proteinSeq;
	}

	public double getTarget() {
		return target;
	}

	public void setTarget(double target) {
		this.target = target;
	}

	public double getPred() {
		return pred;
	}

	public void setPred(double pred) {
		this.pred = pred;
	}
	
	
}
