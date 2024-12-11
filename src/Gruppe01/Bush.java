package Gruppe01;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;


public class Bush extends BushBerry implements Actor, DynamicDisplayInformationProvider {
    private int stepCount;

    public Bush() {
        this.stepCount = 0;
    }

    /**
     * This method display a bush png.
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.red, "bush");
    }


    /**
     * This method makes the bush grow into a bushBarry when the count is at 60 (3 days).
     * @param world The current world
     */
    public void act(World world) {
        stepCount++;

        if (stepCount >= 60) {
            Location currentLocation = world.getLocation(this);
            world.delete(this);
            BushBerry berry = new BushBerry();
            world.setTile(currentLocation, berry);
        }
    }
}
