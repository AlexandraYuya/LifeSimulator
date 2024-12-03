package itumulator.world;

import itumulator.executable.DisplayInformation;
import itumulator.simulator.Actor;

import java.awt.*;

public class CarcassFungi extends Carcass implements Actor {
    protected int stepCount;
    protected int amount;
    private boolean hasAmount;

    public CarcassFungi() {
        stepCount = 0;
        amount = 20;
        this.hasAmount = true;
    }

    @Override
    public void act(World world) {
        stepCount++;

        nearCarcassFungi(world);

        // After 20 steps (half day), remove carcass
        if (stepCount == 20) {
            System.out.println("CarcassFungi removed!");
            world.delete(this);
        }
    }

    @Override
    public void placeInWorld(World world) {
        super.placeInWorld(world);
    }

    public int getAmount() {
        return 0;
    }

    public boolean hasAmount() {
        return true;
    }

    public void nearCarcassFungi(World world) {

        // Get location of the carcassFungi
        Location curLocation = world.getLocation(this);

        int radius = 8;
        boolean carcassTransformed = false;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                // Skip current location
                if (dx == 0 && dy == 0) continue;

                Location checkLocation = new Location(curLocation.getX() + dx, curLocation.getY() + dy);

                // check if  the location is within radius
                if (    checkLocation.getX() >=0 &&
                        checkLocation.getX() < world.getSize() &&
                        checkLocation.getY() >=0 &&
                        checkLocation.getY() < world.getSize()) {

                    Object tileObject = world.getTile(checkLocation);
                    if (tileObject == null) {
                        System.out.println("No object at: " + checkLocation);
                        continue;
                    }


                    if (tileObject instanceof Carcass && !(tileObject instanceof CarcassFungi)) {
                        System.out.println("Carcass found at: " + checkLocation.getX() + ", " + checkLocation.getY() + ")");

                        world.delete(tileObject);
                        CarcassFungi newCarcassFungi = new CarcassFungi();
                        world.setTile(checkLocation, newCarcassFungi);

                        System.out.println("Carcass transformed at: " + checkLocation.getX() + ", " + checkLocation.getY() + ")");
                        carcassTransformed = true;
                        return;
                    }
                }
            }
        }
        System.out.println("No carcass found in the radius of " + radius);
    }
//    public void eatCarcass(World world) {
//        System.out.println("consumeBerries called, hasBerries is: " + hasAmount()); // Debug print
//        if (hasAmount) {
//            hasAmount = false;  // False
//            // Get current location before deleting
//            Location currentLocation = world.getLocation(this);
//            System.out.println("Transforming berry at " + currentLocation); // Debug print
//            // Remove the Berry
//            world.delete(this);
//        } else {
//                System.out.println("Attempted to consume CarcassFungi but not enough amount available"); // Debug print
//            }
//        }
    }


