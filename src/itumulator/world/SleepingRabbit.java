package itumulator.world;
import itumulator.simulator.Actor;

public class SleepingRabbit extends Rabbit implements Actor {
    private Location sleepLocation;

    public SleepingRabbit(Location location) {
        this.sleepLocation = location;
    }

    @Override
    public void act(World world) {
        if(world.isDay()) {
            Location currentLocation = world.getLocation(this);
            world.delete(this);
            Rabbit rabbit = new Rabbit();
            world.setTile(currentLocation, rabbit);
        }
    }
}
