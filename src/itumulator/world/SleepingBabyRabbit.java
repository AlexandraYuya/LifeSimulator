package itumulator.world;
import itumulator.simulator.Actor;

public class SleepingBabyRabbit extends BabyRabbit implements Actor{
    private Location sleepLocation;

    public SleepingBabyRabbit(Location location) {
        this.sleepLocation = location;
    }

    @Override
    public void act(World world) {
        if(world.isDay()) {
            Location currentLocation = world.getLocation(this);
            world.delete(this);
            BabyRabbit babyRabbit = new BabyRabbit();
            world.setTile(currentLocation, babyRabbit);
        }
    }
}
