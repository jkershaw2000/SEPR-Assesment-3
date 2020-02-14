package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The class which is used to create an animation for when the alien /engine/fortress/station is destroyed.
 *
 * Class added by Dalai Java.
 */

public class Explosion {
    public static final float FRAME_LENGTH = 0.01f; // the length of frame
    public static final int OFFSET = 16;
    public static final int SIZE = 40; // the size of frame
    private static Animation anim = null;
    float x, y;
    float statetime;
    public boolean remove = false;

    public Explosion (float x, float y){
        this.x = x - OFFSET;
        this.y = y - OFFSET;
        statetime = 0;

        if (anim == null){
            anim = new Animation(FRAME_LENGTH, TextureRegion.split(new Texture("animation.png"), SIZE, SIZE)[0]);
        }
    }

    public void update (float deltatime){
        statetime += deltatime;
        if (anim.isAnimationFinished(statetime)){
            remove = true;
        }
    }

    public void render (SpriteBatch batch){
        batch.draw((TextureRegion) anim.getKeyFrame(statetime), x, y);
    }
}
