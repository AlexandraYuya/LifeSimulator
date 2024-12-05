import java.awt.Color;

import Gruppe01.*;
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
public class Main implements PRNG {
    public static void main(String[] args) throws FileNotFoundException {
    System.out.println("USING SEED: " + seed);
        // Load the file -->
        // Change filename as needed
//        File file = new File("./resources/data/tf3-1a.txt");
        File file = new File("./resources/data/t2-6a.txt");

        Scanner sc = new Scanner(file); // scans the file content

        // Read the world size dynamically, extracted from file
        int size = Integer.parseInt(sc.nextLine());
        Program program = new Program(size, 800, 1200);
        World world = program.getWorld();

        // Process each line for entities (grass, rabbit, burrow) and each of their counts, (since each file have varying number of lines)
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            // can be in <type> <count> pairs or <type> <countMIN-MAX> <(x,y)>
            String[] parts = line.split(" ");
            StringBuilder name = new StringBuilder();

            for (String part : parts) {
                if (!part.matches(".*\\d+.*")) {
                    name.append(part);
                } else {
                    //if it is a number it stops
                    break;
                }
            }
            if (parts.length > 2 && parts[0].equalsIgnoreCase("carcass") && parts[1].equalsIgnoreCase("fungi") ) {
                parts = new String[]{name.toString(),parts[2]}; // new array
            }
            System.out.println(name + " " + parts[0]);

            if (parts.length >= 2) {
                int coord1 = 0;
                int coord2 = 0;
                boolean hasCoordinate = false;
                // normalize type
                String type = parts[0].trim().toLowerCase();
                // calls partsCount method, defined lower in file
                int count = parseCount(parts[1].trim());
                Wolf alphaWolf = null;

                if (parts.length > 2 && parts[0].equalsIgnoreCase("bear")) {
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
                                AdultRabbit rabbit = new AdultRabbit();
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
                                // Create an alpha wolf for the first iteration
                                if (i == 0) {
                                    alphaWolf = new Wolf(null);
                                    alphaWolf.placeInWorld(world);
                                    alphaWolf.addCave(world);
                                    alphaWolf.act(world);
                                } else {
                                    // Assign wolf to the alpha's pack
                                    Wolf wolf = new Wolf(alphaWolf);
                                    wolf.placeInWorld(world);
                                    wolf.act(world);
                                }
                                break;
                            case "berry":
                                BushBerry berry = new BushBerry();
                                berry.placeInWorld(world);
                                break;
                            case "carcass":
                                Carcass carcass = new Carcass();
                                carcass.placeInWorld(world);
                                break;
                            case "carcassfungi":
                                CarcassFungi carcassFungi = new CarcassFungi();
                                carcassFungi.placeInWorld(world);
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
            return min + (PRNG.rand().nextInt(max - min + 1));
        } else {
            // else returns directly the one digit integer
            return Integer.parseInt(countStr);
        }
    }
}