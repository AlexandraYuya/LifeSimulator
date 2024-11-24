import java.awt.Color;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The main class is where we run our simulating world from
 * Here we read the files (we were given txt files)
 * Then the world is initialized with entities(Grass,rabbits etc)
 * So the steps are: reads the file, place the correct entities from the file, place the entities in our wold,
 * then run the simulation with the entities
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        // Load the file -->
        // Change filename as needed
        File file = new File("./resources/data/t1-2cde.txt");
//        File file = new File("./resources/data/t1-2fg.txt");
        Scanner sc = new Scanner(file); // scans the file content

        // Read the world size dynamically, extracted from file
        int size = Integer.parseInt(sc.nextLine());
        Program program = new Program(size, 800, 1200);
        World world = program.getWorld();

        // Here we set the default display information for all entities, so the grass will get the png image fx
        program.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass"));
        program.setDisplayInformation(Rabbit.class, new DisplayInformation(Color.gray, "rabbit-large"));
        program.setDisplayInformation(BabyRabbit.class, new DisplayInformation(Color.gray, "rabbit-small"));
        program.setDisplayInformation(SleepingBabyRabbit.class, new DisplayInformation(Color.gray, "rabbit-small-sleeping"));
        program.setDisplayInformation(SleepingRabbit.class, new DisplayInformation(Color.gray, "rabbit-sleeping"));
        program.setDisplayInformation(Burrow.class, new DisplayInformation(Color.black, "hole"));

        // Process each line for entities (grass, rabbit, burrow) and each of their counts
        // since each file have varying number of lines

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            // can be in <type> <count> pairs or <type> <countMIN-MAX>
            String[] parts = line.split(" ");
            if (parts.length >= 2) {
                // normalize type
                String type = parts[0].trim().toLowerCase();
                // calls partsCount method, defined lower in file
                int count = parseCount(parts[1].trim());

                for (int i = 0; i < count; i++) {
                    switch (type) {
                        case "grass":
                            Grass grass = new Grass();
                            grass.placeInWorld(world);
                            grass.act(world);
                            break;
                        case "rabbit":
                            Rabbit rabbit = new Rabbit();
                            rabbit.placeInWorld(world);
                            rabbit.act(world);
                            break;
                        case "burrow":
                            new Burrow().placeInWorld(world);
                            break;
                        default:
                            System.out.println("Unknown entity type: " + type);
                            break;
                    }
                }
            }
        }
        // closes the scanner
        sc.close();

        // Show the simulation
        program.show();

        // Simulate the world
        for (int i = 0; i < 250; i++) {
            program.simulate();
        }
    }

    // here we handle the count (as in grass 3 or rabbit 10-20), either an integer is a value between min-max or it is just a single value
    /**
     * Parses(decodes) a count string that represents either a single number or a range of numbers
     * fx:("3" or "10-20"). If the count string contains a range (has a dash),
     * a random number within the specified range is returned. Otherwise, the single value is returned.
     *
     * @param countStr the string representing the count (either a single number or a range)
     * @return an integer representing the count, either a single value or a random value within a range
     * See comments for better understanding of the code
     */
    private static int parseCount(String countStr) {
        // only executes in integers with intervals
        if (countStr.contains("-")) {
            // split by dash so we can process min and max value
            String[] range = countStr.split("-");
            int min = Integer.parseInt(range[0].trim());
            int max = Integer.parseInt(range[1].trim());
            // pick a random value between min and max
            return min + (int) (Math.random() * (max - min + 1));
        } else {
            // else returns directly the one digit integer
            return Integer.parseInt(countStr);
        }
    }
}