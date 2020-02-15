package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.misc.Button;
import com.mygdx.game.misc.Stopwatch;
import com.mygdx.game.sprites.Pipe;
import java.util.List;
import java.util.Random;
import com.badlogic.gdx.utils.Timer;

import java.util.*;

//ASSESSMENT 3 - Created as part orf implementing minigame
/**
 * An implementation of the abstract class which controls the minigame. Creates a pipe-connecting game using the Pipe
 * class
 *
 * @author Peter Clark
 */
public class MinigameState extends State {
    Button giveup;
    Pipe[][] positions;
    String finished;
    int[] rotations = new int[] {0, 90, 180, 270};
    Texture minigameComplete;
    Texture minigameLost;
    PlayState playState;
    Stopwatch stopwatch;
    BitmapFont font;

    /**
     * Constructor to initialise the minigame
     *
     * @param gameStateManager the class containing the stack of States
     * @param playState the PlayState the minigame is being initialised on top of
     * @param font the font used to write the time and instructions onto the screen
     */
    public MinigameState(GameStateManager gameStateManager, PlayState playState, BitmapFont font) {
        super(gameStateManager);
        this.font = font;
        this.playState = playState;
        Random rand = new Random();
        positions = new Pipe[6][4];
        int startPos = rand.nextInt(3);
        positions[0][startPos] = new Pipe(new Vector2(500,400 + startPos * 100), 100, 100, new Texture("StartPipe.png"), 0, new int[] {0});
        int endPos = rand.nextInt(3);
        positions[5][endPos] = new Pipe(new Vector2(1000,400 + endPos * 100), 100, 100, new Texture("StartPipe.png"), 180, new int[] {180});
        //If we reach a dead end (unlikely but possible), find another path until one is found
        Boolean pathFound = false;
        stopwatch = new Stopwatch(60);
        while (!pathFound) {
            pathFound = findPath(new Vector2(1, startPos), new Vector2(5, endPos), new Vector2(0, startPos));
            System.out.println(pathFound);
            if (!pathFound) {
                for (int i = 1; i < 5; i++) {
                    for (int j = 0; j < 4; j++) {
                        positions[i][j] = null;
                    }
                }
            }
        }
        fillScreen();
        giveup = new Button(new Texture("giveup.png"), new Texture ("giveup.png"), 190, 49, new Vector2(750,200),false,false);
        minigameComplete = new Texture("MinigameComplete.png");
        minigameLost = new Texture("MinigameLost.png");
        finished = null;
    }

    /**
     * Updates the game logic before the next render() is called.
     * @param deltaTime the amount of time which has passed since the last render() call
     */
    @Override
    public void update(float deltaTime) {
        stopwatch.update();
        if (giveup.mouseInRegion() && giveup.isLocked() == false) {
            giveup.setActive(true);
            if (Gdx.input.isTouched()) {
                playState.setMinigameWon(false);
                gameStateManager.pop();
            }
        } else {
            giveup.setActive(false);
        }
    }

    /**
     * Used to draw elements onto the screen.
     * @param spriteBatch a container for all elements which need rendering to the screen.
     */
    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        stopwatch.drawTime(spriteBatch, font);
        font.draw(spriteBatch, "Connect the pipes!" , 700, 900);
        for (int i = 0; i < 6; i++) {
            for (Pipe pipe : positions[i]) {
                if (!(pipe == null)) {
                    pipe.getDrawable().draw(spriteBatch);
                }
            }
        }
        spriteBatch.draw(giveup.getTexture(), giveup.getPosition().x, giveup.getPosition().y, giveup.getWidth(), giveup.getHeight());

        Boolean allCorrect = true;
        for (int i = 0; i < 6; i++) {
            for (Pipe pipe : positions[i]) {
                if(!(pipe == null)) {
                    if (Gdx.input.justTouched() && pipe.inPipeRange()) {
                        pipe.rotate();
                    }
                    if (!pipe.isCorrectRotation()) {
                        allCorrect = false;
                    }
                }
            }
        }
        if (finished == "won") {
            //Makes the 'minigame complete' sprite appear
            spriteBatch.draw(minigameComplete, 600, 600, 503, 73);
        }
        if (finished == "lost") {
            //Makes the 'minigame complete' sprite appear
            spriteBatch.draw(minigameLost, 700, 600, 330, 73);
        }
        spriteBatch.end();
        if (allCorrect && finished == null) {
            finished = "won";
            playState.setMinigameWon(true);
            //Sleeps for one second
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    gameStateManager.pop();
                }
            }, 1);
            //Returns to the game
        }
        if (stopwatch.getTime() > 60 && finished == null) {
            finished = "lost";
            playState.setMinigameWon(false);
            //Sleeps for one second
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    gameStateManager.pop();
                }
            }, 1);
            //Returns to the game
        }
    }

    /**
     * Finds a random path from the start to the end, creating a sequence of pipes as it does so
     * @param pos The current position to be looked at
     * @param finalPos The destination
     * @param lastPos The point before the current position to be looked at
     */
    public Boolean findPath(Vector2 pos, Vector2 finalPos, Vector2 lastPos) {
        if (pos.y == finalPos.y && pos.x == 4) {
            //If we're the pipe away from the end then add the final pipe and end
            positions[(int) pos.x][(int) pos.y] = choosePipe(lastPos, pos, finalPos);
            System.out.println("End reached");
            return true;
        }
        else {
            List<Vector2> directions = new ArrayList<Vector2> (Arrays.asList(new Vector2(0,1),
                    new Vector2(0,-1), new Vector2(1,0), new Vector2(-1,0)));
            //Check if left movement is valid
            if (pos.x - 1 < 1 || !(positions[(int) pos.x - 1][(int) pos.y] == null)
                    || ((pos.y + 1 > 3 || !(positions[(int) pos.x - 1][(int) pos.y + 1] == null))
                    && (pos.y - 1 < 0 || !(positions[(int) pos.x - 1][(int) pos.y - 1] == null)))) {
                directions.remove(new Vector2(-1,0));
            }
            //Check if right movement is valid
            if (pos.x + 1 > 4 || !(positions[(int) pos.x + 1][(int) pos.y] == null)
                    || ((pos.y + 1 > 3 || !(positions[(int) pos.x + 1][(int) pos.y + 1] == null))
                    && (pos.y - 1 < 0 || !(positions[(int) pos.x + 1][(int) pos.y - 1] == null))
                    && (pos.x + 2 > 4 || !(positions[(int) pos.x + 2][(int) pos.y] == null)))) {
                directions.remove(new Vector2(1,0));
            }
            //Check if up movement is valid
            if (pos.y + 1 > 3 || !(positions[(int) pos.x][(int) pos.y + 1] == null)
                    || ((pos.x + 1 > 4 || !(positions[(int) pos.x + 1][(int) pos.y + 1] == null))
                    && (pos.x - 1 < 1 || !(positions[(int) pos.x - 1][(int) pos.y + 1] == null))
                    && (pos.y + 2 > 3 || !(positions[(int) pos.x][(int) pos.y + 2] == null)))) {
                directions.remove(new Vector2(0,1));
            }
            //Check if down movement is valid
            if (pos.y - 1 < 0 || !(positions[(int) pos.x][(int) pos.y - 1] == null)
                    || ((pos.x + 1 > 4 || !(positions[(int) pos.x + 1][(int) pos.y - 1] == null))
                    && (pos.x - 1 < 1 || !(positions[(int) pos.x - 1][(int) pos.y - 1] == null))
                    && (pos.y - 2 < 0 || !(positions[(int) pos.x][(int) pos.y - 2] == null)))) {
                directions.remove(new Vector2(0,-1));
            }
            //If we reach a dead end, return false
            if (directions.size() == 0) {
                return false;
            }
            //Else find next grid position in path
            else {
                Vector2 nextPos = pos.cpy();
                nextPos.add(directions.get((new Random()).nextInt(directions.size())));
                positions[(int) pos.x][(int) pos.y] = choosePipe(lastPos, pos, nextPos);
                return findPath(nextPos, finalPos, pos);
            }
        }
    }

    /**
     * Chooses which pipe should be used to connect the current square to the one before it and the one after it
     * @param lastPos the point in the path before the one we are placing a pipe on
     * @param currentPos the point in the path we are placing a pipe on
     * @param nextPos the point in the path after the one we are placing a pipe on
     * @return the pipe which is required in the position currentPos
     */
    public Pipe choosePipe(Vector2 lastPos, Vector2 currentPos, Vector2 nextPos) {
        //If last pipe was behind
        if (lastPos.x == currentPos.x - 1) {
            //Straight pipe across
            if (nextPos.x == currentPos.x + 1) {
                return new Pipe(new Vector2(500 + 100 * currentPos.x, 400 + 100 *  currentPos.y), 100,
                        100, new Texture ("straightPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {90, 270});
            }
            //Pipe bending forwards + upwards
            else if (nextPos.y == currentPos.y + 1) {
                return new Pipe(new Vector2(500 + 100 * currentPos.x, 400 + 100 *  currentPos.y), 100,
                        100, new Texture ("BendyPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {180});
            }

            //Pipe bending forwards + downwards
            else if (nextPos.y == currentPos.y - 1) {
                return new Pipe(new Vector2(500 + 100 * currentPos.x, 400 + 100 *  currentPos.y), 100,
                        100, new Texture ("BendyPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {270});
            }
        }
        //If last pipe was above
        else if (lastPos.y == currentPos.y + 1) {
            //Straight pipe down
            if (nextPos.y == currentPos.y - 1) {
                return new Pipe(new Vector2(500 + 100 * currentPos.x, 400 + 100 *  currentPos.y), 100,
                        100, new Texture ("StraightPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {0, 180});
            }
            //Pipe bending down and to the right
            else if (nextPos.x == currentPos.x + 1) {
                return new Pipe(new Vector2(500 + 100 * currentPos.x, 400 + 100 *  currentPos.y), 100,
                        100, new Texture ("BendyPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {90});
            }
            //Pipe bending down and to the left
            else if (nextPos.x == currentPos.x - 1) {
                return new Pipe(new Vector2(500 + 100 * currentPos.x, 400 + 100 *  currentPos.y), 100,
                        100, new Texture ("BendyPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {180});
                }
            }
        //If last pipe was below
        else if (lastPos.y == currentPos.y - 1) {
            //Straight pipe up
            if (nextPos.y == currentPos.y + 1) {
                return new Pipe(new Vector2(500 + 100 * currentPos.x, 400 + 100 *  currentPos.y), 100,
                        100, new Texture ("StraightPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {0, 180});
            }
            //Pipe bending up and to the right
            else if (nextPos.x == currentPos.x + 1) {
                return new Pipe(new Vector2(500 + 100 * currentPos.x, 400 + 100 *  currentPos.y), 100,
                        100, new Texture ("BendyPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {0});
            }
            //Pipe bending up and to the left
            else if (nextPos.x == currentPos.x - 1) {
                return new Pipe(new Vector2(500 + 100 * currentPos.x, 400 + 100 *  currentPos.y), 100,
                        100, new Texture ("BendyPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {270});
            }
        }
        //If last pipe was to the right
        else if (lastPos.x == currentPos.x + 1) {
            //Straight pipe across
            if (nextPos.x == currentPos.x- 1) {
                return new Pipe(new Vector2(500 + 100 * currentPos.x, 400 + 100 *  currentPos.y), 100,
                        100, new Texture ("StraightPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {90, 270});
            }
            //Pipe bending to the left and up
            else if (nextPos.y == currentPos.y + 1) {
                return new Pipe(new Vector2(500 + 100 * currentPos.x, 400 + 100 *  currentPos.y), 100,
                        100, new Texture ("BendyPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {90});
            }
            //Pipe bending to the left and down
            else if (nextPos.y == currentPos.y - 1) {
                return new Pipe(new Vector2(500 + 100 * currentPos.x, 400 + 100 *  currentPos.y), 100,
                        100, new Texture ("BendyPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {0});
            }
        }
        return null;
    }

    /**
     * Fills any square which is not blank with a random pipe
     * These pipes will be accepted with any rotation so do not affect the game
     */
    public void fillScreen() {
        String[] typesOfPipe = new String[] {"StraightPipe.png", "BendyPipe.png"};
        for (int i = 1; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                if (positions[i][j] == null) {
                    positions[i][j] = new Pipe(new Vector2(500 + 100 * i, 400 + 100 * j), 100,
                            100, new Texture (typesOfPipe[(new Random()).nextInt(typesOfPipe.length)]),
                            rotations[(new Random()).nextInt(rotations.length)], new int[] {0, 90, 180, 270});
                }
            }
        }
    }


    @Override
    public void dispose() {

    }
}
