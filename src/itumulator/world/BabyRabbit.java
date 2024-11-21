package itumulator.world;
import itumulator.executable.Program;
import itumulator.simulator.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class BabyRabbit implements Actor{
    private int life;
    private int energy;
    private int stepCount;

    public BabyRabbit() {
        this.life = 10;
        this.energy = 100;
        this.stepCount = 0;
    }

    @Override
    public void act(World world) {

    }
}
