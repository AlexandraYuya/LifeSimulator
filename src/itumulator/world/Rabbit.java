package itumulator.world;

import itumulator.simulator.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Rabbit extends Animal implements Actor {
    // Tracks if the rabbit has already dug a burrow
    protected boolean hasDugBurrow;
    // Reference to the burrow the rabbit dug
    protected Burrow myBurrow;
    // Stores location before entering a burrow
    protected Location previousLocation;
    protected boolean isInBurrow;

    public Rabbit() {
        super(0,100);
        this.hasDugBurrow = false;
        this.myBurrow = null;
        this.previousLocation = null;
        this.isInBurrow = false;
    }

    @Override
    public void act(World world) {
        super.act(world);
    }

    /**
     * This is the method we are using to handle night. It will make is possible for rabbits to in or
     * outside the borrow.
     * @param world The current world.
     */
    @Override
    public void handleNight(World world) {
        super.handleNight(world);
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
            }
        }
    }

    @Override
    public void handleDay(World world) {
        checkInBurrow(world);
    }

    /**
     * This is the method make the rabbits move random in the world.
     * @param world The current world.
     */

    @Override
    public void moveRandomly(World world) {
        super.moveRandomly(world);
    }

    /**
     * This is the method is giving energy and removing grass when a rabbit eats it.
     * @param world The current world.
     */
    @Override
    public void eat(World world) {
        super.eat(world);
        Location curLocation = world.getLocation(this);
        Object entity = world.getNonBlocking(curLocation);
        if(entity instanceof Grass) {
            if(energy <= 95) {
                energy += 5;
                System.out.println("Ate grass new energy:" + energy);
            }
            world.delete(entity);
        }
    }

    /**\
     * This method checks if rabbit is in burrow and reinstates them if so.
     * @param world The current world.
     */
    public void checkInBurrow(World world) {
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

    /**
     * This is the method place the rabbit in the world
     * @param world The current world.
     */
    @Override
    public void placeInWorld(World world) {
        super.placeInWorld(world);
    }
}
