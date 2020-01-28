package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.misc.Button;
import com.mygdx.game.sprites.Pipe;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MinigameState extends State {
    Pipe pipe1, pipe2, pipe3, pipe4;
    List<Pipe> pipes = new ArrayList<Pipe>();
    Button giveup;

    /**
     * Constructor to initialise the camera and mouse and set the
     * GameStateManager variable
     *
     * @param gameStateManager the class containing the stack of States
     */
    protected MinigameState(GameStateManager gameStateManager) {
        super(gameStateManager);
        giveup = new Button(new Texture("giveup.png"), new Texture ("giveup.png"), 190, 49, new Vector2(200,200),false,false);
        pipe1 = new Pipe(new Vector2(5,5), 32, 32, new Texture("truck.png"), 0, 0);
        pipe2 = new Pipe(new Vector2(5,150), 32, 32, new Texture("truck.png"), 0, 90);
        pipe3 = new Pipe(new Vector2(150,5), 32, 32, new Texture("truck.png"), 0, 180);
        pipe4 = new Pipe(new Vector2(150,150), 32, 32, new Texture("truck.png"), 0, 270);
        pipes.add(pipe1);
        pipes.add(pipe2);
        pipes.add(pipe3);
        pipes.add(pipe4);
    }

    @Override
    public void update(float deltaTime) {
        if (giveup.mouseInRegion() && giveup.isLocked() == false){
            giveup.setActive(true);
            if (Gdx.input.isTouched()) {
                gameStateManager.pop();
            }
        }
        else {
            giveup.setActive(false);
        }

        Boolean allCorrect = true;
        for (Pipe pipe : pipes) {
            if (Gdx.input.justTouched() && pipe.inPipeRange()) {
                pipe.rotate();
            }
            if (!pipe.isCorrectRotation()) {
                allCorrect = false;
            }
        }
        if (allCorrect) {
            gameStateManager.pop();
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        for(Pipe pipe: pipes) {
            pipe.getDrawable().draw(spriteBatch);
        }
        spriteBatch.draw(giveup.getTexture(), giveup.getPosition().x, giveup.getPosition().y, giveup.getWidth(), giveup.getHeight());
        spriteBatch.end();
    }

    @Override
    public void dispose() {

    }
}
