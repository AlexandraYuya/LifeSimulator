package itumulator.world;
import Gruppe01.*;

import itumulator.executable.Program;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;
public class BearTest {
    Bear bear;
    World world;

    @Before
    public void setUp() throws Exception {
        Program program = new Program(5, 800, 1200);
        world = program.getWorld();

        bear = new Bear();
        bear.placeInWorld(world);
        bear.act(world);
    }

    @Test
    public void die() {
        bear.die(world);
        assertTrue(true);
        //check if it was deleted properly
    }

    @Test
    public void testEnergyDecrease() {
        int initialEnergy = bear.energy;
        bear.handleDay(world);
        assertEquals(initialEnergy - 1, bear.energy);
    }

    @Test
    public void testEatBerry() {
        Location bearLoc = world.getLocation(bear);
        Set<Location> validNeighbors = world.getEmptySurroundingTiles(bearLoc);

        if (!validNeighbors.isEmpty()) {
            Location nearbyLoc = validNeighbors.iterator().next();
            int initialEnergy = bear.energy;

            BushBerry berry = new BushBerry();
            world.setTile(nearbyLoc, berry);
            bear.eat(world);
            assertEquals(initialEnergy + 5, bear.energy);
        }
    }

    @Test
    public void testEatRabbit() {
        Location bearLoc = world.getLocation(bear);
        Set<Location> validNeighbors = world.getEmptySurroundingTiles(bearLoc);

        if (!validNeighbors.isEmpty()) {
            Location nearbyLoc = validNeighbors.iterator().next();
            AdultRabbit rabbit = new AdultRabbit();
            world.setTile(nearbyLoc, rabbit);
            bear.eat(world);
            assertTrue(world.getTile(nearbyLoc) instanceof Carcass);
        }
    }



}
