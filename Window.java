package assignment;

import com.sun.source.tree.Tree;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;


public class Window extends JFrame {

    private PlotEarth plotEarth;

    public Window(String fileName, String seaLevel) throws FileNotFoundException {
        setTitle("Map of Earth");
        getContentPane().setPreferredSize(new Dimension(1280,720));

        plotEarth = new PlotEarth(fileName);
        plotEarth.setSeaLevel(seaLevel);

        add(plotEarth);
        addMouseListener(plotEarth);

        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) throws FileNotFoundException {

        String arg = args.length==0 || args == null ? "0": args[0];
        new Window("src/earth.xyz", arg);

    }
}

