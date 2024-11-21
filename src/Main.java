import java.awt.Color;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        // Load the file
        File file = new File("./resources/data/t1-2cde.txt"); // Change filename as needed
        Scanner sc = new Scanner(file); // scans the file content

        // Read the world size dynamically, extracted from file
        int size = Integer.parseInt(sc.nextLine());
        Program program = new Program(size, 800, 500);
        World world = program.getWorld();

        // Process each line for entities (grass, rabbit, burrow) and each of their counts
        while (sc.hasNextLine()) { // since each file have varying number of lines
            String line = sc.nextLine();
            String[] parts = line.split(" "); // can be in <type> <count> pairs or <type> <countMIN-MAX>
            if (parts.length >= 2) {
                String type = parts[0].trim().toLowerCase(); // normalize type
                int count = parseCount(parts[1].trim()); // calls partsCount method, defined lower in file

                // Place entities dynamically, (their functionality has now been moved to each respective class)
                for (int i = 0; i < count; i++) {
                    switch (type) {
                        case "grass":
                            new Grass().placeInWorld(world, program);
                            program.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass"));
                            break;
                        case "rabbit":
                            Rabbit rabbit = new Rabbit();
                            rabbit.placeInWorld(world, program);
                            program.setDisplayInformation(Rabbit.class, new DisplayInformation(Color.gray, "rabbit-large"));
                            rabbit.act(world);
                            break;
                        case "burrow":
                            new Burrow().placeInWorld(world, program);
                            program.setDisplayInformation(Burrow.class, new DisplayInformation(Color.black, "hole-small"));
                            break;
                        case "babyrabbit":
                            BabyRabbit babyrabbit = new BabyRabbit();
                            babyrabbit.placeInWorld(world, program);
                            program.setDisplayInformation(Rabbit.class, new DisplayInformation(Color.gray, "rabbit-large"));
                            babyrabbit.act(world);
                            break;
                        default:
                            System.out.println("Unknown entity type: " + type);
                            break;
                    }
                }
            }
        }
        // close scanner
        sc.close();

        // Show the simulation
        program.show();

        // Simulate the world
        for (int i = 0; i < 200; i++) {
            program.simulate();
        }
    }

    // here we handle the count (as in grass 3 or rabbit 10-20), either an integer is a value between min-max or it is just a single value
    private static int parseCount(String countStr) {
        if (countStr.contains("-")) { // only executes in integers with intervals
            String[] range = countStr.split("-"); // split by dash so we can process min and max value
            int min = Integer.parseInt(range[0].trim());
            int max = Integer.parseInt(range[1].trim());
            return min + (int) (Math.random() * (max - min + 1)); // pick a random value between min and max
        } else {
            return Integer.parseInt(countStr); // else returns directly the one digit integer
        }
    }
}