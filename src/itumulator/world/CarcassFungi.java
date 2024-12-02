package itumulator.world;

import itumulator.executable.DisplayInformation;

import java.awt.*;

public class CarcassFungi extends Carcass {
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
        // After 5 steps (half day), remove carcass
        if (stepCount == 10) {
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


