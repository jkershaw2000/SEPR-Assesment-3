import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.sprites.Entity;
import com.mygdx.game.sprites.Firetruck;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Graphics.class})
public class FireTruckTest {
    private Entity instance;
    private boolean expected;


    //Instance of entity class to test methods on
    Entity testEntity = new Entity(new Vector2(200, 200), 100, 100, null);

    //Instance of the FireTruck class to test on
    Firetruck testFireTruck = new Firetruck(new Vector2(100, 100 ), 100, 100, null,
            103, 10, null, 10, 12, 13, true);

    //ASSESSMENT 3
    @Before
    public void setup() {
        Gdx.graphics = PowerMockito.mock(Graphics.class);
        lenient().when(Gdx.graphics.getDeltaTime()).thenReturn(1f);
    }

    //Testing basic constructor functionality with getters
    @Test
    public void constructorShouldSetCorrectParametersToVariables() {
        assertEquals(13, testFireTruck.getMaxWater());
        assertEquals(13, testFireTruck.getCurrentWater());
        assertTrue(testFireTruck.isSelected());
    }

    //Testing that updateCurrentWater works with standard input
    @Test
    public void updateCurrentWaterShouldChangeCurrentWaterStandard() {
        testFireTruck.updateCurrentWater(10);
        assertEquals(3, testFireTruck.getCurrentWater());
    }

    //Testing that update water works with boundary value to go to 0
    @Test
    public void updateCurrentWaterShouldAllowForWaterToBeZero() {
        testFireTruck.updateCurrentWater(13);
        assertEquals(0, testFireTruck.getCurrentWater());
    }

    //Testing that update water if in the negative water level sets it to 0
    @Test
    public void updateCurrentWaterShouldSetNegativeWaterLevelsToZero() {
        testFireTruck.updateCurrentWater(14);
        assertEquals(0, testFireTruck.getCurrentWater());
    }

    //ASSESSMENT 3
    @Test
    public void truckShouldMoveWhenCommandedTest() {
        testFireTruck.move(2);
        assertEquals(testFireTruck.getPosition().x, 110);
    }

    //ASSESSMENT 3
    @Test
    public void movingEquallyInAllDirectionsShouldResultInOriginalPositionTest() {
        testFireTruck.move(1);
        testFireTruck.move(2);
        testFireTruck.move(3);
        testFireTruck.move(4);
        assertEquals(testFireTruck.getPosition(), new Vector2(100,100));
    }

    //ASSESSMENT 3
    @Test
    public void willCollideReturnsTrueIfInRange() {
        Entity e = new Entity(new Vector2(105, 100), 1, 1, null);
        assertTrue(testFireTruck.willCollide(e, 2));
    }

    //ASSESSMENT 3
    @Test
    public void willCollideReturnsFalseIfNotInRange() {
        Entity e = new Entity(new Vector2(100, 300), 1, 1, null);
        assertFalse(testFireTruck.willCollide(e, 3));
    }
}
