package itumulator.world;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;

import java.awt.*;

public class Fungi extends CarcassFungi implements DynamicDisplayInformationProvider {
    private int stepCount;
    private int life;
    private boolean carcassTransformed;

    public Fungi(){
        this.stepCount = 0;
        this.life = 5;
        carcassTransformed = false;
    }

    @Override
    public DisplayInformation getInformation() {
        if(isBig) {
            return new DisplayInformation(Color.DARK_GRAY, "fungi");
        }else {
            return new DisplayInformation(Color.BLACK, "fungi-small");
        }
    }

    @Override
    public void act(World world) {
        stepCount++;

        nearFungi(world);
    }

    public void nearFungi(World world) {

        // Get location of the carcassFungi
        Location curLocation = world.getLocation(this);

        int radius = 8;
//        boolean carcassTransformed = false;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                // Skip current location
                if (dx == 0 && dy == 0) continue;

                Location checkLocation = new Location(curLocation.getX() + dx, curLocation.getY() + dy);

                // check if the location is within radius
                if (    checkLocation.getX() >=0 &&
                        checkLocation.getX() < world.getSize() &&
                        checkLocation.getY() >=0 &&
                        checkLocation.getY() < world.getSize()) {

                    Object tileObject = world.getTile(checkLocation);
                    if (tileObject == null) {
                        System.out.println("No object at: " + checkLocation);
                        continue;
                    }


                    if (tileObject instanceof Carcass && !(tileObject instanceof Fungi)) {
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
}


