import java.awt.Color;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.io.File;

public class Main {
//hej
    public static void main(String[] args) throws FileNotFoundException {
        int size = 3;
        Program p = new Program(size, 800, 1200);
        World w = p.getWorld();
        File grassA = new File("./resources/data/t1-1a.txt");
//        File grassB = new File("t1-1b");
//        File grassC = new File("t1-1c");
        Scanner sc = new Scanner(grassA);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            System.out.println(line);
        }
//        w.setTile(new Location(0, 0), new <MyClass>());

//        p.setDisplayInformation(<MyClass>.class, new DisplayInformation(<Color>, "<ImageName>"));

        p.show();

        for (int i = 0; i < 200; i++) {
            p.simulate();}
    }
}