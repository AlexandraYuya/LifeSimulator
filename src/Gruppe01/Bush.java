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

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.red, "bush");
    }

    /**
     * This method will count the steps, and it makes the bush grow into a barry when the count is at 60
     * After 60 steps (3 days), transforms Bush back into BushBerry
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
