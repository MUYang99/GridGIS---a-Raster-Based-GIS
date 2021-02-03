package kth.ag2411.project;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferUShort;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class DataManagement {
	private List<Layer> layerList;
	
	public DataManagement() {
		layerList = new ArrayList<Layer>();
	}
	
	public void addFileToList(Layer layer) {
		layerList.add(layer);
	}
	
	public List<Layer> getLayerList() {
		return layerList;
	}
	
	//RETURNS NAME 
	public void readList(String filePath) {
		try {
			//LIST OF FILES
			File directoryPath = new File (filePath);
			File filesList[] = directoryPath.listFiles();
			
			//PRINT ALL FILES
			System.out.println("Here is a List of all files in the specified directory");
			
			for (File file: filesList) {
				System.out.println("File name: " + file.getName());
				System.out.println("File path: " + file);
			}
				
			} catch (Exception e) { 
				e.printStackTrace(); {
					System.out.println("Error Listing Files ");
				};
			}	
	}
	
	//READ FILE
	public void readFile ( String fileName, String filePath ) {
		try {
			if ( filePath.endsWith(".txt") ) {
				Layer tempLayer = new Layer(fileName, filePath);
				if(!layerList.contains(fileName)) {
					//System.out.println(tempLayer.nCols);
					layerList.add(tempLayer);	
				}
				else {
					System.out.println("Layer already exists.");
				}
			}
			if ( filePath.endsWith(".png") ) {
				System.out.println(filePath);
				Layer tempLayer = this.readPNG(filePath);
				if(!layerList.contains(fileName)) {
					//System.out.println(tempLayer.nCols);
					layerList.add(tempLayer);	
				}
				else {
					System.out.println("Layer already exists.");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace(); {
				System.out.println("Error Reading one File ");
			};
		}
		
		
	}
	
	//SAVE FILE AS TEXT
	public void saveFileText(Layer layer, String fileName, String outputFileName) {
		try {
			layer.save(layer.name + ".txt");
			
			
		} catch (Exception e) {
			e.printStackTrace(); {
					System.out.println("Error Saving one File");
			}
		}
	}
	
	
		
	//READ ALL FILES - HOW TO MAKE RESOLUTION DYNAMIC FOR PNG??
	public void readFilesList (String filePath) {
		try {
			//LIST OF FILES
			//set folder to read from
			File fileDirectory = new File(filePath);
			layerList.clear();
			
			//read each file in folder and add them to the list
			for (final File fileEntry : fileDirectory.listFiles()) {
				System.out.println(fileEntry);
				if((layerList.isEmpty()) || (!layerList.contains(fileEntry.getName()))) {
					
					String name = fileEntry.getName().trim();
					
					if (fileEntry.getName().endsWith(".txt")) {
			        	Layer tempLayer = new Layer(name, fileEntry.getAbsolutePath());
			        	layerList.add(tempLayer);
					}

					if (fileEntry.getName().endsWith(".png")) { // || fileEntry.getName().endsWith(".jpg") || fileEntry.getName().endsWith(".tiff")) {
						 
						 layerList.add(this.readPNG(filePath));
					}
		        }
			}
			
							
			}catch (Exception e) { 
			e.printStackTrace(); {
				System.out.println("Error Reading Files ");
			};
		}
	}
	
	public Layer readPNG ( String filePath ) {

		File imageFile = new File(filePath);
		File metaFile = new File(filePath.substring(0, filePath.length() - 4) + "_Meta.txt");
		
		Layer tempLayer = null;
		
		try {
			BufferedImage image = ImageIO.read(imageFile.getAbsoluteFile());
			String name = imageFile.getName();
			
			System.out.println( name );
			
			//read metadata

			int nRows = image.getHeight();
			int nCols = image.getWidth();
			double[] origin = {0, 0};
			origin[0] = 0;
			origin[1] = 0;
			double resolution = 10; //SHOULD BE DYNAMIC
			double nullValue = -9999;
			double max = 255;
			double min = 0;
			
			//Reads metadata if it exists
			if ( metaFile.exists() ) {
				FileReader filelines = new FileReader(metaFile);
				BufferedReader BufReader = new BufferedReader(filelines);
				
				String line_row;
				line_row = BufReader.readLine().substring(14);
				nCols = Integer.parseInt(line_row);			//nCols
				line_row = BufReader.readLine().substring(14);
				nRows = Integer.parseInt(line_row);			//nRows
				line_row = BufReader.readLine().substring(14);
	
				//origin
				origin[0] = Double.parseDouble(line_row);			
				line_row = BufReader.readLine().substring(14);	
				origin[1] = Double.parseDouble(line_row);			
				line_row = BufReader.readLine().substring(14);
				resolution = Double.parseDouble(line_row);	//resolution - Scale			
				line_row = BufReader.readLine().substring(14);
				nullValue = Double.parseDouble(line_row);	//nullValue
				line_row = BufReader.readLine().substring(14);	
				max = Double.parseDouble(line_row);			//max
				line_row = BufReader.readLine().substring(14);
				min = Double.parseDouble(line_row);			//min
			} 
			
			tempLayer = new Layer( name, nRows, nCols, origin, resolution, nullValue );
			
			//IF GRAYSCALE IMAGE
			System.out.println("Image " + image.getType());
			System.out.println("Compared " + BufferedImage.TYPE_USHORT_GRAY);
			if ( image.getType() == BufferedImage.TYPE_USHORT_GRAY ) {
				
				DataBufferUShort buffer = (DataBufferUShort) image.getRaster().getDataBuffer(); // Safe cast as img is of type TYPE_USHORT_GRAY 

				// Get data
				short[] arrayUShort = buffer.getData();

				for (int y = 0; y < nRows; y++) {
					for (int x = 0; x < nCols; x++) {
						
						// Access it like:
						double value = arrayUShort[x + y * nCols];	
						if(value < 0) {
							value += 65536;
						}

						value = 1 - ( value / 65536 ); //Normalize and invert :)))
						
						//scale it to the value span of the image then sound it for correct values 
						value =  scale( value, min, max); 
						value = Math.round( value ); 
						
						tempLayer.values[y][x]  = value; 
						
				}
			}
			
			//IF OTHER IMAGE FORMAT
			} else { //if ( image.getType() == BufferedImage.TYPE_INT_RGB ) {

				for (int y = 0; y < nRows; y++) {
					for (int x = 0; x < nCols; x++) {
//						System.out.println(y + " " + x);
						
						Color color = new Color(image.getRGB(x, y));
						float[] HSB = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
						double grey = HSB[2]; //get brightness (grayscale)
						
						grey = 1 - grey;						
						grey = scale( grey, min, max );
						
						tempLayer.values[y][x] =  grey;
						
						
					}
				}
				
			} 
//			else {
//				throw new IOException("Could not load image type. Supported types: Gray and RGB." );
//			}
			
			
		
		} catch (Exception e) {
			System.out.println("Error loading image " + imageFile.getName());
			System.out.println(e);
		}
		System.out.println("Print " + tempLayer.name);
//		tempLayer.print();
		return tempLayer;
	}

	public BufferedImage getBufferedImage (String layerName) {
		BufferedImage layerImage = null;
		
		for (Layer layer : layerList) {
			if (layer.name.equals(layerName)) {
				layerImage = layer.toImage();
			}
		}
		
		return layerImage;
	}
	
	public Layer getLayer(String layerName) {
		Layer _layer = null;
		
		for (Layer layer : layerList) {
//			System.out.println("***********"+layer.name);
			if (layer.name.equals(layerName)) {
				
				_layer = layer;
			}
		}
		
		return _layer;
	}
	
	
	//SAVE ALL FILES
//	public void saveAllFiles(String outputFileName) {
//		File outDirectory = new File(outputFileName);
//		File directoryPath = new File (outputFileName);
//		File filesList[] = directoryPath.listFiles();
//		try {
//		FileWriter fWriter = new FileWriter(outFile);
//		
//		for (File file: filesList) {
//			fWriter.write("ncols " + nCols + "\n");
//			fWriter.write("nrows " + nRows + "\n");
//			fWriter.write("xllcorner " + xllcorner + "\n");
//			fWriter.write("yllcorner"  + yllcorner + "\n");
//			fWriter.write("cellsize " + resolution + "\n");
//			fWriter.write("NODATA_value " + nullValue + "\n");
//			
//			int i = 0;
//			for (int j=0; j < nCols; j++) {
//				fWriter.write(i*nCols+j);
//			}
//			
//			fWriter.write("\n");
//			fWriter.close();
//		}
//
//				
//		} catch (Exception e) {
//			e.printStackTrace(); {
//					System.out.println("Error Saving Files");
//			}
//		}
//	}
//}
	
	
	public double scale (double value, double min, double max) {
				
		return ( ( value * (max - min) ) + min);
	
	}
}
