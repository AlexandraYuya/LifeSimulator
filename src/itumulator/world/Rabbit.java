package itumulator.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Rabbit {
    protected int energy;
    // Tracks if the rabbit has already dug a burrow
    protected boolean hasDugBurrow;
    // Reference to the burrow the rabbit dug
    protected Burrow myBurrow;
    // Stores location before entering a burrow
    protected Location previousLocation;
    // Tracks if the rabbit is sleeping outside
//    protected boolean isSleeping;
    protected boolean isInBurrow;

    public Rabbit() {
        this.energy = 100;
        this.hasDugBurrow = false;
        this.myBurrow = null;
        this.previousLocation = null;
//        this.isSleeping = false;
        this.isInBurrow = false;
    }

    /**\
     * This method checks if rabbit is in burrow and reinstates them if so.
     * @param world The current world.
     */
    // START BURROW CHECK METHOD -->
    protected void checkInBurrow(World world) {
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
    }
    // <-- END BURROW CHECK METHOD

    /**
     * This is the method we are using to handle night. It will make is possible for rabbits to in or
     * outside the borrow.
     * @param world The current world.
     */
    // START NIGHT HANDLER METHOD -->
    protected void handleNight(World world) {
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
//                isSleeping = true;
//                world.delete(this);
//                SleepingRabbit sleepingRabbit = new SleepingRabbit(curLocation);
//                world.setTile(curLocation, sleepingRabbit);
                System.out.println("ZZZzzz Rabbit is sleeping outside at: " + curLocation);
            }
        }
    }
    // <-- END NIGHT HANDLER METHOD

    /**
     * This is the method make the rabbits move random in the world.
     * @param world The current world.
     */
    // START RANDOM MOVEMENT METHOD -->
    protected void moveRandomly(World world) {
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
    // <-- END RANDOM MOVEMENT METHOD

    /**
     * This is the method is giving energy and removing grass when a rabbit eats it.
     * @param world The current world.
     */
    // START EAT METHOD -->
    protected void eat(World world) {
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
     * This is the method place the rabbit in the world
     * @param world The current world.
     */
    // START PLACING METHOD -->
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
    // <-- END PLACING METHOD
}
