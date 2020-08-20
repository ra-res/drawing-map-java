package assignment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Map;
import java.util.Scanner;

public class SystemIN{

    Earth earth;

    public SystemIN(String fileName) throws FileNotFoundException {
        earth = new Earth();
        earth.readDataMap(fileName);
    }

    public void userInput(){

        Scanner in = new Scanner(System.in);

        while(true){

            System.out.println("Please enter an altitude or enter quit to disconnect!");

            String input = in.nextLine();
            input = input.toLowerCase();

            if(input.equals("quit")){
                System.out.println("Bye!");
                break;
            }

            try{

                double doubleInput = Double.parseDouble(input);
                earth.percentageAbove(doubleInput);

            } catch (NumberFormatException | FileNotFoundException e){
                System.out.println(input + "is not a valid float number");

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void ex3(){


        Scanner in = new Scanner(System.in);

        while (true){

            System.out.println("Please enter longitude (0-360) and latitude (-90-90), or 'quit' to exit");


            String input = in.nextLine();
            input = input.toLowerCase();

            if (input.equals("quit")){
                System.out.println("Bye!");
                break;
            }

            try {
                String[] inputArray = input.split(" ");

                if (inputArray.length != 2){
                    System.out.println("Please enter BOTH longitude (0-360) and latitude (-90-90), or 'quit' to exit");
                    continue;
                }

                double lng, lat;

                lng = Double.parseDouble(inputArray[0]);
                lat = Double.parseDouble(inputArray[1]);

                if (lng < 0 || lng > 360 || lat <- 90 || lat > 90){
                    System.out.println("Longitude or latitude is outside range. Please try again, or 'quit' to exit");
                    continue;
                }

                System.out.println("Please enter 1 for percentage or 2 for altitude");
                String choice = in.nextLine();
                choice = choice.trim();

                double alt = earth.getAltitude(lng, lat);

                if (choice.equals("1"))
                    earth.percentageAbove(alt);
                 else if ( choice.equals("2"))
                    System.out.printf("Altitude for the given coordinates is: %.2f %n", alt);


            } catch (NumberFormatException e) {
                System.out.println("Longitude or latitude is invalid. Please try again, or 'quit' to exit");

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        SystemIN in = new SystemIN("src/earth.xyz");
//        in.userInput();
        in.ex3();

//        MapCoordinate a = new MapCoordinate(200,400,20);
//        MapCoordinate b = new MapCoordinate(200,400, 20);
//
//        System.out.println(a.equals(b));
//        System.out.println(a.compareTo(b));

    }


}


