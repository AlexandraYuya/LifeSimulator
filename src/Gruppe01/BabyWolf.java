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
        // Remove ourselves from the pack since baby is now grown
        this.alphaWolf.pack.remove(this);
        // Delete baby rabbit (this will remove it from both world and tile)
        world.delete(this);
        // Create new adult rabbit
        Wolf adultWolf = new Wolf(this.alphaWolf);
        // Add adult rabbit to world
        world.add(adultWolf);
        // Place adult rabbit at the location
        world.setTile(curLocation, adultWolf);
    }
}
