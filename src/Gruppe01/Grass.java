package Gruppe01;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Grass implements NonBlocking, Actor, DynamicDisplayInformationProvider, PRNG {
    // counter to track how many steps, we only want grass to move randomly for every X amount of steps
    private int stepCount = 0;

    /**
     * This method display a grass png.
     */
    @Override
    public DisplayInformation getInformation() {
            return new DisplayInformation(Color.GREEN, "grass");
    }

    /**
     * counts steps and spread grass every 10 steps.
     * And it can spread every 20 steps.
     * @param world The current world
     */
    @Override
    public void act(World world) {
        stepCount++;

        // Spread grass every 20 steps
        if (stepCount % 20 == 0) {
            double chance = PRNG.rand().nextDouble();
            if (chance < 0.7) {
                spreadGrass(world);
            }
        }
    }

    /**
     * The method spread grass to a random empty surrounding tile.
     * @param world The current world
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
                Location spreadLocation = availableTiles.get(PRNG.rand().nextInt(availableTiles.size()));
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
            int x = PRNG.rand().nextInt(size);
            int y = PRNG.rand().nextInt(size);
            location = new Location(x, y);
        }
        if (!world.containsNonBlocking(location)) {
            world.setTile(location, this);
        }
    }
}
