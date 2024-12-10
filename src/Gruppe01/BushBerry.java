package Gruppe01;


import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;

public class BushBerry implements DynamicDisplayInformationProvider {
    private boolean hasBerries;

    public BushBerry() {
        this.hasBerries = true;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.red, "bush-berries");
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
     * // Get current location before deleting
     * @param world The current world
     */
    public void consumeBerries(World world) {
        if (hasBerries) {
            hasBerries = false;
            Location currentLocation = world.getLocation(this);
            world.delete(this);
            Bush bush = new Bush();
            world.setTile(currentLocation, bush);
        }
    }

    /**
     * This is the method place the berry in the world.
     * @param world The current world
     */
    public void placeInWorld(World world) {
        int size = world.getSize();
        Location location = null;

        while (location == null || world.getTile(location) != null) {
            int x = PRNG.rand().nextInt(size);
            int y = PRNG.rand().nextInt(size);
            location = new Location(x, y);
        }
        if (!world.containsNonBlocking(location)) {
            world.setTile(location, this);
        }
    }
}




