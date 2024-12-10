package itumulator.world;

import Gruppe01.Carcass;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import itumulator.world.World;
import itumulator.world.Location;
import itumulator.executable.DisplayInformation;
import java.awt.Color;

public class CarcassTest {
    private World world;
    private static final int WORLD_SIZE = 10;

    @Before
    public void setUp() {
        world = new World(WORLD_SIZE);
    }

    @Test
    public void testConstructorWithParameters() {
        Carcass carcass = new Carcass(true, 5);
        assertTrue("Carcass should be small", carcass.isSmall);
        assertEquals("Amount should be 5", 5, carcass.getAmount());
    }

    @Test
    public void testDefaultConstructor() {
        Carcass carcass = new Carcass();
        assertEquals("Default amount should be 11", 11, carcass.getAmount());
    }

    @Test
    public void testGetInformationLargeCarcass() {
        Carcass carcass = new Carcass(false, 11);
        DisplayInformation info = carcass.getInformation();
        assertEquals("Large carcass should have dark gray color", Color.DARK_GRAY, info.getColor());
        assertEquals("Large carcass should have correct image string", "carcass", info.getImageKey());
    }

    @Test
    public void testGetInformationSmallCarcass() {
        Carcass carcass = new Carcass(true, 5);
        DisplayInformation info = carcass.getInformation();
        assertEquals("Small carcass should have black color", Color.BLACK, info.getColor());
        assertEquals("Small carcass should have correct image string", "carcass-small", info.getImageKey());
    }

    @Test
    public void testActMethodDecay() {
        Carcass carcass = new Carcass(true, 5);
        world.setTile(new Location(0, 0), carcass);

        // Simulate 20 steps (one day)
        for (int i = 0; i < 20; i++) {
            carcass.act(world);
        }

        assertEquals("Amount should decrease by 1 after 20 steps", 4, carcass.getAmount());
    }

    @Test
    public void testEatCarcass() {
        Carcass carcass = new Carcass(true, 2);
        world.setTile(new Location(0, 0), carcass);

        carcass.eatCarcass(world);
        assertEquals("Amount should decrease by 1 after eating", 1, carcass.getAmount());

        carcass.eatCarcass(world);
        assertNull("Carcass should be removed from world when amount reaches 0",
                world.getTile(new Location(0, 0)));
    }

    @Test
    public void testPlaceInWorld() {
        Carcass carcass = new Carcass(true, 5);
        carcass.placeInWorld(world);

        boolean found = false;
        for (int x = 0; x < WORLD_SIZE; x++) {
            for (int y = 0; y < WORLD_SIZE; y++) {
                if (world.getTile(new Location(x, y)) == carcass) {
                    found = true;
                    break;
                }
            }
        }
        assertTrue("Carcass should be placed somewhere in the world", found);
    }

    @Test
    public void testMultipleDecayCycles() {
        Carcass carcass = new Carcass(true, 3);
        world.setTile(new Location(0, 0), carcass);

        // Simulate 40 steps (2 days)
        for (int i = 0; i < 40; i++) {
            carcass.act(world);
        }

        assertEquals("Amount should decrease by 2 after 40 steps", 1, carcass.getAmount());
    }

}