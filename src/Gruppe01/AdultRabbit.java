package Gruppe01;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import java.awt.*;
import java.util.*;
import java.util.List;


public class AdultRabbit extends Rabbit implements Actor, DynamicDisplayInformationProvider {

    public AdultRabbit() {
        life = 3;
    }

    /**
     * Sets the life, used for Junit accessibility.
     * @param life AdultRabbit
     */
    public void setLife(int life) {
        this.life = life;
    }

    /**
     * This method sets the rabbits png based on certain conditions.
     * @return DisplayInformation
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
     * Normal day behavior for rabbits(move random, eat, mating and dig borrows).
     * @param world The current world
     */
    @Override
    public void handleDay(World world) {
        super.handleDay(world);
        if (life > 0 && energy > 0) {
            energy--;
            super.moveRandomly(world);
            super.eat(world);
            tryToMate(world);
            digProbability(world);
        }
    }

    /**
     * The method makes sure that each rabbit only digs one borrow.
     * @param world The current world
     */
    private void digProbability(World world) {
        if(hasDugBurrow) {
            return;
        }
        double chance = PRNG.rand().nextDouble();
        if (chance < 0.3) {
            Location curLocation = world.getLocation(this);
            if (!world.containsNonBlocking(curLocation)) {
                Burrow burrow = new Burrow();
                world.setTile(curLocation, burrow);
                this.myBurrow = burrow;
                burrow.setOwner(this);
                hasDugBurrow = true;
            }
        }
    }

    /**
     * This is the method is used for reproducing more baby rabbits.
     * @param world The current world
     */
    private void tryToMate(World world) {
        if (energy < 20) {
            return;
        }
        Location currentLocation = world.getLocation(this);
        Set<Location> surroundingTiles = world.getSurroundingTiles(currentLocation);
        Set<AdultRabbit> nearbyRabbits = world.getAll(AdultRabbit.class, surroundingTiles);

        if (!nearbyRabbits.isEmpty()) {
            double chance = PRNG.rand().nextDouble();
            if (chance <= 0.4) {
                Set<Location> emptyTiles = world.getEmptySurroundingTiles(currentLocation);
                if (!emptyTiles.isEmpty()) {
                    List<Location> tilesList = new ArrayList<>(emptyTiles);
                    Location babyLocation = tilesList.get(PRNG.rand().nextInt(tilesList.size()));

                    BabyRabbit baby = new BabyRabbit();
                    world.add(baby);
                    world.setTile(babyLocation, baby);

                    energy -= 20;
                }
            }
        }
    }
}
