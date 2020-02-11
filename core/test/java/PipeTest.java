import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.sprites.Pipe;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Pipe.SpriteMaker.class)
//ASSESSMENT 3 - Created as part of implementing minigame
public class PipeTest {

    @Mock
    Texture mockedImg;

    @Before
    public void setup() {
        Sprite mockedSprite = Mockito.mock(Sprite.class);
        mockedImg = Mockito.mock(Texture.class);
        PowerMockito.mockStatic(Pipe.SpriteMaker.class);
        when(Pipe.SpriteMaker.makeSprite(any(Texture.class), any(Vector2.class),
                anyInt())).thenReturn(mockedSprite);
        Gdx.input = mock(Input.class);
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
    public void pipeShouldReturnTrueWhenInCorrectRotation() {
        Pipe p = new Pipe(new Vector2(0,0), 5, 5, mockedImg, 0, new int[] {0});
        Assertions.assertTrue(p.isCorrectRotation());
    }

    @Test
    public void pipeShouldReturnFalseWhenNotInCorrectRotation() {
        Pipe p = new Pipe(new Vector2(0,0), 5, 5, mockedImg, 0, new int[] {90});
        Assertions.assertFalse(p.isCorrectRotation());
    }

    @Test
    public void pipeShouldReturnTrueWhenInRangeTest() {
        when(Gdx.input.getX()).thenReturn(50);
        when(Gdx.input.getY()).thenReturn(1030);
        Pipe p = new Pipe(new Vector2(45,45), 20, 20, mockedImg, 0, null);
        Assertions.assertTrue(p.inPipeRange());
    }

    @Test
    public void pipeShouldReturnFalseWhenNotInRangeTest() {
        when(Gdx.input.getX()).thenReturn(50);
        when(Gdx.input.getY()).thenReturn(1030);
        Pipe p = new Pipe(new Vector2(100,100), 20, 20, mockedImg, 0, null);
        Assertions.assertFalse(p.inPipeRange());
    }
}
