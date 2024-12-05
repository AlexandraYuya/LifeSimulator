package itumulator.world;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Bear extends Animal implements Actor, DynamicDisplayInformationProvider, PRNG {
    private int radius;
    private Location startingPoint;


    public Bear() {
        super(10,100, false);
        this.radius = 2;
    }

    /**
     * This method give the Bear a sleeping png or an awake png.
     * If the Bear is a sleep it will get a sleeping bear png.
     * If it was awake it will get an awake bear.
     */
    @Override
    public DisplayInformation getInformation() {
        if(isNight){
            return new DisplayInformation(Color.ORANGE, "bear-sleeping");
        } else {
            return new DisplayInformation(Color.ORANGE, "bear");
        }
    }

    /**
     * This method accounts for all the behavior of bear in the superclass animal.
     * @param world The current world
     */
    @Override
    public void act(World world) {
        super.act(world);
    }

    /**
     * This is the method is used for handle day from the super class animal.
     * It makes them do normal daytime behavior.
     * @param world The current world.
     */
    @Override
    public void handleDay(World world) {
        super.handleDay(world);
        // Resume normal daytime behavior
        if (life > 0 && energy > 0) {
            energy--;
            eat(world);
            moveInCircRandomly(world);
        }
    }

    /**
     *This method makes the bear walk random in a circular space within the radius of 2.
     * @param world The current world.
     */
    private void moveInCircRandomly(World world) {
        if(energy > 0) {
            Location curLocation = world.getLocation(this);
            Set<Location> neighbours = world.getEmptySurroundingTiles(curLocation);
            Set<Location> limitedNeighbours = new HashSet<>();

            for (Location loc : neighbours) {
                if(isWithinRadius(startingPoint, loc, radius)){
                    limitedNeighbours.add(loc);
                }
            }

            if (!limitedNeighbours.isEmpty()) {
                List<Location> list = new ArrayList<>(limitedNeighbours);
                Location newLocation = list.get(PRNG.rand().nextInt(list.size()));
                world.move(this, newLocation);
            }
        }
    }

    /**
     * This method is used for checking if the bear is within the allowed radius.
     * @param center target radius
     * @return (dx * dx + dy * dy) <= (radius * radius)
     */
    private boolean isWithinRadius(Location center, Location target, int radius) {
        int dx = center.getX() - target.getX();
        int dy = center.getY() - target.getY();
        return (dx * dx + dy * dy) <= (radius * radius);
    }

    /**
     * This method makes it possible for the bears to eat rabbits, berries and carcasses.
     * @param world The current world.
     */
    @Override
    public void eat(World world) {
        super.eat(world);
        Location curLocation = world.getLocation(this);
        Set<Location> surroundingTiles = world.getSurroundingTiles(curLocation);

        if (!surroundingTiles.isEmpty()) {
            for (Location nearbyLocation : surroundingTiles) {  // Loop through all surrounding tiles
                Object entity = world.getTile(nearbyLocation);
                double chance = PRNG.rand().nextDouble();

                if (entity instanceof AdultRabbit && chance <= 0.7) { //here checked if it is a rabbit and adds 70% chance
                    System.out.println("Bear Ate a poor Rabbit - New energy level:" + energy);
                    world.delete(entity);
                    Carcass carcass = new Carcass(isSmall);
                    carcass.isSmall();
                    world.setTile(nearbyLocation, carcass);
                    break;
                }
                if (entity instanceof Berry && chance <= 0.9 && !(entity instanceof Bush)) { //here checked if it is a berry and adds 90% chance
                    energy += 5;
                    System.out.println("Bear Ate some berries - New energy level:" + energy);
                    ((Berry) entity).consumeBerries(world);
                    break;
                }
                if (entity instanceof Carcass) { //here checked if it is a carcassFungi or carcass
                    energy += 5;
                    System.out.println("Bear Ate a bit of a carcass - New energy level:" + energy);
                    ((Carcass) entity).eatCarcass(world);
                    break;
                }
            }
        }
    }

        /**
         * This is the method place the Bear in the world.
         * @param world The current world.
         */
        @Override
        public void placeInWorld (World world){
            super.placeInWorld(world);
            startingPoint = location;
        }
        public void placeInWorld (World world,int x, int y){
            Location location = new Location(x, y);
            if (!world.containsNonBlocking(location)) {
                world.setTile(location, this);
                startingPoint = location;
            }
        }
    }

