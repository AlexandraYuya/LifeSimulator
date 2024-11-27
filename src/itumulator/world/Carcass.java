package itumulator.world;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;

import java.awt.*;

public class Carcass implements Actor, DynamicDisplayInformationProvider {
    private int stepCount;

    public Carcass() {
        stepCount = 0;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.DARK_GRAY, "carcass");
    }

        @Override
        public void act(World world) {
            stepCount++;

            // After 5 steps (half day), remove carcass
            if (stepCount == 5) {
                System.out.println("Carcass removed!");
                world.delete(this);
            }
        }
    }
