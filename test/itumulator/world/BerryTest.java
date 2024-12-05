package itumulator.world;
import Gruppe01.BushBerry;

import itumulator.executable.Program;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BerryTest {
    private BushBerry berry;
    private World world;

    @Before
    public void setUp() {
        Program program = new Program(5, 800, 500);
        world = program.getWorld();
        berry = new BushBerry();
        berry.placeInWorld(world);

    }

    @Test public void testBerry() {
        berry.consumeBerries(world);
    assertTrue();
    }
}
