package Gruppe01;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Bear extends Animal implements Actor, DynamicDisplayInformationProvider{
    private int radius;
    private Location startingPoint;

    public Bear() {
        super(15,100, false, 20);
        this.radius = 2;
    }

    /**
     * This method give the Bear a sleeping png or an awake png.
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
     * @param world The current world.
     */
    @Override
    public void handleDay(World world) {
        super.handleDay(world);
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
            for (Location nearbyLocation : surroundingTiles) {
                Object entity = world.getTile(nearbyLocation);
                double chance = PRNG.rand().nextDouble();

                if (entity instanceof AdultRabbit && chance <= 0.7) {
                    world.delete(entity);
                    Carcass carcass = new Carcass(isSmall, 10);
                    world.setTile(nearbyLocation, carcass);
                    break;
                }
                if (entity instanceof BushBerry && chance <= 0.9 && !(entity instanceof Bush)) {
                    energy += 5;
                    ((BushBerry) entity).consumeBerries(world);
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

