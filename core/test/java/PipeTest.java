import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.sprites.Pipe;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Pipe.SpriteMaker.class)
public class PipeTest {

    Texture mockedImg;

    @Before
    public void setup() {
        Sprite mockedSprite = Mockito.mock(Sprite.class);
        mockedImg = Mockito.mock(Texture.class);
        PowerMockito.mockStatic(Pipe.SpriteMaker.class);
        when(Pipe.SpriteMaker.makeSprite((Texture) any(), (Vector2) any(),
                (Integer) any())).thenReturn(mockedSprite);
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
