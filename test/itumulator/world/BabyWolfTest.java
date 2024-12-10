package itumulator.world;

import Gruppe01.BabyWolf;
import Gruppe01.Wolf;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class BabyWolfTest {
    BabyWolf babyWolf;
    Wolf alphaWolf;
    World world;

    @Before
    public void setUp() throws Exception {
        Program program = new Program(5, 800, 1200);
        world = program.getWorld();

        // Create alpha wolf first
        alphaWolf = new Wolf(alphaWolf);
        alphaWolf.placeInWorld(world);

        // Create baby wolf with alpha wolf as parent
        babyWolf = new BabyWolf(alphaWolf);
        babyWolf.placeInWorld(world);
        babyWolf.act(world);
    }

    @Test
    public void testInitialState() {
        assertEquals(15, babyWolf.getLife());
        assertTrue(world.contains(babyWolf));
        assertTrue("Baby wolf should be in alpha's pack",
                alphaWolf.getPack().contains(babyWolf));
    }

    @Test
    public void test_grow() {
        // Verify initial state is BabyWolf
        world.getEntities().forEach((key, value) -> {
            if (key instanceof BabyWolf) {
                Assert.assertEquals(BabyWolf.class, key.getClass());
            }
        });

        // Perform growth
        babyWolf.grow(world);

        // Verify the baby wolf was transformed to adult wolf
        boolean foundAdultWolf = false;
        for (Object entity : world.getEntities().keySet()) {
            if (entity instanceof Wolf && !(entity instanceof BabyWolf)) {
                foundAdultWolf = true;
                break;
            }
        }
        assertTrue("Should find an adult wolf after growth", foundAdultWolf);
        assertFalse("Baby wolf should be removed from alpha's pack after growth",
                alphaWolf.getPack().contains(babyWolf));
    }

    @Test
    public void testDisplayInformation() {
        DisplayInformation info = babyWolf.getInformation();
        assertEquals("Should use wolf-small image", "wolf-small", info.getImageKey());
        assertEquals("Should be gray color", Color.GRAY, info.getColor());
    }

    @Test
    public void testPackMembership() {
        assertTrue("Baby wolf should be in alpha's pack initially",
                alphaWolf.getPack().contains(babyWolf));
        babyWolf.grow(world);
        assertFalse("Baby wolf should not be in alpha's pack after growing",
                alphaWolf.getPack().contains(babyWolf));
    }

    @Test
    public void testLocationAfterGrowth() {
        Location initialLocation = world.getLocation(babyWolf);
        babyWolf.grow(world);

        boolean foundAdultAtSameLocation = false;
        for (Object entity : world.getEntities().keySet()) {
            if (entity instanceof Wolf && !(entity instanceof BabyWolf)) {
                if (world.getLocation(entity).equals(initialLocation)) {
                    foundAdultAtSameLocation = true;
                    break;
                }
            }
        }
        assertTrue("Adult wolf should be at the same location as the baby wolf was",
                foundAdultAtSameLocation);
    }
}