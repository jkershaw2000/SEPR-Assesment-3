import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.sprites.Pipe;
import com.mygdx.game.states.GameStateManager;
import com.mygdx.game.states.MinigameState;
import com.mygdx.game.states.PlayState;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

public class MinigameTest {

    MinigameState m;

    @Test
    public void setUp() {
        GameStateManager g = new GameStateManager();
        PlayState p = Mockito.mock(PlayState.class);
        BitmapFont f = Mockito.mock(BitmapFont.class);
        m = new MinigameState(g,p,f);
    }

    @Test
    public void straightAcrossLineShouldChooseStraightAcrossPipeTest() {
        Pipe p = m.choosePipe(new Vector2(0,0), new Vector2(1,0), new Vector2(2,0));
        Assertions.assertEquals(p.getTexture(), "straightPipe.png");
        Assertions.assertEquals(p.correctRotations, new int[] {90, 270});
    }

}
