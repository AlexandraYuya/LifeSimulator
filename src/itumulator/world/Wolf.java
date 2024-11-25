package itumulator.world;

import itumulator.simulator.Actor;

public class Wolf implements Actor {
    @Override
    public void act(World world) {

    }


    /**
     * This is the method place the Wolf in the world
     * @param world The current world.
     */
    public void placeInWorld(World world) {
        int size = world.getSize();
        Location location = null;

        while (location == null || !world.isTileEmpty(location)) {
            int x = (int) (Math.random() * size);
            int y = (int) (Math.random() * size);
            location = new Location(x, y);
        }
        if (!world.containsNonBlocking(location)) {
            world.setTile(location, this);
        }
    }
}
