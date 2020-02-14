import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.sprites.Entity;
import com.mygdx.game.sprites.Firetruck;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Graphics.class, Input.class})
public class FireTruckTest {

    //Instance of entity class to test methods on
    Entity testEntity = new Entity(new Vector2(200, 200), 100, 100, null);

    //Instance of the FireTruck class to test on
    Firetruck testFireTruck = new Firetruck(new Vector2(100, 100 ), 100, 100, null,
            103, 10, null, 10, 12, 13, true);

    //ASSESSMENT 3
    @Before
    public void setup() {
        Gdx.graphics = PowerMockito.mock(Graphics.class);
        Gdx.input = PowerMockito.mock(Input.class);
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
    public void willCollideShouldReturnTrueIfInRangeTest() {
        Entity e = new Entity(new Vector2(105, 100), 1, 1, null);
        assertTrue(testFireTruck.willCollide(e, 2));
    }

    //ASSESSMENT 3
    @Test
    public void willCollideShouldReturnFalseIfNotInRangeTest() {
        Entity e = new Entity(new Vector2(100, 300), 1, 1, null);
        assertFalse(testFireTruck.willCollide(e, 3));
    }

    //ASSESSMENT 3
    @Test
    public void truckShouldChangeDirectionWhenKeyPressedTest() {
        testFireTruck.setSelected(true);
        lenient().when(Gdx.input.isKeyPressed(Input.Keys.D)).thenReturn(true);
        assertEquals(testFireTruck.truckDirection(), 270f);
    }

    //ASSESSMENT 3
    @Test
    public void truckShouldTurnDiagonalWhenTwoKeysPressedTest() {
        testFireTruck.setSelected(true);
        lenient().when(Gdx.input.isKeyPressed(Input.Keys.W)).thenReturn(true);
        lenient().when(Gdx.input.isKeyPressed(Input.Keys.A)).thenReturn(true);
        assertEquals(testFireTruck.truckDirection(), 45f);
    }

    @Test
    public void truckShouldReturnZeroIfNegativeHealthTest() {
        testFireTruck.setCurrentHealth(-1);
        assertEquals(testFireTruck.getCurrentHealth(),0);
    }

    //ASSESSMENT 3
    @Test
    public void truckShouldMoveWhenAbleToTest() {
        lenient().when(Gdx.input.isKeyPressed(Input.Keys.W)).thenReturn(true);
        testFireTruck.truckMovement(new ArrayList<Entity>());
        assertEquals(testFireTruck.getPosition(), new Vector2(100,110));
    }

    //ASSESSMENT 3
    @ParameterizedTest
    @CsvSource(value = {"100, 1500, 51","100, 200, 47",
            "30, 250, 29", "1900, 250, 32"})
    public void truckShouldNotMoveWhenOffScreenTest(int x, int y, int key) {
        Gdx.input = PowerMockito.mock(Input.class);
        lenient().when(Gdx.input.isKeyPressed(key)).thenReturn(true);
        testFireTruck.setPosition(x, y);
        testFireTruck.truckMovement(new ArrayList<Entity>());
        assertEquals(testFireTruck.getPosition(), new Vector2(x,y));
    }

    //ASSESSMENT 3
    @ParameterizedTest
    @CsvSource(value = {"51, 100, 110", "47, 100, 90", "29, 90, 100", "32, 110, 100"})
    public void truckShouldNotMoveWhenTouchingObstacleTest(int key, int x, int y) {
        Gdx.input = PowerMockito.mock(Input.class);
        Gdx.graphics = PowerMockito.mock(Graphics.class);
        lenient().when(Gdx.input.isKeyPressed(key)).thenReturn(true);
        lenient().when(Gdx.graphics.getDeltaTime()).thenReturn(1f);
        testFireTruck.truckMovement(new ArrayList<>(Arrays.asList(
                new Entity(new Vector2(x,y), 10, 10, null))));
        assertEquals(testFireTruck.getPosition(), new Vector2(100,100));
    }
}
