import Gruppe01.*;
import itumulator.executable.Program;
import itumulator.world.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The main class is where we run our simulating world from.
 * Here we read the txt files with world size and various input types.
 * The world is initialized with entities (Grass,rabbits etc.).
 * Then we run the simulation with the entities.
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("./resources/data/t3-2ab.txt");

        Scanner sc = new Scanner(file);

        int size = Integer.parseInt(sc.nextLine());
        Program program = new Program(size, 800, 1200);
        World world = program.getWorld();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");
            StringBuilder name = new StringBuilder();

            for (String part : parts) {
                if (!part.matches(".*\\d+.*")) {
                    name.append(part);
                } else {
                    break;
                }
            }
            if (parts.length > 2 && parts[0].equalsIgnoreCase("carcass") && parts[1].equalsIgnoreCase("fungi") ) {
                parts = new String[]{name.toString(),parts[2]};
            }

            if (parts.length >= 2) {
                int coord1 = 0;
                int coord2 = 0;
                boolean hasCoordinate = false;
                String type = parts[0].trim().toLowerCase();
                int count = parseCount(parts[1].trim());
                Wolf alphaWolf = null;

                if (parts.length > 2 && parts[0].equalsIgnoreCase("bear")) {
                    hasCoordinate = true;
                    String[] coordinate = parts[2].split(",");
                    String coordinate1 = coordinate[0];
                    String coordinate2 = coordinate[1];
                    coord1 = Integer.parseInt(coordinate1.substring(1, coordinate1.length()));
                    coord2 = Integer.parseInt(coordinate2.substring(0, coordinate2.length() - 1));
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
                                if (i == 0) {
                                    alphaWolf = new Wolf(null);
                                    alphaWolf.placeInWorld(world);
                                    alphaWolf.addCave(world);
                                    alphaWolf.act(world);
                                } else {
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
                                break;
                        }
                    }
                }
        }
        sc.close();

        program.show();

        for (int i = 0; i < 250; i++) {
            program.simulate();
        }
    }

    /**
     * Parses(decodes) a count string that represents either a single number or a range of numbers f.eks. ("3" or "10-20").
     * If the count string contains a range (has a dash), a random number within the specified range is returned.
     * Otherwise, the single value is returned.
     * @param countStr the string representing the count (either a single number or a range)
     * @return an integer representing the count, either a single value or a random value within a range.
     */
    private static int parseCount(String countStr) {
        if (countStr.contains("-")) {
            String[] range = countStr.split("-");
            int min = Integer.parseInt(range[0].trim());
            int max = Integer.parseInt(range[1].trim());
            return min + (PRNG.rand().nextInt(max - min + 1));
        } else {
            return Integer.parseInt(countStr);
        }
    }
}