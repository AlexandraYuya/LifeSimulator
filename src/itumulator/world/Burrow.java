package itumulator.world;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;

import java.awt.*;

public class Burrow implements Actor, NonBlocking, DynamicDisplayInformationProvider {
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

    public AdultRabbit getOwner() {
        return owner;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.GRAY, "hole-small");
    }

    @Override
    public void act(World world) {
        // Burrow doesn't act

    }
    /**
     * This is the method place the rabbit in the world
     * @param world The current world.
     */
    public void placeInWorld(World world) {
        int size = world.getSize();
        Location location = null;

        while (location == null || world.getNonBlocking(location) != null) {
            int x = (int) (Math.random() * size);
            int y = (int) (Math.random() * size);
            location = new Location(x, y);
        }

        world.setTile(location, this);
    }
}
