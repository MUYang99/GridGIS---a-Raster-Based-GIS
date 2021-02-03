package kth.ag2411.project;

//import java.util.ArrayList;
import java.util.HashMap;
//import java.util.*;


public class MainOperation {
	
	// Attributes
	public String name; // name of this layer
		
	public Layer PerformOperation (String name, Layer layer1, Layer layer2, int operation, 
			int id, boolean IsSquare, int radius, double search) {
		Layer outLayer = new Layer(name, layer1.nRows, layer1.nCols, layer1.origin, layer1.resolution, layer1.nullValue);
		//List<int[]> indexList = new ArrayList<int[]>();
		HashMap<Integer, int[]> HashIndexList = new HashMap<Integer, int[]>();
		outLayer.values = new double[layer1.nRows][layer1.nCols];
		Calculations calc = new Calculations();
		int a = 0;
		
		// local
		if (operation == 0) { 
			a = 0;
			for (int i=0; i<layer1.nRows; ++i) {
				for (int j=0; j<layer1.nCols; ++j) {
					int[] index = {0,0};
					index[0] = i;
            		index[1] = j;
            		HashIndexList.put(a, index); 
            		if (!isNull(layer1, layer2, i, j) ) {
    					outLayer.values[i][j] = calc.execute(layer1, layer2, id, HashIndexList, search);
            		} else {
            			outLayer.values[i][j] = layer1.nullValue;
            		}
					HashIndexList.clear();
					}
				}
			}
		
		// focal
		if (operation == 1) {
			a = 0;
			for (int i=0; i<layer1.nRows; ++i) {
	            for (int j=0; j<layer1.nCols; ++j) {
	            	HashIndexList = layer1.getNeighborhood(i*layer1.nCols+j, radius, IsSquare);
        			if (HashIndexList == null || isNull(layer1, layer2, i, j)) {
        				outLayer.values[i][j] = layer1.nullValue;
        			} else {
        				outLayer.values[i][j] = calc.execute(layer1, null, id, HashIndexList, search);
        			}
        		}
			}
		}

		
		// zonal
		if (operation == 2) { //zonal
			a = 0;
			HashMap<Integer, Double> zones = new HashMap<Integer, Double>();
			for (int i=0; i<layer1.nRows; ++i) {
				for (int j=0; j<layer1.nCols; ++j) {
					if (!zones.containsValue(layer2.values[i][j])) {
						
	            		zones.put(a, layer2.values[i][j]);  
	            		HashIndexList.clear();
	            		int z = 0;
	            		int n = 0;
	            		for (int r=0; r<layer1.nRows; ++r) {
	        	            for (int t=0; t<layer1.nCols; ++t) {
	        	            	if (layer2.values[r][t]==zones.get(a)) {
	        	            		int[] index = {0,0};
	        	            		index[0] = r;
	        	            		index[1] = t;
	        	            		HashIndexList.put(z, index); 
	        	            		z = z+1;
	        	            		if (layer1.values[r][t] == layer1.nullValue) {
	        	            			n = n+1;
	        	            		}
    	        					}
	        	            	}
	        	            }
	            		//System.out.println(a);
	            		a = a+1;
	            		//System.out.println(HashIndexList.size());
						for (int q=0; q<layer1.nRows; ++q) {
							for (int u=0; u<layer1.nCols; ++u) {
								if (layer2.values[q][u]==zones.get(a-1)) {
				            		if (isNull(layer1, layer2, q, u) || n !=  0) {
				            			outLayer.values[q][u] = layer1.nullValue;
				            		} else {
				            			outLayer.values[q][u] = calc.execute(layer1, null, id, HashIndexList, search);
				            		}
								}
							}
						}
    				}
				}
			}	
		}					
		return outLayer;
	}
	private boolean isNull(Layer layer, Layer layer2, int x, int y) {
		if( ( layer.values[x][y] == layer.nullValue ) || ( layer2.values[x][y] == layer2.nullValue ) ) {
			return true;
		}
		return false;
	}
	
}