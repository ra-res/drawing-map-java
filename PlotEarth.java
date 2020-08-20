package assignment;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlotEarth extends Plot implements MouseListener {

    Earth earth;
    Map<Double, TreeMap<Double, Double>> earthMap;
    double[][] earthArray;

    private int gridSize = 1;

    int seaLevel;

    double mouseX, mouseY;
    Double lastLong, lastLat, lastAlt, distanceTo;

    MapCoordinate temp;

    List<MapCoordinate> mapCoordinateList;

    public PlotEarth(String fileName) throws FileNotFoundException {

        setScaleX(-180, 180);
        setScaleY(-90, 90);
        start(fileName);

        mapCoordinateList = new ArrayList<MapCoordinate>();
    }

    private void start(String fileName) throws FileNotFoundException {

        earth = new Earth();
//        earth.readDataArray(fileName);
//        earthArray = earth.getArrayOfEarth();
//        earth.generateMap(0.4);
        earth.readDataMap(fileName);
        earthMap = earth.getMapOfEarth();

    }

    public int getGridSize() {
        return gridSize;
    }

    public void setSeaLevel(String seaLevels) {
        String[] arg = seaLevels.split(" ");
        try {
            seaLevel = Integer.parseInt(arg[0]);
            this.seaLevel = seaLevel;
        } catch (NumberFormatException e) {
            double seaLevel = Double.parseDouble(arg[0]);
            this.seaLevel = (int) seaLevel;
        }

    }


    public Color defineColor(double z) { //hardcoded function, adding really high altitudes will crush the program

        int red = 0, green = 0, blue = 0;

        if (z > -10000 && z < 10000) { // safety net for hardcoded values

            if (z < 0) {

                red = 0;
                green = 120 + (int) (z / 100);
                blue = 255 + (int) (z / 100);

            } else if (z < 200) {

                red = 50 + (int) (z / 5);
                green = 140 + (int) (z / 5);
                blue = 40 + (int) (z / 5);

            } else if (z < 350) {

                red = 230 - (int) (z / 5);
                green = 255 - (int) (z / 5);
                blue = 200 - (int) (z / 5);

            } else if (z < 2000) {

                red = 255 - (int) (z / 20);
                green = 230 - (int) (z / 20);
                blue = 200 - (int) (z / 20);

            } else {

                red = 150 - (int) (z / 500);
                green = 130 - (int) (z / 500);
                blue = 100 - (int) (z / 500);

            }

        } else {

            if (z > 10000) {

                red = 180;
                green = 180;
                blue = 180;

            } else if (z < -10000) {

                red = 0;
                green = 20;
                blue = 180;

            }
        }
        return new Color(red, green, blue);
    }


    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);


        Graphics2D g2d = (Graphics2D) g;


        this.width = getWidth();
        this.height = getHeight();
//      Printing from array
//        for (double[] coordinates : earthArray) {
//            g2d.setColor(defineColor(coordinates[2] - seaLevel));
//            if (coordinates[0] < 180) {
//                g2d.fillRect(scaleX(coordinates[0]), scaleY(coordinates[1]), gridSize, gridSize);
//            } else {
//                g2d.fillRect(scaleX(coordinates[0] - 360), scaleY(coordinates[1]), gridSize, gridSize);
//            }
//        }

        earthMap.entrySet().forEach(entry -> {

            double x = entry.getKey();

            TreeMap<Double, Double> yz = entry.getValue();

            yz.entrySet().forEach(entry2nd-> {

                double y = entry2nd.getKey();
                double z = entry2nd.getValue();

                g2d.setColor(defineColor(z - seaLevel));
                if (x < 180) {
                    g2d.fillRect(scaleX(x), scaleY(y), gridSize, gridSize);
                } else {
                    g2d.fillRect(scaleX(x - 360), scaleY(y), gridSize, gridSize);
              }

            });
        });


//        ));
//            converting coordinates above 180 to negative values, but it makes map really low quality
//            double div = (int)(coordinates[0]/180);
//            double mod = (int)(coordinates[0]%180);
//            if (div >= 1){
//                g2d.fillRect(scaleX(-180+mod),scaleY(coordinates[1]), gridSize, gridSize+2);
//            } else {
//                g2d.fillRect(scaleX(coordinates[0]), scaleY(coordinates[1]), gridSize, gridSize);
//           }


    }

    File outFile = createFile("clicked_coordinates.txt");

    private File createFile(String fileName) {

        File file = new File(fileName);

        try {
            if (file.exists() && file.isFile())
                file.delete();
            file.createNewFile();

        } catch (IOException e) {
            System.out.println("Could not create file. Please try again!");
        }

        return file;
    }


    public void writeToFile(MapCoordinate a, File fileName) {

        try {

            FileWriter fw = new FileWriter(fileName, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.append(a.toString());
            pw.append("\n");
            pw.close();
            fw.close();

            ArrayList<MapCoordinate> lines = new ArrayList<MapCoordinate>();
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            String curLine = reader.readLine();
            double x, y, z;
            String[] lineArr;
            while (curLine != null) {
                lineArr = curLine.split("\t");
                x = Double.parseDouble(lineArr[0]);
                y = Double.parseDouble(lineArr[1]);
                z = Double.parseDouble(lineArr[2]);
                lines.add(new MapCoordinate(x, y, z));
                curLine = reader.readLine();
            }

            Collections.sort(lines);

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            for (MapCoordinate line : lines) {
                writer.write(line.toString());
                writer.newLine();
            }

            reader.close();
            writer.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void deleteFromFile(MapCoordinate delete, File file) throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){

            ArrayList<MapCoordinate> lines = new ArrayList<MapCoordinate>();
            String curLine = reader.readLine();

            double x, y, z;
            String[] lineArr;
            while (curLine != null) {
                lineArr = curLine.split("\t");
                 x = Double.parseDouble(lineArr[0]);
                 y = Double.parseDouble(lineArr[1]);
                 z = Double.parseDouble(lineArr[2]);
                 lines.add(new MapCoordinate(x, y, z));
                 curLine = reader.readLine();
            }

            lines.removeIf(temp -> temp.ALTITUDE == delete.ALTITUDE && temp.LATITUDE == delete.LATITUDE && temp.LONGITUDE == delete.LONGITUDE);

            Collections.sort(lines);

            try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (MapCoordinate line : lines) {
                    writer.write(line.toString());
                    writer.newLine();
                }
                writer.close();
            }catch (IOException e){
                e.printStackTrace();
            }

            reader.close();

    }   catch(IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
    }

}



    @Override
    public void mouseClicked(MouseEvent e) {
        AtomicBoolean found = new AtomicBoolean(false);
        switch(e.getButton()) {
            case MouseEvent.BUTTON1: {

                mouseX = e.getX();
                mouseY = e.getY();

                if (earthArray != null) {
                    for (double[] coordinates : earthArray) {
                        if ((scaleX(coordinates[0]) == mouseX && scaleY(coordinates[1]) == mouseY) || (scaleX(coordinates[0] - 360) == mouseX && scaleY(coordinates[1]) == mouseY)) {

                            temp = new MapCoordinate(coordinates[0], coordinates[1], (coordinates[2] - seaLevel));
                            mapCoordinateList.add(temp);

                            if (lastLong != null && lastLat != null && lastAlt != null) { // check if last point exists
                                for (MapCoordinate mapCo : mapCoordinateList) {
                                    if (mapCo.LONGITUDE == lastLong && mapCo.LATITUDE == lastLat && mapCo.ALTITUDE == lastAlt) {
                                        distanceTo = mapCo.distanceTo(temp); // setting distance between points
                                    }
                                }
                            }

                            System.out.printf("Longitude: %.2f Latitude: %.2f Altitude %.2f %n", coordinates[0], coordinates[1], (coordinates[2] - seaLevel));
                            if (distanceTo != null)
                                System.out.printf("Distance from last clicked coordinate to this coordinate is : %.2f km %n", distanceTo);


                            int index = mapCoordinateList.size() - 1;

                            writeToFile(mapCoordinateList.get(index), outFile);

                            Collections.sort(mapCoordinateList);


                            lastLong = coordinates[0]; // reset pointers to last coordinate
                            lastLat = coordinates[1];
                            lastAlt = coordinates[2] - seaLevel;

                            break;
                        }
                    }
                    break;

                }else if (earthMap != null){
                    earthMap.entrySet().forEach(entry -> {

                        double x = entry.getKey();

                        TreeMap<Double, Double> yz = entry.getValue();

                        yz.entrySet().forEach(entry2nd-> {

                            if (found.get() == false){
                                double y = entry2nd.getKey();
                                double z = entry2nd.getValue();

                                if ((scaleX(x) == mouseX && scaleY(y) == mouseY) || (scaleX(x - 360) == mouseX && scaleY(y) == mouseY)) {

                                    temp = new MapCoordinate(x, y, (z - seaLevel));
                                    mapCoordinateList.add(temp);

                                if (lastLong != null && lastLat != null && lastAlt != null) { // check if last point exists
                                    for (MapCoordinate mapCo : mapCoordinateList) {
                                        if (mapCo.LONGITUDE == lastLong && mapCo.LATITUDE == lastLat && mapCo.ALTITUDE == lastAlt) {
                                            distanceTo = mapCo.distanceTo(temp); // setting distance between points
                                        }
                                    }
                                }

                                System.out.printf("Longitude: %.2f Latitude: %.2f Altitude %.2f %n", x, y, (z - seaLevel));
                                if (distanceTo != null)
                                    System.out.printf("Distance from last clicked coordinate to this coordinate is : %.2f km %n", distanceTo);


                                int index = mapCoordinateList.size() - 1;

                                writeToFile(mapCoordinateList.get(index), outFile);

                                Collections.sort(mapCoordinateList);


                                lastLong = x; // reset pointers to last coordinate
                                lastLat = y;
                                lastAlt = z - seaLevel;

                                found.set(true);

                            }
                        }});
                    });
                } break;
            }

            case MouseEvent.BUTTON3: {

                if (lastLong != null && lastLat != null && lastAlt != null) { // check if last point exists

                    for (MapCoordinate mapCo : mapCoordinateList){

                        if (mapCo.LONGITUDE == lastLong && mapCo.LATITUDE == lastLat && mapCo.ALTITUDE == lastAlt){

                            mapCoordinateList.remove(mapCo);
                            System.out.printf("You deleted Longitude: %.2f Latitude: %.2f Altitude: %.2f %n", lastLong, lastLat, lastAlt);
                            try {
                                deleteFromFile(mapCo, outFile);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        }
                    }

                } else {

                    System.out.println("No coordinate to delete!");

                }

                break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
