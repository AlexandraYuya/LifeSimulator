package itumulator.world;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.simulator.Actor;

public class Grass implements Actor, NonBlocking {
    @Override
    public void act(World world) {
        // Grass does not act
    }

    public void placeInWorld(World world, Program program) {
        int size = world.getSize();
        Location location = null;

        while (location == null || world.getNonBlocking(location) != null) {
            int x = (int) (Math.random() * size);
            int y = (int) (Math.random() * size);
            location = new Location(x, y);
        }

        world.setTile(location, this);
        program.setDisplayInformation(Grass.class, new DisplayInformation(java.awt.Color.green, "grass"));
    }
}
