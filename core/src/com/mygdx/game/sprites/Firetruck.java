package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * The class which creates a firetruck object to be controlled by the user within the PlayState.
 *
 * @author Cassie Lillystone
 */

public class Firetruck extends Character {

    private int maxWater;
    private int currentWater;
    private boolean selected;
    private boolean refilling = true;

    //Dalai Java - Repair fire engines at fire station
    private int maxHealth;
    private int currentHealth;
    public boolean canBeDamaged;
    private float prevDir = 0;

    public Firetruck(Vector2 position, int width, int height, Texture texture, int maxHealth, int range, Unit target,
                     int speed, int dps, int maxWater, boolean selected) {
        super(position, width, height, texture, maxHealth, range, target, speed, dps);
        this.maxWater = maxWater;
        this.currentWater = maxWater;
        this.selected = selected;

        //Dalai Java - Repair fire engines at fire station
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.canBeDamaged = true; // DJ - making fire engines invulnerable when in station and vulnerable when the aren't
    }

    /**
     * A method which controllers Firetruck movement depending on the direction input
     * @param direction 1 = Left, 2 = Right, 3 = Up, 4 = Down
     */
    public void move(int direction) { // 1, 2, 3, 4 --> Left, Right, Up, Down
        float deltaTime = Gdx.graphics.getDeltaTime();
        if (direction == 2) {
            //Move right
            setPosition(getPosition().x + getSpeed() * deltaTime, getPosition().y);
        } else if (direction == 1) {
            //Move left
            setPosition(getPosition().x - getSpeed() * deltaTime, getPosition().y);
        } else if (direction == 3) {
            //Move up
            setPosition(getPosition().x, getPosition().y + getSpeed() * deltaTime);
        } else if (direction == 4) {
            //Move down
            setPosition(getPosition().x, getPosition().y - getSpeed() * deltaTime);
        }
    }

    /**
     * A method which checks if the Firetruck will collide with the input object if it moves on the input direction
     * next game tick.
     * @param other the object that the collision will be checked for
     * @param direction 1 = Left, 2 = Right, 3 = Up, 4 = Down
     * @return true if the objects will collide, otherwise false
     */
    public boolean willCollide(Entity other, int direction) {

        if (direction == 1) { // left
            if (getPosition().x - getSpeed() * Gdx.graphics.getDeltaTime() >= other.getTopRight().x || getTopRight().x <= other.getPosition().x ||
                    getPosition().y >= other.getTopRight().y || getTopRight().y <= other.getPosition().y) {
                return false;
            }
        }

        else if (direction == 2) { // right

            if ((getTopRight().x + getSpeed() * Gdx.graphics.getDeltaTime() <= other.getPosition().x || getPosition().x >= other.getTopRight().x ||
                    getPosition().y >= other.getTopRight().y || getTopRight().y <= other.getPosition().y)) {
                return false;
            }
        }

        else if (direction == 3) { // up
            if (getPosition().y >= other.getTopRight().y || getTopRight().y + getSpeed() * Gdx.graphics.getDeltaTime()<= other.getPosition().y ||
                    getPosition().x >= other.getTopRight().x || getTopRight().x <= other.getPosition().x) {
                return false;
            }
        }

        else if (direction == 4) { // down
            if (getPosition().y - getSpeed() * Gdx.graphics.getDeltaTime() >= other.getTopRight().y || getTopRight().y <= other.getPosition().y ||
                    getPosition().x >= other.getTopRight().x || getTopRight().x <= other.getPosition().x) {
                return false;
            }
        }
        return true;
    }

    public int getMaxWater() {
        return maxWater;
    }

    public int getCurrentWater() {
        return currentWater;
    }

    //Dalai Java - Repair fire engines at fire station
    public int getMaxHealth() { return maxHealth; }

    public int getCurrentHealth() {
        if (currentHealth < 0){
            return 0;
        } else{
            return currentHealth;
        }
    }


    public boolean isSelected() {
        return selected;
    }

    public boolean isRefilling() {
        return refilling;
    }

    public void setRefilling(boolean refilling) { this.refilling = refilling; }

    public void setCurrentWater(int currentWater) {
        this.currentWater = currentWater;
    }

    //Dalai Java - Repair fire engines at fire station
    public void setCurrentHealth(int currentHealth){ this.currentHealth = currentHealth; }

    public void updateCurrentWater(int waterUsed) {
        if ((this.currentWater - waterUsed) < 0) {
            this.currentWater = 0;
            return;
        }
        this.currentWater -=  waterUsed;
    }

    //Dalai Java - Repair fire engines at fire station
    public void updateCurrentHealth(int healthUsed) {
        if ((this.currentHealth - healthUsed) < 0) {
            this.currentHealth = 0;
            return;
        }
        this.currentHealth -=  healthUsed;
    }


    /**
     * @author Dalai Java
     * @return float which is the direction the fire truck should point
     */
    public float truckDirection(){
        if(this.isSelected()) {
            if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) {
                prevDir = 315f;
            } else if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)) {
                prevDir = 45f;
            } else if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.S)) {
                prevDir = 90f+45f;
            } else if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) {
                prevDir = 180f+45f;
            }else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                prevDir = 0f;
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                prevDir = 90f;
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                prevDir = 180f;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                prevDir = 270f;
            }
        }
        return prevDir;
    }

    /**
     * moved movement handling to the Firetruck class
     * @author Dalai Java
     * @param obstacles requires list of obstacles to handle collisions
     */
    public void truckMovement(ArrayList<Entity> obstacles) {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {

            boolean obstacleCollision = false;
            if (this.getPosition().y >= 1043 - this.getHeight()) {
                obstacleCollision = true;
            }
            for (Entity obstacle : obstacles) {
                if (this.willCollide(obstacle, 3)) {
                    obstacleCollision = true;
                }
            }
            if (!obstacleCollision) {
                this.move(3);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            boolean obstacleCollision = false;
            if (this.getPosition().y <= 212) {
                obstacleCollision = true;
            }
            for (Entity obstacle : obstacles) {
                if (this.willCollide(obstacle, 4)) {
                    obstacleCollision = true;
                }
            }
            if (!obstacleCollision) {
                this.move(4);
            }

        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            boolean obstacleCollision = false;
            if (this.getPosition().x <= 33) {
                obstacleCollision = true;
            }
            for (Entity obstacle : obstacles) {
                if (this.willCollide(obstacle, 1)) {
                    obstacleCollision = true;
                }
            }
            if (!obstacleCollision) {
                this.move(1);
            }

        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {

            boolean obstacleCollision = false;
            if (this.getPosition().x >= 1888 - this.getWidth()) {
                obstacleCollision = true;
            }
            for (Entity obstacle : obstacles) {
                if (this.willCollide(obstacle, 2)) {
                    obstacleCollision = true;
                }
            }
            if (!obstacleCollision) {
                this.move(2);
            }

        }
    }




    public void setSelected(boolean selected) {
        this.selected = selected;
    }



}