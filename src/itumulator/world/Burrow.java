package itumulator.world;

import itumulator.executable.Program;
import itumulator.simulator.Actor;

public class Burrow implements Actor, NonBlocking {
    // Associate rabbit that dug this burrow
    private Rabbit owner;

    public Burrow() {
        // initially there's no owner
        this.owner = null;
    }

    public void setOwner(Rabbit rabbit) {
        this.owner = rabbit;
    }

    public Rabbit getOwner() {
        return owner;
    }

    @Override
    public void act(World world) {
        // Burrow doesn't act

    }

    public void placeInWorld(World world) {
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
