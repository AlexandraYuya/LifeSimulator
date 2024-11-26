package itumulator.world;

import itumulator.executable.Program;
import itumulator.simulator.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Grass implements NonBlocking, Actor {
    // counter to track how many steps, we only want grass to move randomly for every X amount of steps
    private int stepCount = 0;

    /**
     * counts steps and spread grass every 10 steps
     * @param world The current world.
     */
    @Override
    public void act(World world) {
        stepCount++;

        // Spread grass every 10 steps
        if (stepCount >= 10) {
            stepCount = 0; // Reset step count
            double spreadProbability = 0.7; // 70% chance
            if (Math.random() < spreadProbability) {
                spreadGrass(world);
            }
        }
    }

    /**
     * Spreads grass to a random empty surrounding tile.
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

    /**
     * This is the method place the grass in the world
     * @param world The current world.
     */
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
