package itumulator.world;
import itumulator.executable.Program;
import itumulator.simulator.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class BabyRabbit implements Actor{
    private int life;
    private int energy;
    private int stepCount;
    private Location previousLocation;
    private boolean isSleeping;
    private boolean isInBurrow;

    public BabyRabbit() {
        this.life = 15; // babies start with 15 lives??
        this.energy = 100;
        this.stepCount = 0;
        this.previousLocation = null;
        this.isSleeping = false;
        this.isInBurrow = false;
    }

    @Override
    public void act(World world) {
        stepCount++;
        if (stepCount == 20) {
            stepCount = 0;
            life--;
        }

        if (life == 10){
            grow(world);
            System.out.println("Baby rabbit finally grew up!!!");
            return;
        }

        if (world.isNight()) {
            handleNight(world);
        } else {
            handleDay(world);
        }
        System.out.println("Baby Rabbit life: " + life);
        System.out.println("Baby Rabbit energy: " + energy);
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
                // if they've safely made it to burrow energy goes up by 10
                if(energy <= 90) {
                    energy += 10;
                }
                System.out.println("Baby rabbit entered a burrow at: " + previousLocation);
            }else {
                isSleeping = true;
                world.remove(this);
                SleepingBabyRabbit sleepingBabyRabbit = new SleepingBabyRabbit(curLocation);
                world.setTile(curLocation, sleepingBabyRabbit);
                System.out.println("ZZZzzz Baby rabbit is sleeping outside at: " + curLocation);
            }
        }
    }

    private void handleDay(World world) {
        if (isSleeping) {
            // Wake up from sleeping
            isSleeping = false;
            System.out.println("Baby rabbit woke up from sleeping.");

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

    private void eat(World world) {
        Location curLocation = world.getLocation(this);
        Object hasGrass = world.getNonBlocking(curLocation);
        if(hasGrass instanceof Grass) {
            if(energy <= 95) {
                energy += 5;
            }
            world.delete(hasGrass);
        }
    }

    private void grow(World world) {
        Location curLocation = world.getLocation(this);
        // Delete baby rabbit (this will remove it from both world and tile)
        world.delete(this);
        // Create new adult rabbit
        Rabbit adultRabbit = new Rabbit();
        // Add adult rabbit to world
        world.add(adultRabbit);
        // Place adult rabbit at the location
        world.setTile(curLocation, adultRabbit);
    }
}
