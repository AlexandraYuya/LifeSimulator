package itumulator.world;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;

import java.awt.*;


public class BabyRabbit extends Rabbit implements Actor, DynamicDisplayInformationProvider {

    public BabyRabbit() {
        life = 5; // reduced to 5 life
    }

    @Override
    public DisplayInformation getInformation() {
        if(isNight){
            return new DisplayInformation(Color.BLACK, "rabbit-small-sleeping");
        } else {
            return new DisplayInformation(Color.GRAY, "rabbit-small");
        }
    }

    /**
     * This is the method we use for handling day. The bay rabbit will wake up and make them do normal day behavior.
     * @param world The current world.
     */
    // START DAY HANDLER METHOD -->
    public void handleDay(World world) {
        super.handleDay(world);

        // Resume normal daytime behavior
        if (life > 0 && energy > 0) {
            energy--;
            super.moveRandomly(world);
            super.eat(world);
        }
        if (life == 3){
            grow(world);
            System.out.println("Baby rabbit finally grew up!!!");
        }
    }

    /**
     * This is the method will grow the baby rabbit to an adult rabbit.
     * @param world The current world.
     */
    private void grow(World world) {
        Location curLocation = world.getLocation(this);
        // Delete baby rabbit (this will remove it from both world and tile)
        world.delete(this);
        // Create new adult rabbit
        AdultRabbit adultRabbit = new AdultRabbit();
        // Add adult rabbit to world
        world.add(adultRabbit);
        // Place adult rabbit at the location
        world.setTile(curLocation, adultRabbit);
    }
}
