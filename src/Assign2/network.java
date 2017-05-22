package Assign2;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class network {
	
	vector[][] SOM;
	Random rand;
	
	int radialFunc;
	double outerRadius;
	double learningRate;
	
	double maxRadiusLattice;
	double maxRadiusSpace;
	
	
	
	int networkSize;
	
	
	public network(int networkSize, int vectorSize, range[] valueRange, Random rand, double learnRate, int radFunc, double outerR){
		
		
		radialFunc = radFunc;
		outerRadius = outerR;
		learningRate = learnRate;
		this.networkSize = networkSize;
		
		maxRadiusLattice = Math.floor((double)networkSize/2);
		maxRadiusSpace = maxRadius(valueRange);
		
		
		SOM = new vector[networkSize][networkSize];
		this.rand = rand;
		
		for(int i=0;i<networkSize;i++){
			for(int j=0;j<networkSize;j++){
				
				SOM[i][j] = new vector(vectorSize, valueRange, this.rand);
				
			}
		}
		
		
	}
	
	public double maxRadius(range[] valRange){
		
		double sum=0;
		
		for(int i=0; i<valRange.length;i++){
			double diff = valRange[i].getMax() - valRange[i].getMin(); 
			diff = Math.pow(diff, 2);
			sum = sum + diff;
			
		}
		
		
		return Math.sqrt(sum);
	}
	
	public void train(ArrayList<vector> trainingData, int maxE, double lrDecayRate, double rDecayRate){
		
		double learningRate;
	
		int epoch = 0;
		int maxEpochs = maxE;
		
		double minDistance = Integer.MAX_VALUE;
		int minIndexX = 0;
		int minIndexY =0;
		
		vector minNode = null;
		vector randomData = null;
		
		double changeInVector;
		
		
		
		while(epoch<maxEpochs){

			minDistance = Integer.MAX_VALUE;
			//pick a random dataset
			int randomIndex = rand.nextInt(trainingData.size());
	
			randomData = trainingData.get(randomIndex);

			
			
			for(int i=0; i<SOM.length;i++){
				for(int j=0; j<SOM.length;j++){
					double dist = getDistance(SOM[i][j],randomData);
					
					if(dist<minDistance){
						minDistance = dist;
						minIndexX = i; 
						minIndexY = j;
						minNode = SOM[i][j];
					}
				}
			}
			

			
			learningRate = updateLearningRate(epoch, maxEpochs, lrDecayRate);
			//update Vector
			for(int i=0; i<minNode.totalAttributes();i++){

				changeInVector = learningRate*(randomData.getAttribute(i)-minNode.getAttribute(i));
				minNode.updateAttribute(i, changeInVector);
	
			}

			//update classification of BMU
			
			if(randomData.getClassification() == 0){
				minNode.bad((double)epoch/maxEpochs);
			}else{
				minNode.good((double)epoch/maxEpochs);
			}
			
			
			double classificationRadius = 0;
			double nodeDistance = 0;
			double radialNeighborInfluence = 0;
			
			//update Neighbors
			
			for(int i=0; i<SOM.length;i++){
				for(int j=0; j<SOM.length;j++){
						
						
						
						
						//find influence
						classificationRadius = radiusDecay(maxRadiusLattice,epoch,maxEpochs, rDecayRate);
						
						nodeDistance = latticeDistance(minIndexX, minIndexY, i,j);
						
						radialNeighborInfluence = radialFunction(maxRadiusLattice, nodeDistance, epoch, maxEpochs, rDecayRate);
						
						

						
						
				
						
						if(nodeDistance<classificationRadius){
							
							if(randomData.getClassification() == 1){
								SOM[i][j].good((double)epoch/maxEpochs*radialNeighborInfluence);
							}else{
								SOM[i][j].bad((double)epoch/maxEpochs*radialNeighborInfluence);
							}
							
							
							
							
						}
				
						
						
						//distance between each node and 
						
						for(int k=0; k<minNode.totalAttributes();k++){
							
							changeInVector = learningRate*radialNeighborInfluence*(randomData.getAttribute(k)-SOM[i][j].getAttribute(k));

							SOM[i][j].updateAttribute(k, changeInVector);

							
						}
						
				}
			}

			epoch++;
			
		}
		
		
		
	}
	
	public double classify(ArrayList<vector> testingData){
		
		int accuracy=0;
		
		for(int i=0; i<testingData.size();i++){
			double minDistance = Double.MAX_VALUE;
			int classification = 2;
			
			
			for(int j=0; j<SOM.length;j++){
				for(int k=0; k<SOM.length;k++){
					double distance = getDistance(testingData.get(i), SOM[j][k]);

					
					if(distance<minDistance){
						
						minDistance = distance;
						
						if(SOM[j][k].getGood() > SOM[j][k].getBad()){
							classification = 1;
						}else if(SOM[j][k].getGood() < SOM[j][k].getBad()){
							classification = 0;
						}else{
							classification = 2;
						}

					}
					
					
				}
			}

			
			
			if(classification == testingData.get(i).getClassification()) accuracy++;			
		}
		
		return (double)accuracy/(double)testingData.size();
		
	}
	

	

	public double updateLearningRate(int t, int tMax, double rate){
		return (learningRate*Math.exp(-rate*t/tMax));
	}
	
	
	
	
	public double latticeDistance(int minX,int minY, int nextX, int nextY){
		
		int distanceX = Math.abs(minX-nextX);
		int distanceY = Math.abs(minY-nextY);;
		
		if(distanceX > maxRadiusLattice){
			distanceX = networkSize - distanceX ;
		}
		
		if(distanceY > maxRadiusLattice){
			distanceY = networkSize - distanceY ;
		}
		
		
		return Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
	}
	
	
	
	
	public double radialFunction(double rad, double distance, int time, int maxTime, double decay){
		//0 = gaussian, 1= mexican hat
		
		if(radialFunc ==0 ){
			return Math.exp(-radius(rad,time, maxTime,decay)*Math.pow(distance, 2));

		}else if(radialFunc ==1){
			return (2*Math.exp(-radius(rad,time, maxTime, decay)*Math.pow(distance, 2)) - Math.exp(-outerRadius*radius(rad, time, maxTime,decay)*Math.pow(distance, 2)));
		}
		//add more if needed
		
		
		
		
		return 0;
	}
	
	public double radius(double rad, int time, int maxTime, double decay){
		

			return 1.0/(2.0*Math.pow(radiusDecay(rad, time, maxTime, decay), 2.0));

		
		
	}
	
	public double radiusDecay(double rad, int time, int maxTime, double decay){
		
		return rad*Math.exp(-(double)time*decay/((double)maxTime/((double)Math.log(maxRadiusLattice))));
		
	}
	
	public double getDistance(vector V, vector D){
		double sum=0;
		
		for(int i=0; i<V.totalAttributes();i++){
			sum = sum + Math.pow(V.getAttribute(i) - D.getAttribute(i), 2);
		}
		
		return Math.sqrt(sum);
		
		
	}

}
