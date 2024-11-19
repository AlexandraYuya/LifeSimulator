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
        // Step 1: Read the file
        File grassA = new File("./resources/data/t1-1a.txt");
        File grassB = new File("./resources/data/t1-1b.txt");
        File grassC = new File("./resources/data/t1-1c.txt");
        Scanner sc = new Scanner(grassA);

        // Step 2: Read the world size
        int size = Integer.parseInt(sc.nextLine());
        Program p = new Program(size, 800, 1000);
        World w = p.getWorld();

        // Step 3: Initialize the HashMap for object types
        Map<String, Integer> objectCounts = new HashMap<>();

        // Step 4: Process the second line for the type and count
        if (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" "); // Assuming the format is: <Type> <Count>
            if (parts.length == 2) {
                String type = parts[0].trim();
                int count = Integer.parseInt(parts[1].trim());
                objectCounts.put(type, count);
            }
        }

        sc.close();

        // Step 5: Example: Add "grass" objects dynamically based on the count
        if (objectCounts.containsKey("grass")) {
            int grassCount = objectCounts.get("grass");
            for (int i = 0; i < grassCount; i++) {
                int x = (int) (Math.random() * size); // Random x-coordinate
                int y = (int) (Math.random() * size); // Random y-coordinate
                Location loc = new Location(x, y);

                // Example: Place grass on the map (assuming Grass class exists)
                w.setTile(loc, new itumulator.world.Grass());
            }
        }

        // Step 6: Set DisplayInformation for other objects (example setup)
        p.setDisplayInformation(itumulator.world.Grass.class, new DisplayInformation(Color.green, "grass"));

        p.show();

        for (int i = 0; i < 200; i++) {
            p.simulate();}
    }
}