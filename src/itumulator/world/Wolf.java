package itumulator.world;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Wolf implements Actor, DynamicDisplayInformationProvider {
    private int life;
    private int energy;
    private int stepCount;
    // Reference to the alpha wolf of this pack
    private Wolf alphaWolf;
    // Check whether this wolf is the alpha
    private boolean isAlphaWolf;
    // Wolves in this pack (only populated for the alpha wolf)
    private List<Wolf> pack;
    private boolean hasCave;
    private Cave myCave;
//    private Location previousLocation;
    private boolean isInCave;

    public Wolf(Wolf alphaWolf) {
        this.life = 10;
        this.energy = 100;
        this.stepCount = 0;
        this.alphaWolf = alphaWolf;
        this.myCave = null;
//        this.previousLocation = null;
        this.hasCave = false;
        this.isInCave = false;

        if (alphaWolf == null) {
            // If there is no alpha, this wolf becomes the alpha
            this.isAlphaWolf = true;
            // Initialize a new pack
            this.pack = new ArrayList<>();
            // Alpha includes itself in the pack
            this.pack.add(this);
        } else {
            // If there is already an alpha, join its pack
            this.isAlphaWolf = false;
            alphaWolf.addPackMember(this);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.GRAY, "wolf");
    }

    /**
     * This adds new wolves to this alpha wolf's pack.
     * Method only used by the alpha wolf.
     * @param wolf Takes in the current wolf
     */
    private void addPackMember(Wolf wolf) {
        if (isAlphaWolf) {
            pack.add(wolf);
        }
    }

    private void addCave(World world) {
        if (isAlphaWolf && !hasCave) {
            Location curLocation = world.getLocation(this);
            Set<Location> emptyTiles = world.getEmptySurroundingTiles(curLocation);
            if (!emptyTiles.isEmpty()) {
                List<Location> tilesList = new ArrayList<>(emptyTiles);
                Location caveLocation = tilesList.get(new Random().nextInt(tilesList.size()));

                Cave cave = new Cave();
                world.setTile(caveLocation, cave);

//                myCave = cave; // Assign the cave to this alpha wolf
//                hasCave = true;

                // Assign the same cave to all pack members
                for (Wolf wolf : pack) {
                    wolf.myCave = cave;
                    wolf.hasCave = true;
                }

                System.out.println("Cave created for alpha wolf and pack at: " + caveLocation);
            }
        }
    }


    /**
     * This method accounts for all the behavior of wolves
     * @param world The current world
     */
    @Override
    public void act(World world) {
        stepCount++;

        if (stepCount % 20 == 0) {
            life--;
        }

        if(world.isNight()) {
            handleNight(world);
        } else {
            handleDay(world);
            System.out.println("Wolf life: " + life);
            System.out.println("Wolf energy: " + energy);
        }

        addCave(world);
    }

    private void handleNight(World world) {
        if (isAlphaWolf) {

            if (myCave != null) {
                Location caveLocation = world.getLocation(myCave);

                if (caveLocation != null) {

                    // Move all wolves in the pack to the cave
                    for (Wolf wolf : pack) {
                        if (!wolf.isInCave) {
                            Location wolfCurLocation = world.getLocation(wolf);
                            Set<Location> surroundingTiles = world.getSurroundingTiles(caveLocation);
                            Set<Location> emptySurroundingTiles = world.getEmptySurroundingTiles(caveLocation);
                            if (!surroundingTiles.contains(wolfCurLocation)) {
                                if (!emptySurroundingTiles.isEmpty()) {
                                    List<Location> list = new ArrayList<>(emptySurroundingTiles);
                                    Location nextStep = list.get(new Random().nextInt(list.size()));
                                    world.move(wolf, nextStep);
                                }
                            }
                            else if (surroundingTiles.contains(wolfCurLocation)) {
                                world.remove(wolf);
                                wolf.isInCave = true;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @param world
     */
    private void handleDay(World world) {
        // Only proceed if the wolf is in the cave
        if (isInCave) {
            if (myCave != null) {
                // Find the cave's location
                Location caveLocation = world.getLocation(myCave);
                if (caveLocation != null) {
                    // Get all empty surrounding tiles around the cave
                    Set<Location> emptyTiles = world.getEmptySurroundingTiles(caveLocation);
                    if (!emptyTiles.isEmpty()) {
                        // Randomly pick one of the empty tiles
                        List<Location> list = new ArrayList<>(emptyTiles);

                        for (Wolf wolf : pack) {
                            int randomChoice = new Random().nextInt(list.size());
                            Location newLocation = list.get(randomChoice);
                            list.remove(randomChoice);
                            world.setTile(newLocation, wolf);
                        }
                    }
                }
            }
            isInCave = false;
        }

        if (life > 0 && energy > 0) {
            if (isAlphaWolf) {
                moveRandomly(world);
                energy--;
            } else {
                followAlpha(world);
                energy--;
            }
        }
        eat(world);
        hasDied(world);
    }

    /**
     * This method determines the probability of eating a rabbit.
     * Replenishes energy.
     * @param world The current world
     */
    private void eat(World world) {
        Location curLocation = world.getLocation(this);
        Set<Location> surroundingTiles = world.getSurroundingTiles(curLocation);

        if (!surroundingTiles.isEmpty()) {
            Location rabbitLocation = surroundingTiles.iterator().next();
            Object isRabbit = world.getTile(rabbitLocation);
            double eatProbability = 0.7;
            double reducedEatProbability = 0.5;
            if (isRabbit instanceof AdultRabbit) {
                // 70% chance of eating a rabbit whilst having energy
                if(energy > 0 && Math.random() <= eatProbability) {
                    if (energy <= 90) {
                        energy += 10;
                    }
                    world.delete(isRabbit);
                    Carcass carcass = new Carcass();
                    world.setTile(rabbitLocation, carcass);
                    System.out.println("Ate a poor Rabbit - New energy level:" + energy);
                }
                if(energy <= 0 && Math.random() <= reducedEatProbability) {
                    // If no energy then gain more energy from eating but chances of eating are reduced
                    energy += 20;
                    world.delete(isRabbit);
                    Carcass carcass = new Carcass();
                    world.setTile(rabbitLocation, carcass);
                    System.out.println("Ate a poor Rabbit - New energy level:" + energy + ". Rejuvenated!");
                }
            }
        }
    }

    /**
     * This method kills off wolves when their life is equal to 0
     * @param world The current world
     */
    private void hasDied(World world) {
        if (life <= 0) {
            world.delete(this);
            System.out.println("A Wolf has died.");
        }
    }

    /**
     * This method moves the alpha to a random nearby empty tile.
     * @param world The current world.
     */
    private void moveRandomly(World world) {
        Location curLocation = world.getLocation(this);
        Set<Location> neighbours = world.getEmptySurroundingTiles(curLocation);

        if (!neighbours.isEmpty()) {
            Random rand = new Random();
            List<Location> list = new ArrayList<>(neighbours);
            Location newLocation = list.get(rand.nextInt(list.size()));
            world.move(this, newLocation);
            System.out.println("Alpha wolf moved to: " + newLocation);
        }
    }

    /**
     * This method moves the alpha's pack wolves in accordance to the alphas location, always following it.
     * Only used by non-alpha wolves.
     * @param world The current world.
     */
    private void followAlpha(World world) {
        if (alphaWolf != null && world.contains(alphaWolf)) {
            Location alphaLocation = world.getLocation(alphaWolf);
            Set<Location> emptyTiles = world.getEmptySurroundingTiles(alphaLocation);

            if (!emptyTiles.isEmpty()) {
                List<Location> list = new ArrayList<>(emptyTiles);
                Random rand = new Random();
                Location newLocation = list.get(rand.nextInt(list.size()));
                world.move(this, newLocation);
            }
        }
    }

    /**
     * This method places the wolves based on if they're the alpha, or pack wolves.
     * @param world The current world.
     */
    public void placeInWorld(World world) {
        // Places alpha wolf in the world
        if (isAlphaWolf) {
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

        } else {
            // Places pack wolves in the world based on alpha's location
            Location alphaLocation = world.getLocation(alphaWolf);
            Set<Location> nearbyTiles = world.getEmptySurroundingTiles(alphaLocation);

            if (!nearbyTiles.isEmpty()) {
                List<Location> list = new ArrayList<>(nearbyTiles);
                Location newLocation = list.get(new Random().nextInt(list.size()));
                world.setTile(newLocation, this);
                System.out.println("Wolf placed near pack at: " + newLocation);
            }
        }
    }
}