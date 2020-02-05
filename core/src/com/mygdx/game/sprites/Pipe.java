package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Kroy;

import java.util.Arrays;

//ASSESSMENT 3 - Created as part of implementing minigame
/**
 * The class which is used to create a pipe for the minigame and includes all attributes and methods relating to an
 * individual pipe
 *
 * @author Peter Clark
 */
public class Pipe extends Entity {
    public int rotation;
    public int[] correctRotations;
    private Sprite drawable;

    public Pipe (Vector2 position, int width, int height, Texture texture, int rotation, int[] correctRotations) {
        super(position, width, height, texture);
        this.rotation = rotation;
        this.correctRotations = correctRotations;
        this.drawable = SpriteMaker.makeSprite(texture, position, rotation);
    }

    /**
     * A class which creates a sprite given a number of attributes required for that sprite
     */
    public static class SpriteMaker {
        public static Sprite makeSprite(Texture texture, Vector2 position, int rotation) {
            Sprite sprite = new Sprite(texture);
            sprite.setPosition(position.x,position.y);
            sprite.setRotation(rotation);
            return sprite;
        }
    }

    /**
     * Checks whether the mouse is currently hovering over the pipe
     * @return true if so, false otherwise
     */
    public Boolean inPipeRange() {
        Vector2 mousePos = new Vector2(Gdx.input.getX(), Kroy.HEIGHT - Gdx.input.getY());
        if (mousePos.x >= (getPosition().x) && mousePos.x <= (getPosition().x + getWidth())
                && mousePos.y >= (getPosition().y) && mousePos.y <= (getPosition().y
                + getHeight())) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Changes the given rotation of the pipe and the SpriteBatch containing the pipe
     */
    public void rotate() {
        rotation = (rotation + 90) % 360;
        this.drawable.setRotation(rotation);
    }

    /**
     * Checks whether the current rotation of the pipe is one of the correct rotations
     * @return true if it is correct, false otherwise
     */
    public boolean isCorrectRotation() {
        for (int i = 0; i < correctRotations.length; i++) {

            if (correctRotations[i] == rotation) {
                return true;
            }
        }
        return false;
    }

    public Sprite getDrawable() {
        return this.drawable;
    }

    public int getRotation() {return this.rotation;}

}
