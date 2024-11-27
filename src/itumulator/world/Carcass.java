package itumulator.world;

import itumulator.simulator.Actor;

public class Carcass implements Actor {
    private int stepCount;

    public Carcass() {
        stepCount = 0;
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
