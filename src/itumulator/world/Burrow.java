package itumulator.world;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;

import java.awt.*;

public class Burrow implements NonBlocking, DynamicDisplayInformationProvider, PRNG {
    // Associate rabbit that dug this burrow
    private AdultRabbit owner;

    public Burrow() {
        // initially there's no owner
        this.owner = null;
    }
    /**
     * This is the method will get the owner of the borrow.
     * @param rabbit The current rabbit.
     */
    public void setOwner(AdultRabbit rabbit) {
        this.owner = rabbit;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.GRAY, "hole-small");
    }

    /**
     * This is the method place the rabbit in the world
     * @param world The current world.
     */
    public void placeInWorld(World world) {
        int size = world.getSize();
        Location location = null;

        while (location == null || world.getNonBlocking(location) != null) {
            int x = PRNG.rand().nextInt(size);
            int y = PRNG.rand().nextInt(size);
            location = new Location(x, y);
        }

        world.setTile(location, this);
    }
}
