import itumulator.executable.Program;
import itumulator.world.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        // Load the file
        File file = new File("./resources/data/t1-1c.txt"); // Change filename as needed
        Scanner sc = new Scanner(file);

        // Read the world size dynamically, extracted from file
        int size = Integer.parseInt(sc.nextLine());
        Program program = new Program(size, 800, 1200);
        World world = program.getWorld();

        // Process each line for entities (grass, rabbit, burrow) and each of their counts
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");
            if (parts.length >= 2) {
                String type = parts[0].trim().toLowerCase();
                int count = parseCount(parts[1].trim());

                // Place entities dynamically, (their functionality has now been moved to each respective class)
                for (int i = 0; i < count; i++) {
                    switch (type) {
                        case "grass" -> new Grass().placeInWorld(world, program);
                        case "rabbit" -> new Rabbit().placeInWorld(world, program);
                        case "burrow" -> new Burrow().placeInWorld(world, program);
                        default -> System.out.println("Unknown entity type: " + type);
                    }
                }
            }
        }
        sc.close();

        // Show the simulation
        program.show();

        // Simulate the world
        for (int i = 0; i < 200; i++) {
            program.simulate();
        }
    }

    private static int parseCount(String countStr) {
        if (countStr.contains("-")) {
            String[] range = countStr.split("-");
            int min = Integer.parseInt(range[0].trim());
            int max = Integer.parseInt(range[1].trim());
            return min + (int) (Math.random() * (max - min + 1));
        } else {
            return Integer.parseInt(countStr);
        }
    }
}