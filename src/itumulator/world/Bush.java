package itumulator.world;
import itumulator.simulator.Actor;


public class Bush extends Berry implements Actor{
    private int stepCount;

    public Bush() {
        this.stepCount = 0;
    }

    public void act(World world) {
        stepCount++;

        // After 60 steps (3 days), transform back into Berry
        if (stepCount >= 60) {
            System.out.println("Bush growing berries again!");
            Location currentLocation = world.getLocation(this);
            world.delete(this);
            Berry berry = new Berry();
            world.setTile(currentLocation, berry);
        }
    }

// Har fjernet da den får allerede dens placeInWorld() metode fra dens super klasse
//    @Override
//    public void placeInWorld(World world) {
//        int size = world.getSize();
//        Location location = null;
//
//        while (location == null || !world.isTileEmpty(location)) {
//            int x = (int) (Math.random() * size);
//            int y = (int) (Math.random() * size);
//            location = new Location(x, y);
//        }
//        if (!world.containsNonBlocking(location)) {
//            world.setTile(location, this);
//        }
//    }

}
