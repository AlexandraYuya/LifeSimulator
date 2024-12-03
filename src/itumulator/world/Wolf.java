package itumulator.world;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Wolf extends Animal implements Actor, DynamicDisplayInformationProvider {
    // Reference to the alpha wolf of this pack
    public Wolf alphaWolf;
    // Check whether this wolf is the alpha
    private boolean isAlphaWolf;
    // Wolves in this pack (only populated for the alpha wolf)
    public List<Wolf> pack;
    private boolean hasCave;
    private Cave myCave;
    private boolean isInCave;

    public Wolf(Wolf alphaWolf) {
        super(1, 100);
        this.alphaWolf = alphaWolf;
        this.myCave = null;
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
            this.myCave = this.alphaWolf.myCave;
            this.hasCave = this.alphaWolf.hasCave;
            this.pack = this.alphaWolf.pack;
            this.isInCave = this.alphaWolf.isInCave;

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

    public void addCave(World world) {
        Location curLocation = world.getLocation(this);
        Set<Location> emptyTiles = world.getEmptySurroundingTiles(curLocation);
        if (!emptyTiles.isEmpty()) {
            List<Location> tilesList = new ArrayList<>(emptyTiles);
            Location caveLocation = tilesList.get(new Random().nextInt(tilesList.size()));

            Cave cave = new Cave();
            world.setTile(caveLocation, cave);
            this.myCave = cave;
            this.hasCave = true;

            System.out.println("Cave created for alpha wolf and pack at: " + caveLocation);
        } else {
            System.out.println("FATAL!!! No empty tiles found");
            System.exit(1);
        }
    }

    /**
     * This method accounts for all the behavior of wolves
     * @param world The current world
     */
    @Override
    public void act(World world) {
        super.act(world);

            if (world.isNight()) {
                handleNight(world);
            } else {
                handleDay(world);
                super.die(world);
                System.out.println(this + " life: " + life);
                System.out.println(this + " energy: " + energy);
            }

        if (this instanceof BabyWolf) {
//            if (life == 10 && stepCount == 20){
            // TODO: FIX THIS THRESHOLD ISSUE
            if(stepCount > 20) {
                ((BabyWolf) this).grow(world);
                System.out.println("Baby wolf finally grew up!!!");
            }
        }
    }

    @Override
    public void handleNight(World world) {
        super.handleNight(world);
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
                                    int random_choice = new Random().nextInt(list.size());
                                    Location nextStep = list.get(random_choice);
                                    world.move(wolf, nextStep);
                                    list.remove(random_choice);
                                }
                            }
                            else if (surroundingTiles.contains(wolfCurLocation)) {
                                world.remove(wolf);
                                wolf.isInCave = true;
                            }
                        }
                    }
                }
            } else {
                System.out.println("FATAL!!! Wolf has no cave associated with it!");
                System.exit(1);
            }
        }
    }

    /**
     *
     * @param world
     */
    @Override
    public void handleDay(World world) {
        super.handleDay(world);
        // Only proceed if the wolf is in the cave
        if (isInCave) {

            if (life > 0 && energy >= 20) {
                // NOTE: THIS DECREASES ENERGY!!!
                tryToMate(world);
            }

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
                            if (list.size() <= 0) {
                                System.out.println("FATAL ERROR! NO MORE FREE SPACE TO MOVE TO IN handleDay wolf");
                                System.exit(1);
                            }
                            int randomChoice = new Random().nextInt(list.size());
                            Location newLocation = list.get(randomChoice);
                            world.setTile(newLocation, wolf);
                            list.remove(randomChoice);
                            wolf.isInCave = false;
                        }
                    } else {
                        System.out.println(" No space for wolf... \uD83E\uDD14");
                        System.exit(1);
                    }
                }
            }
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
    }

    /**
     * This method determines the probability of eating a rabbit.
     * Replenishes energy.
     * @param world The current world
     */
    @Override
    public void eat(World world) {
        super.eat(world);
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
     * This method moves the alpha to a random nearby empty tile.
     * @param world The current world.
     */
    @Override
    public void moveRandomly(World world) {
        super.moveRandomly(world);
    }

    /**
     * This method moves the alpha's pack wolves in accordance to the alphas location, always following it.
     * Only used by non-alpha wolves.
     * @param world The current world.
     */
    private void followAlpha(World world) {
        if (alphaWolf != null && world.contains(alphaWolf)) {
//            System.out.println("Contains alpha: " + world.contains(alphaWolf));
//            System.out.println("followAlphaWolf: " + alphaWolf);
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
     * This is the method we are using for matting so there will come baby rabbits.
     * @param world The current world.
     */
    private void tryToMate(World world) {
        // Here we check if rabbit has enough energy to reproduce (as they go -20 if they do)
        // Don't try to mate if energy is too low
        if (energy < 20) {
            return;
        }

        // Only proceed if there are at least two wolves in the pack (hermaphrodite wolves)
        if (pack.size() >= 2) {
            // 30% chance to create a baby
            // Returns a value between 0.0 and 1.0
            double chance = new Random().nextDouble();

            // 30% chance (0.3 = 30%)
            if(chance <= 0.3) {
                // Create a baby
                BabyWolf baby = new BabyWolf(this.alphaWolf);
                world.add(baby);

                // Decrease energy after successful reproduction
                // TODO: FIX THIS ENERGY ISSUE!!!!
                energy -= 20;
                System.out.println("++++++++++++++++++++++++++++++++++++++++++A baby wolf was born! Parent energy now: " + energy);
            }
        }
    }

    /**
     * This method places the wolves based on if they're the alpha, or pack wolves.
     * @param world The current world.
     */
    @Override
    public void placeInWorld(World world) {
        // Places alpha wolf in the world
        if (isAlphaWolf) {
            super.placeInWorld(world);
                
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