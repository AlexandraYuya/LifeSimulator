package Gruppe01;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;

public class BabyWolf extends Wolf implements Actor, DynamicDisplayInformationProvider {

    public BabyWolf(Wolf alphaWolf) {
        super(alphaWolf);
        life = 15;
    }

    /**
     * This is the method will display a baby wolf in the world.
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.GRAY, "wolf-small");
    }

    /**
     * This is the method will grow the baby wolf to an adult wolf.
     * @param world The current world
     */
    public void grow(World world) {
        Location curLocation = world.getLocation(this);
        this.alphaWolf.pack.remove(this);
        world.delete(this);
        Wolf adultWolf = new Wolf(this.alphaWolf);
        world.add(adultWolf);
        world.setTile(curLocation, adultWolf);
    }
}
