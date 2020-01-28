package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.sprites.Pipe;

import java.util.List;

public class MinigameState extends State {
    Pipe pipe1;
    List<Pipe> pipes;

    /**
     * Constructor to initialise the camera and mouse and set the
     * GameStateManager variable
     *
     * @param gameStateManager the class containing the stack of States
     */
    protected MinigameState(GameStateManager gameStateManager) {
        super(gameStateManager);
        pipe1 = new Pipe(new Vector2(5,5), 5, 5, new Texture("truck.png"), 1, 3);
        pipes.add(pipe1);
    }

    @Override
    public void update(float deltaTime) {
        for (Pipe pipe : pipes) {
            if (Gdx.input.isTouched() && pipe.inPipeRange()) {
                pipe.rotate();
            }
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void dispose() {

    }
}
