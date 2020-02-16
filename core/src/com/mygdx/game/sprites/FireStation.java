package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class FireStation extends Unit{

    private int health;

    public FireStation(Vector2 position, int width, int height, Texture texture, int maxHealth){
        super(position, width, height, texture, maxHealth);
        health = maxHealth;


    }

    public void damage(){
        health--;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
