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

    /**
     * Constructor to initialise the camera and mouse and set the
     * GameStateManager variable
     *
     * @param gameStateManager the class containing the stack of States
     */
    protected MinigameState(GameStateManager gameStateManager) {
        super(gameStateManager);
        Random rand = new Random();
        positions = new Pipe[4][6];
        int startPos = rand.nextInt(3);
        positions[0][rand.nextInt(startPos)] = new Pipe(new Vector2(200,200 + startPos * 100), 100, 100, new Texture("StartPipe.png"), 0, 0);
        int endPos = rand.nextInt(3);
        positions[5][rand.nextInt(endPos)] = new Pipe(new Vector2(800,200 + endPos * 100), 100, 100, new Texture("StartPipe.png"), 0, 0);
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
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        for (int i = 0; i < 3; i++) {
            for (Pipe pipe : positions[i]) {
                pipe.getDrawable().draw(spriteBatch);
            }
        }
        spriteBatch.draw(giveup.getTexture(), giveup.getPosition().x, giveup.getPosition().y, giveup.getWidth(), giveup.getHeight());
        spriteBatch.end();
    }

    @Override
    public void dispose() {

    }
}
