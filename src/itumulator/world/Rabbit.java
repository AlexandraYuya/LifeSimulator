package itumulator.world;

import itumulator.executable.DisplayInformation;
import itumulator.simulator.Actor;

import java.awt.*;
import java.util.*;
import java.util.List;


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
    /**
     * counts steps and calls two different methods one for night and one for day
     * @param world The current world.
     */
    @Override
    public void act(World world) {
        stepCount++;
        if (stepCount == 20) {
            stepCount = 0;
            life--;
        }

        if (world.isNight()) {
            handleNight(world);
        } else {
            handleDay(world);
        }
        System.out.println("Rabbit life: " + life);
        System.out.println("Rabbit energy: " + energy);
    }

    /**
     * This is the method we are using to handle night. It will make is possible for rabbits to in or
     * outside the borrow.
     * @param world The current world.
     */
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
                if(energy <= 90) {
                    energy += 10;
                }
                System.out.println("Rabbit entered a burrow at: " + previousLocation);
            }else {
                isSleeping = true;
                world.remove(this);
                SleepingRabbit sleepingRabbit = new SleepingRabbit(curLocation);
                world.setTile(curLocation, sleepingRabbit);
                System.out.println("ZZZzzz Rabbit is sleeping outside at: " + curLocation);
            }
        }
    }

    /**
     * This is the method we are using to handle day. It will make the rabbits wake up and reset their location.
     * They can now do normal daytime behavior.
     * @param world The current world.
     */
    private void handleDay(World world) {
        if (isSleeping) {
            // Wake up from sleeping
            isSleeping = false;
            System.out.println("Rabbit woke up from sleeping.");

            Location sleepLocation = previousLocation;
            if(sleepLocation != null) {
                world.delete(world.getTile(sleepLocation));
                world.setTile(sleepLocation, this);
                previousLocation = null;
            }
        }

        if(isInBurrow) {
            if (previousLocation != null) {
                if(world.isTileEmpty(previousLocation)) {
                    // Restore to previous location
                    world.setTile(previousLocation, this);
                }else {
                    Set<Location> emptyNeighbours = world.getEmptySurroundingTiles(previousLocation);
                    if (!emptyNeighbours.isEmpty()) {
                        Random rand = new Random();
                        List<Location> list = new ArrayList<>(emptyNeighbours);
                        Location location = list.get(rand.nextInt(list.size()));
                        world.setTile(location, this);
                    }
                }
                previousLocation = null;
                isInBurrow = false;
            }
        }

        // Resume normal daytime behavior
        if (life > 0 && energy > 0) {
            energy--;
            moveRandomly(world);
            eat(world);
            tryToMate(world);
            digProbability(world);
        }
        if (life <= 0) {
            // Remove rabbit from the world
            world.delete(this);
            System.out.println("A rabbit has died.");
        }
    }
    /**
     * This is the method make the rabbits move random in the world.
     * @param world The current world.
     */
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

    /**
     * This is the method we use for digging borrows, and it makes sure that a rabbit can only dig one borrow.
     * @param world The current world.
     */
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
    /**
     * This is the method is giving energy and removing grass when a rabbit eats it.
     * @param world The current world.
     */
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
    /**
     * This is the method we are using for matting so there will come baby rabbits.
     * @param world The current world.
     */
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
     * This is the method place the rabbit in the world
     * @param world The current world.
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
