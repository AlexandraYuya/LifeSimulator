package itumulator.world;

import Gruppe01.AdultRabbit;
import itumulator.executable.Program;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RabbitTest {
    AdultRabbit rabbit;
    World world;

    @Before
    public void setUp() throws Exception {
        Program program = new Program(5, 800, 1200);
        world = program.getWorld();

        rabbit = new AdultRabbit();
        rabbit.placeInWorld(world);
        rabbit.act(world);
    }

    @Test
    public void die() {
    rabbit.die(world);
        assertTrue(true);
        //check if it was deleted properly
    }
}