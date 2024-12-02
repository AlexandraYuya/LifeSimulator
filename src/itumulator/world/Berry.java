package itumulator.world;

public class Berry {
    private boolean hasBerries;

    public Berry() {
        // Initialize berries to true when created
        this.hasBerries = true;
    }

    public boolean hasBerries() {

        return hasBerries;
    }

    /**
     *
     * @param world The current world.
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
     * This is the method place the grass in the world
     * @param world The current world.
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




