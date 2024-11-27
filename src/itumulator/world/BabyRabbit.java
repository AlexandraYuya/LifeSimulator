package itumulator.world;
import itumulator.simulator.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class BabyRabbit extends Rabbit implements Actor{
    private int life;
    private int stepCount;

    public BabyRabbit() {
        this.life = 5; // reduced to 5 life
        this.stepCount = 0;
    }

    /**
     * It will make the baby rabbit grow after 5 days, and it calls two different methods one for night and one for day
     * @param world The current world.
     */
    @Override
    public void act(World world) {
        stepCount++;
        if (stepCount == 20) {
            stepCount = 0;
            life--;
        }

        if (life == 3){
            grow(world);
            System.out.println("Baby rabbit finally grew up!!!");
            return;
        }

        if (world.isNight()) {
            super.handleNight(world);
        } else {
            handleDay(world);
        }
        System.out.println("Baby Rabbit life: " + life);
        System.out.println("Baby Rabbit energy: " + energy);
    }

    /**
     * This is the method we use for handling day. The bay rabbit will wake up and make them do normal day behavior.
     * @param world The current world.
     */
    // START DAY HANDLER METHOD -->
    private void handleDay(World world) {
        super.checkInBurrow(world);

        // Resume normal daytime behavior
        if (life > 0 && energy > 0) {
            energy--;
            super.moveRandomly(world);
            super.eat(world);
        }
    }
    // <-- END DAY HANDLER METHOD

    /**
     * This is the method will grow the baby rabbit to an adult rabbit.
     * @param world The current world.
     */
    // START GROW METHOD -->
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
    // <-- END GROW METHOD
}
