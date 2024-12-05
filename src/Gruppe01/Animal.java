package Gruppe01;

import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Animal implements PRNG {
    protected int life;
    protected int energy;
    protected int stepCount;
    protected Location location;
    protected boolean isNight = false;
    protected boolean isSmall;
    protected int amount;

    public Animal(int life, int energy, boolean isSmall, int amount) {
        this.life = life;
        this.energy = energy;
        this.isSmall = isSmall;
        this.amount = amount;
    }

    /**
     * This method accounts for all the behavior of all the animals in the world.
     * The method accounts for their life, energy and if it is night or day.
     * If it is night it will turn on the handleNight.
     * If it is not night it will turn on handle day.
     * @param world The current world
     */
    public void act(World world) {
        stepCount++;
        if (stepCount % 20 == 0) {
            life--;
            System.out.println(this +" Life: "+ life + " Energy: " + energy);
        }

        if (world.isNight()) {
            isNight = true;
            handleNight(world);
        } else {
            isNight = false;
            if(!die(world)) {
                handleDay(world);
            }
        }
    }

    /**
     * This method handle night for animals.
     * @param world The current world
     */
    public void handleNight(World world) {}

    /**
     * This method handle day for animals.
     * @param world The current world
     */
    public void handleDay(World world) {}

    /**
     * This method accounts for all the dying animals and replace it with a carcass.
     * Now it is only for wolves.
     * @param world The current world
     */
    public boolean die(World world) {
        if(life <= 0 && world.isOnTile(this)) {

            if (this instanceof Wolf) {
                Wolf wolf = (Wolf) this;
                wolf.pack.remove(wolf);

                if(wolf.isAlphaWolf) {
                    System.out.println("Alpha Wolf has DIIEDD, promoting noob");
                    if(!wolf.pack.isEmpty()) {
                        Wolf newAlpha = wolf.pack.get(0);
                        newAlpha.isAlphaWolf = true;
                        for(Wolf w : wolf.pack) {
                            w.alphaWolf = newAlpha;
                        }
                    }
                }
            }

            Location curLocation = world.getLocation(this);
            Carcass carcass = new Carcass(isSmall, amount);
            world.delete(this);
            world.setTile(curLocation, carcass);
            System.out.println(this + "has Died and turned into a carcass!!!!");
            return true;
        }
        return false;
    }

    /**
     * This method accounts for the animals moving in random directions.
     * @param world The current world
     */
    public void moveRandomly(World world) {
        if (world.isOnTile(this)) {
            if (energy >= 0 && life >= 0) {
                Location curLocation = world.getLocation(this);
                Set<Location> neighbours = world.getEmptySurroundingTiles(curLocation);

                if (!neighbours.isEmpty()) {
                    List<Location> list = new ArrayList<>(neighbours);
                    Location newLocation = list.get(PRNG.rand().nextInt(list.size()));
                    world.move(this, newLocation);
                }
            }
        }
    }

    /**
     * This method accounts for animals eating.
     * @param world The current world
     */
    public void eat(World world) {
    }

    /**
     * This method places the animals in the world.
     * Is uses the PRNG interface.
     * @param world The current world
     */
    public void placeInWorld(World world) {
        int size = world.getSize();
        location = null;

        while (location == null || world.getTile(location) != null) {
            int x = PRNG.rand().nextInt(size);
            int y = PRNG.rand().nextInt(size);
            location = new Location(x, y);
        }

        if (!world.containsNonBlocking(location)) {
            world.setTile(location, this);
        }
    }
}
