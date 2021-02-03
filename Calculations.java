package kth.ag2411.project;

import java.util.ArrayList;
import java.util.HashMap;

public class Calculations {
	
	//public boolean Name?(string), layer1, layer2, operation(string), condition (number(string)), 
	//IsSquare(boolean), radius(double), containsValue(one value(string))
	Double value;
	
	public double execute(Layer layer, Layer layer2, int id, HashMap<Integer, int[]> indexList, double search) {
		value = 0.0;

		
		try {
			// Sum
			if(id == 0) {
				for (int[] index: indexList.values()) {
					int x = index[0];
					int y = index[1];
					if (layer2 == null) {
						value += layer.values[x][y];
					} else {
						value += layer.values[x][y] + layer2.values[x][y];
					}
				}
			}
			
			// Subtraction
			if(id == 1) {
				for (int[] index: indexList.values()) {
					int x = index[0];
					int y = index[1];
					if (layer2 == null) {
						value -= layer.values[x][y];
					} else {
						value = layer.values[x][y] - layer2.values[x][y];
					}
				}
			}
			
			// multiplication
			if(id == 2) {
				value = 1.0;
				for (int[] index: indexList.values()) {
					int x = index[0];
					int y = index[1];
					if (layer2 == null) {
						value = layer.values[x][y]*value;
					} else {
						value = layer.values[x][y]*layer2.values[x][y];
					}
				}
			}
			
			// division
			if(id == 3) {
				for (int[] index: indexList.values()) {
					int x = index[0];
					int y = index[1];
					
					if ( layer2.values[x][y] == 0 ) {
						value += layer.values[x][y] / layer2.values[x][y];
					} else {
						value = layer.nullValue;
					}
					
				}
			}
			
			// binary 
			if(id == 4) {
				Boolean flag = false;
				for (int[] index: indexList.values()) {
					int x = index[0];
					int y = index[1];
//					System.out.println("X: " + x + " Y: " + y); {
					if( layer.values[x][y] == search ) {
						flag = true;
//						System.out.println("Found correct!");
					}
					if (layer2 != null) {
						if( layer2.values[x][y] == search ) {
							flag = true;
//							System.out.println("Found correct!");
						}
					}
				}
				if(flag) {
					value = 1.0;
				} else {
					value = 0.0;
				}
			}
			
			// mean
			if(id == 5) {
				if (layer2 == null) {
					for (int[] index: indexList.values()) {
						int x = index[0];
						int y = index[1];
						value += layer.values[x][y];
					}
					value /= indexList.size();
				}
				else {
					int x = indexList.get(0)[0];
					int y = indexList.get(0)[1];
					value += layer.values[x][y];
					value += layer2.values[x][y];
					value /= 2;
				}
			}
			
			// max
			if(id == 6) {
				value = Double.NEGATIVE_INFINITY;
				for (int[] index: indexList.values()) {
					int x = index[0];
					int y = index[1];
					if(layer.values[x][y] > value) {
						value = layer.values[x][y];
						}
					if(layer2 != null) {
						if(layer2.values[x][y] > value) {
							value = layer2.values[x][y];
						}
					}
				}
			}
			
			// min
			if(id == 7) {
				value = Double.POSITIVE_INFINITY;

				for (int[] index: indexList.values()) {
					int x = index[0];
					int y = index[1];
					if(layer.values[x][y] < value) {
						value = layer.values[x][y];
						if(layer2 != null) {
							if(layer2.values[x][y] < value) {
								value = layer2.values[x][y];
							}
						}
					}
				}
			}
			
			// variety
			if(id == 8) {
				ArrayList<Double> used = new ArrayList<Double>();

				for (int[] index: indexList.values()) {
					int x = index[0];
					int y = index[1];
					if (!used.contains(layer.values[x][y])){
						used.add(layer.values[x][y]);			
					}		
				}	
				value = (double) used.size();
			}
			
		//error catching
		} catch(Exception e) {
			System.out.println("Error when performing calculation.");
			System.out.println("Exception: " + e);
		}
		double rvalue = value;
		value = 0.0;
		return rvalue;
		
	}
}