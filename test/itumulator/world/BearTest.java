package itumulator.world;
import Gruppe01.BabyRabbit;
import Gruppe01.Bear;

import itumulator.executable.Program;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
public class BearTest {
    Bear bear;
    World world;

    @Before
    public void setUp() throws Exception {
        Program program = new Program(5, 800, 1200);
        world = program.getWorld();

        bear = new BabyRabbit();
        bear.placeInWorld(world);
        bear.act(world);
    }

    @Test
    public void die() {
        bear.die(world);
        assertTrue(true);
        //check if it was deleted properly
    }
    


}
