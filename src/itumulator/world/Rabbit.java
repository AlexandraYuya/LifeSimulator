package itumulator.world;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.simulator.Actor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class Rabbit implements Actor {
    private int life;
    private int energy;
    private int stepCount;
    // Tracks if the rabbit has already dug a burrow
    private boolean hasDugBurrow;
    // Reference to the burrow the rabbit dug
    private Burrow myBurrow;
    // Stores location before entering a burrow
    private Location previousLocation;
    // Tracks if the rabbit is sleeping outside
    private boolean isSleeping;
    private boolean isInBurrow;


    public Rabbit() {
        this.life = 10;
        this.energy = 100;
        this.stepCount = 0;
        this.hasDugBurrow = false;
        this.myBurrow = null;
        this.previousLocation = null;
        this.isSleeping = false;
        this.isInBurrow = false;
    }

    @Override
    public void act(World world) {

        if (world.isNight()) {
            handleNight(world);
        } else {
            handleDay(world);
        }
    }

    private void handleNight(World world) {
        if (world.isOnTile(this)) {
            previousLocation = world.getLocation(this); // get the location before removal
            Location curLocation = world.getLocation(this);
            // Get only surrounding tiles
            Set<Location> surroundingTiles = world.getSurroundingTiles(curLocation);
            // Find burrows in surrounding tiles
            Set<Burrow> nearbyBurrow = world.getAll(Burrow.class, surroundingTiles);
            // check if curLocation is on a burrow
            Object onBurrow = world.getNonBlocking(curLocation);

            // Only proceed if there are nearby burrows or on a burrow
            if (!nearbyBurrow.isEmpty() || onBurrow instanceof Burrow) {
                world.remove(this);
                isInBurrow = true;
                System.out.println("Rabbit entered a burrow at: " + previousLocation);
            }else {
                isSleeping = true;
                System.out.println("ZZZzzz Rabbit is sleeping outside at: " + curLocation);
            }
        }
    }

    private void handleDay(World world) {
        if (isSleeping) {
            // Wake up from sleeping
            isSleeping = false;
            System.out.println("Rabbit woke up from sleeping.");
        }

        if(isInBurrow && !world.isOnTile(this)) {
            if (previousLocation != null) {
                world.setTile(previousLocation, this); // Restore to previous location
                previousLocation = null;
                isInBurrow = false;
                System.out.println("Previous location: " + previousLocation);
            }
            return;
        }

        // Resume normal daytime behavior
        stepCount++;
        if (stepCount == 20) {
            stepCount = 0;
            life--;
        }
        if (life > 0 && energy > 0) {
            energy--;
            moveRandomly(world);
            eat(world);
            tryToMate(world);
            digProbability(world);
        }
    }

    private void moveRandomly(World world) {
        if(energy > 0) {
            Location curLocation = world.getLocation(this);
            Set<Location> neighbours = world.getEmptySurroundingTiles(curLocation);

            if (!neighbours.isEmpty()) {
                Random rand = new Random();
                List<Location> list = new ArrayList<>(neighbours);
                Location newLocation = list.get(rand.nextInt(list.size()));
                world.move(this, newLocation);
                world.setCurrentLocation(newLocation);
            }
        }
    }

    // DIG METHOD -->
    private void digProbability(World world) {
        // only 1 burrow can be dug per rabbit
        if(hasDugBurrow) {
            return;
        }
        // 30% chance of digging
        double digProbability  = 0.3;
        if (Math.random() < digProbability) {
            Location curLocation = world.getLocation(this);
            if (!world.containsNonBlocking(curLocation)) {
                Burrow burrow = new Burrow();
                world.setTile(curLocation, burrow);
                // associate rabbit with a burrow
                this.myBurrow = burrow;
                // associate burrow with a rabbit
                burrow.setOwner(this);
                // mark rabbit as has dug burrow
                hasDugBurrow = true;
            }
        }
    }
    // <-- END DIG METHOD

    // START EAT METHOD -->
    private void eat(World world) {
        Location curLocation = world.getLocation(this);
        Object hasGrass = world.getNonBlocking(curLocation);
        if(hasGrass instanceof Grass) {
            if(energy <= 95) {
                energy += 5;
                System.out.println("Ate grass new energy:" + energy);
            }
            world.delete(hasGrass);
        }
    }
    // <-- END EAT METHOD

    // START MATING METHOD -->
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
    // <-- END MATING METHOD

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
