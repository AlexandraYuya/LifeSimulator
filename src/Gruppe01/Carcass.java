package Gruppe01;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;

public class Carcass implements Actor, DynamicDisplayInformationProvider {
    protected int stepCount;
    protected boolean hasAmount;
    protected int amount;
    protected boolean isSmall;

    public Carcass(boolean isSmall, int amount) {
        this.isSmall = isSmall;
        stepCount = 0;
        this.amount = amount;
        this.hasAmount = true;
    }

    public Carcass() {
        stepCount = 0;
        this.hasAmount = true;
        this.amount = 11;
    }

    public int getAmount() {
        return amount;
    }

    public boolean getIsSmall() {
        return isSmall;
    }

    /**
     * This method looks if the dead animal is big or small.
     * If the dead animal was big it will replace it with a big carcass.
     * If it as small it will replace it with a small carcass.
     */
    @Override
    public DisplayInformation getInformation() {
       if (!isSmall && amount > 10) {
           return new DisplayInformation(Color.DARK_GRAY, "carcass");
       } else {
           return new DisplayInformation(Color.BLACK, "carcass-small");
       }
    }

    /**
     * This method accounts for all the behavior of carcass.
     * It will take from the amount every day.
     * @param world The current world
     */
    @Override
    public void act(World world) {
        stepCount++;

        if (stepCount % 20 == 0) {
            amount--;
        }
    }

    /**
     * This method takes from the amount when someone is eating from it.
     * The carcass will be deleted from the world when the amount is 0.
     * @param world The current world
     */
    public void eatCarcass(World world) {
        if (hasAmount) {
            amount--;
        }
        if(amount <= 0){
            hasAmount = false;
            world.delete(this);
        }
    }

    /**
     * This method places the carcass in the world.
     * @param world The current world
     */
    public void placeInWorld(World world) {
        int size = world.getSize();
        Location location = null;

        while (location == null || world.getTile(location) != null) {
            int x = PRNG.rand().nextInt(size);
            int y = PRNG.rand().nextInt(size);
            location = new Location(x, y);
        }
        if (!world.containsNonBlocking(location)) {
            world.setTile(location, this);
        }
    }

}
