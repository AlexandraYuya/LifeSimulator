import java.awt.Color;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.*;

import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        // Step 1: Load the file, we can switch out with any file here and they should all work
        File file = new File("./resources/data/t1-2cde.txt"); // Change filename as needed
        Scanner sc = new Scanner(file);

        // Step 2: Read the world size
        int size = Integer.parseInt(sc.nextLine()); // First line is world size
        Program program = new Program(size, 800, 1200);
        World world = program.getWorld();

        // Step 3: Process each line for entities and counts
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" "); // Assuming the second line and thereafter has the format: <Type> <Count> or <Type> <CountRange>

            if (parts.length >= 2) {
                String type = parts[0].trim().toLowerCase(); // Normalize type
                int count = parseCount(parts[1].trim()); // Handle ranges like "3-7"

                // Place entities dynamically
                for (int i = 0; i < count; i++) {
                    int x,y;
                    Location location = null;

                    while (location == null || !world.isTileEmpty(location)) {
                        x = (int) (Math.random() * size); // Random x-coordinate
                        y = (int) (Math.random() * size); // Random y-coordinate
                        location = new Location(x, y);
                    }

                    // Place entities "grass, rabbit or burrow" based on type
                    switch (type) {
                        case "grass":
                            if (world.isTileEmpty(location)) {
                                world.setTile(location, new Grass());
                                program.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass"));
                            }
                            break;

                        case "rabbit":
                            world.setTile(location, new Rabbit());
                            program.setDisplayInformation(Rabbit.class, new DisplayInformation(Color.gray, "rabbit-small"));
                            break;


                        case "burrow":
                            world.setTile(location, new Burrow());
                            program.setDisplayInformation(Burrow.class, new DisplayInformation(Color.black, "hole-small"));
                            break;

                        default:
                            System.out.println("Unknown entity type: " + type);
                            break;
                    }
                }
            }
        }
        sc.close();

        // Step 4: Show the simulation
        program.show();

        // Step 5: Simulate the world
        for (int i = 0; i < 200; i++) {
            program.simulate();
        }
    }

    private static int parseCount(String countStr) {
        if (countStr.contains("-")) { // check if it's a interval
            String[] range = countStr.split("-"); // split interval by '-' symbol so we can process both values seperately
            int min = Integer.parseInt(range[0].trim());
            int max = Integer.parseInt(range[1].trim());
            return min + (int) (Math.random() * (max - min + 1)); // Random value in within min, max
        } else {
            return Integer.parseInt(countStr); // If it's a single number, it returns it directly as is.
        }
    }
}