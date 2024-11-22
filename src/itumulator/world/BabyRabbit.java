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
        if (world.isNight()) {
            handleNight(world);
        } else {
            handleDay(world);
        }




        if (this.life == 10){
            grow(world);
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

    public void grow(World world) {
        Location currentLocation = world.getLocation(this);
        // Create new adult rabbit
        Rabbit adultRabbit = new Rabbit();
        // Delete baby rabbit (this will remove it from both world and tile)
        world.delete(this);
        // Add adult rabbit to world
        world.add(adultRabbit);
        // Place adult rabbit at the location
        world.setTile(currentLocation, adultRabbit);
    }
}
