package itumulator.world;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;

import java.awt.*;
import java.util.*;
import java.util.List;


public class AdultRabbit extends Rabbit implements Actor, DynamicDisplayInformationProvider {
    public AdultRabbit() {
        life = 3;
    }

    @Override
    public DisplayInformation getInformation() {
        if(isNight){
            return new DisplayInformation(Color.BLACK, "rabbit-sleeping");
        } else {
            return new DisplayInformation(Color.GRAY, "rabbit-large");
        }
    }

    /**
     * This is the method we are using to handle day. It will make the rabbits wake up and reset their location.
     * They can now do normal daytime behavior.
     * @param world The current world.
     */
    @Override
    public void handleDay(World world) {
        super.handleDay(world);

        // Resume normal daytime behavior
        if (life > 0 && energy > 0) {
            energy--;
            super.moveRandomly(world);
            super.eat(world);
            tryToMate(world);
            digProbability(world);
        }
    }

    /**
     * This is the method we use for digging borrows, and it makes sure that a rabbit can only dig one borrow.
     * @param world The current world.
     */
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

    /**
     * This is the method we are using for matting so there will come baby rabbits.
     * @param world The current world.
     */
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
        Set<AdultRabbit> nearbyRabbits = world.getAll(AdultRabbit.class, surroundingTiles);

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
}
