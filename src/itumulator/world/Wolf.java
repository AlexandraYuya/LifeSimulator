package itumulator.world;
import itumulator.simulator.Actor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Wolf implements Actor {
    private int life;
    private int energy;
    private int stepCount;

    public Wolf() {
        this.life = 10;
        this.energy = 100;
        this.stepCount = 0;
    }

    @Override
    public void act(World world) {
        stepCount++;
        // check if a full night has passed with 0 energy
        if (stepCount % 20 == 0 || energy <= 0) {
            life--;
            System.out.println("Wolf has lost life due to zero energy or a day has past. Remaining lives: " + life);
        }
        moveRandomly(world); //Added this so they move
        hasDied(world);
        eat(world);

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
            Object isRabbit = world.getTile(rabbitLocation);
            if (isRabbit instanceof Rabbit && Math.random() <= 0.7) { //here checked if it is a rabbit and adds 70% chance
                energy += 10;
                System.out.println("Ate a poor Rabbit - New energy level:" + energy);
                world.delete(isRabbit);
                world.move(this, rabbitLocation);
            }

        }
    }


/**
 * This is a method that check if the wolf has 0 life and then is remove it
 * @param world The current world.*/
    public void hasDied(World world){
        if (life <= 0) {
            // Remove wolf from the world
            world.delete(this);
            System.out.println("A Wolf has died.");
        }
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
     * This is the method place the Wolf in the world
     * @param world The current world.*/
    public void placeInWorld(World world) {
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


}
