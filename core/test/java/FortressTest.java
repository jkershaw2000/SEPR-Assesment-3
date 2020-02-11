import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.sprites.Fortress;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

//66% line coverage

public class FortressTest {

    //testing to ensure the constructor worked through testing of getters
    @Test
    public void constructorShouldGiveCorrectSpawn() {
        Fortress f = new Fortress(new Vector2(100, 100), 100, 100, null,
                100, 5.0f, 1);
        assertEquals(5.0f, f.getSpawnRate());
    }

    //ASSESSMENT 3
    @Test
    public void correctAlienPositionsShouldBeAddedForGivenLevel() {
        //Instance of Fortress to ensure the constructor works as intended
        Fortress f = new Fortress(new Vector2(100, 100), 100, 100, null,
                100, 5.0f, 3);
        //Need to change this into parameterized tests
        Assertions.assertTrue(f.getAlienPositions().contains(new Vector2(833, 756)));
        Assertions.assertTrue(f.getAlienPositions().contains(new Vector2(961, 756)));
    }
}
