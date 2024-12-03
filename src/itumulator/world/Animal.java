package itumulator.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class Animal{
    protected int life;
    protected int energy;
    protected int stepCount;
    protected Location location;

    public Animal(int life, int energy) {
        this.life = life;
        this.energy = energy;
    }

    public void act(World world) {
        stepCount++;
        if (stepCount % 20 == 0) {
            life--;
        }
        die(world);
    }

    public void handleNight(World world) {}
    public void handleDay(World world) {}

    public void die(World world) {
//        Location curLocation = world.getLocation(this);
        if(life <= 0) {
            world.delete(this);
//            Carcass carcass = new Carcass();
//            world.add(carcass);
//            world.setTile(curLocation, carcass);
            System.out.println(this + "has DIEDDD!!!!");
        }
    }

    public void moveRandomly(World world) {
        if(energy >= 0 && life >= 0) {
            Location curLocation = world.getLocation(this);
            Set<Location> neighbours = world.getEmptySurroundingTiles(curLocation);

            if (!neighbours.isEmpty()) {
                Random rand = new Random();
                List<Location> list = new ArrayList<>(neighbours);
                Location newLocation = list.get(rand.nextInt(list.size()));
                world.move(this, newLocation);
            }
        }
    }

    public void eat(World world) {
    }

    public void placeInWorld(World world) {
            int size = world.getSize();
            location = null;

            while (location == null || !world.isTileEmpty(location)) {
                int x = (int) (Math.random() * size);
                int y = (int) (Math.random() * size);
                location = new Location(x, y);
            }
    }
}
