import java.awt.Color;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import java.util.*;

public class Main {
//hej
    public static void main(String[] args) {
        int size = 5;
        Program p = new Program(size, 800, 1200);
        World w = p.getWorld();

//        w.setTile(new Location(0, 0), new <MyClass>());

//        p.setDisplayInformation(<MyClass>.class, new DisplayInformation(<Color>, "<ImageName>"));

        p.show();

        for (int i = 0; i < 200; i++) {
            p.simulate();}
    }
}