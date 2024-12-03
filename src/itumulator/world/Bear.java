package itumulator.world;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Bear extends Animal implements Actor, DynamicDisplayInformationProvider{
    private int radius;
    private Location startingPoint;
    private boolean isNight = false;


    public Bear() {
        super(10,100);
        this.radius = 2;
    }

    @Override
    public DisplayInformation getInformation() {
        if(isNight){
            return new DisplayInformation(Color.ORANGE, "bear-sleeping");
        } else {
            return new DisplayInformation(Color.ORANGE, "bear");
        }
    }

    @Override
    public void act(World world) {
        super.act(world);
            if (world.isNight()) {
                isNight = true;
            } else {
                isNight = false;
                handleDay(world);
                System.out.println("Bear life: " + life);
                System.out.println("Bear energy: " + energy);
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
            eat(world);
            moveInCircRandomly(world);
        }
    }

    /**
     *
     * @param world The current world.
     */
    // Move random in a circular space with radius 2
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
                Random rand = new Random();
                List<Location> list = new ArrayList<>(limitedNeighbours);
                Location newLocation = list.get(rand.nextInt(list.size()));
                world.move(this, newLocation);
            }
        }
    }

    /**
     *
     * @param center
     * @param target
     * @param radius
     * @return
     */
    private boolean isWithinRadius(Location center, Location target, int radius) {
        int dx = center.getX() - target.getX();
        int dy = center.getY() - target.getY();
        return (dx * dx + dy * dy) <= (radius * radius);
    }

    /**
     * This method makes it possible for the bears to eat rabbits & berries.
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

                if (entity instanceof AdultRabbit && Math.random() <= 0.8) { //here checked if it is a rabbit and adds 70% chance
                    energy += 10;
                    System.out.println("Bear Ate a poor Rabbit - New energy level:" + energy);
                    world.delete(entity);
                    Carcass carcass = new Carcass();
                    world.setTile(nearbyLocation, carcass);
                    break;
                }
                if (entity instanceof Berry && Math.random() <= 0.9 && !(entity instanceof Bush)) { //here checked if it is a berry and adds 80% chance
                    energy += 5;
                    System.out.println("Bear Ate some berries - New energy level:" + energy);
                    ((Berry) entity).consumeBerries(world);
                    break;
                }
                if (entity instanceof Carcass) { //here checked if it is a carcassFungi or carcass
                    energy += 5;
                    System.out.println("Bear Ate a bit of a Carcass - New energy level:" + energy);
                    ((Carcass) entity).eatCarcass(world);
                    break;
                }
            }
        }
    }

        /**
         * This is the method place the Bear in the world
         * @param world The current world.
         */
        @Override
        public void placeInWorld (World world){
            super.placeInWorld(world);
            if (!world.containsNonBlocking(location)) {
                world.setTile(location, this);
                startingPoint = location;
            }
        }

        public void placeInWorld (World world,int x, int y){
            Location location = new Location(x, y);
            if (!world.containsNonBlocking(location)) {
                world.setTile(location, this);
                startingPoint = location;
            }
        }
    }

