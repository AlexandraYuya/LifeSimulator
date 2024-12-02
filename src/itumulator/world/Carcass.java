package itumulator.world;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;

import java.awt.*;

public class Carcass implements Actor, DynamicDisplayInformationProvider {
    protected int stepCount;
    private boolean hasAmount;

    public Carcass() {
                stepCount = 0;
                this.hasAmount = true;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.DARK_GRAY, "carcass");
    }

        @Override
        public void act(World world) {
            stepCount++;

            // After 5 steps (half day), remove carcass
            if (stepCount == 5) {
                System.out.println("Carcass removed!");
                world.delete(this);
            }
        }


    public void placeInWorld(World world) {
        int size = world.getSize();
        Location location = null;

        while (location == null || world.getNonBlocking(location) != null) {
            int x = (int) (Math.random() * size);
            int y = (int) (Math.random() * size);
            location = new Location(x, y);
        }
        if (!world.containsNonBlocking(location)) {
            world.setTile(location, this);
        }
    }

    public boolean hasAmount() {
        return true;
    }

    public void eatCarcass(World world) {
        System.out.println("consumeBerries called, hasBerries is: " + hasAmount()); // Debug print
        if (hasAmount) {
            hasAmount = false;  // False
            // Get current location before deleting
            Location currentLocation = world.getLocation(this);
            System.out.println("Transforming berry at " + currentLocation); // Debug print
            // Remove the Berry
            world.delete(this);
        } else {
            System.out.println("Attempted to consume CarcassFungi but not enough amount available"); // Debug print
        }
    }


}
