package Gruppe01;


import itumulator.world.Location;
import itumulator.world.World;

public class Berry implements PRNG {
    private boolean hasBerries;

    public Berry() {
        // Initialize berries to true when created
        this.hasBerries = true;
    }

    /**
     * This method is used for getting the value of hasBerries.
     * @return hasBerries
     */
    public boolean hasBerries() {
        return hasBerries;
    }

    /**
     * This method is used when they are berry's get eaten.
     * When it gets eat the berry will be deleted from the world and become a bush.
     * @param world The current world
     */
    public void consumeBerries(World world) {
        System.out.println("consumeBerries called, hasBerries is: " + hasBerries()); // Debug print
        if (hasBerries) {
            hasBerries = false;  // False
            // Get current location before deleting
            Location currentLocation = world.getLocation(this);
            System.out.println("Transforming berry at " + currentLocation); // Debug print
            // Remove the Berry
            world.delete(this);
            // Create and place new Bush in same location
            Bush bush = new Bush();
            world.setTile(currentLocation, bush);
            System.out.println("Bush placed at: " + currentLocation); // Debug print
        } else {
            System.out.println("Attempted to consume berries but none available"); // Debug print
        }
    }

    /**
     * This is the method place the berry in the world.
     * @param world The current world
     */
    public void placeInWorld(World world) {
        int size = world.getSize();
        Location location = null;

        while (location == null || world.getNonBlocking(location) != null) {
            int x = PRNG.rand().nextInt(size);
            int y = PRNG.rand().nextInt(size);
            location = new Location(x, y);
        }
        if (!world.containsNonBlocking(location)) {
            world.setTile(location, this);
        }
    }
}




