package assignment;

import javax.swing.*;

public class Plot extends JComponent {

    public int width = 360, height = 180;

    public double xmin=0,  xmax=1, ymin=0, ymax=1;

    public  int scaleX(double x) {
        return (int) (width * (x - xmin) / (xmax - xmin));
    }

    public  int scaleY(double y) {
        return (int) (height * (ymin - y)/(ymax - ymin)+height);
    }

    public  void setScaleX(double min, double max) { xmin = min;   xmax = max;   }

    public  void setScaleY(double min, double max) { ymin = min;   ymax = max; }

}
