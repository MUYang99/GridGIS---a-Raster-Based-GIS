# GridGIS_a-Raster-Based-GIS

![image](https://github.com/MUYang99/GridGIS_a-Raster-Based-GIS/blob/main/Imgs/GridGIS.jpg)

While developing GridGIS, the goal was to create an easily used GIS competent enough to do everything the basic GIS user is desiring, while not complicating it. This is why GridGIS is based on Map Algebraic operations and an uncomplicated interface. The range of operations provided in GridGIS are enough to provide all GridGIS users with a versatile tool, which operations you after reading this manual will know how to use.

The Development team wants to thank You for choosing GridGIS as the software for your future geographical work, and hope you will enjoy using it. The GridGIS development team is
not interested in your money, which is why the software will be open source to be available for the greater public. All users are however very welcome with feedback to improve the
areas of usage and the user friendliness of our software.

Happy GIS:ing!

Best regards,

the Development Team

Yang Mu
& Jonas Bylerius
& Micael Makenzius
& Ann-Sofi Lenander

# Instruction

GridGIS is able to process ASCII text files (.txt) and PNG files as input. If the user has access to convert other file types into text files, the user has the ability to process a great deal of data in GridGIS. It is important that the input files have the , otherwise the user will receive an error message.

The user can import both PNG images exported from GridGIS but also images acquired from other sources. Image files produced through GridGIS have an associated meta-data file which contains information to scale the image to the correct values. Images from other sources do not contain such information and will be scaled to a value between 0 and 255.

The GridGIS software is able to save the processed input files into ASCII text files (.txt) and into PNG files. The user is able to select the desired type of output after running a selected operation. How to save output of the software is covered in Chapter 6. The result of the software will also show the output of the processed data as an image, so the user can see the result of the operation.

The types of operations provided by GridGIS are raster based and uses map algebra, meaning that the operations are done to all cells using one or two raster layers. The operations of GridGIS are either based on local, focal or zonal operations. GridGIS can perform one operation at the time. Should the user be interested in performing several operations, the user is able to do so by saving the output result of one operation, and using the same output for the next operation.

# Operations
## Local Operations - Usage Examples

LocalSum 
The local sum operation can be used for combining data of two maps. If the user is interested both in the road infrastructure and the locations of water bodies, a map of the infrastructure and the water bodies can be added together, so the content of both maps are shown in one map.

LocalSubstraction 
If the user wants to look at a map of road infrastructure without buildings, the local subtraction operation can be used. If the user has a map visualizing both buildings and roads, the user can subtract houses from that map. This may require the user to use the local binary function for creating a map containing only numbers.

LocalDivision 
The local division operation may be useful if the user is looking to analyse the content of a map with respect to content of another map. An example of this could be if the user has a grid population density map, and wants to analyse a property per capita.

LocalBinary 
The local binary operation is good if the user is interested in looking for one or two specific values of one or two maps. If the user inputs two maps, the user must understand the meaning of the applied search value for both of the maps. This function can work for either looking for all buildings in a map. It can also be used as a step in a
map analysation, for example by using the map containing only houses, and combining that map in another raster operation.

LocalMean 
The local mean operation takes the mean value of the same position of each input map. This could be used for taking the mean value of temperature maps, of temperature measurements of two different dates..

LocalMax 
The local max operation returns the maximum value of the two input maps. This operation can be used for finding the maximum value of for example the minimum snow thickness of two different occasions.

LocalMin 
The local min operation returns the minimum value of the two input maps. This operation can be used for finding the maximum value of for example precipitation of an area of different occasions.

LocalVariety 
Local variety will return either 1 or two, as it returns the count of values of the same position in two input files. This type of map maybe valuable as a step in an map analysis. An example of this is looking at built environments of different times in history, to see how much areas that have been exploited compared to previously.


## Focal Operations - Usage Examples

FocalSum 
Focal sum returns the sum of the values in a neighborhood. This may be useful for calculating the sum of population within a radius from a hospital or similar.

FocalBinary 
Focal binary lets the user search for a value within a map neighborhood. If the neighborhood contains the value, the center cell of the neighborhood will get the value searched for. As the operations is performed for all grid
positions, the result will be a buffered area around the applied search value. 

FocalMean 
Focal mean returns the mean value of the neighborhood. This could be used as a filter for smoothing the input file.

FocalMax 
Focal max will return the maximum value of the neighborhood, which may be good for finding and emphasizing areas of high elevation.

FocalMin 
Focal min will return the minimum value of the neighborhood. This can be useful while analysing a temperature map.

FocalVariety 
Focal variety will return the count of values within the neighborhood. This operation can be useful for looking at the diversity of functions in a built environment.


## Zonal operations - Usage Examples

ZonalSum 
The zonal sum operation will return the sum of all values within a zone. This operation can be useful if wanting to calculate for example the sum of population within an administrative zone.

ZonalBinary 
The zonal binary operation can be used to search for an applied search value within a zone. If the zone input file layer contains districts, the user can use the zonal binary to find all zones containing elementary schools,
or all zones containing traffic lights.

ZonalMean 
The zonal mean will return the mean value of all values within a zone. This can be used for analysing, as the mean value of a property within a zone can work as a step map, used in further operations.

ZonalMax 
The zonal max operation will return the maximum value within a zone for the whole zone. This operation can be useful, for example you might want to know what's the highest point in each zone.

ZonalMin 
The zonal min operation will return the minimum value within a zone for the whole zone. If comparing precipitation of different zones a ZonalMin operation can be used to find the least amount of precipitation in different
zones.

ZonalVariety 
The zonal variety will return the count of values within a zone. This operation can be used to find the diversity of function within all specified zones within a built environment.
