package itumulator.world;

import itumulator.simulator.Actor;

import java.util.*;

public class Bear implements Actor {
    private int energy;
    int radius; // private or not?
    Location startingPoint;

    public Bear() {
        this.energy = 100;
        this.radius = 2;
    }
    @Override
    public void act(World world) {

        eat(world);
        moveInCircRandomly(world);

    }


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

    //
    private boolean isWithinRadius(Location center, Location target, int radius) {
        int dx = center.getX() - target.getX();
        int dy = center.getY() - target.getY();
        return (dx * dx + dy * dy) <= (radius * radius);
    }

    /**
     * This method makes it possible for the bears to eat rabbits & berries.
     * @param world The current world.
     */
    private void eat(World world) {
        Location curLocation = world.getLocation(this);
        Set<Location> surroundingTiles = world.getSurroundingTiles(curLocation);

        if (!surroundingTiles.isEmpty()) {
            Location rabbitLocation = surroundingTiles.iterator().next();
            Location berryLocation = surroundingTiles.iterator().next();

            Object isRabbit = world.getTile(rabbitLocation);
            Object isBerry = world.getTile(berryLocation);

            if (isRabbit instanceof Rabbit && Math.random() <= 0.8) { //here checked if it is a rabbit and adds 70% chance
                energy += 10;
                System.out.println("Bear Ate a poor Rabbit - New energy level:" + energy);
                world.delete(isRabbit);
                world.move(this, rabbitLocation);
            }
            if (isBerry instanceof Berry && Math.random() <= 0.2) { //here checked if it is a berry and adds 80% chance
                energy += 5;
                System.out.println("Bear Ate some berries - New energy level:" + energy);
                ((Berry) isBerry).consumeBerries();
            }
        }
    }

        /**
         * This is the method place the Bear in the world
         * @param world The current world.
         */
        public void placeInWorld (World world){
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
        }

        public void placeInWorld (World world,int x, int y){
            int size = world.getSize();
            Location location = new Location(x, y);
            if (!world.containsNonBlocking(location)) {
                world.setTile(location, this);
                startingPoint = location;
            }
        }
    }

