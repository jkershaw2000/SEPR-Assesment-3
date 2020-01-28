package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Kroy;

public class Pipe extends Entity {
    private int rotation;
    private int correctRotation;
    private Sprite drawable;

    public Pipe (Vector2 position, int width, int height, Texture texture, int rotation, int correctRotation) {
        super(position, width, height, texture);
        this.rotation = rotation;
        this.correctRotation = correctRotation;
        this.drawable = new Sprite(texture);
        this.drawable.setPosition(position.x,position.y);
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
        if (rotation == correctRotation) {
            return true;
        }
        else {
            return false;
        }
    }

    public Sprite getDrawable() {
        return this.drawable;
    }

}
