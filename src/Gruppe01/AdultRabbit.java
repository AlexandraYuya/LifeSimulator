package Gruppe01;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.*;
import java.util.List;


public class AdultRabbit extends Rabbit implements Actor, DynamicDisplayInformationProvider, PRNG {
    public AdultRabbit() {
        life = 3;
    }


    /**
     * This method give the rabbit a sleeping png or an awake png.
     * If the Rabbit is a sleep it will get a sleeping rabbit png.
     * If it was awake it will get an awake rabbit.
     */
    @Override
    public DisplayInformation getInformation() {
        if(isNight){
            return new DisplayInformation(Color.BLACK, "rabbit-sleeping");
        } else {
            return new DisplayInformation(Color.GRAY, "rabbit-large");
        }
    }

    /**
     * This is the method we are using to handle day.
     * The method implement normal day behavior for rabbits such as move random, eat, mating and dig borrows.
     * handleDay, moveRandom and eat all comes from the super class Rabbit.
     * @param world The current world
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
     * This method is for digging borrows.
     * It makes sure that each rabbit only digs one borrow.
     * @param world The current world
     */
    private void digProbability(World world) {
        // only 1 burrow can be dug per rabbit
        if(hasDugBurrow) {
            return;
        }
        // 30% chance of digging
        double chance = PRNG.rand().nextDouble();
        if (chance < 0.3) {
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
     * This is the method is used for reproducing more baby rabbits.
     * @param world The current world
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
            double chance = PRNG.rand().nextDouble();
            // 40% chance (0.4 = 40%)
            if (chance <= 0.4) {
                // Look for empty tile to place baby
                Set<Location> emptyTiles = world.getEmptySurroundingTiles(currentLocation);
                if (!emptyTiles.isEmpty()) {
                    // Pick a random empty tile for the baby
                    List<Location> tilesList = new ArrayList<>(emptyTiles);
                    Location babyLocation = tilesList.get(PRNG.rand().nextInt(tilesList.size()));

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
    public void setLife(int life) {
        this.life = life;
    }
}
