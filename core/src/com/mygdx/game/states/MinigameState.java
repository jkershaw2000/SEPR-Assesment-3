package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.misc.Button;
import com.mygdx.game.sprites.Pipe;
import java.util.List;
import java.util.Random;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MinigameState extends State {
    Pipe pipe1, pipe2, pipe3, pipe4;
    List<Pipe> pipes = new ArrayList<Pipe>();
    Button giveup;
    Pipe[][] positions;
    Boolean finished;
    int[] rotations = new int[] {0, 90, 180, 270};

    /**
     * Constructor to initialise the minigame
     *
     * @param gameStateManager the class containing the stack of States
     */
    protected MinigameState(GameStateManager gameStateManager) {
        super(gameStateManager);
        Random rand = new Random();
        positions = new Pipe[6][4];
        int startPos = rand.nextInt(3);
        positions[0][startPos] = new Pipe(new Vector2(400,400 + startPos * 200), 100, 100, new Texture("StartPipe.png"), 0, new int[] {0});
        int endPos = rand.nextInt(3);
        positions[5][endPos] = new Pipe(new Vector2(1600,400 + endPos * 200), 100, 100, new Texture("StartPipe.png"), 180, new int[] {180});
        giveup = new Button(new Texture("giveup.png"), new Texture ("giveup.png"), 190, 49, new Vector2(200,200),false,false);
        finished = false;
    }

    @Override
    public void update(float deltaTime) {
        if (giveup.mouseInRegion() && giveup.isLocked() == false) {
            giveup.setActive(true);
            if (Gdx.input.isTouched()) {
                gameStateManager.pop();
            }
        } else {
            giveup.setActive(false);
        }

        Boolean allCorrect = true;
        for (int i = 0; i < 3; i++) {
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
            if (allCorrect && !finished) {
                finished = true;
                gameStateManager.pop();
            }
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        for (int i = 0; i < 6; i++) {
            for (Pipe pipe : positions[i]) {
                if (!(pipe == null)) {
                    pipe.getDrawable().draw(spriteBatch);
                }
            }
        }
        spriteBatch.draw(giveup.getTexture(), giveup.getPosition().x, giveup.getPosition().y, giveup.getWidth(), giveup.getHeight());
        spriteBatch.end();
    }

    /**
     * Finds a random path from the start to the end, creating a sequence of pipes as it does so
     * @param pos The current position to be looked at
     * @param finalPos The destination
     * @param lastPos The point before the current position to be looked at
     */
    public void findPath(Vector2 pos, Vector2 finalPos, Vector2 lastPos) {
        if (pos.x == finalPos.x && pos.y == 4) {
            //If we're the pipe away from the end then add the final pipe and end
            if (pos.x == finalPos.x - 1) {
                positions[(int) pos.x][(int) pos.y] = choosePipe(lastPos, pos, finalPos);
            }
            else {
                List<Vector2> directions = new ArrayList<Vector2> (Arrays.asList(new Vector2(0,1),
                        new Vector2(0,-1), new Vector2(1,0), new Vector2(-1,0)));
                if (pos.x - 1 < 1 || !(positions[(int) pos.x - 1][(int) pos.y] == null)
                    || (pos.y + 1 > 3 || !(positions[(int) pos.x - 1][(int) pos.y + 1] == null))
                    && (pos.y - 1 < 0 || !(positions[(int) pos.x - 1][(int) pos.y - 1] == null))) {
                    directions.remove(new Vector2(-1,0));
                }
                if (pos.x + 1 > 4 || !(positions[(int) pos.x + 1][(int) pos.y] == null)
                    || (pos.y + 1 > 3 || !(positions[(int) pos.x + 1][(int) pos.y + 1] == null))
                    && (pos.y - 1 < 0 || !(positions[(int) pos.x + 1][(int) pos.y - 1] == null))) {
                    directions.remove(new Vector2(1,0));
                }
                if (pos.y + 1 > 3 || !(positions[(int) pos.x][(int) pos.y + 1] == null)
                    || (pos.x + 1 > 4 || !(positions[(int) pos.x + 1][(int) pos.y + 1] == null))
                    && (pos.x - 1 < 1 || !(positions[(int) pos.x - 1][(int) pos.y + 1] == null))) {
                    directions.remove(new Vector2(0,1));
                }
                if (pos.y - 1 < 0 || !(positions[(int) pos.x][(int) pos.y - 1] == null)
                    || (pos.x + 1 > 4 || !(positions[(int) pos.x + 1][(int) pos.y - 1] == null))
                    && (pos.x - 1 < 1 || !(positions[(int) pos.x - 1][(int) pos.y - 1] == null))) {
                    directions.remove(new Vector2(0,-1));
                }
                Vector2 nextPos = pos.add(directions.get((new Random()).nextInt(directions.size())));
                positions[(int) pos.x][(int) pos.y] = choosePipe(lastPos, pos, nextPos);
                findPath(nextPos, finalPos, pos);
            }
        }
    }

    public Pipe choosePipe(Vector2 lastPos, Vector2 currentPos, Vector2 nextPos) {
        //If last pipe was behind
        if (lastPos.x == currentPos.x - 1) {
            //Straight pipe across
            if (nextPos.x == currentPos.x + 1) {
                return new Pipe(new Vector2(400 + 200 * currentPos.x, 400 + 200 *  currentPos.y), 100,
                        100, new Texture ("straightPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {90, 270});
            }
            //Pipe bending forwards + upwards
            else if (nextPos.y == currentPos.y + 1) {
                return new Pipe(new Vector2(400 + 200 * currentPos.x, 400 + 200 *  currentPos.y), 100,
                        100, new Texture ("BendyPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {180});
            }

            //Pipe bending forwards + downwards
            else if (nextPos.y == currentPos.y - 1) {
                return new Pipe(new Vector2(400 + 200 * currentPos.x, 400 + 200 *  currentPos.y), 100,
                        100, new Texture ("BendyPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {270});
            }
        }
        //If last pipe was above
        else if (lastPos.y == currentPos.y + 1) {
            //Straight pipe down
            if (nextPos.y == currentPos.y - 1) {
                return new Pipe(new Vector2(400 + 200 * currentPos.x, 400 + 200 *  currentPos.y), 100,
                        100, new Texture ("StraightPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {0, 180});
            }
            //Pipe bending down and to the right
            else if (nextPos.x == currentPos.x + 1) {
                return new Pipe(new Vector2(400 + 200 * currentPos.x, 400 + 200 *  currentPos.y), 100,
                        100, new Texture ("BendyPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {90});
            }
            //Pipe bending down and to the left
            else if (nextPos.x == currentPos.x - 1) {
                return new Pipe(new Vector2(400 + 200 * currentPos.x, 400 + 200 *  currentPos.y), 100,
                        100, new Texture ("BendyPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {180});
                }
            }
        //If last pipe was below
        else if (lastPos.y == currentPos.y - 1) {
            //Straight pipe up
            if (nextPos.y == currentPos.y + 1) {
                return new Pipe(new Vector2(400 + 200 * currentPos.x, 400 + 200 *  currentPos.y), 100,
                        100, new Texture ("StraightPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {0, 180});
            }
            //Pipe bending up and to the right
            else if (nextPos.x == currentPos.x + 1) {
                return new Pipe(new Vector2(400 + 200 * currentPos.x, 400 + 200 *  currentPos.y), 100,
                        100, new Texture ("BendyPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {0});
            }
            //Pipe bending up and to the left
            else if (nextPos.x == currentPos.x - 1) {
                return new Pipe(new Vector2(400 + 200 * currentPos.x, 400 + 200 *  currentPos.y), 100,
                        100, new Texture ("BendyPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {270});
            }
        }
        //If last pipe was to the right
        else if (lastPos.x == currentPos.x + 1) {
            //Straight pipe across
            if (nextPos.x == currentPos.x- 1) {
                return new Pipe(new Vector2(400 + 200 * currentPos.x, 400 + 200 *  currentPos.y), 100,
                        100, new Texture ("StraightPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {90, 270});
            }
            //Pipe bending to the left and up
            else if (nextPos.y == currentPos.y + 1) {
                return new Pipe(new Vector2(400 + 200 * currentPos.x, 400 + 200 *  currentPos.y), 100,
                        100, new Texture ("BendyPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {90});
            }
            //Pipe bending to the left and down
            else if (nextPos.y == currentPos.y - 1) {
                return new Pipe(new Vector2(400 + 200 * currentPos.x, 400 + 200 *  currentPos.y), 100,
                        100, new Texture ("BendyPipe.png"),
                        rotations[(new Random()).nextInt(rotations.length)], new int[] {0});
            }
        }
        return null;
    }

    @Override
    public void dispose() {

    }
}
