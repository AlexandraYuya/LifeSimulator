package itumulator.world;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Wolf extends Animal implements Actor, DynamicDisplayInformationProvider, PRNG {
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
        super(10, 100, false);
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
     * This will display a wolf png.
     * @return wolf
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

    /**
     * This method adds a cave on an empty tile in the world.
     * @param world The current world
     */
    public void addCave(World world) {
            Location curLocation = world.getLocation(this);
            Set<Location> emptyTiles = world.getEmptySurroundingTiles(curLocation);
            if (!emptyTiles.isEmpty()) {
                List<Location> tilesList = new ArrayList<>(emptyTiles);
                Location caveLocation = tilesList.get(PRNG.rand().nextInt(tilesList.size()));

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
     * It uses the superclass animal.
     * @param world The current world
     */
    @Override
    public void act(World world) {
        super.act(world);

        if (this instanceof BabyWolf) {
            if(life == 10) {
                ((BabyWolf) this).grow(world);
                System.out.println("Baby wolf finally grew up!!!");
            }
        }
    }

    /**
     * The method handle the night of the wolves.
     * If moves all the wolves into a cave for sleeping.
     * It uses the superclass animal.
     * @param world The Current world
     */
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
                                    int random_choice = PRNG.rand().nextInt(list.size());
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
     * This method handle normal day behavior.
     * If the wolves is in the cave then is will try to mate, but it will decrease their energy.
     * If the wolves is outside the cave the wolves will follow the alpha wolf.
     * The alpha wolf will walk random around.
     * It uses the superclass animal.
     * @param world The current world
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
                            int randomChoice = PRNG.rand().nextInt(list.size());
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
            } else {
                followAlpha(world);
            }
            energy--;
        }
        eat(world);
    }

    /**
     * This method determines the probability of eating a rabbit or a carcass.
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
            Object entity = world.getTile(rabbitLocation);
            if (entity instanceof AdultRabbit) {
                double chance = PRNG.rand().nextDouble();
                // 70% chance of eating a rabbit whilst having energy
                if(energy > 0 && chance <= 0.7) {
                    world.delete(entity);
                    Carcass carcass = new Carcass(isSmall);
                    carcass.isSmall();
                    world.setTile(rabbitLocation, carcass);
                    System.out.println("Wolf ate a poor Rabbit - New energy level:" + energy);
                }
            }
            if (entity instanceof Carcass) { //here checked if it is a carcassFungi or carcass
                energy += 5;
                System.out.println("Wolf ate a bit of a Carcass - New energy level:" + energy);
                ((Carcass) entity).eatCarcass(world);
            }
        }
    }

    /**
     * This method moves the alpha to a random nearby empty tile.
     * @param world The current world
     */
    @Override
    public void moveRandomly(World world) {
        super.moveRandomly(world);
    }

    /**
     * This method moves the alpha's pack wolves in accordance to the alphas location, always following it.
     * Only used by non-alpha wolves.
     * @param world The current world
     */
    private void followAlpha(World world) {
        if (alphaWolf != null && world.contains(alphaWolf)) {
            Location alphaLocation = world.getLocation(alphaWolf);
            Set<Location> emptyTiles = world.getEmptySurroundingTiles(alphaLocation);

            if (!emptyTiles.isEmpty()) {
                List<Location> list = new ArrayList<>(emptyTiles);
                Location newLocation = list.get(PRNG.rand().nextInt(list.size()));
                world.move(this, newLocation);
            }
        }
    }

    /**
     * This is the method we are using for reproducing wolves.
     * @param world The current world.
     */
    private void tryToMate(World world) {
        // Don't try to mate if energy is too low
        if (energy < 20) {
            return;
        }

        // Only proceed if there are at least two wolves in the pack (hermaphrodite wolves)
        if (pack.size() > 1) {
            // 30% chance to create a baby
            // Returns a value between 0.0 and 1.0
            double chance = PRNG.rand().nextDouble();

            if(chance < 0.3) {
                // Create a baby
                BabyWolf baby = new BabyWolf(this.alphaWolf);
                world.add(baby);

                // Decrease energy after successful reproduction
                energy -= 20;
                System.out.println("+++++++++++A baby wolf was born! Parent energy now: " + energy);
            }
        }
    }

    /**
     * This method places the wolves based on if they're the alpha, or pack wolves.
     * @param world The current world
     */
    @Override
    public void placeInWorld(World world) {
        // Places alpha wolf in the world
        if (isAlphaWolf) {
            super.placeInWorld(world);

        } else {
            // Places pack wolves in the world based on alpha's location
            Location alphaLocation = world.getLocation(alphaWolf);
            Set<Location> nearbyTiles = world.getEmptySurroundingTiles(alphaLocation);

            if (!nearbyTiles.isEmpty()) {
                List<Location> list = new ArrayList<>(nearbyTiles);
                Location newLocation = list.get(PRNG.rand().nextInt(list.size()));
                world.setTile(newLocation, this);
                this.location = newLocation;
                System.out.println("Wolf placed near pack at: " + newLocation);
            } else {
                super.placeInWorld(world);
            }
        }
    }
}