import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.sprites.Alien;
import com.mygdx.game.sprites.Firetruck;
import com.mygdx.game.sprites.Pipe;
import com.mygdx.game.sprites.Unit;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PipeTest {

    Texture mockedImg;

    @Before
    public void setup() {
        mockedImg = Mockito.mock(Texture.class);
    }

    @Test
    public void pipeShouldRotateCorrectlyTest() {
        Pipe p = new Pipe(new Vector2(0,0), 5, 5, mockedImg, 0, null);
        p.rotate();
        Assertions.assertEquals(p.getRotation(), 90);
    }

    @Test
    public void rotationShouldResetToZeroTest() {
        Pipe p = new Pipe(new Vector2(0,0), 5, 5, mockedImg, 270, null);
        p.rotate();
        Assertions.assertEquals(p.getRotation(), 0);
    }

    @Test
    public void returnsTrueWhenInCorrectRotation() {
        Pipe p = new Pipe(new Vector2(0,0), 5, 5, mockedImg, 0, new int[] {0});
        Assertions.assertTrue(p.isCorrectRotation());
    }
}
