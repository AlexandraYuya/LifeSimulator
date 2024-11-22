package itumulator.world;



import itumulator.executable.Program;
import itumulator.simulator.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class Rabbit implements Actor {
    private int life;
    private int energy;
    private int stepCount;
    // Tracks if the rabbit has already dug a burrow
    private boolean hasDugBurrow;
    // Reference to the burrow the rabbit dug
    private Burrow myBurrow;
    private boolean resting = false;
    private boolean sleeping = false;
    private Location lastPosition;

    public Rabbit() {
        this.life = 10;
        this.energy = 100;
        this.stepCount = 0;
        this.hasDugBurrow = false;
        this.myBurrow = null;
    }

    @Override
    public void act(World world) {
        if (life <= 0) {
            world.delete(this);
            System.out.println("A rabbit has died :C");
            return;
        }

        stepCount++;

        // only execute for every 20 steps 1 day
        if (stepCount == 20) {
            // reset step count
            stepCount = 0;
            // lose 1 life point after a full day has passed
            life--;
        }

        // for each step, energy is depleted by 1.
        if(energy > 0) {
            energy--;
        }

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

        eat(world);
        tryToMate(world);
        digProbability(world);

        System.out.println("energy" + energy);
        System.out.println("Life" + life);
    }

    // DIG METHOD -->
    private void digProbability(World world) {
        // only 1 burrow can be dug per rabbit
        if(hasDugBurrow) {
            return;
        }
        // 10% chance of digging
        double digProbability  = 0.1;
        if (Math.random() < digProbability) {
            Location curLocation = world.getLocation(this);
            if (!world.containsNonBlocking(curLocation)) {
                Burrow burrow = new Burrow();
                world.setTile(curLocation, burrow);
                // associate rabbit with a burrow
                this.myBurrow = burrow;
                // associate burrow with a rabbit
                burrow.setOwner(this);
                // mark rabbit as has dug burrow
                hasDugBurrow = true;
            }
        }
    }

    public Burrow getMyBurrow() {
        return myBurrow;
    }

    public boolean hasDugBurrow() {
        return myBurrow != null;
    }
    // <-- END DIG METHOD

    // START EAT METHOD -->
    private void eat(World world) {
        Location curLocation = world.getLocation(this);
        Object hasGrass = world.getNonBlocking(curLocation);
        if(hasGrass instanceof Grass) {
            if(energy <= 95) {
                energy += 5;
                System.out.println("Ate grass new energy:" + energy);
            }
            world.delete(hasGrass);
        }
    }
    // <-- END EAT METHOD

    // START MATING METHOD -->
    private void tryToMate(World world) {
        // Here we check if rabbit has enough energy to reproduce (as they go -20 if they do)
        // Don't try to mate if energy is too low
        if (energy < 20) {
            return;
        }
        Location currentLocation = world.getLocation(this);
        // Get only surrounding tiles (not including current location)
        Set<Location> surroundingTiles = world.getSurroundingTiles(currentLocation);
        // Find rabbits in surrounding tiles
        Set<Rabbit> nearbyRabbits = world.getAll(Rabbit.class, surroundingTiles);

        // Only proceed if there are nearby rabbits
        if (!nearbyRabbits.isEmpty()) {
            // 30% chance to create a baby
            // Returns a value between 0.0 and 1.0
            double chance = new Random().nextDouble();
            // 30% chance (0.3 = 30%)
            if (chance < 0.3) {
                // Look for empty tile to place baby
                Set<Location> emptyTiles = world.getEmptySurroundingTiles(currentLocation);
                if (!emptyTiles.isEmpty()) {
                    // Pick a random empty tile for the baby
                    List<Location> tilesList = new ArrayList<>(emptyTiles);
                    Location babyLocation = tilesList.get(new Random().nextInt(tilesList.size()));

                    // Create and place baby
                    BabyRabbit baby = new BabyRabbit();
                    world.add(baby);
                    world.setTile(babyLocation, baby);

                    // Decrease energy after successful reproduction
                    energy -= 20;
                    System.out.println("A baby rabbit was born! Parent energy now: " + energy);
                }
            }
        }
    }
    // <-- END MATING METHOD

    /**
     * WRITE ALL PARAM?
     * @param world an absolute URL giving the base location of the ima
     * param program
     *
     */
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
