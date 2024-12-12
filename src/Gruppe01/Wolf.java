package Gruppe01;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Wolf extends Animal implements Actor, DynamicDisplayInformationProvider {
    protected Wolf alphaWolf;
    protected boolean isAlphaWolf;
    protected List<Wolf> pack;
    protected boolean hasCave;
    protected Cave myCave;
    protected boolean isInCave;
    protected boolean hasBeenAttacked = false;
    protected static final List<List<Wolf>> wolfNet = new ArrayList<>();

    public Wolf(Wolf alphaWolf) {
        super(10, 100, false, 15);
        this.alphaWolf = alphaWolf;
        this.myCave = null;
        this.hasCave = false;
        this.isInCave = false;

        if (alphaWolf == null) {
            this.alphaWolf = this;
            this.isAlphaWolf = true;
            this.pack = new ArrayList<>();
            this.pack.add(this);
            if(!wolfNet.contains(this.pack)) {
                wolfNet.add(this.pack);
            }
        } else {
            this.isAlphaWolf = false;
            this.myCave = this.alphaWolf.myCave;
            this.hasCave = this.alphaWolf.hasCave;
            this.pack = this.alphaWolf.pack;
            this.isInCave = this.alphaWolf.isInCave;

            alphaWolf.addPackMember(this);
        }
    }

    /**
     * This is get method for pack.
     */
    public List<Wolf> getPack() {
        return pack;
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
     * This adds new wolves to the alpha wolf's pack. The method only used by the alpha wolf.
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

            } else {
                System.exit(1);
            }
        }
    /**
     * This method accounts for all the behavior of wolves. It uses the superclass animal.
     * @param world The current world
     */
    @Override
    public void act(World world) {
        super.act(world);

        if (this instanceof BabyWolf) {
            if(life == 10) {
                ((BabyWolf) this).grow(world);
            }
        }
    }

    /**
     * The method handle the night of the wolves. It moves all the wolves into a cave for sleeping.
     * It uses the superclass animal.
     * @param world The Current world
     */
    @Override
    public void handleNight(World world) {
        super.handleNight(world);
        if(this.hasBeenAttacked) {
            this.hasBeenAttacked = false;
        }

        if (isAlphaWolf) {

            if (myCave != null) {
                Location caveLocation = world.getLocation(myCave);

                if (caveLocation != null) {

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
                System.exit(1);
            }
        }
    }

    /**
     * This method handles normal day behavior.
     * If the wolves are in the cave, they will try to mate, which will decrease their energy.
     * If the wolves are outside the cave, they will follow the alpha wolf.
     * The alpha wolf walks randomly around.
     * It uses the superclass animal.
     * @param world The current world
     */
    @Override
    public void handleDay(World world) {
        super.handleDay(world);
        if (isInCave) {

            if (life > 0 && energy >= 20) {
                tryToMate(world);
            }

            if (myCave != null) {

                Location caveLocation = world.getLocation(myCave);
                if (caveLocation != null) {
                    Set<Location> emptyTiles = world.getEmptySurroundingTiles(caveLocation);
                    if (!emptyTiles.isEmpty()) {
                        List<Location> list = new ArrayList<>(emptyTiles);
                        for (Wolf wolf : pack) {
                            if (list.size() <= 0) {
                                System.exit(1);
                            }
                            int randomChoice = PRNG.rand().nextInt(list.size());
                            Location newLocation = list.get(randomChoice);
                            world.setTile(newLocation, wolf);
                            list.remove(randomChoice);
                            wolf.isInCave = false;
                        }
                    } else {
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
            eat(world);
            fight(world);
            energy--;
        }
    }

    /**
     * This method checks to see if other wolf packs are nearby, if so they fight.
     * Wolves can only fight and be fought once per day.
     * @param world The current world
     */
    private void fight(World world) {
        for(List<Wolf> pack : wolfNet) {
            if(this.pack == pack) {
                continue;
            }

            if(pack.isEmpty()) {
                wolfNet.remove(pack);
                continue;
            }

            for(Wolf wolfInPack : pack) {
                if(wolfInPack.hasBeenAttacked) {
                    continue;
                }

                Location wolfLoc = world.getLocation(this);
                Set<Location> surroundingTiles = world.getSurroundingTiles(wolfLoc);
                List<Location> list = new ArrayList<>(surroundingTiles);
                for(Location loc : list) {
                    Object entity = world.getTile(loc);
                    if(entity == wolfInPack && !(entity instanceof BabyWolf)) {
                        double chance = PRNG.rand().nextDouble();
                        if(chance < 0.5) {
                            wolfInPack.life -= 3;
                            wolfInPack.energy -= 20;
                            wolfInPack.hasBeenAttacked = true;
                        }
                    }
                }
            }
        }
    }

    /**
     * This method determines the probability of killing a rabbit and eating a carcass.
     * Replenishes energy.
     * @param world The current world
     */
    @Override
    public void eat(World world) {
        super.eat(world);
        Location curLocation = world.getLocation(this);
        Set<Location> surroundingTiles = world.getSurroundingTiles(curLocation);

        if (!surroundingTiles.isEmpty()) {
            for(Location nearbyLocation : surroundingTiles) {
                Object entity = world.getTile(nearbyLocation);
                double chance = PRNG.rand().nextDouble();

                if (entity instanceof AdultRabbit && chance <= 0.8) {
                    world.delete(entity);
                    Carcass carcass = new Carcass(isSmall, 10);
                    world.setTile(nearbyLocation, carcass);
                    break;
                }
                if (entity instanceof Carcass) {
                    energy += 5;
                    ((Carcass) entity).eatCarcass(world);
                    break;
                }
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
     * This is the method is used for reproducing wolves.
     * @param world The current world.
     */
    private void tryToMate(World world) {
        if (energy < 20) {
            return;
        }

        if (pack.size() > 1) {
            double chance = PRNG.rand().nextDouble();

            if(chance < 0.3) {
                BabyWolf baby = new BabyWolf(alphaWolf);
                world.add(baby);

                energy -= 20;
            }
        }
    }

    /**
     * This method places the wolves based on if they're an alpha, or pack wolves.
     * @param world The current world
     */
    @Override
    public void placeInWorld(World world) {
        if (isAlphaWolf) {
            super.placeInWorld(world);

        } else {
            Location alphaLocation = world.getLocation(alphaWolf);
            Set<Location> nearbyTiles = world.getEmptySurroundingTiles(alphaLocation);

            if (!nearbyTiles.isEmpty()) {
                List<Location> list = new ArrayList<>(nearbyTiles);
                Location newLocation = list.get(PRNG.rand().nextInt(list.size()));
                world.setTile(newLocation, this);
                this.location = newLocation;
            } else {
                super.placeInWorld(world);
            }
        }
    }
}