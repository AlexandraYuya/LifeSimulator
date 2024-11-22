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
        // only execute for every 20 steps 1 day
        if (stepCount == 20) {
            // reset step count
            stepCount = 0;
            // lose 1 life point after a full day has passed
            life--;
        }

        // for each step, energy is depleted by 1.
        energy--;
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
        //remember to add all stuff under act
        tryToMate(world);

        // Rabbit can dig hole
        // 10% chance
        double digProbability  = 0.1;
        if (Math.random() < digProbability) {
            if (!world.containsNonBlocking(curLocation)) {
                world.setTile(curLocation, new Burrow());
            }
        }

        System.out.println("energy" + energy);
        System.out.println("Life" + life);

        if (life == 0 || energy == 0) {
            world.delete(this);
            return;
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
        // Here we check if rabbit has enough energy to reproduce (as they go -20 if they do)
        // Don't try to mate if energy is too low
        if (energy < 20) {
            return;
        }
        Location currentLocation = world.getLocation(this);
        // Get only surrounding tiles (not including current location)
        Set<Location> surroundingTiles = world.getSurroundingTiles(currentLocation);
        // Find rabbits in surrounding tiles
        Set<Rabbit> nearbyRabbits = world.getAll(Rabbit.class, surroundingTiles);

        // Only proceed if there are nearby rabbits
        if (!nearbyRabbits.isEmpty()) {
            // 30% chance to create a baby
            // Returns a value between 0.0 and 1.0
            double chance = new Random().nextDouble();
            // 30% chance (0.3 = 30%)
            if (chance < 0.3) {
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

                    // Decrease energy after successful reproduction
                    energy -= 20;
                    System.out.println("A baby rabbit was born! Parent energy now: " + energy);
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
    public void placeInWorld(World world) {
        int size = world.getSize();
        Location location = null;

        while (location == null || !world.isTileEmpty(location)) {
            int x = (int) (Math.random() * size);
            int y = (int) (Math.random() * size);
            location = new Location(x, y);
        }
        if (!world.containsNonBlocking(location)) {
            world.setTile(location, this);
        }
    }
}
