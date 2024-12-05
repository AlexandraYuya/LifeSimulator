package itumulator.world;
<<<<<<< HEAD
import Gruppe01.Berry;
import Gruppe01.Bush;
import itumulator.executable.Program;
=======
import Gruppe01.BushBerry;
>>>>>>> 91d463ef57d06d86021ea272f8b0e68afe388fc1
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BerryTest {
    private BushBerry berry;


    @Before
    public void setUp() {
        berry = new BushBerry();


    }

    @Test public void testBerry() {
    berry.consumeBerries(world);
    assertTrue(world.getLocation(berry)= instanceof);
    }
}
