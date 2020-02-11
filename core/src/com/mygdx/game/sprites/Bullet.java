package com.mygdx.game.sprites;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.states.PlayState;
/**
 * The class which is used to create multiple streams of water projectiles and contains their properties.
 *
 * Class added by Dalai Java.
 */

public class Bullet extends Entity {
    private Vector2 startPosition;
    private float speed;
    private Vector2 direction;
    private float length;
    private int damage;
    private float maxLength;

    public Bullet(Vector2 position, int width, int height, Texture texture, Vector2 targetCoords, float speed,
                  int damage, float maxLength) {
        super(position, width, height, texture);
        startPosition = position;
        this.speed = speed;
        this.damage = damage;
        this.maxLength =maxLength;

        //Add Random variance to the bullet direction.
        targetCoords.x *= Math.random()* (((1.05 - 0.95) + 1) + 0.95);
        targetCoords.y *= Math.random()* (((1.05 - 0.95) + 1) + 0.95);
        this.direction = new Vector2(targetCoords.x - position.x,targetCoords.y - position.y).nor();
    }

    public Bullet (Vector2 position, int width, int height, Texture texture, Vector2 targetCoords, float speed,
                   int damage) {
        super(position, width, height, texture);
        startPosition = position;
        this.speed = speed;
        this.damage = damage;
        this.direction = new Vector2(targetCoords.x - position.x,targetCoords.y - position.y).nor();
    }

    /**
     * A method which updates the bullets properties each game tick
     */
    public void update() {
        setPosition(getPosition().x + speed * direction.x, getPosition().y + speed * direction.y);
        setLength();
    }

    /**
     * A method which updates the bullets properties each game tick
     * @param instance the Unit which the method will check the bullet collision for
     * @return true if the bullet is in contact with the Unit object, otherwise false
     */
    public boolean hitUnit(Unit instance) {
        if (getTopRight().y < instance.getPosition().y || getPosition().y > instance.getTopRight().y ||
                getTopRight().x < instance.getPosition().x || getPosition().x > instance.getTopRight().x) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Calculates the distance between the current position and the startPosition of the vector to check its length.
     * Used to ensure the firetruck can only shoot in a limited range.
     */

    public void setLength() {
        this.length = getPosition().dst(startPosition);
    }

    public float getLength() {
        return length;
    }

    public float getMaxLength() {
        return maxLength;
    }

    public int getDamage() {
        return damage;
    }

    public float getSpeed() { return speed;}

    public Vector2 getDirection() { return this.direction; }
}
