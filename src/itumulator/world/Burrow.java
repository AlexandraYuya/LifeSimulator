package itumulator.world;

import itumulator.executable.Program;
import itumulator.simulator.Actor;

public class Burrow implements Actor, NonBlocking {
    @Override
    public void act(World world) {
        // Burrow behavior, if any, can go here
    }

    public void placeInWorld(World world, Program program) {
        int size = world.getSize();
        Location location = null;

        while (location == null || !world.isTileEmpty(location)) {
            int x = (int) (Math.random() * size);
            int y = (int) (Math.random() * size);
            location = new Location(x, y);
        }

        world.setTile(location, this);
    }
}
