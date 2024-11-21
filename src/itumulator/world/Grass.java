package itumulator.world;

import itumulator.executable.Program;
import itumulator.simulator.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Grass implements NonBlocking, Actor {
    private int stepCount = 0; // counter to track how many steps, we only want grass to move randomly for every X amount of steps

    @Override
    public void act(World world) {
        stepCount++;

        // Check if no grass exists in the world and place 5 grass if necessary
        if (!world.contains(this)) {
            placeInitialGrass(world, 5);
            return; // Exit early to avoid spreading in the same step
        }

        // Spread grass every 10 steps
        if (stepCount >= 10) {
            stepCount = 0; // Reset step count
            double spreadProbability = 0.9; // 90% chance
            if (Math.random() < spreadProbability) {
                spreadGrass(world);
            }
        }
    }

    /**
     * Places a specified number of grass objects at random empty locations in the world.
     *
     * @param world      The current world.
     * @param grassCount The number of grass objects to place.
     */
    private void placeInitialGrass(World world, int grassCount) {
        int size = world.getSize();
        for (int i = 0; i < grassCount; i++) {
            Location grassLocation = null;

            // Find a random empty location
            while (grassLocation == null || world.getNonBlocking(grassLocation) != null) {
                int x = (int) (Math.random() * size);
                int y = (int) (Math.random() * size);
                grassLocation = new Location(x, y);
            }

            Grass newGrass = new Grass();
            world.add(newGrass);
            world.setTile(grassLocation, newGrass);
        }
    }

    /**
     * Spreads grass to a random empty surrounding tile.
     *
     * @param world The current world.
     */
    private void spreadGrass(World world) {

        Location curLocation = world.getLocation(this);

        if (curLocation != null) {
            Set<Location> surroundingTiles = world.getSurroundingTiles(curLocation);
            List<Location> availableTiles = new ArrayList<>();

            for (Location tile : surroundingTiles) {
                // Only add tiles without grass
                if (world.getNonBlocking(tile) == null) {
                    availableTiles.add(tile);
                }
            }

            if (!availableTiles.isEmpty()) {
                // Randomly pick one empty surrounding tile
                Random r = new Random();
                Location spreadLocation = availableTiles.get(r.nextInt(availableTiles.size()));
                Grass newGrass = new Grass();
                world.setTile(spreadLocation, newGrass);
            }
        }
    }


//        if (stepCount >= 10) { // only execute for every 10 steps
//            stepCount = 0; // reset count back to 0
//
//            // add random spread probability
//            double spreadProbability = 0.5; // 50% chance
//            if (Math.random() < spreadProbability) {
//                Location curLocation = world.getLocation(this); // get curLocation of 'this' grass
//
//                if (curLocation != null) {
//                    // find empty surrounding tiles
//                    Set<Location> surroundingTiles = world.getSurroundingTiles(curLocation);
//
//                    List<Location> availableTiles = new ArrayList<>(); // check for tiles without grass entity within the surrounding tiles
//                    for (Location tile : surroundingTiles) { // loop through the surrounding tiles and get tile only if it's not a 'non-blocking' tile, meaning grass
//                        if (world.getNonBlocking(tile) == null) {
//                            availableTiles.add(tile); // add to available tiles list
//                        }
//                    }
//
//                    if (!availableTiles.isEmpty()) { // only if there are available tiles
//                        // randomly pick one empty surrounding tile
//                        Random r = new Random();
//                        Location spreadLocation = availableTiles.get(r.nextInt(availableTiles.size()));
//                        Grass newSpreadGrass = new Grass();
//                        world.setTile(spreadLocation, newSpreadGrass); // set new spread grass onto world
//                    }
//                }
//            }
//        }
//    }

    public void placeInWorld(World world) {
        int size = world.getSize();
        Location location = null;

        while (location == null || world.getNonBlocking(location) != null) {
            int x = (int) (Math.random() * size);
            int y = (int) (Math.random() * size);
            location = new Location(x, y);
        }
        if (!world.containsNonBlocking(location)) {
            world.setTile(location, this);
        }
    }
}
