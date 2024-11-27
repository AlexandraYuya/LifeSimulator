package itumulator.world;
import itumulator.simulator.Actor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Wolf implements Actor {
    // Reference to the alpha wolf of this pack
    private Wolf alphaWolf;
    // Check whether this wolf is the alpha
    private boolean isAlphaWolf;
    // Wolves in this pack (only populated for the alpha wolf)
    private List<Wolf> pack;
    private int life;
    private int energy;
    private int stepCount;

    public Wolf(Wolf alphaWolf) {
        this.alphaWolf = alphaWolf;
        this.life = 10;
        this.energy = 100;
        this.stepCount = 0;

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
            alphaWolf.addPackMember(this);
        }
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
     * This method accounts for all the behavior of wolves
     * @param world The current world
     */
    @Override
    public void act(World world) {
        stepCount++;
        if (stepCount % 20 == 0 || energy <= 0) {
            life--;
        }

        if (isAlphaWolf) {
            moveRandomly(world);
        } else {
            followAlpha(world);
        }

        eat(world);
        hasDied(world);
    }

    /**
     * This method determines the probability of eating a rabbit.
     * Replenishes energy.
     * @param world The current world
     */
    private void eat(World world) {
        Location curLocation = world.getLocation(this);
        Set<Location> surroundingTiles = world.getSurroundingTiles(curLocation);

        if (!surroundingTiles.isEmpty()) {
            Location rabbitLocation = surroundingTiles.iterator().next();
            Object isRabbit = world.getTile(rabbitLocation);
            if (isRabbit instanceof AdultRabbit && Math.random() <= 0.7) { //here checked if it is a rabbit and adds 70% chance
                energy += 10;
                System.out.println("Ate a poor Rabbit - New energy level:" + energy);
                world.delete(isRabbit);
                Carcass carcass = new Carcass();
                world.setTile(rabbitLocation, carcass);
            }
        }
    }

    /**
     * This method kills off wolves when their life is equal to 0
     * @param world The current world
     */
    private void hasDied(World world) {
        if (life <= 0) {
            world.delete(this);
            System.out.println("A Wolf has died.");
        }
    }

    /**
     * This method moves the alpha to a random nearby empty tile.
     * @param world The current world.
     */
    private void moveRandomly(World world) {
        Location curLocation = world.getLocation(this);
        Set<Location> neighbours = world.getEmptySurroundingTiles(curLocation);

        if (!neighbours.isEmpty()) {
            Random rand = new Random();
            List<Location> list = new ArrayList<>(neighbours);
            Location newLocation = list.get(rand.nextInt(list.size()));
            world.move(this, newLocation);
            System.out.println("Alpha wolf moved to: " + newLocation);
        }
    }

    /**
     * This method moves the alpha's pack wolves in accordance to the alphas location, always following it.
     * Only used by non-alpha wolves.
     * @param world The current world.
     */
    private void followAlpha(World world) {
        if (alphaWolf != null && world.contains(alphaWolf)) {
            Location alphaLocation = world.getLocation(alphaWolf);
            Location wolfLocation = world.getLocation(this);
            Set<Location> emptyTiles = world.getEmptySurroundingTiles(alphaLocation);

            if (!emptyTiles.isEmpty()) {
                List<Location> list = new ArrayList<>(emptyTiles);
                Random rand = new Random();
                Location newLocation = list.get(rand.nextInt(list.size()));
                world.move(this, newLocation);
                System.out.println("Pack wolf moved from " + wolfLocation + " to " + newLocation);
            }
        }
    }

    /**
     * This method places the wolves based on if they're the alpha, or pack wolves.
     * @param world The current world.
     */
    public void placeInWorld(World world) {
        // Places alpha wolf in the world
        if (isAlphaWolf) {
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

        } else {
            // Places pack wolves in the world based on alpha's location
            Location alphaLocation = world.getLocation(alphaWolf);
            Set<Location> nearbyTiles = world.getEmptySurroundingTiles(alphaLocation);

            if (!nearbyTiles.isEmpty()) {
                List<Location> list = new ArrayList<>(nearbyTiles);
                Location newLocation = list.get(new Random().nextInt(list.size()));
                world.setTile(newLocation, this);
                System.out.println("Wolf placed near pack at: " + newLocation);
            }
        }
    }
}