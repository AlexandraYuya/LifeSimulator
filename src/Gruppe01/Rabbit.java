package Gruppe01;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Rabbit extends Animal implements Actor {
    protected boolean hasDugBurrow;
    protected Burrow myBurrow;
    protected Location previousLocation;
    protected boolean isInBurrow;

    public Rabbit() {
        super(0,100, true, 10);
        this.hasDugBurrow = false;
        this.myBurrow = null;
        this.previousLocation = null;
        this.isInBurrow = false;
    }

    /**
     * This method accounts for all the behavior of rabbits in the superclass animal.
     * @param world The current world
     */
    @Override
    public void act(World world) {
        super.act(world);
    }

    /**
     * This is the method we are using to handle night.
     // Get only surrounding tiles
     // Find burrows in surrounding tiles
     // check if curLocation is on a burrow
     // Only proceed if there are nearby burrows or on a burrow
     * It makes it possible for rabbits to sleep inside or outside the borrow.
     * @param world The current world
     */
    @Override
    public void handleNight(World world) {
        super.handleNight(world);
        if (world.isOnTile(this)) {
            previousLocation = world.getLocation(this);
            Location curLocation = world.getLocation(this);
            Set<Location> surroundingTiles = world.getSurroundingTiles(curLocation);
            Set<Burrow> nearbyBurrow = world.getAll(Burrow.class, surroundingTiles);
            Object onBurrow = world.getNonBlocking(curLocation);

            if (!nearbyBurrow.isEmpty() || onBurrow instanceof Burrow) {
                world.remove(this);
                isInBurrow = true;
                if(energy <= 90) {
                    energy += 10;
                }
            }
        }
    }

    /**
     * This is the method we are using to handle day from the super class Animal.
     * The method does also implement the method of checkInBorrow.
     * @param world The current world
     */
    @Override
    public void handleDay(World world) {
        checkInBurrow(world);
    }

    /**
     * This is the method make the rabbits move random in the world.
     * @param world The current world
     */
    @Override
    public void moveRandomly(World world) {
        super.moveRandomly(world);
    }

    /**
     * This is the method is giving energy and removing grass when a rabbit eats it.
     * @param world The current world
     */
    @Override
    public void eat(World world) {
        super.eat(world);
        Location curLocation = world.getLocation(this);
        Object entity = world.getNonBlocking(curLocation);
        if(entity instanceof Grass) {
            energy += 5;
            world.delete(entity);
        }
    }

    /**
     * This method checks if rabbit is in burrow and reinstates them if so.
     * @param world The current world
     */
    public void checkInBurrow(World world) {
        if(isInBurrow) {
            if (previousLocation != null) {
                if(world.isTileEmpty(previousLocation)) {
                    world.setTile(previousLocation, this);
                }else {
                    Set<Location> emptyNeighbours = world.getEmptySurroundingTiles(previousLocation);
                    if (!emptyNeighbours.isEmpty()) {
                        List<Location> list = new ArrayList<>(emptyNeighbours);
                        Location location = list.get(PRNG.rand().nextInt(list.size()));
                        world.setTile(location, this);
                    }
                }
                previousLocation = null;
                isInBurrow = false;
            }
        }
    }

    /**
     * This is the method place the rabbit in the world.
     * @param world The current world
     */
    @Override
    public void placeInWorld(World world) {
        super.placeInWorld(world);
    }
}
