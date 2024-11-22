package itumulator.world;
import itumulator.simulator.Actor;

public class SleepingRabbit implements Actor {
    private Location sleepLocation;

    public SleepingRabbit(Location location) {
        this.sleepLocation = location;
    }

    @Override
    public void act(World world) {
        // Sleeping rabbits doesn't act
    }
}
