package itumulator.world;



import itumulator.executable.Program;
import itumulator.simulator.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class Rabbit implements Actor {
    private int life;
    private int energy;
    private int stepCount;

    public Rabbit() {
        this.life = 10;
        this.energy = 100;
        this.stepCount = 0;
    }

    @Override
    public void act(World world) {
        stepCount++;
        if (stepCount == 10) { // only execute for every 20 steps 1 day
            stepCount = 0;
            life--;
        }
            energy--;
            int stepCounter = 0;
            Location curLocation = world.getLocation(this);
            Set<Location> neighbours = world.getEmptySurroundingTiles(curLocation);
            if (!neighbours.isEmpty()) {
                Random rand = new Random();
                List<Location> list = new ArrayList<>(neighbours);
                Location newLocation = list.get(rand.nextInt(list.size()));
                world.move(this, newLocation);
                world.setCurrentLocation(newLocation);
            }
            eat(world);
            System.out.println("energy" + energy);
            System.out.println("Life" + life);

            if (life == 0) {
                world.delete(this);
            }
            if (energy <= 0) {
                world.delete(this);
            }
    }

    private void eat(World world) {
        Location curLocation = world.getLocation(this);
        Object hasGrass = world.getNonBlocking(curLocation);
        if(hasGrass instanceof Grass) {
            if(energy <= 100) {
                energy += 5;
            }
            world.delete(hasGrass);
        }

    }

    private void tryToMate(World world) {
        Location currentLocation = world.getLocation(this);
        Object otherObject = world.getTile(currentLocation);

        // Only proceed if the other object is a rabbit and it's not this rabbit
        if (otherObject instanceof Rabbit && otherObject != this) {
            // 50% chance to create a baby
            if (new Random().nextBoolean()) {
                // Look for empty tile to place baby
                Set<Location> emptyTiles = world.getEmptySurroundingTiles(currentLocation);
                if (!emptyTiles.isEmpty()) {
                    // Pick a random empty tile for the baby
                    List<Location> tilesList = new ArrayList<>(emptyTiles);
                    Location babyLocation = tilesList.get(new Random().nextInt(tilesList.size()));

                    // Create and place baby
                    BabyRabbit baby = new BabyRabbit();
                    world.add(baby);
                    world.setTile(babyLocation, baby);
                }
            }
        }
    }


    /**
     * WRITE ALL PARAM?
     * @param world an absolute URL giving the base location of the ima
     * param program
     *
     */
    public void placeInWorld(World world, Program program) {
        int size = world.getSize();
        Location location = null;

        while (location == null || !world.isTileEmpty(location)) {
            int x = (int) (Math.random() * size);
            int y = (int) (Math.random() * size);
            location = new Location(x, y);
        }

        world.setTile(location, this);
    }
}
