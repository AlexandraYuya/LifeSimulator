package itumulator.world;
import itumulator.executable.Program;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BerryTest {
    private Berry berry;
    World world;


    @Before
    public void setUp() {
        Program program = new Program(5, 800, 500);
        world = program.getWorld();
        berry = new Berry();
        berry.placeInWorld(world);

    }

    @Test public void testBerry() {
    berry.consumeBerries(world);
    assertTrue(curren);
    }
}
