package itumulator.world;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;

import java.awt.*;

public class Carcass implements Actor, DynamicDisplayInformationProvider, PRNG {
    protected int stepCount;
    protected boolean hasAmount;
    protected boolean isBig;
    protected boolean isSmall;
    protected int amount;

    public Carcass() {
        stepCount = 0;
        this.hasAmount = true;
        this.isBig = false;
        this.isSmall = false;
        this.amount = 25;
    }

    public boolean hasAmount() {
        return true;
    }

    public boolean isSmall() {
        return isBig = false;
    }

    public boolean isBig() {
        return isSmall = false;
    }

    @Override
    public DisplayInformation getInformation() {
       if (isBig){
           return new DisplayInformation(Color.DARK_GRAY, "carcass");
       } else {
           return new DisplayInformation(Color.BLACK, "carcass-small");
       }
    }

    @Override
    public void act(World world) {
        stepCount++;

        if (stepCount % 20 == 0) {
            System.out.println("carcass decayed by -1!");
            amount--;
        }
    }


    public void eatCarcass(World world) {
        if (hasAmount) {
            amount--;
        } else {
            System.out.println("Attempted to consume carcass but not enough amount available"); // Debug print
        }
        if(amount <= 0){
            hasAmount = false;
            world.delete(this);
        }
    }

    public void placeInWorld(World world) {
        int size = world.getSize();
        Location location = null;

        while (location == null || world.getNonBlocking(location) != null) {
            int x = PRNG.rand().nextInt(size);
            int y = PRNG.rand().nextInt(size);
            location = new Location(x, y);
        }
        if (!world.containsNonBlocking(location)) {
            world.setTile(location, this);
        }
    }

}
