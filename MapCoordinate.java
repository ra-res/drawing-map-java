package assignment;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MapCoordinate extends Object implements Comparable<MapCoordinate> {

    public final double LATITUDE, LONGITUDE, ALTITUDE;

    public MapCoordinate(double longitude, double latitude, double altitude) {
        LONGITUDE = longitude;
        LATITUDE = latitude;
        ALTITUDE = altitude;
    }

    public double distanceTo(MapCoordinate other){
        double y1 = this.LATITUDE;
        double x1 = this.LONGITUDE;
        double y2 = other.LATITUDE;
        double x2 = other.LONGITUDE;

        final double greatCircleRadius = 6371;
        double deltaX = Math.toRadians(x2 - x1);
        double deltaY = Math.toRadians(y2 - y1);
        x1 = Math.toRadians(x1);
        x2 = Math.toRadians(x2);

        double a = Math.pow(Math.sin(deltaX/2),2) + Math.pow(Math.sin(deltaY/2),2) * Math.cos(x1) * Math.cos(x2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return greatCircleRadius * c;

    }

    @Override
    public boolean equals(Object other){
        if (other == this) return true;
        if (!(other instanceof MapCoordinate)) return false;
        MapCoordinate o = (MapCoordinate) other;
        return Double.compare(ALTITUDE, o.ALTITUDE) == 0
                && Double.compare(LATITUDE, o.LATITUDE) == 0
                && Double.compare(LONGITUDE, o.LONGITUDE) == 0;
    }

    @Override
    public String toString(){
        return String.valueOf(this.LONGITUDE +"\t" + this.LATITUDE + "\t" + this.ALTITUDE);

//        int space = 20;
//
//        int longLength = space - String.valueOf(this.LONGITUDE).length();
//        int latLength = space - String.valueOf(this.LATITUDE).length();
//        int altLength = space - String.valueOf(this.ALTITUDE).length();
//
//        StringBuilder sb = new StringBuilder();
//
//        sb.append(this.LONGITUDE);
//        for (int i = 0; i < longLength; i++)
//            sb.append(" ");
//
//        sb.append(this.LATITUDE);
//        for (int i = 0; i < latLength; i++)
//            sb.append(" ");
//
//        sb.append(this.ALTITUDE);
//        for (int i = 0; i < altLength; i++)
//            sb.append(" ");

//        return sb.toString();
    }

    @Override
    public int compareTo(MapCoordinate other) {

        if (this.ALTITUDE == other.ALTITUDE
                && this.LATITUDE == other.LATITUDE
                && this.LONGITUDE == other.LONGITUDE)
            return 0;

        if (this.ALTITUDE > other.ALTITUDE)
            return 1;

        if (this.ALTITUDE < other.ALTITUDE)
            return -1;

        if (this.ALTITUDE == other.ALTITUDE
                && this.LATITUDE > other.LATITUDE)
            return 1;

        if (this.ALTITUDE == other.ALTITUDE
                && this.LATITUDE < other.LATITUDE)
            return -1;

        if (this.ALTITUDE == other.ALTITUDE
                && this.LATITUDE == other.LATITUDE
                && this.LONGITUDE > other.LONGITUDE)
            return 1;

        if (this.ALTITUDE == other.ALTITUDE
                && this.LATITUDE == other.LATITUDE
                && this.LONGITUDE < other.LONGITUDE)
            return -1;

        return 0;

    }

}
