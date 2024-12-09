package itumulator.world;

import Gruppe01.AdultRabbit;
import Gruppe01.Wolf;
import itumulator.executable.Program;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WolfTest {
    Wolf alphaWolf;
    World world;


    @Before
    public void setUp() throws Exception {
        Program program = new Program(5, 800, 600);
        world = program.getWorld();

        wolf = new Wolf(Wolf alphaWolf);
        wolf.placeInWorld(world);
        rabbit.act(world);
    }

    @Test
    public void die() {
        alphaWolf.setLife(0);
        alphaWolf.die(world);
        assertTrue(!world.contains(rabbit));
        //check if it was deleted properly
    }
}
