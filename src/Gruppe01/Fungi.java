package Gruppe01;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;

public class Fungi extends CarcassFungi implements DynamicDisplayInformationProvider {
    protected boolean carcassTransformed;
    protected boolean isSmall;

    public Fungi(boolean isSmall){
        amount = 5;
        carcassTransformed = false;
        this.isSmall = isSmall;
  }

    /**
     * This method gives the fungi a small or at normal size, dependent on the size of the carcassFungi.
     */
    @Override
    public DisplayInformation getInformation() {
        if(!isSmall && amount > 2) {
            return new DisplayInformation(Color.DARK_GRAY, "fungi");
        }else {
            return new DisplayInformation(Color.BLACK, "fungi-small");
        }
    }

    /**
     * This method accounts for all the behavior of fungi.
     * @param world The current world
     */
    @Override
    public void act(World world) {
        stepCount++;

        if (stepCount % 20 == 0) {
            amount--;
        }

        nearFungi(world);

        if (amount <= 0) {
            world.delete(this);
        }
    }

    /**
     * This method is used for the Fungi to find surrounding tiles with healthy carcasses.
     * It transforms a normal carcass into carcassFungi.
     * @param world The current world
     */
    public void nearFungi(World world) {
        Location curLocation = world.getLocation(this);

        int radius = 5;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (dx == 0 && dy == 0) continue;

                Location checkLocation = new Location(curLocation.getX() + dx, curLocation.getY() + dy);

                if (    checkLocation.getX() >=0 &&
                        checkLocation.getX() < world.getSize() &&
                        checkLocation.getY() >=0 &&
                        checkLocation.getY() < world.getSize()) {

                    Object tileObject = world.getTile(checkLocation);
                    if (tileObject == null) {
                        continue;
                    }

                    if (tileObject instanceof Carcass foundCarcass && !(tileObject instanceof CarcassFungi)) {

                        world.delete(tileObject);
                        CarcassFungi newCarcassFungi = new CarcassFungi(foundCarcass.isSmall, 5);
                        world.setTile(checkLocation, newCarcassFungi);

                        if (foundCarcass.isSmall) {
                            amount +=2;
                        } else {
                            amount += 4;
                        }
                        carcassTransformed = true;
                        return;
                    }
                }
            }
        }
    }
}


