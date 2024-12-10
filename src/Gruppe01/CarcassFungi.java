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
     *
     * @param world The current world
     */
    @Override
    public void act(World world) {
        stepCount++;

        if (stepCount % 20 == 0) {
            amount--;
        }
        if (amount <= 0) {
            Location curLocation = world.getLocation(this);
            world.delete(this);
            Fungi fungi = new Fungi(isSmall);
            world.setTile(curLocation, fungi);
        }
    }

    /**
     * This method places the carcassFungi in the world by the superclass Carcass.
     *
     * @param world The current world
     */
    @Override
    public void placeInWorld(World world) {
        super.placeInWorld(world);
    }
}


