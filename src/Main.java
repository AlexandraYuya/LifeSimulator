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
        File file = new File("./resources/data/t2-6a.txt");

        Scanner sc = new Scanner(file); // scans the file content

        // Read the world size dynamically, extracted from file
        int size = Integer.parseInt(sc.nextLine());
        Program program = new Program(size, 800, 2000);
        World world = program.getWorld();

        // Here we set the default display information for all entities, so the grass will get the png image fx
        program.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass"));
        program.setDisplayInformation(Rabbit.class, new DisplayInformation(Color.darkGray, "rabbit-large"));
        program.setDisplayInformation(SleepingRabbit.class, new DisplayInformation(Color.darkGray, "rabbit-sleeping"));
        program.setDisplayInformation(BabyRabbit.class, new DisplayInformation(Color.gray, "rabbit-small"));
        program.setDisplayInformation(SleepingBabyRabbit.class, new DisplayInformation(Color.gray, "rabbit-small-sleeping"));
        program.setDisplayInformation(Burrow.class, new DisplayInformation(Color.black, "hole"));
        program.setDisplayInformation(Bear.class, new DisplayInformation(Color.orange, "bear"));
        program.setDisplayInformation(SleepingBear.class, new DisplayInformation(Color.darkGray, "bear-sleeping"));
        program.setDisplayInformation(Wolf.class, new DisplayInformation(Color.black, "wolf"));
        program.setDisplayInformation(Carcass.class, new DisplayInformation(Color.red, "carcass"));
        program.setDisplayInformation(Berry.class, new DisplayInformation(Color.red, "bush-berries"));
        program.setDisplayInformation(Berry.class, new DisplayInformation(Color.red, "bush"));

        // Process each line for entities (grass, rabbit, burrow) and each of their counts
        // since each file have varying number of lines
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            // can be in <type> <count> pairs or <type> <countMIN-MAX>
            String[] parts = line.split(" ");

            if (parts.length >= 2) {
                int coord1 = 0;
                int coord2 = 0;
                boolean hasCoordinate = false;
                // normalize type
                String type = parts[0].trim().toLowerCase();
                // calls partsCount method, defined lower in file
                int count = parseCount(parts[1].trim());

                if (parts.length > 2) {
                    hasCoordinate = true;
                    String[] coordinate = parts[2].split(",");
                    String coordinate1 = coordinate[0];
                    String coordinate2 = coordinate[1];
                    coord1 = Integer.parseInt(coordinate1.substring(1, coordinate1.length()));
                    coord2 = Integer.parseInt(coordinate2.substring(0, coordinate2.length() - 1));
                    System.out.println(coord1 + " " + coord2);
                }

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
                            case "bear":
                                Bear bear = new Bear();
                                if(hasCoordinate) {
                                    bear.placeInWorld(world, coord1, coord2);
                                }else {
                                    bear.placeInWorld(world);
                                }
                                bear.act(world);
                                break;
                            case "wolf":
                                    Wolf wolf = new Wolf();
                                    wolf.placeInWorld(world);
                                    wolf.act(world);
                                break;
                            case "berry":
                                Berry berry = new Berry();
                                berry.placeInWorld(world);
                                break;
                            default:
                                System.out.println("Unknown entity type: " + type);
                                break;
                        }
                    }
                }
            Wolf.resetPack();
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