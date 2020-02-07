import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.sprites.Pipe;
import com.mygdx.game.states.GameStateManager;
import com.mygdx.game.states.MinigameState;
import com.mygdx.game.states.PlayState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public class MinigameTest {

    MinigameState m;
    Texture mockedImg;
    SpriteBatch mockBatch;

    @Before
    public void setUp() {
        GameStateManager g = new GameStateManager();
        PlayState p = Mockito.mock(PlayState.class);
        BitmapFont f = Mockito.mock(BitmapFont.class);
        mockBatch = Mockito.mock(SpriteBatch.class);
        mockedImg = Mockito.mock(Texture.class);
        m = new MinigameState(g,p,f);
        Gdx.input = mock(Input.class);
    }

    @Test
    public void touchingPipeShouldRotateItTest() {
        //Probably not possible
        Pipe p = new Pipe(new Vector2(0,0), 5, 5, mockedImg, 0, null);
    }
}
