package Gruppe01;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;

public class Fungi extends CarcassFungi implements DynamicDisplayInformationProvider {
    private boolean carcassTransformed;

    public Fungi(){
        amount = 5;
        carcassTransformed = false;
    }

    /**
     * This method give the fungi a small or at normal size.
     * If the carcassFungi was big it will get a normal fungi.
     * If it was small it will get a small fungi.
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
            System.out.println(amount + " Fungi decayed by -1!");
            amount--;
        }

        nearFungi(world);

        if (amount <= 0) {
            world.delete(this);
            System.out.println("Fungi removed!");
        }
    }

    /**
     * This is the method is for the Fungi to find surrounding carcass without fungi in it.
     * It transforms normal carcass into carcassFungi.
     * @param world The current world
     */
    public void nearFungi(World world) {
        // Get location of the carcassFungi
        Location curLocation = world.getLocation(this);

        int radius = 8;
        //boolean carcassTransformed = false;

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
//                        System.out.println("No object at: " + checkLocation);
                        continue;
                    }


                    if (tileObject instanceof Carcass && !(tileObject instanceof CarcassFungi)) {
                        System.out.println("Carcass found at: " + checkLocation.getX() + ", " + checkLocation.getY() + ")");

                        world.delete(tileObject);
                        CarcassFungi newCarcassFungi = new CarcassFungi();
                        world.setTile(checkLocation, newCarcassFungi);

                        System.out.println("Carcass transformed at: " + checkLocation.getX() + ", " + checkLocation.getY() + ")");
                        carcassTransformed = true;
                        amount+=3;
                        System.out.println("Fungi successfully infected carcass");
                        return;
                    }
                }
            }
        }
        System.out.println("No carcass found in the radius of " + radius);
    }
}


