package Assign2;

public class range {
	
	double max;
	double min;
	
	public range(){
		max = (double)Integer.MIN_VALUE;
		min = (double)Integer.MAX_VALUE;
	}
	
	public void updateRange(double value){
		
		if(value>max) max = value;
		if(value<min) min = value;
		
	}
	
	public double getMax(){
		return max;
	}
	
	
	public double getMin(){
		return min;
	}
	
	
	
	

}
