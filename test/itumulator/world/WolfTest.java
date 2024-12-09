package itumulator.world;

import Gruppe01.AdultRabbit;
import Gruppe01.Carcass;
import Gruppe01.Wolf;
import itumulator.executable.Program;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class WolfTest {
    Wolf alphaWolf;
    Wolf packWolf;
    World world;


    @Before
    public void setUp() {
        Program program = new Program(5, 800, 1200);
        world = program.getWorld();

        alphaWolf = new Wolf(null);
        alphaWolf.placeInWorld(world);

        packWolf = new Wolf(alphaWolf);
        packWolf.placeInWorld(world);
    }


    @Test
    public void testEat() {
        Location wolfLoc = world.getLocation(alphaWolf);
        Set<Location> neighbors = world.getEmptySurroundingTiles(wolfLoc);
        Location nearbyLoc = neighbors.iterator().next();

        AdultRabbit rabbit = new AdultRabbit();
        world.setTile(nearbyLoc, rabbit);
        assertEquals(100, alphaWolf.energy);
        alphaWolf.eat(world);
        assertEquals(100, alphaWolf.energy);
        alphaWolf.eat(world);
        assertEquals(105, alphaWolf.energy);
        assertTrue(world.getTile(nearbyLoc) instanceof Carcass);
    }

    @Test
    public void followAlpha(){
        Location wolfLoc = world.getLocation(alphaWolf);
        Wolf packWolfExtra = new Wolf(alphaWolf);
        packWolfExtra.placeInWorld(world);

        Location curLocation1 = world.getLocation(packWolf);
        Location curLocation2 = world.getLocation(packWolfExtra);

        //pegar loc que nao sao alphas (em variaveis)
        //usar handleday()
        //asert not equals com a loca antiga com a atual dos que nao sao alpha



    }



}
