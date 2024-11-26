package itumulator.world;
import itumulator.simulator.Actor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class Wolf implements Actor {
    // Tracks this pack's alpha wolf location
    private static Location alphaLocation = null;
    // Wolves in this pack
    private static List<Wolf> pack = new ArrayList<>();
    // Determines whether this wolf is the alpha
    private boolean isAlphaWolf = false;
    private int life;
    private int energy;
    private int stepCount;

    public Wolf() {
        this.life = 10;
        this.energy = 100;
        this.stepCount = 0;
    }

    /**
     * This makes it possible for the wolves to perform their actions
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void act(World world) {
        stepCount++;
        // check if a full night has passed with 0 energy
        if (stepCount % 20 == 0 || energy <= 0) {
            life--;
            System.out.println("Wolf has lost life due to zero energy or a day has past. Remaining lives: " + life);
        }
        if(isAlphaWolf) {
            moveRandomly(world); //Added this so they move
            movePack(world);
        }
        hasDied(world);
        eat(world);
    }

    /**
     * This method makes it possible for the wolves to eat rabbits.
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
            if (isRabbit instanceof Rabbit && Math.random() <= 0.2 && energy == 0) { //here checked if it is a rabbit, energy = 0 and adds 20% chance
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
    private void hasDied(World world){
        if (life <= 0) {
            // Remove wolf from the world
            world.delete(this);
            System.out.println("A Wolf has died.");
        }
    }

    /**
     * This method moves the alpha wolf randomly
     * @param world the current world
     */
    private void moveRandomly(World world) {
        if(energy > 0 && isAlphaWolf) {
            Location curLocation = world.getLocation(this);
            Set<Location> neighbours = world.getEmptySurroundingTiles(curLocation);

            if (!neighbours.isEmpty()) {
                Random rand = new Random();
                List<Location> list = new ArrayList<>(neighbours);
                Location newLocation = list.get(rand.nextInt(list.size()));
                world.move(this, newLocation);
                alphaLocation = newLocation;
                System.out.println("Alpha wolf moved to: " + newLocation);
            }
        }
    }

    /**
     * This method moves the wolves in accordance to their alpha wolf
     * @param world The current world
     */
    private void movePack(World world) {

        for(Wolf wolf : pack) {
                Location alphaLocation = world.getLocation(this);
                Location wolfLocation = world.getLocation(wolf);
                Set<Location> emptyTiles = world.getEmptySurroundingTiles(alphaLocation);

                if (!emptyTiles.isEmpty()) {
                    List<Location> list = new ArrayList<>(emptyTiles);
                    Random rand = new Random();
                    Location newLocation = list.get(rand.nextInt(list.size()));

                    world.move(wolf, newLocation);
                    System.out.println("Pack wolf moved from " + wolfLocation + " to " + newLocation);
            }
        }
    }

    /**
     * This is the method place the wolves in the world
     * @param world The current world.
     *
     */
    public void placeInWorld(World world) {
        if (isAlphaWolf || alphaLocation == null) {
            // Place the first wolf randomly
            int size = world.getSize();
            Location location = null;

            while (location == null || !world.isTileEmpty(location)) {
                int x = (int) (Math.random() * size);
                int y = (int) (Math.random() * size);
                location = new Location(x, y);
            }

            world.setTile(location, this);
            alphaLocation = location; // Save the location of the first wolf
            isAlphaWolf = true;
            System.out.println("First wolf placed at: " + location);
        } else {
            // Join an existing alpha's pack
            Set<Location> nearbyTiles = world.getEmptySurroundingTiles(alphaLocation);

            if (!nearbyTiles.isEmpty()) {
                List<Location> list = new ArrayList<>(nearbyTiles);
                Location newLocation = list.get(new Random().nextInt(list.size()));
                world.setTile(newLocation, this);
                System.out.println("Wolf placed near pack at: " + newLocation);
            }
            pack.add(this);
        }
    }

    /**
     * This method resets the pack, so that we can have unique packs per inputted line
     */
    // Reset pack for a new line of wolves
    public static void resetPack() {
            alphaLocation = null; // Clear alpha's location
            System.out.println("Reset alpha wolf and its pack.");
        }
}
