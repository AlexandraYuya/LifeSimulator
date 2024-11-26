package itumulator.world;

import itumulator.simulator.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Bear implements Actor {
    int energy;

    public Bear() {
        this.energy = 100;
    }
    @Override
    public void act(World world) {

        eat(world);
        moveRandomly(world);
    }

    private void moveRandomly(World world) {
        if(energy > 0) {
            Location curLocation = world.getLocation(this);
            Set<Location> neighbours = world.getEmptySurroundingTiles(curLocation);

            if (!neighbours.isEmpty()) {
                Random rand = new Random();
                List<Location> list = new ArrayList<>(neighbours);
                Location newLocation = list.get(rand.nextInt(list.size()));
                world.move(this, newLocation);
                world.setCurrentLocation(newLocation);
            }
        }
    }

    /**
     * This method makes it possible for the wolfs to eat rabbits.
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

            if (isRabbit instanceof Rabbit && Math.random() <= 1.0) { //here checked if it is a rabbit and adds 70% chance
                energy += 10;
                System.out.println("Bear Ate a poor Rabbit - New energy level:" + energy);
                world.delete(isRabbit);
                world.move(this, rabbitLocation);
            }
            if (isBerry instanceof Berry && Math.random() <= 1.0) { //here checked if it is a berry and adds 80% chance
                energy += 10;
                System.out.println("Bear Ate some berries - New energy level:" + energy);
                world.delete(isBerry);
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
            }
        }
    }

