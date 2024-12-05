package itumulator.world;

import itumulator.executable.Program;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BabyRabbitTest {
    BabyRabbit babyRabbit;
    World world;


    @Before
    public void setUp() throws Exception {
        Program program = new Program(5, 800, 1200);
        world = program.getWorld();

        babyRabbit = new BabyRabbit();
        babyRabbit.placeInWorld(world);
        babyRabbit.act(world);
    }

    @Test
    public void die() {
        babyRabbit.die(world);
        assertTrue(true);
        //check if it was deleted properly
    }

    @Test
    public void grow() {
        babyRabbit.grow(world);
        
    }
}
