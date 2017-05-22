package Assign2;

import java.util.Random;
import java.util.Scanner;

public class vector {
	
	int numAttributes;
	double[] attributes;
	int classification;
	double good=0;
	double bad=0;
	
	public vector(int n, range[] valueRange, Random rand){
		numAttributes = n;
		this.attributes = new double[numAttributes];
		
		//initalize vectors
		for(int i=0; i<attributes.length;i++){
			attributes[i] = valueRange[i].getMin() + (valueRange[i].getMax() - valueRange[i].getMin()) * rand.nextDouble();
			
			
			
		}
		
		
		
	}
	
	public vector(String[] attributes, int num){
		numAttributes = num;
		
		this.attributes = new double[numAttributes];
		classification = Integer.parseInt(attributes[0]);
		
		int k =1;
		
		for(int i=1; i<attributes.length;i++){
			
			
			try{
				this.attributes[i-k] = Double.parseDouble(attributes[i]);
			}catch(NumberFormatException e){
				k++;
			}
			
			
		}
		
	}
	
	public void bad(double x){
		bad = bad + x;
		
	}
	
	public void good(double x){
		good = good + x;
			
		}
	
	public double getBad(){
		return bad;
	}
	
	public double getGood(){
		return good;
	}
	
	public int totalAttributes(){
		return numAttributes;
	}
	
	public double getAttribute(int index){
		return attributes[index];
	}
	
	public void updateAttribute(int index, double value){
		attributes[index] = attributes[index] + value;
	}
	
	public int getClassification(){
		return classification;
	}
	
	public void setClassification(int c){
		classification = c;
	}
	
	public void intializeVector(){
		
	}
	

}
