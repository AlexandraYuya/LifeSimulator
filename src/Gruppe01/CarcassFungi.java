package Gruppe01;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;


public class CarcassFungi extends Carcass implements Actor {

    public CarcassFungi(boolean isSmall, int amount) {
        super(isSmall, amount);
    }

    public CarcassFungi() {
        amount = 5;
    }

    /**
     * This method accounts for all the behavior of carcassFungi.
     * The method takes away from the amount every day.
     * If the amount is 0 the carcassFungi will be deleted from the world and replace it with a fungi.
     * @param world The current world
     */
    @Override
    public void act(World world) {
        stepCount++;

        // After 20 steps (half day), remove carcass
        if (stepCount % 20 == 0) {
            System.out.println(amount + " CarcassFungi decayed by -1!");
            amount--;
        }
        if (amount <= 0) {
            Location curLocation = world.getLocation(this);
            world.delete(this);
            Fungi fungi = new Fungi(isSmall);
            world.setTile(curLocation, fungi);
            System.out.println("CarcassFungi removed!");
        }
    }

    /**
     * This method places the carcassFungi in the world by the superclass Carcass.
     * @param world The current world
     */
    @Override
    public void placeInWorld(World world) {
        super.placeInWorld(world);
    }

//    public int getAmount() {
//        return 0;
//    }

//    public boolean hasAmount() {
//        return true;
//    }


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


