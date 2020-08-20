package assignment;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Earth {

    private double[][] arrayOfEarth;

    private Map<Double[], Double> pMap;

    private Map<Double, TreeMap<Double, Double>> mapOfEarth;

    List<double[]> tempList;


    public void readDataArray(String fileName) throws FileNotFoundException {

        FileReader in = new FileReader(fileName);

        try (BufferedReader br = new BufferedReader(in)) {

            String line;
            String delimiter = "\t";
            String[] elem;
            int row = 0, col = 3;
            double[] parsedElems;
            tempList = new ArrayList<double[]>();

            while ((line = br.readLine()) != null) {

                elem = line.split(delimiter);

                parsedElems = new double[3];
                parsedElems[0] = Double.parseDouble(elem[0]);
                parsedElems[1] = Double.parseDouble(elem[1]);
                parsedElems[2] = Double.parseDouble(elem[2]);

                tempList.add(parsedElems);

                row++;
            }

            arrayOfEarth = tempList.toArray(new double[row][col]);

            tempList.clear();

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<double[]> aboveAndBelow(double altitude, boolean choice) throws FileNotFoundException {

        List<double[]> above = new ArrayList<double[]>();
        List<double[]> below = new ArrayList<double[]>();

        if (arrayOfEarth == null || arrayOfEarth.length == 0) {
                readDataArray("src/earth.xyz");
        }

        for (double[] x : arrayOfEarth) {
            if (x[2] > altitude) {
                above.add(x);
            } else{
                below.add(x);
            }
        }

        if (choice == true){
            return above;
        }

        return below;
    }

    public List<double[]> coordinatesAbove(double altitude) throws FileNotFoundException {
        return aboveAndBelow(altitude, true); }

    public List<double[]> coordinatesBelow(double altitude) throws IOException {
        return aboveAndBelow(altitude, false); }

    public void percentageAbove(double altitude) throws IOException {
        List<double[]> above = coordinatesAbove(altitude);
        int aboveSize = above.size();
        int arraySize = arrayOfEarth.length;
        double result = ((double)  aboveSize / (double)  arraySize) * 100;
        DecimalFormat value = new DecimalFormat("#.#");
        System.out.println(String.format("Proportion of coordinates above altitude %s : %s", value.format(altitude), value.format(result))+"%");
    }
    public void percentageBelow(double altitude) throws IOException {
        List<double[]> above = coordinatesBelow(altitude);
        int aboveSize = above.size();
        int arraySize = arrayOfEarth.length;
        double result = ((double)  aboveSize / (double)  arraySize) * 100;
        DecimalFormat value = new DecimalFormat("#.#");
        System.out.println(String.format("Proportion of coordinates below altitude %s : %s", value.format(altitude), value.format(result))+"%");
    }

    public void readDataMap(String filename) throws FileNotFoundException{

        FileReader in = new FileReader(filename);

        try (BufferedReader br = new BufferedReader(in)) {

            mapOfEarth = new TreeMap<Double, TreeMap<Double, Double>>();

            String line;

            String[] elem;

            int row = 0;

            while ((line = br.readLine()) != null) {
                elem = line.split("\t");

                double x,y,z;

                x = Double.parseDouble(elem[0]);
                y = Double.parseDouble(elem[1]);
                z = Double.parseDouble(elem[2]);

                row++;

                if(mapOfEarth.get(x) == null){
                    mapOfEarth.put(x, new TreeMap<>());
                }
                mapOfEarth.get(x).put(y, z);
            }

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateMap(double resolution) {

        mapOfEarth = new TreeMap<Double, TreeMap<Double, Double>>();

        double longitude = 360/resolution, latitude=180/resolution;

        int nCoodinates = (int) (longitude*latitude);

        Random r = new Random();

        for(int i = 0; i < nCoodinates; i++){
            double x = r.nextDouble()*360;
            double y = -90+(r.nextDouble()*180);
            double z = r.nextInt(19998)-9999;

            if(mapOfEarth.get(x) == null){

                mapOfEarth.put(x, new TreeMap<>());
            }

            mapOfEarth.get(x).put(y, z);
        }
    }

    public void generateMapIncrementing(double resolution) {

        mapOfEarth = new TreeMap<Double, TreeMap<Double, Double>>();

        int noOfCoordinates = (int)((360/resolution)*(180/resolution));

        Random r = new Random();

        for (double i = 0; i < 360 ; ++resolution){
            for (double j = -90; j < 180; ++resolution){

                if(mapOfEarth.get(i) == null){
                    mapOfEarth.put(i, new TreeMap<>());
                }
                double randAlt = r.nextInt(19998)-9999;
                mapOfEarth.get(i).put(j, randAlt);

            }
        }
    }


    public double[][] getArrayOfEarth(){ return arrayOfEarth; }

    public Map<Double, TreeMap<Double, Double>> getMapOfEarth(){ return mapOfEarth; }


    private double z = 0;

    public double getAltitude(double longitude, double latitude){

        if (mapOfEarth != null) {
            mapOfEarth.entrySet().forEach(entry->{

                double x = entry.getKey();
                TreeMap<Double,Double> yz = entry.getValue();
                Set<Double> keys = yz.keySet();

                for (Double key : keys)
                   if ( x == longitude && key == latitude)
                       z = yz.get(key);
        });
        }

        if (arrayOfEarth != null)
            for (int i = 0; i < arrayOfEarth.length; i++)
                if (arrayOfEarth[i][0] == longitude && arrayOfEarth[i][1] == latitude)
                    z = arrayOfEarth[i][2];

        return z;
    }

    public void printMapOfEarth(){
        mapOfEarth.entrySet().forEach(entry->{
            System.out.println("KEY IS :" + entry.getKey() + "VALUE IS : " + entry.getValue() + "/n");
        });
        System.out.println("Size of map of earth is :" + mapOfEarth.size());
    }

    public void printXYZFromMap(){

        mapOfEarth.entrySet().forEach(entry->{

            double x = entry.getKey();

            TreeMap<Double,Double> yz = entry.getValue();

            Set<Double> keys = yz.keySet();

            for (Double a : keys){
                System.out.println("x: " + x + " y: " +a + " z: " + yz.get(a));
        }});
    }

    public void printArray(){

        if (arrayOfEarth == null || arrayOfEarth.length == 0)
            System.out.println("Populate array before printing it!");

        for (double[] i : arrayOfEarth)
            System.out.println(Arrays.toString(i));

    }

}
