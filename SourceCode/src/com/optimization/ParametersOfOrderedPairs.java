package com.optimization;

public class ParametersOfOrderedPairs {

	String op1,op2,NFR1,NFR2;
	double combinedcontribution,threshold, normalizedpriority, delta, bigdel, sigma, roh;
	int op1prio,op2prio;
	public int getOp1prio() {
		return op1prio;
	}
	public void setOp1prio(int op1prio) {
		this.op1prio = op1prio;
	}
	public int getOp2prio() {
		return op2prio;
	}
	public double getSigma() {
		return sigma;
	}
	public void setSigma(double sigma) {
		this.sigma = sigma;
	}
	public double getRoh() {
		return roh;
	}
	public void setRoh(double roh) {
		this.roh = roh;
	}
	public void setOp2prio(int op2prio) {
		this.op2prio = op2prio;
	}
	public String getOp1() {
		return op1;
	}
	public void setOp1(String op1) {
		this.op1 = op1;
	}
	public String getOp2() {
		return op2;
	}
	public void setOp2(String op2) {
		this.op2 = op2;
	}
	public String getNFR1() {
		return NFR1;
	}
	public void setNFR1(String nFR1) {
		NFR1 = nFR1;
	}
	public String getNFR2() {
		return NFR2;
	}
	public void setNFR2(String nFR2) {
		NFR2 = nFR2;
	}
	public double getCombinedcontribution() {
		return combinedcontribution;
	}
	public void setCombinedcontribution(double combinedcontribution) {
		this.combinedcontribution = combinedcontribution;
	}
	public double getThreshold() {
		return threshold;
	}
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	public double getNormalizedpriority() {
		return normalizedpriority;
	}
	public void setNormalizedpriority(double normalizedpriority) {
		this.normalizedpriority = normalizedpriority;
	}
	public double getDelta() {
		return delta;
	}
	public void setDelta(double delta) {
		this.delta = delta;
	}
	public double getBigdel() {
		return bigdel;
	}
	public void setBigdel(double bigdel) {
		this.bigdel = bigdel;
	}
	
}
