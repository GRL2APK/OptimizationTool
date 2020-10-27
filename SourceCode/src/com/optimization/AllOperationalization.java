package com.optimization;
import java.util.*;
public class AllOperationalization {
	int priority;
	String NFR;
	ArrayList<String>operationalizationList;
	public AllOperationalization()
	{
		operationalizationList=new ArrayList<String>();
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getNFR() {
		return NFR;
	}
	public void setNFR(String nFR) {
		NFR = nFR;
	}
	public ArrayList<String> getOperationalizationList() {
		return operationalizationList;
	}
	public void setOperationalizationList(ArrayList<String> operationalizationList) {
		this.operationalizationList = operationalizationList;
	}
	
}
