package itumulator.world;
import itumulator.simulator.Actor;

import itumulator.simulator.Actor;

public class SleepingBear implements Actor {
    private Location sleepLocation;

    public SleepingBear(Location location) {
        this.sleepLocation = location;
    }

    @Override
    public void act(World world) {
        // Sleeping rabbits doesn't act
    }
}