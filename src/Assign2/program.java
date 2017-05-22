package Assign2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class program {
	
	
	static int latticeLength = 15;
	static double testingSplit = 0.15;
	
	
	//parameters
	static double learningRate =.1; //0.1 - gaussian
	static int radialFunction = 1; //0 - gaussian, 1 - mexican hat
	static int maxEpochs = 1000;
	static double learningRateDecayRate = 1;
	static double radiusDecayRate = 6;
	
	
	//Mexican Hat Function parameters
	static double outerRadius = 0.3;
	
	
	
	static int iterations = 1;
	
	
	
	
	
	
	
	static int seed=2222;
	static Random rand = new Random(seed);
	
	static int numAttributes;
	static ArrayList<vector> data;
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//read file
		String files[] = {"L30fft_16", "L30fft_25", "L30fft_32", "L30fft_64"};
		
		
		
	
					
					for(int m=0; m<files.length;m++){
						
						try {
							readFile("data/" + files[m] + ".out");
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							System.out.println("File not found!");
						}
						
						range[] valueRange = getRange();
						int split = (int)Math.floor(data.size()*testingSplit);
						double sum=0;
						
						
						for(int i=0;i<iterations;i++){
							
							Collections.shuffle(data,rand);
													
							network SOM = new network(latticeLength, numAttributes, valueRange, rand, learningRate, radialFunction, outerRadius);
							
							//split data
							ArrayList<vector> trainingData = new ArrayList<vector>(data.subList(0, data.size()-split));
							ArrayList<vector> testingData = new ArrayList<vector>(data.subList(data.size()-split, data.size()));
							
							
							SOM.train(trainingData,maxEpochs, learningRateDecayRate, radiusDecayRate);
							
							System.out.println("Dataset: " + files[m]);
							
							for(int j=0; j<SOM.SOM.length;j++){
								for(int k=0; k<SOM.SOM.length;k++){
									
									//System.out.print(SOM.SOM[j][k].getClassification() + " ");
									
									if(SOM.SOM[j][k].getGood()>SOM.SOM[j][k].getBad()){
										System.out.print("1 ");
									}else if(SOM.SOM[j][k].getGood()<SOM.SOM[j][k].getBad()){
										System.out.print("0 ");
									}else{
										System.out.print("2 ");
									}
									
								}
								System.out.println();
								
							}
							
							System.out.println();

							double acc=SOM.classify(testingData);
							sum = sum + acc;

							
						}
						
						System.out.println("Accuracy: " + sum/iterations);

					}
					
					System.out.println();
					

		
		
	}
	
	public static void readFile(String fileName) throws FileNotFoundException{
		
		String[] header;
		
		String line;
		String[] attributes;
		
		data = new ArrayList<vector>();
		Scanner fileScanner = new Scanner(new FileReader(fileName));
		
		header = fileScanner.nextLine().split(" ");
		
		numAttributes = Integer.parseInt(header[1]);
		
		
		
		
		while(fileScanner.hasNextLine()){
			line = fileScanner.nextLine();
			attributes = line.split(" ");

			data.add(new vector(attributes, numAttributes));

		}
		
		
		
		
	}
	
	public static range[] getRange(){
		
		range[] valueRange = new range[numAttributes];
		
		for(int i=0; i<data.size();i++){
			vector temp = data.get(i);
			for(int j=0;j<temp.totalAttributes();j++){
				
				if(valueRange[j] == null){
					valueRange[j] = new range();
				}
				
				valueRange[j].updateRange(temp.getAttribute(j));

			}
			
			
		}
		
		
		return valueRange;
	}

}
