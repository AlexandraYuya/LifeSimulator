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
        this.life = 5; // reduced to 5 life
        this.energy = 100;
        this.stepCount = 0;
        this.previousLocation = null;
        this.isSleeping = false;
        this.isInBurrow = false;
    }

    /**
     * It will make the baby rabbit grow after 5 days, and it calls two different methods one for night and one for day
     * @param world The current world.
     */
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
    /**
     * This is the method we use for handling night. If differentiate between sleeping in a borrow or outside.
     * If they sleep in a borrow they will get 10 energy.
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
                // if they've safely made it to burrow energy goes up by 10
                if(energy <= 90) {
                    energy += 10;
                }
                System.out.println("Baby rabbit entered a burrow at: " + previousLocation);
            }else {
                isSleeping = true;
                world.delete(this);
                SleepingBabyRabbit sleepingBabyRabbit = new SleepingBabyRabbit(curLocation);
                world.setTile(curLocation, sleepingBabyRabbit);
                System.out.println("ZZZzzz Baby rabbit is sleeping outside at: " + curLocation);
            }
        }
    }

    /**
     * This is the method we use for handling day. The bay rabbit will wake up and make them do normal day behavior.
     * @param world The current world.
     */
    private void handleDay(World world) {
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

    /**
     * This is the method makes the baby rabbits move random.
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
     * This is the method let the baby rabbit eat while getting 5 energy from it and the grass will be deleted.
     * @param world The current world.
     */
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
    /**
     * This is the method will grow the baby rabbit to an adult rabbit.
     * @param world The current world.
     */
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
