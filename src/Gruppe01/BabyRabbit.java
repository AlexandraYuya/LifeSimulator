package Gruppe01;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;


public class BabyRabbit extends Rabbit implements Actor, DynamicDisplayInformationProvider {

    public BabyRabbit() {
        life = 5;
    }

    /**
     * This method give the baby rabbit a sleeping png or an awake png.
     * If the baby rabbit is a sleep it will get a sleeping baby rabbit png.
     * If it was awake it will get an awake baby rabbit.
     */
    @Override
    public DisplayInformation getInformation() {
        if(isNight){
            return new DisplayInformation(Color.BLACK, "rabbit-small-sleeping");
        } else {
            return new DisplayInformation(Color.GRAY, "rabbit-small");
        }
    }

    /**
     * This is the method we use for handling day.
     * The baby rabbit will wake up and do normal day behavior.
     * Handle day comes from the super class.
     * @param world The current world
     */
    // START DAY HANDLER METHOD -->
    public void handleDay(World world) {
        super.handleDay(world);
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
     * This method will "grow" the baby rabbit to an adult rabbit.
     * @param world The current world
     */
    public void grow(World world) {
        Location curLocation = world.getLocation(this);
        world.delete(this);
        AdultRabbit adultRabbit = new AdultRabbit();
        world.add(adultRabbit);
        world.setTile(curLocation, adultRabbit);
    }

}
