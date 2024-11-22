package itumulator.world;
import itumulator.simulator.Actor;

public class SleepingBabyRabbit implements Actor {
    private Location sleepLocation;

    public SleepingBabyRabbit(Location location) {
        this.sleepLocation = location;
    }

    @Override
    public void act(World world) {
        // Sleeping rabbits doesn't act
    }
}
