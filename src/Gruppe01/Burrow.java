package Gruppe01;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.awt.*;

public class Burrow implements NonBlocking, DynamicDisplayInformationProvider {
    protected AdultRabbit owner;

    public Burrow() {
        this.owner = null;
    }

    /**
     * This method will get the owner of a borrow.
     * @param rabbit The current rabbit
     */
    public void setOwner(AdultRabbit rabbit) {
        this.owner = rabbit;
    }

    /**
     * This method display a small borrow png.
     */
    @Override
    public DisplayInformation getInformation() {

        return new DisplayInformation(Color.GRAY, "hole-small");
    }

    /**
     * This method place the borrow in the world
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

        world.setTile(location, this);
    }
}
