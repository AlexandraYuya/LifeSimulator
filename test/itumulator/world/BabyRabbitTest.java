package itumulator.world;

import Gruppe01.AdultRabbit;
import Gruppe01.BabyRabbit;
import Gruppe01.Rabbit;
import itumulator.executable.Program;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import Gruppe01.Grass;
import static org.junit.Assert.assertNull;

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
    public void testExists() {
        assertEquals(5, babyRabbit.getLife());
        assertTrue(world.contains(babyRabbit));
    }

    @Test
    public void testGrow() {
        world.getEntities().forEach((key, value) -> Assert.assertEquals(BabyRabbit.class, key.getClass()));
        babyRabbit.grow(world);
        world.getEntities().forEach((key, value) -> Assert.assertEquals(AdultRabbit.class, key.getClass()));
    }

    @Test
    public void testHandleDay() {
        babyRabbit.setLife();
        world.getEntities().forEach((key, value) -> {
            Assert.assertEquals(BabyRabbit.class, key.getClass());
            Assert.assertEquals(3, ((BabyRabbit)key).getLife());
        });
        babyRabbit.handleDay(world);
        world.getEntities().forEach((key, value) -> {
            Assert.assertEquals(AdultRabbit.class, key.getClass());
            Assert.assertEquals(3, ((AdultRabbit)key).getLife());
        });
    }

    @Test
    public void eatTest() {
        Location rabbitLocation = world.getLocation(babyRabbit);
        Grass grass = new Grass();
        world.setTile(rabbitLocation, grass);

        int initialEnergy = babyRabbit.getEnergy();
        babyRabbit.eat(world);

        Object entityAfterEating = world.getNonBlocking(rabbitLocation);

        assertNull("Grass will be eaten", entityAfterEating);
        assertEquals("Energy increses after eating",
                initialEnergy + 5,
                babyRabbit.getEnergy());
    }

    


}
