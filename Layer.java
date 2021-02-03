package kth.ag2411.project;
 /*  */

import java.io.FileReader;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferUShort;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;





public class Layer {
	
		// Attributes
		public String name; // name of this layer
		public int nRows; // number of rows
		public int nCols; // number of columns
		public double[] origin = new double[2]; // x,y-coordinates of lower-left corner
		public double resolution; // cell size
		public double[][] values; // data. Alternatively, public double[][] values;
		public double nullValue; // value designated as "No data"
		
		//Constructor 
		public Layer (String layerName, String fileName) {		
				try { 	// Exception may be thrown while reading (and writing) a file.
						// Get access to the lines of Strings stored in the file
						FileReader filelines = new FileReader(fileName);
						BufferedReader BufReader = new BufferedReader(filelines);
						
						name = layerName;
						// A sting that lines from raster will be saved to.
						String line_row;
						// Read first line, which starts with "ncols"
						line_row = BufReader.readLine().substring(14);
						nCols = Integer.parseInt(line_row);
						// Read second line, which starts with "nrows"
						line_row = BufReader.readLine().substring(14);
						nRows = Integer.parseInt(line_row);					
						// Read third line, which starts with "xllcorner"
						line_row = BufReader.readLine().substring(14);
						origin[0] = Double.parseDouble(line_row);					
						// Read forth line, which starts with "yllcorner"
						line_row = BufReader.readLine().substring(14);
						origin[1] = Double.parseDouble(line_row);					
						// Read fifth line, which starts with "cellsize"
						line_row = BufReader.readLine().substring(14);
						resolution = Double.parseDouble(line_row);					
						// Read sixth line, which starts with "NODATA_value"
						line_row = BufReader.readLine().substring(14);
						nullValue = Double.parseDouble(line_row);	
						// An array that can contain values fitting the range of the raster
						values = new double[nRows][nCols];
						
						// Read each of the remaining lines, which represents a row of raster
						int a = 0;
						while (line_row != null) {
							// Looking at each row "i" in the raster
							line_row = BufReader.readLine();
							if (line_row != null) {
								for (int b = 0; b<nCols; b++) 
									// Looking at each value from each column "j" in the row "i" from the raster
									values[a][b] = Double.parseDouble(line_row.split(" ")[b]);
							a++;}
							}
						BufReader.close();
				} catch (IOException e) {
						e.printStackTrace(); }
		}
		public Layer(String outLayerName, int nRows, int nCols, double[] origin, double resolution, double nullValue) {
				// construct a new layer by assigning a value to each of its attributes
				this.name = outLayerName; // on the left hand side are the attributes of
				this.nRows = nRows; // the new layer;
				this.nCols = nCols; // on the right hand side are the parameters.
				this.origin = origin; 
				this.resolution = resolution;
				this.nullValue = nullValue;
				this.values = new double[nRows][nCols];
				}

		// Print
		public void print(){
				
				//Print this layer to console
				System.out.println("ncols "+nCols);
				System.out.println("nrows "+nRows);
				System.out.println("xllcorner "+origin[0]);
				System.out.println("yllcorner "+origin[1]);
				System.out.println("cellsize "+resolution);
				System.out.println("NODATA_value " + nullValue);
				
				for (int i = 0; i < nRows; i++) {
						for (int j = 0; j < nCols; j++) {
							System.out.print(values[i][j]+" ");
						}
						System.out.println();
				}
		}
		// Save
		public void save(String outputFileName) {
			// save this layer as an ASCII file that can be imported to ArcGIS
			try {	
					//Open a writable file to write into
					File file = new File (outputFileName);
					FileWriter fWriter = new FileWriter(file);
					
					//Write the metadata
					fWriter.write("ncols         "+nCols+"\n");
					fWriter.write("nrows         "+nRows+"\n");
					fWriter.write("xllcorner     "+origin[0]+"\n");
					fWriter.write("yllcorner     "+origin[1]+"\n");
					fWriter.write("cellsize      "+resolution+"\n");
					fWriter.write("NODATA_value  "+nullValue+"\n");
					
					//write the values
					for (int i = 0; i < nRows; i++) {
						for (int j = 0; j < nCols; j++) {
							fWriter.write(values[i][j]+" ");
						}
						fWriter.write("\n");			
					}
					fWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		
		public void saveImage (String outputFileName) {
			//SAVE FILE AS IMAGE
			try {
				
				File outputfile = new File(outputFileName);
				
				BufferedImage image = new BufferedImage(nCols, nRows, BufferedImage.TYPE_USHORT_GRAY);
				
//				WritableRaster raster = image.getRaster();
				DataBufferUShort buffer = (DataBufferUShort) image.getRaster().getDataBuffer(); // Safe cast as img is of type TYPE_USHORT_GRAY 
				short[] arrayUShort = buffer.getData();
				double Max = getMax();
				double Min = getMin();
				
				for (int y = 0; y < nRows; y++) {
					for (int x = 0; x < nCols; x++) {

						int value = (int) ((( values[y][x] - Min)/(Max - Min)) * 65535); // 
						

						buffer.setElem(x + y * nCols, 65535 - value);
					}
				}
				
				
				ImageIO.write(image, "png", outputfile);
				
			} catch (Exception e) {
				System.out.println("Error saving output as image - " + e);
			}
			
			try {	
				//Open a writable file to write into
				File file = new File (outputFileName.substring(0, outputFileName.length() - 4) + "_Meta.txt");
				FileWriter fWriter = new FileWriter(file);
				
				//Write the metadata
				fWriter.write("ncols         "+ nCols +"\n");
				fWriter.write("nrows         "+ nRows +"\n");
				fWriter.write("xllcorner     "+ origin[0] +"\n");
				fWriter.write("yllcorner     "+ origin[1] +"\n");
				fWriter.write("cellsize      "+ resolution +"\n");
				fWriter.write("NODATA_value  "+ nullValue +"\n");
				fWriter.write("Max_value     "+ this.getMax() +"\n");
				fWriter.write("Min_value     "+ this.getMin() +"\n");
				fWriter.close();
			} catch (Exception e) {
				System.out.println("Error saving metadata for image - " + e);
			}
		}
		
		public HashMap<Integer, int[]> getNeighborhood(int i, int r, boolean IsSquare) {	
			int h = 0;
			// Convert i in 1D array to (r,c) in 2D array
			int rows = i/nCols; // the row number of cell i
			int cols = i%nCols; // the column number of cell i
			//List<int[]> indexListHood = new ArrayList<int[]>();
			HashMap<Integer, int[]> HashIndexList = new HashMap<Integer, int[]>();
			HashIndexList.clear();
			int nu = 0;
			for (int a = Math.max(rows-r, 0); a <= Math.min(rows+r, nRows-1); a++) {
				for (int b = Math.max(cols-r, 0); b <= Math.min(cols+r, nCols-1); b++) {
					if (IsSquare == false) {
						if (((a-rows)*(a-rows)+(b-cols)*(b-cols)) <= r*r) {
							int[] index = {0,0};
							index[0] = a;
    	            		index[1] = b;
    	            		HashIndexList.put(h, index);
    	            		h = h+1;
    	            		if (values[a][b] == nullValue) {
    	            			nu = nu + 1;
    	            		}
						}
					} else {
						int[] index = {0,0};
						index[0] = a;
	            		index[1] = b;
	            		HashIndexList.put(h, index);
	            		h = h+1;
	            		if (values[a][b] == nullValue) {
	            			nu = nu + 1;
	            		}
					}
				}
			}
			if (nu != 0) {
				HashIndexList = null;
			}		
			return HashIndexList;
		}
		
		public BufferedImage toImage() {
			
			// visualize a BufferedImage of the layer in color
			// This object represents a 24-bit RBG image with a width of 20 pixels
			// (corresponding to the number of columns) and a height of 30 pixels
			// (corresponding to the number of rows).
			BufferedImage image = new BufferedImage(nCols, nRows, BufferedImage.TYPE_INT_RGB);
			
			// The above image is empty. To color the image, you first need to get access to
			// its raster, which is represented by the following object.
			WritableRaster raster = image.getRaster();
			// These statements make a grayscale value and assign it to the pixel at the
			// top-left corner of the raster.
			int[] color = new int[3];
			double Max = getMax();
			double Min = getMin();
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					if (values[i][j] != nullValue) {
						color[0] = (int) (255-((values[i][j]-Min)/(Max-Min))*255); // Red
						color[1] = (int) (255-((values[i][j]-Min)/(Max-Min))*255); // Green
						color[2] = (int) (255-((values[i][j]-Min)/(Max-Min))*255); // Blue
						raster.setPixel(j, i, color);
						
					}else {
						color[0] = (int) (255); // Red
						color[1] = (int) (0); // Green
						color[2] = (int) (0); // Blue
						raster.setPixel(j, i, color);
					}
				}
			}
			return image;
	}
	
		public BufferedImage toImage(double[] ColorValues) {
		// visualize a BufferedImage of the layer in color
		// This object represents a 24-bit RBG image 
		BufferedImage image = new BufferedImage(nCols, nRows, BufferedImage.TYPE_INT_RGB);
		
		WritableRaster raster = image.getRaster();
		Random random = new Random();
		
        Map<Double, int[]> colorMap = new HashMap<>();

        for (double value : ColorValues) {
            int[] color = new int[3];
            color[0] = random.nextInt(256);
            color[1] = random.nextInt(256);
            color[2] = random.nextInt(256);
            colorMap.put(value, color);
        }

        for (int i=0; i<nRows; ++i) {
            for (int j=0; j<nCols; ++j) {
                for (double ColorValue : ColorValues) {
                    if (values[i][j] == ColorValue) {
                        raster.setPixel(j, i, colorMap.get(ColorValue));
                        break;
                    }
                    int[] white = new int[3];
                    white[0] = (int) nullValue;
                    white[1] = (int) nullValue;
                    white[2] = (int) nullValue;
                    raster.setPixel(j, i, white);
                }
            }
        }
		return image;
	}
		

		public double useGetMinIndirectly() {
			double min = this.getMin(); // calling getMin()
			return min;
			}		
		
		public double useGetMaxIndirectly() {
			double max = this.getMax(); // calling getMax()
			return max;
			}
		
		private double getMax() {
			double max = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					if (values[i][j] > max) {
						if (values[i][j] != nullValue) {
							max = values[i][j];
						}
					}
				}
			}
			return max;
		}
		private double getMin() {
			double min = Double.POSITIVE_INFINITY;
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					if (values[i][j] < min) {
						if (values[i][j] != nullValue) {
							min = values[i][j];
						}
					}
				}
			}
			return min;
		}

		public double scale (double value, double min, double max) {
					
			return ( ( value * (max - min) ) + min);
		
		}
}
