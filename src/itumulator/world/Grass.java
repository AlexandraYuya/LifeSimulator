package itumulator.world;

import itumulator.executable.Program;
import itumulator.simulator.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Grass implements Actor, NonBlocking {
    private int stepCount = 0; // counter to track how many steps, we only want grass to move randomly for every X amount of steps

    @Override
    public void act(World world) {
        stepCount++;
        if (stepCount >= 10) { // only execute for every 10 steps
            stepCount = 0; // reset count back to 0

            // add random spread probability
            double spreadProbability = 0.3; // 30% chance
            if (Math.random() < spreadProbability) {
                Location curLocation = world.getLocation(this); // get curLocation of 'this' grass
                if (curLocation != null) {
                    // find empty surrounding tiles
                    Set<Location> surroundingTiles = world.getSurroundingTiles(curLocation);

                    List<Location> availableTiles = new ArrayList<>(); // check for tiles without grass entity within the surrounding tiles
                    for (Location tile : surroundingTiles) { // loop through the surrounding tiles and get tile only if it's not a 'non-blocking' tile, meaning grass
                        if (world.getNonBlocking(tile) == null) {
                            availableTiles.add(tile); // add to available tiles list
                        }
                    }

                    if (!availableTiles.isEmpty()) { // only if there are available tiles
                        // randomly pick one empty surrounding tile
                        Random r = new Random();
                        Location spreadLocation = availableTiles.get(r.nextInt(availableTiles.size()));
                        Grass newSpreadGrass = new Grass();
                        world.setTile(spreadLocation, newSpreadGrass); // set new spread grass onto world
                    }
                }
            }
        }
    }

    public void placeInWorld(World world, Program program) {
        int size = world.getSize();
        Location location = null;

        while (location == null || world.getNonBlocking(location) != null) {
            int x = (int) (Math.random() * size);
            int y = (int) (Math.random() * size);
            location = new Location(x, y);
        }

        world.setTile(location, this);
    }
}
