package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Kroy;

import java.util.Arrays;

public class Pipe extends Entity {
    private int rotation;
    private int[] correctRotations;
    private Sprite drawable;

    public Pipe (Vector2 position, int width, int height, Texture texture, int rotation, int[] correctRotations) {
        super(position, width, height, texture);
        this.rotation = rotation;
        this.correctRotations = correctRotations;
        this.drawable = new Sprite(texture);
        //This doesn't have to be a sprite, keep that in mind for testing
        this.drawable.setPosition(position.x,position.y);
        this.drawable.setRotation(rotation);
    }

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

    public void rotate() {
        rotation = (rotation + 90) % 360;
        this.drawable.setRotation(rotation);
    }

    public boolean isCorrectRotation() {
        if (Arrays.asList(correctRotations).contains(rotation)) {
            return true;
        }
        else {
            return false;
        }
    }

    public Sprite getDrawable() {
        return this.drawable;
    }

    public int getRotation() {return this.rotation;}

}
