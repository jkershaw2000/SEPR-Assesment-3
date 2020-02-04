import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.sprites.Pipe;
import com.mygdx.game.states.GameStateManager;
import com.mygdx.game.states.MinigameState;
import com.mygdx.game.states.PlayState;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

public class MinigameTest {

    @Test
    public void setUp() {
        GameStateManager g = new GameStateManager();
        PlayState p = Mockito.mock(PlayState.class);
        MinigameState m = new MinigameState(g,p);
    }


}
