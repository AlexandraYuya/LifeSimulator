package itumulator.world;

import itumulator.executable.Program;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RabbitTest {
    AdultRabbit rabbit;
    World world;

    @Before
    public void setUp() throws Exception {
        Program program = new Program(5, 800, 600);
        world = program.getWorld();

        rabbit = new AdultRabbit();
        rabbit.placeInWorld(world);
        rabbit.act(world);
    }

    @Test
    public void die() {
        rabbit.life = 0;
        rabbit.die(world);
        assertTrue(!world.contains(rabbit));
        //check if it was deleted properly
    }
}