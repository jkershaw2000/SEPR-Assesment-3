package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Kroy;
import com.mygdx.game.misc.Button;
import com.mygdx.game.misc.Stopwatch;
import com.mygdx.game.sprites.*;


import java.util.ArrayList;
import java.util.Random;

/**
 * Implementation of the abstract class State which contains the methods and attributes required to control the
 * gameplay logic for each level as well as rendering to the screen.
 *
 * @author Lucy Ivatt and Bethany Gilmore
 */

public class PlayState extends State {
    //Dalai Java - explosion
    ArrayList<Explosion> explosions;

    private final float GAME_WIDTH = 1856;
    private final float GAME_HEIGHT = 832;

    private Texture background;
    private Texture map;

    private boolean levelLost;
    private boolean levelWon;
    private Preferences saveData;

    private Button quitLevel;
    private Button quitGame;

    private Stopwatch stopwatch;
    private float alienSpawnCountdown;
    private float timeSinceAlienKilled;
    private float timeSinceLastFortressRegen;
    private float timeLimit;
    private float timeTaken;

    private Entity fireStation;
    private Fortress fortress;
    private Firetruck firetruck1;
    private Firetruck firetruck2;
    // Code Added by Dalai Java
    private Firetruck firetruck3;
    private Firetruck firetruck4;

    private ArrayList<Entity> obstacles = new ArrayList<Entity>();
    public ArrayList<Firetruck> firetrucks = new ArrayList<Firetruck>();
    private ArrayList<Firetruck> destroyedFiretrucks = new ArrayList<Firetruck>();
    private ArrayList<Alien> aliens = new ArrayList<Alien>();
    private ArrayList<Projectile> bullets = new ArrayList<Projectile>();
    private ArrayList<Projectile> water = new ArrayList<Projectile>();

    // Dalia Java - array list of fire stations position
    private ArrayList<Vector2> fireStationPositions = new ArrayList<Vector2>();

    //Dalai Java - Repair fire engines at fire station
    private ArrayList<Projectile> health = new ArrayList<Projectile>();

    private BitmapFont ui;
    private BitmapFont healthBars;
    private String level;

    private Sound waterShoot = Gdx.audio.newSound(Gdx.files.internal("honk.wav"));

    private Boolean minigameWon = false;

    public PlayState(GameStateManager gsm, int levelNumber) {
        super(gsm);

        //Dalai Java - explosion
        explosions = new ArrayList<Explosion>();

        background = new Texture("LevelProportions.png");

        quitLevel = new Button(new Texture("PressedQuitLevel.png"),
                new Texture("NotPressedQuitLevel.png"),350 / 2, 100 / 2,
                new Vector2(30, 30), false, false);

        quitGame = new Button(new Texture("PressedQuitGame.png"),
                new Texture("NotPressedQuitGame.png"), 350 / 2, 100 / 2,
                new Vector2(1920 - 30 - 350 / 2, 30), false, false);

        level = Integer.toString(levelNumber); // Used as a key when saving level progress

        levelLost = false;
        levelWon = false;

        saveData = Gdx.app.getPreferences("Kroy");

        ui = new BitmapFont(Gdx.files.internal("font.fnt"));
        ui.setColor(Color.DARK_GRAY);

        healthBars = new BitmapFont();

        alienSpawnCountdown = 0;
        timeSinceLastFortressRegen = 0;
        timeSinceAlienKilled = -1;


        Vector2 firetruck1pos = null;
        Vector2 firetruck2pos = null;
        // Code Added by Dalai Java
        Vector2 firetruck3pos = null;
        Vector2 firetruck4pos = null;

        if (levelNumber == 1) { // Bottom left coordinate of map --> (33, 212) Each grid square = 32px

            firetruck1pos = new Vector2(33 + 10 * 32, 212 + 6 * 32);
            firetruck2pos = new Vector2(33 + 11 * 32, 212 + 6 * 32);

            // Code Added by Dalai Java - defining positions of the two additional fire engines
            firetruck3pos = new Vector2(33 + 10 * 32, 212 + 5 * 32);
            firetruck4pos = new Vector2(33 + 11 * 32, 212 + 5 * 32);

            timeLimit = 90;
            map = new Texture("level1background.png");

            // Level 1 Obstacles - These are used to create the hit boxes for the buildings so that the player
            // can't drive through them. You can create any rectangular hit box as one singular entity. To calculate
            // the coords do:

            // X_COORD = 33 + (GRID_X * 32)    and    Y_COORD = 212 + (GRID_Y * 32)
            // Where (33, 212) is the bottom left corner of the game screen and GRID_X, and GRID_Y is the grid position
            // of the bottom left corner of the hit box you want to create. These are multiplied by 32 as each grid
            // square is 32 pixels in both height and width.

            { obstacles.add(new Entity(new Vector2(257, 628), 64, 64, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(257, 724), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(289, 756), 32, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(257, 820), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(257, 564), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(289, 532), 32, 32, new Texture("teal.jpg")));

            obstacles.add(new Entity(new Vector2(513, 532), 32, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(513, 564), 64, 32, new Texture("teal.jpg")));

            obstacles.add(new Entity(new Vector2(577, 692), 32, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(577, 724), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(577, 436), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(609, 468), 32, 32, new Texture("teal.jpg")));

            obstacles.add(new Entity(new Vector2(737, 404), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(737, 692), 64, 64, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(833, 404), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(929, 404), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1025, 404), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1057, 436), 32, 32, new Texture("teal.jpg")));

            obstacles.add(new Entity(new Vector2(1121, 404), 64, 64, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1121, 500), 32, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1121, 532), 64, 32, new Texture("teal.jpg")));

            obstacles.add(new Entity(new Vector2(1217, 404), 64, 64, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1217, 532), 64, 32, new Texture("teal.jpg")));

            obstacles.add(new Entity(new Vector2(1345, 436), 32, 96, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1377, 468), 32, 96, new Texture("teal.jpg")));

            obstacles.add(new Entity(new Vector2(961, 692), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1249, 692), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1249, 628), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1345, 692), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1345, 628), 64, 32, new Texture("teal.jpg")));

            obstacles.add(new Entity(new Vector2(33 + 24 * 32, 212 + 22 * 32), 6 * 32, 4 * 32,
                    new Texture("teal.jpg"))); }

            // Level 1 Firestation
            fireStation = new FireStation(new Vector2(33 + 8 * 32, 212 + 4 * 32), 128, 128,
                    new Texture("teal.jpg"), 1000);

            // Level 1 Fortress
            fortress = new Fortress(new Vector2(33 + 24 * 32, 212 + 22 * 32), 6 * 32, 4 * 32,
                    new Texture("grey.png"), 10000, 1.5f, 1);
        }

        else if (levelNumber == 2) {

            firetruck1pos = new Vector2(33 + 2 * 32, 212 + 4 * 32);
            firetruck2pos = new Vector2(33 + 2 * 32, 212 + 5 * 32);

            // PlaceHolders
            firetruck3pos = new Vector2(33 + 2 * 32, 212 + 6 * 32);
            firetruck4pos = new Vector2(33 + 2 * 32, 212 + 6 * 32);

            timeLimit = 120;
            map = new Texture("level2background.png");

            // Level 2 Obstacles - These are used to create the hit boxes for the buildings so that the player
            // can't drive through them. You can create any rectangular hit box as one singular entity. To calculate
            // the coords do:

            // X_COORD = 33 + (GRID_X * 32)    and    Y_COORD = 212 + (GRID_Y * 32)
            // Where (33, 212) is the bottom left corner of the game screen and GRID_X, and GRID_Y is the grid position
            // of the bottom left corner of the hit box you want to create. These are multiplied by 32 as each grid
            // square is 32 pixels in both height and width.

            {obstacles.add(new Entity(new Vector2(225, 212), 192, 64, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(225, 308), 224, 128, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(257, 436), 192, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(321, 468), 96, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(449, 340), 352, 128, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(641, 308), 128, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(545, 468), 320, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(801, 436), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(801, 404), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(577, 500), 288, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(609, 532), 224, 160, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(609, 692), 192, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(641, 724), 128, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(705, 756), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(801, 756), 416, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(801, 788), 384, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(769, 820), 384, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(865, 852), 224, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(961, 884), 96, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(833, 724), 448, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(865, 692), 32, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(993, 660), 288, 64, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1057, 596), 224, 64, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1025, 468), 256, 128, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1057, 436), 96, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1313, 468), 32, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1313, 500), 320, 256, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1473, 468), 288, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1505, 436), 256, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1569, 404), 160, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1633, 500), 64, 128, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1697, 500), 64, 96, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1377, 756), 224, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1409, 788), 160, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1409, 820), 128, 32, new Texture("teal.jpg")));

            obstacles.add(new Entity(new Vector2(97, 692), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(321, 916), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(353, 948), 32, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(449, 756), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(481, 724), 32, 32, new Texture("teal.jpg")));

            obstacles.add(new Entity(new Vector2(1121, 244), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1153, 276), 32, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1665, 820), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1665, 788), 32, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(897, 372), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(897, 340), 32, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(33 + 36 * 32, 212 + 19 * 32), 4 * 32, 4 * 32,
                    new Texture("teal.jpg")));}

            // Level 2 Fire Station
            fireStation = new FireStation(new Vector2(33 + 1 * 32, 212 + 4 * 32), 64, 128,
                    new Texture("teal.jpg"), 1000);

            // Level 2 Fortress
            fortress = new Fortress(new Vector2(33 + 36 * 32, 212 + 19 * 32), 4 * 32, 4 * 32,
                    new Texture("grey.png"),
                    12500, 4, 2);
        }

        else if (levelNumber == 3) {

            firetruck1pos = new Vector2(33 + 27 * 32, 212 + 3 * 32);
            firetruck2pos = new Vector2(33 + 28 * 32, 212 + 3 * 32);
            // Placeholders
            firetruck3pos = new Vector2(33 + 27 * 32, 212 + 4 * 32);
            firetruck4pos = new Vector2(33 + 28 * 32, 212 + 4 * 32);

            timeLimit = 60;

            map = new Texture("level3background.png");

            // Level 3 Obstacles - These are used to create the hit boxes for the buildings so that the player
            // can't drive through them. You can create any rectangular hit box as one singular entity. To calculate
            // the coords do:

            // X_COORD = 33 + (GRID_X * 32)    and    Y_COORD = 212 + (GRID_Y * 32)
            // Where (33, 212) is the bottom left corner of the game screen and GRID_X, and GRID_Y is the grid position
            // of the bottom left corner of the hit box you want to create. These are multiplied by 32 as each grid
            // square is 32 pixels in both height and width.

            {obstacles.add(new Entity(new Vector2(737, 244), 32, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(705, 276), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(609, 500), 32, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(577, 532), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(961, 532), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1281, 308), 32, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1281, 340), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1697, 340), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1729, 372), 32, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1665, 500), 32, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(1665, 532), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(673, 724), 64, 32, new Texture("teal.jpg")));
            obstacles.add(new Entity(new Vector2(705, 756), 32, 32, new Texture("teal.jpg")));

            // For loops to create diagonal wall obstacle
            for (int i = 0; i<= 192; i += 32){
                obstacles.add(new Entity(new Vector2(257 + i, 1012 - i), 64, 32, new Texture("teal.jpg")));
            }

            obstacles.add(new Entity(new Vector2(33 + 14 * 32, 212 + 18 * 32), 32, 32, new Texture("teal.jpg")));

            for (int i = 0; i<= 192; i += 32){
                obstacles.add(new Entity(new Vector2(1601 - i, 1012 - i), 64, 32, new Texture("teal.jpg")));
            }

            obstacles.add(new Entity(new Vector2(33 + 43 * 32, 212 + 18 * 32), 32, 32, new Texture("teal.jpg")));

            for (int i = 0; i<= 352; i += 32){
                obstacles.add(new Entity(new Vector2(577 + i, 692 - i), 64, 32, new Texture("teal.jpg")));
            }

            obstacles.add(new Entity(new Vector2(33 + 17 * 32, 212 + 16 * 32), 32, 32, new Texture("teal.jpg")));

            for (int i = 0; i<= 320; i += 32){
                obstacles.add(new Entity(new Vector2(1281 - i, 692 - i), 64, 32, new Texture("teal.jpg")));
            }

            obstacles.add(new Entity(new Vector2(33 + 40 * 32, 212 + 16 * 32), 32, 32, new Texture("teal.jpg"))); }

            // Level 3 Fire Station
            fireStation = new FireStation(new Vector2(33 + 27*32, 212), 96, 128,
                    new Texture("teal.jpg"), 1000);

            // Level 3 Fortress
            fortress = new Fortress(new Vector2(33 + 24*32, 212 + 32*21), 224, 96,
                    new Texture("grey.png"),
                    15000, 2, 3);
        }

        // Code Added by Dalai Java - Implementing the next 3 levels to bring the total fortresses up to 6
        else if (levelNumber == 4) {
            firetruck1pos = new Vector2(33 + (48 * 32), 212 + (23 * 32));
            firetruck2pos = new Vector2(33 + (49 * 32), 212 + (23 * 32));
            firetruck3pos = new Vector2(33 + (54 * 32), 212 + (23 * 32));
            firetruck4pos = new Vector2(33 + (55 * 32), 212 + (23 * 32));

            {
                obstacles.add(new Entity(new Vector2(33 + 3 * 32, 212 + 19 * 32),284, 160, new Texture("teal.jpg"))); // Fortress Hit Box

                // Obstacle Number refer to numbers on "Level 4 - Obstacle Index.png"
                obstacles.add(new Entity(new Vector2( 33 + 1 *32, 212 + 13 * 32), 64,64, new Texture("teal.jpg"))); // Obstacle 1
                obstacles.add(new Entity(new Vector2(33 + 7 * 32, 212 + 0 *32), 64, 64, new Texture("teal.jpg"))); // Obstacle 2
                obstacles.add(new Entity(new Vector2(33 + 11 * 32, 212 + 5 * 32), 32, 32, new Texture("teal.jpg")));// Obstacle 3
                obstacles.add(new Entity(new Vector2(33 + 12 * 32, 212 + 5 * 32), 64, 64, new Texture("teal.jpg")));// Obstacle 4
                obstacles.add(new Entity(new Vector2(33 + 15 * 32, 212 + 0 * 32), 64, 64, new Texture("teal.jpg")));// Obstacle 5
                obstacles.add(new Entity(new Vector2(33 + 17 * 32, 212 + 1 * 32), 128, 32, new Texture("teal.jpg")));// Obstacle 6
                obstacles.add(new Entity(new Vector2(33 + 23 * 32, 212 + 4 * 32), 64, 64, new Texture("teal.jpg")));// Obstacle 7
                obstacles.add(new Entity(new Vector2(33 + 30 * 32, 212 + 4 * 32), 64, 96, new Texture("teal.jpg")));// Obstacle 8
                obstacles.add(new Entity(new Vector2(33 + 32 * 32, 212 + 0 * 32), 64, 64, new Texture("teal.jpg")));// Obstacle 9
                obstacles.add(new Entity(new Vector2(33 + 34 * 32, 212 + 11 * 32), 64, 64, new Texture("teal.jpg")));// Obstacle 10
                obstacles.add(new Entity(new Vector2(33 + 43 * 32, 212 + 9 * 32), 64, 64, new Texture("teal.jpg")));// Obstacle 11
                obstacles.add(new Entity(new Vector2(33 + 52 * 32, 212 + 1 * 32), 64, 32, new Texture("teal.jpg")));// Obstacle 12
                obstacles.add(new Entity(new Vector2(33 + 36 * 32, 212 + 17 * 32), 64, 64, new Texture("teal.jpg")));// Obstacle 13
                obstacles.add(new Entity(new Vector2(33 + 43 * 32, 212 + 22 * 32), 64, 64, new Texture("teal.jpg")));// Obstacle 14
                obstacles.add(new Entity(new Vector2(33 + 34 * 32, 212 + 22 * 32), 64, 64, new Texture("teal.jpg")));// Obstacle 15
                obstacles.add(new Entity(new Vector2(33 + 21 * 32, 212 + 23 * 32), 64, 32, new Texture("teal.jpg")));// Obstacle 16
                obstacles.add(new Entity(new Vector2(33 + 24 * 32, 212 + 23 * 32), 64, 32, new Texture("teal.jpg")));// Obstacle 17
                obstacles.add(new Entity(new Vector2(33 + 28 * 32, 212 + 23 * 32), 64, 32, new Texture("teal.jpg")));// Obstacle 18
                obstacles.add(new Entity(new Vector2(33 + 16 * 32, 212 + 23 * 32), 64, 32, new Texture("teal.jpg")));// Obstacle 19
                obstacles.add(new Entity(new Vector2(33 + 16 * 32, 212 + 24 * 32), 32, 32, new Texture("teal.jpg")));// Obstacle 20
                obstacles.add(new Entity(new Vector2(33 + 30 * 32, 212 + 20 * 32), 64, 32, new Texture("teal.jpg")));// Obstacle 21
                obstacles.add(new Entity(new Vector2(33 + 31 * 32, 212 + 19 * 32), 32, 32, new Texture("teal.jpg")));// Obstacle 22
                obstacles.add(new Entity(new Vector2(33 + 22 * 32, 212 + 15 * 32), 64, 32, new Texture("teal.jpg")));// Obstacle 24
                obstacles.add(new Entity(new Vector2(33 + 23 * 32, 212 + 14 * 32), 32, 32, new Texture("teal.jpg")));// Obstacle 25
                obstacles.add(new Entity(new Vector2(33 + 30 * 32, 212 + 15 * 32), 64, 32, new Texture("teal.jpg")));// Obstacle 26
                obstacles.add(new Entity(new Vector2(33 + 31 * 32, 212 + 13 * 32), 32, 64, new Texture("teal.jpg")));// Obstacle 27
                obstacles.add(new Entity(new Vector2(33 + 40 * 32, 212 + 22 * 32), 32, 64, new Texture("teal.jpg")));// Obstacle 28
                obstacles.add(new Entity(new Vector2(33 + 43 * 32, 212 + 18 * 32), 64, 32, new Texture("teal.jpg")));// Obstacle 29
                obstacles.add(new Entity(new Vector2(33 + 43 * 32, 212 + 15 * 32), 64, 32, new Texture("teal.jpg")));// Obstacle 30
                obstacles.add(new Entity(new Vector2(33 + 44 * 32, 212 + 14 * 32), 32, 32, new Texture("teal.jpg")));// Obstacle 31
                obstacles.add(new Entity(new Vector2(33 + 54 * 32, 212 + 18 * 32), 64, 64, new Texture("teal.jpg")));// Obstacle 32
                obstacles.add(new Entity(new Vector2(33 + 53 * 32, 212 + 19 * 32), 32, 32, new Texture("teal.jpg")));// Obstacle 33
                obstacles.add(new Entity(new Vector2(33 + 55 * 32, 212 + 2 * 32), 64, 32, new Texture("teal.jpg")));// Obstacle 34
                obstacles.add(new Entity(new Vector2(33 + 56 * 32, 212 + 1* 32), 32, 32, new Texture("teal.jpg")));// Obstacle 35

                // Lake Collisions
                obstacles.add(new Entity(new Vector2(33 + 4 * 32, 212 + 13 * 32), 32, 32, new Texture("teal.jpg")));// Lake 1
                obstacles.add(new Entity(new Vector2(33 + 5 * 32, 212 + 14 * 32), 160, 32, new Texture("teal.jpg")));// Lake 1
                obstacles.add(new Entity(new Vector2(33 + 5 * 32, 212 + 11 * 32), 224, 96, new Texture("teal.jpg")));// Lake 1
                obstacles.add(new Entity(new Vector2(33 + 6 * 32, 212 + 10 * 32), 224, 32, new Texture("teal.jpg")));// Lake 1
                obstacles.add(new Entity(new Vector2(33 + 7 * 32, 212 + 9 * 32), 160, 32, new Texture("teal.jpg")));// Lake 1
                obstacles.add(new Entity(new Vector2(33 + 8 * 32, 212 + 8 * 32), 128, 32, new Texture("teal.jpg")));// Lake 1
                obstacles.add(new Entity(new Vector2(33 + 10 * 32, 212 + 7 * 32), 32, 32, new Texture("teal.jpg")));// Lake 1

                obstacles.add(new Entity(new Vector2(33 + 16 * 32, 212 + 12 * 32), 160, 96, new Texture("teal.jpg")));// Lake 2
                obstacles.add(new Entity(new Vector2(33 + 17 * 32, 212 + 11 * 32), 96, 32, new Texture("teal.jpg")));// Lake 2

                // River Collisions
                // 12 = 52
                for (int i = 0; i<= 2; i ++){
                    obstacles.add(new Entity(new Vector2(33  + (42+i)  * 32, 212 + (0+i) * 32), 64, 32, new Texture("teal.jpg")));
                }

                obstacles.add(new Entity(new Vector2(33 + 45 * 32, 212 + 3 * 32), 64, 32, new Texture("teal.jpg")));

                for (int i = 0; i<= 5; i ++){
                  obstacles.add(new Entity(new Vector2(33  + (47+i)  * 32, 212 + (4+i) * 32), 64, 32, new Texture("teal.jpg")));
                }

                for (int i = 0; i<= 4; i ++) {
                    obstacles.add(new Entity(new Vector2(33 + (54 + i) * 32, 212 + (10 + i) * 32), 32, 32, new Texture("teal.jpg")));
                }
            }

            fireStation = new FireStation(new Vector2((33 + (48 * 32)), (212 + (21 * 32))), 256, 128,
                    new Texture("teal.jpg"), 1000);

            fortress = new Fortress(new Vector2((33 + (4 * 32)),(212 + (19 * 32))), 300, 160,
                    new Texture("grey.png"), 20000, 6, 4);

            timeLimit = 45;
            map = new Texture("level4background.png");
        }

        else if (levelNumber == 5) {
            firetruck1pos = new Vector2(33 +  5 * 32, 212 + 12 * 32);
            firetruck2pos = new Vector2(33 +  6 * 32, 212 + 12  * 32);
            firetruck3pos = new Vector2(33 +  6 * 32, 212 +13 * 32);
            firetruck4pos = new Vector2(33 +  6 * 32, 212 + 14 * 32);

            {
                // Fortress Collision
                obstacles.add(new Entity(new Vector2(33 + 53 * 32, 212 + 11 * 32), 160, 192, new Texture("teal.jpg")));

                // Obstacle number refers to numbers on "Level 4 - Obstacle Index.jpg"
                obstacles.add(new Entity(new Vector2(33 + 4 * 32, 212 + 23 * 32), 32, 32, new Texture("teal.jpg"))); // Obstacle 1
                obstacles.add(new Entity(new Vector2(33 + 3 * 32, 212 + 22 * 32), 32, 64, new Texture("teal.jpg"))); // Obstacle 2
                obstacles.add(new Entity(new Vector2(33 + 10 * 32, 212 + 21 * 32), 32, 64, new Texture("teal.jpg"))); // Obstacle 3
                obstacles.add(new Entity(new Vector2(33 + 17 * 32, 212 + 16 * 32), 64, 64, new Texture("teal.jpg"))); // Obstacle 4
                obstacles.add(new Entity(new Vector2(33 + 11 * 32, 212 + 10 * 32), 32, 64, new Texture("teal.jpg"))); // Obstacle 5
                obstacles.add(new Entity(new Vector2(33 + 3 * 32, 212 + 5 * 32), 32, 64, new Texture("teal.jpg"))); // Obstacle 6
                obstacles.add(new Entity(new Vector2(33 + 4 * 32, 212 + 6 * 32), 32, 32, new Texture("teal.jpg"))); // Obstacle 7
                obstacles.add(new Entity(new Vector2(33 + 17 * 32, 212 + 7 * 32), 32, 64, new Texture("teal.jpg"))); // Obstacle 8
                obstacles.add(new Entity(new Vector2(33 + 16 * 32, 212 + 8 * 32), 32, 32, new Texture("teal.jpg"))); // Obstacle 9

                obstacles.add(new Entity(new Vector2(33 + 35 * 32, 212 + 17 * 32), 32, 32, new Texture("teal.jpg"))); // Obstacle 10
                obstacles.add(new Entity(new Vector2(33 + 35 * 32, 212 + 9 * 32), 64, 32, new Texture("teal.jpg"))); // Obstacle 11
                obstacles.add(new Entity(new Vector2(33 + 35 * 32, 212 + 8 * 32), 32, 32, new Texture("teal.jpg"))); // Obstacle 12
                obstacles.add(new Entity(new Vector2(33 + 41 * 32, 212 + 12 * 32), 64, 64, new Texture("teal.jpg"))); // Obstacle 13
                obstacles.add(new Entity(new Vector2(33 + 44 * 32, 212 + 22 * 32), 64, 32, new Texture("teal.jpg"))); // Obstacle 14
                obstacles.add(new Entity(new Vector2(33 + 54 * 32, 212 + 22 * 32), 32, 64, new Texture("teal.jpg"))); // Obstacle 15
                obstacles.add(new Entity(new Vector2(33 + 41 * 32, 212 + 0 * 32), 64, 64, new Texture("teal.jpg"))); // Obstacle 16
                obstacles.add(new Entity(new Vector2(33 + 54 * 32, 212 + 2 * 32), 64, 32, new Texture("teal.jpg"))); // Obstacle 17
                obstacles.add(new Entity(new Vector2(33 + 54 * 32, 212 + 1 * 32), 32, 32, new Texture("teal.jpg"))); // Obstacle 18

                // Lake
                obstacles.add(new Entity(new Vector2(33 + 8 * 32, 212 + 3 * 32), 64, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 7 * 32, 212 + 2 * 32), 128, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 5 * 32, 212 + 1 * 32), 256, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 4 * 32, 212 + 0 * 32), 320, 32, new Texture("teal.jpg")));

                // River
                obstacles.add(new Entity(new Vector2(33 + 20 * 32, 212 + 0 * 32), 384, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 21 * 32, 212 + 1 * 32), 320, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 22 * 32, 212 + 2 * 32), 256, 32, new Texture("teal.jpg")));

                obstacles.add(new Entity(new Vector2(33 + 22 * 32, 212 + 5 * 32), 256, 160, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 23 * 32, 212 + 10 * 32), 224, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 23 * 32, 212 + 11 * 32), 196, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 23 * 32, 212 + 12 * 32), 224, 32, new Texture("teal.jpg")));

                obstacles.add(new Entity(new Vector2(33 + 22 * 32, 212 + 15 * 32), 288, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 22 * 32, 212 + 16 * 32), 320, 96, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 23 * 32, 212 + 19 * 32), 256, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 23 * 32, 212 + 20 * 32), 224, 32, new Texture("teal.jpg")));

                obstacles.add(new Entity(new Vector2(33 + 23 * 32, 212 + 23 * 32), 224, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 13 * 32, 212 + 24 * 32), 544, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 9 * 32, 212 + 25 * 32), 736, 32, new Texture("teal.jpg")));


            }

            // Placeholder values for position
            fireStation = new FireStation(new Vector2(33 + 2 * 32, 212 + 12 * 32), 160, 256,
                    new Texture("teal.jpg"),1000);

            fortress = new Fortress(new Vector2(33 +  53 * 32, 212 + 11 * 32), 160, 192,
                    new Texture("grey.png"), 20000, 6, 5);

            timeLimit = 45;
            map = new Texture("level5background.png");
        }


        else if (levelNumber == 6) {
            firetruck1pos = new Vector2(33 +  21 * 32, 212 + 1 * 32);
            firetruck2pos = new Vector2(33 + 21  * 32, 212 + 3 * 32);
            firetruck3pos = new Vector2(33 +  32 * 32, 212 + 1 * 32);
            firetruck4pos = new Vector2(33 +  32 * 32, 212 + 3 * 32);

            {

                obstacles.add(new Entity(new Vector2(33 + 21 * 32, 212 + 23 * 32), 384, 96, new Texture("teal.jpg") ));//fortress Collision
                obstacles.add(new Entity(new Vector2(33 + 25 * 32, 212 + 21 * 32), 128, 64, new Texture("teal.jpg")));


                obstacles.add(new Entity(new Vector2(33 + 10 * 32, 212 + 21 * 32), 64, 64, new Texture("teal.jpg"))); // Obstacle 1
                obstacles.add(new Entity(new Vector2(33 + 18 * 32, 212 + 22 * 32), 64, 64, new Texture("teal.jpg"))); // Obstacle 2
                obstacles.add(new Entity(new Vector2(33 + 10 * 32, 212 + 18 * 32), 64, 32, new Texture("teal.jpg"))); // Obstacle 3
                obstacles.add(new Entity(new Vector2(33 + 22 * 32, 212 + 18 * 32), 64, 32, new Texture("teal.jpg"))); // Obstacle 4
                obstacles.add(new Entity(new Vector2(33 + 19 * 32, 212 + 15 * 32), 64, 64, new Texture("teal.jpg"))); // Obstacle 5
                obstacles.add(new Entity(new Vector2(33 + 13 * 32, 212 + 14 * 32), 32, 64, new Texture("teal.jpg"))); // Obstacle 6
                obstacles.add(new Entity(new Vector2(33 + 10 * 32, 212 + 11 * 32), 64, 64, new Texture("teal.jpg"))); // Obstacle 7
                obstacles.add(new Entity(new Vector2(33 + 23 * 32, 212 + 11 * 32), 64, 64, new Texture("teal.jpg"))); // Obstacle 8
                obstacles.add(new Entity(new Vector2(33 + 25 * 32, 212 + 8 * 32), 32, 64, new Texture("teal.jpg"))); // Obstacle 9
                obstacles.add(new Entity(new Vector2(33 + 11 * 32, 212 + 4 * 32), 64, 64, new Texture("teal.jpg"))); // Obstacle 10
                obstacles.add(new Entity(new Vector2(33 + 16 * 32, 212 + 4 * 32), 32, 64, new Texture("teal.jpg"))); // Obstacle 11

                obstacles.add(new Entity(new Vector2(33 + 30 * 32, 212 + 18 * 32), 32, 64, new Texture("teal.jpg"))); // Obstacle 12
                obstacles.add(new Entity(new Vector2(33 + 29 * 32, 212 + 14 * 32), 64, 32, new Texture("teal.jpg"))); // Obstacle 13
                obstacles.add(new Entity(new Vector2(33 + 29 * 32, 212 + 12 * 32), 64, 32, new Texture("teal.jpg"))); // Obstacle 14
                obstacles.add(new Entity(new Vector2(33 + 33 * 32, 212 + 13 * 32), 64, 64, new Texture("teal.jpg"))); // Obstacle 15
                obstacles.add(new Entity(new Vector2(33 + 35 * 32, 212 + 16 * 32), 64, 32, new Texture("teal.jpg"))); // Obstacle 16
                obstacles.add(new Entity(new Vector2(33 + 38 * 32, 212 + 13 * 32), 64, 64, new Texture("teal.jpg"))); // Obstacle 17
                obstacles.add(new Entity(new Vector2(33 + 37 * 32, 212 + 11 * 32), 64, 32, new Texture("teal.jpg"))); // Obstacle 18
                obstacles.add(new Entity(new Vector2(33 + 43 * 32, 212 + 12 * 32), 64, 64, new Texture("teal.jpg"))); // Obstacle 19
                obstacles.add(new Entity(new Vector2(33 + 42 * 32, 212 + 21 * 32), 64, 64, new Texture("teal.jpg"))); // Obstacle 20
                obstacles.add(new Entity(new Vector2(33 + 44 * 32, 212 + 18 * 32), 32, 64, new Texture("teal.jpg"))); // Obstacle 21
                obstacles.add(new Entity(new Vector2(33 + 46 * 32, 212 + 21 * 32), 32, 64, new Texture("teal.jpg"))); // Obstacle 22
                obstacles.add(new Entity(new Vector2(33 + 37 * 32, 212 + 21 * 32), 96, 64, new Texture("teal.jpg"))); // Obstacle 23
                obstacles.add(new Entity(new Vector2(33 + 38 * 32, 212 + 23 * 32), 32, 32, new Texture("teal.jpg"))); // Obstacle 24
                obstacles.add(new Entity(new Vector2(33 + 38 * 32, 212 + 20 * 32), 32, 32, new Texture("teal.jpg"))); // Obstacle 25
                obstacles.add(new Entity(new Vector2(33 + 32 * 32, 212 + 6 * 32), 64, 64, new Texture("teal.jpg"))); // Obstacle 26
                obstacles.add(new Entity(new Vector2(33 + 32 * 32, 212 + 8 * 32), 32, 32, new Texture("teal.jpg"))); // Obstacle 27
                obstacles.add(new Entity(new Vector2(33 + 36 * 32, 212 + 3 * 32), 128, 32, new Texture("teal.jpg"))); // Obstacle 28
                obstacles.add(new Entity(new Vector2(33 + 44 * 32, 212 + 8 * 32), 64, 32, new Texture("teal.jpg"))); // Obstacle 29
                obstacles.add(new Entity(new Vector2(33 + 43 * 32, 212 + 4 * 32), 32, 64, new Texture("teal.jpg"))); // Obstacle 30
                obstacles.add(new Entity(new Vector2(33 + 46 * 32, 212 + 4 * 32), 32, 64, new Texture("teal.jpg"))); // Obstacle 31

                //River - Left
                obstacles.add(new Entity(new Vector2(33 + 7 * 32, 212 + 0 * 32), 32, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 6 * 32, 212 + 1 * 32), 32, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 5 * 32, 212 + 2 * 32), 32, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 5 * 32, 212 + 3 * 32), 32, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 4 * 32, 212 + 4 * 32), 32, 64, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 3 * 32, 212 + 6 * 32), 32, 128, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 2 * 32, 212 + 10 * 32), 32, 64, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 1 * 32, 212 + 12 * 32), 32, 96, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 2 * 32, 212 + 15 * 32), 32, 256, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 3 * 32, 212 + 23 * 32), 32, 64, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 4 * 32, 212 + 24 * 32), 32, 64, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 5 * 32, 212 + 25 * 32), 32, 64, new Texture("teal.jpg")));

                // River - Right
                obstacles.add(new Entity(new Vector2(33 + 50 * 32, 212 + 0 * 32), 32, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 51 * 32, 212 + 1 * 32), 32, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 52 * 32, 212 + 2 * 32), 32, 64, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 53 * 32, 212 + 4 * 32), 32, 64, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 54 * 32, 212 + 6 * 32), 32, 96, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 55 * 32, 212 + 9 * 32), 32, 128, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 56 * 32, 212 + 13 * 32), 32, 64, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 55 * 32, 212 + 15 * 32), 32, 196, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 54 * 32, 212 + 21 * 32), 32, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 53 * 32, 212 + 22 * 32), 32, 64, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 51 * 32, 212 + 24 * 32), 64, 32, new Texture("teal.jpg")));
                obstacles.add(new Entity(new Vector2(33 + 50 * 32, 212 + 25 * 32), 32, 32, new Texture("teal.jpg")));


            }
            // Placeholder values for position
            fireStation = new FireStation(new Vector2(33 + 21 * 32, 212 + 0 * 32), 320, 96,
                    new Texture("teal.jpg"), 1000);
            // Placeholder values for position
            fortress = new Fortress(new Vector2(33 + 21 * 32, 212 + 23 * 32), 384, 96,
                    new Texture("grey.png"), 20000, 6, 4);

            timeLimit = 45;
            map = new Texture("level6background.png");
        }


        firetruck1 = new Firetruck(firetruck1pos, 25, 25,
                new Texture("truck.png"), 100, 200,
                null, 100, 2,  175,
                true);

        firetruck2 = new Firetruck(firetruck2pos, 25, 25,
                new Texture("truck.png"), 50, 200,
                null, 200, 2,  100,
                false);

        // Code added by Dalai Java - Creates the remaining fire engines and adds to map
        firetruck3 = new Firetruck(firetruck3pos, 25,25,
                    new Texture("truck.png"), 75, 200,
                    null, 150, 5, 75, false);

        firetruck4 = new Firetruck(firetruck4pos, 25,25,
                new Texture("truck.png"), 250, 250,
                null, 50, 3, 125, false);

        firetrucks.add(firetruck1);
        firetrucks.add(firetruck2);

        // Code added by Dalai Java - Adds 3rd and 4th firetrucks to ArrayList
        firetrucks.add(firetruck3);
        firetrucks.add(firetruck4);

        // Code added by Dalia Java - adds fire station locations to an array list
        fireStationPositions.add(new Vector2(33 + 8 * 32, 212 + 4 * 32));
        fireStationPositions.add(new Vector2(33 + 1 * 32, 212 + 4 * 32));
        fireStationPositions.add(new Vector2(33 + 27 * 32,212 + 0 * 32));
        fireStationPositions.add(new Vector2(33 + 48 * 32,212 + 21 * 32));
        fireStationPositions.add(new Vector2(33 + 2 * 32,212 + 12 * 32));
        fireStationPositions.add(new Vector2(33 + 21 * 32,212 + 0 * 32));

        stopwatch = new Stopwatch(timeLimit);

    }

    /**
     * The game logic which is executed due to specific user inputs. Is called in the update method.
     */
    public void handleInput() {

        // Checks for hover and clicks on the 2 buttons located on the play screen.
        if (quitGame.mouseInRegion()){
            quitGame.setActive(true);
            if (Gdx.input.isTouched()) {
                Gdx.app.exit();
                System.exit(0);
                }
            }

        else {
            quitGame.setActive(false);
        }

        if (quitLevel.mouseInRegion()){
            quitLevel.setActive(true);
            if (Gdx.input.isTouched()) {
                gameStateManager.pop();
            }
        }

        else {
            quitLevel.setActive(false);
        }

        // If the user presses the space bar or Q, creates Projectile instance if the selected firetruck has water remaining.
        // Then adds this to the water ArrayList and removes 1 water from the firetrucks tank.

        // Dalai Java - multiple water streams
        for (Firetruck firetruck : firetrucks) {
            if (Gdx.input.isKeyPressed(Input.Keys.Q) && firetruck.isSelected() && firetruck.getCurrentWater() > 0) {
                Projectile drop1 = new Projectile(new Vector2(firetruck.getPosition().x + firetruck.getWidth() / 2, firetruck.getPosition().y + firetruck.getHeight() / 2),5,5,
                        new Texture("lightblue.jpg"),(new Vector2(Gdx.input.getX(),Kroy.HEIGHT - Gdx.input.getY())), 5, firetruck.getDamage(),firetruck.getRange(), "Random");
                water.add(drop1);
                if (saveData.getBoolean("effects")){
                    waterShoot.play();
                }
                firetruck.updateCurrentWater(1);

            }

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && firetruck.isSelected() && firetruck.getCurrentWater() > 0) {
                Projectile drop = new Projectile(new Vector2(firetruck.getPosition().x + firetruck.getWidth() / 2, firetruck.getPosition().y + firetruck.getHeight() / 2), 5, 5,
                        new Texture("lightblue.jpg"), (new Vector2(Gdx.input.getX(), Kroy.HEIGHT - Gdx.input.getY())), 5, firetruck.getDamage(), firetruck.getRange(), "Straight");
                water.add(drop);
                if (saveData.getBoolean("effects")) {
                    waterShoot.play();
                }
                firetruck.updateCurrentWater(1);

            }
        }

        // Opens pause menu if user hits escape
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            //gameStateManager.push(new OptionState(gameStateManager));
            gameStateManager.push(new OptionState(gameStateManager));

        }

        // Switches active firetruck if the mouse clicks within another selectable firetruck.
        Vector2 mousePos = new Vector2(Gdx.input.getX(), Kroy.HEIGHT - Gdx.input.getY());
        if (Gdx.input.isTouched()) {
            for (Firetruck truck : firetrucks) {
                if (mousePos.x >= (truck.getPosition().x) && mousePos.x <= (truck.getPosition().x + truck.getWidth())
                        && mousePos.y >= (truck.getPosition().y) && mousePos.y <= (truck.getPosition().y
                        + truck.getHeight())) {
                    for (Firetruck clearTruck : firetrucks) {
                        clearTruck.setSelected(false);
                    }
                    truck.setSelected(true);

                }
            }
        }

        // Changes which truck is moving and calls the truckMovement() method with the selected truck as input.
        /**
        if (firetruck1.isSelected()) {
            truckMovement(firetruck1);
        } else if (firetruck2.isSelected()) {
            truckMovement(firetruck2);
        } else if (firetruck3.isSelected()){
            truckMovement(firetruck3);
        } else if (firetruck4.isSelected()){
            truckMovement(firetruck4);
        }
         **/
        for(Firetruck truck : firetrucks){
            if(truck.isSelected()) {
                truck.truckMovement(obstacles);
            }
        }

        // Checks if user presses ENTER when game is over and takes them back to level select.
        if ((levelLost || levelWon) && Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            gameStateManager.set(new LevelSelectState(gameStateManager));
        }
    }

    /**
     * Updates the game logic before the next render() is called
     * @param deltaTime the amount of time which has passed since the last render() call
     */
    @Override
    public void update(float deltaTime) {

        // Dalai Java - explosion
        ArrayList<Explosion> explosionsToRemove = new ArrayList<Explosion>();
        for (Explosion explosion: explosions){
            explosion.update(deltaTime);
            if (explosion.remove)
                explosionsToRemove.add(explosion);
        }
        explosions.removeAll(explosionsToRemove);


        // Calls input handler and updates timer each tick of the game.
        handleInput();
        stopwatch.update();

        // Updates aliens and attacks firetruck if there is a firetruck in range and the Aliens attack cooldown is over.
        // Adds the created bullet projectile to the ArrayList bullets
        for (Alien alien : aliens) {

            // Assesment 3 - After half the game time, the alien will begin to move towards the fire station.
            if (timeLimit - stopwatch.getTime() > timeLimit / 2) {
                // will begin to move towards the fire station
                alien.update();
                alien.truckInRange(firetrucks, (FireStation)fireStation);
                if (alien.getTimeSinceAttack() >= alien.getAttackCooldown()) {
                    if (alien.hasTarget()) {
                        Projectile bullet = new Projectile(new Vector2(alien.getPosition().x + alien.getWidth() / 2, alien.getPosition().y + alien.getHeight() / 2), 5, 5,
                                new Texture("red.png"), (new Vector2(alien.getTarget().getPosition().x, alien.getTarget().getPosition().y)), 5, alien.getDamage(), "Straight");
                        bullets.add(bullet);
                        alien.resetTimeSinceAttack();
                    }
                }
                alien.updateTimeSinceAttack(deltaTime);
            } else {
                alien.updateToFireStation(fireStationPositions.get(((Integer.parseInt(level)) - 1)));
            }
        }

        alienSpawnCountdown -= deltaTime;
        timeSinceAlienKilled -= deltaTime;


        // Respawns an alien as long as the spawn cooldown is over and enough time has passed since the
        // user killed an alien.
        if (alienSpawnCountdown <= 0 && timeSinceAlienKilled <= 0) {
            spawnAlien();
            alienSpawnCountdown = fortress.getSpawnRate();
            timeSinceAlienKilled = 0;
        }

        // Updates all bullets each tick, checks if bullet collides with firetruck and then removes health from the
        // firetruck. If a firetruck is destroyed, checks if all have been destroyed and then activates game over screen.


        for (Projectile bullet : new ArrayList<Projectile>(bullets)) {
            bullet.update();
            for (Firetruck truck : new ArrayList<Firetruck>(firetrucks)) {
                if (bullet.hitUnit(truck) && truck.canBeDamaged) {
                    truck.takeDamage(bullet.getDamage());
                    bullets.remove(bullet);
                    if (truck.getCurrentHealth() <= 0) {
                        truck.setSelected(false);
                        firetrucks.remove(truck);
                        if (firetrucks.size() == 0) {
                            levelLost = true;
                            timeTaken = stopwatch.getTime();
                        }
                        destroyedFiretrucks.add(truck);
                    }
                }
            }
        }

        // Refills firetrucks tank if truck reaches the fire station.
        for (Firetruck truck : firetrucks) {
            if (!(truck.getTopRight().y < fireStation.getPosition().y || truck.getPosition().y > fireStation.getTopRight().y ||
                    truck.getTopRight().x < fireStation.getPosition().x || truck.getPosition().x > fireStation.getTopRight().x)) {
                truck.canBeDamaged = false; // DJ - making fire engines invulnerable when in station
                if (!truck.isRefilling()) {
                    truck.setRefilling(true);
                    minigameWon = false;
                    //ASSESSMENT 3 - beings the minigame

                    gameStateManager.push(new MinigameState(gameStateManager, this, ui));

                    truck.setCurrentWater(truck.getMaxWater());

                    //Dalai Java - Repair fire engines at fire station
                    truck.setCurrentHealth(truck.getMaxHealth());
                    System.out.println("Minigame won" + minigameWon);

                }
            } else {
                truck.canBeDamaged = true; // DJ - making fire engines vulnerable when not in station
                truck.setRefilling(false);
            }
        }

        // Updates all water drops each tick, if the drop reaches a certain distance then it is deleted. Otherwise,
        // Checks if drop collides with alien/fortress and then removes health from it if so. If alien dies, removes it
        // and adds its coordinates back to the fortresses potential spawn locations. If fortress reaches 0 then
        // game win screen is called and level progress saved.

        for (Projectile drop : new ArrayList<Projectile>(water)) {
            drop.update();
            if (drop.getLength() > drop.getMaxLength()) {
                drop.dispose();
                water.remove(drop);
            }
            for (Alien alien : new ArrayList<Alien>(aliens)) {
                if (drop.hitUnit(alien)) {
                    alien.takeDamage(drop.getDamage());
                    water.remove(drop);
                    if (alien.getCurrentHealth() == 0) {
                        fortress.getAlienPositions().add(alien.getPosition());
                        alien.dispose();
                        aliens.remove(alien);

                        // Dalai Java - explosion
                        explosions.add(new Explosion(alien.getPosition().x, alien.getPosition().y));

                        timeSinceAlienKilled = fortress.getSpawnRate();
                    }
                }
            }
            if (drop.hitUnit(fortress)) {
                fortress.takeDamage(drop.getDamage());
                if (fortress.getCurrentHealth() == 0) {
                    levelWon = true;
                    timeTaken = stopwatch.getTime();
                    saveData.putBoolean(level, true);
                    saveData.flush();
                }
            }
        }

        // Regenerates fortress health each second
        if(timeSinceLastFortressRegen <= 0) {
            fortress.addHealth(10);
            timeSinceLastFortressRegen = 1;
        }
        timeSinceLastFortressRegen -= deltaTime;

        // If the time is greater than the time limit, calls end game state.
        if (stopwatch.getTime() >= timeLimit) {
            levelLost = true;
        }

        // Forces user back to level select screen, even without needing to press ENTER after 4 seconds.
        if (stopwatch.getTime() > timeLimit + 4) {
            gameStateManager.set(new LevelSelectState(gameStateManager));
        }

        // Forces user back to level select screen, even without needing to press ENTER after 4 seconds.
        if (levelWon && stopwatch.getTime() > timeTaken + 4) {
            gameStateManager.set(new LevelSelectState(gameStateManager));
        }

        // Speeds up the background music when the player begins to run out of time.
        if ((14 < timeLimit - stopwatch.getTime()) && (timeLimit - stopwatch.getTime() < 16)){
            Kroy.INTRO.setPitch(Kroy.ID, 2f);
        }
    }

    /**
     * Used to draw elements onto the screen.
     * @param spriteBatch a container for all elements which need rendering to the screen.
     */
    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.begin();

        // Draws background and map onto play screen
        spriteBatch.draw(background, 0, 0, Kroy.WIDTH, Kroy.HEIGHT);
        spriteBatch.draw(map, 33, 212, 1856, 832);

        // Draws buttons onto play screen
        spriteBatch.draw(quitLevel.getTexture(), quitLevel.getPosition().x, quitLevel.getPosition().y,
                quitLevel.getWidth(), quitLevel.getHeight());

        spriteBatch.draw(quitGame.getTexture(), quitGame.getPosition().x, quitGame.getPosition().y,
                quitGame.getWidth(), quitGame.getHeight());

        // Draws updated firetrucks and overhead water tank statistics.
        for (Firetruck truck : firetrucks) {
            // Dalai Java - changed drawing to make rotation easier (do not need a unique texture for a rotation)
            Sprite toDraw = new Sprite(truck.getTexture());
            toDraw.setPosition(truck.getPosition().x, truck.getPosition().y);
            toDraw.setRegionHeight(truck.getHeight());
            toDraw.setRegionWidth(truck.getWidth());
            toDraw.setRotation(truck.truckDirection());
            toDraw.draw(spriteBatch);
            // DJ --------------------------------------------------------------------------------------
            healthBars.draw(spriteBatch, "Water: " + truck.getCurrentWater(), truck.getPosition().x,
                    truck.getPosition().y + truck.getHeight() + 10);
        }

        // Draws updated fortress HP
        healthBars.draw(spriteBatch, "HP: " + fortress.getCurrentHealth(), fortress.getPosition().x + 70,
                fortress.getPosition().y + fortress.getHeight() + 20);

        // Draws updated alien locations
        for (Alien alien : aliens) {
            spriteBatch.draw(alien.getTexture(), alien.getPosition().x, alien.getPosition().y, alien.getWidth(),
                    alien.getHeight());
            healthBars.draw(spriteBatch, "HP: " + alien.getCurrentHealth(), alien.getPosition().x,
                    alien.getPosition().y + alien.getHeight() + 10);

        }

        // Draws updated projectile locations

        for (Projectile bullet : bullets) {
            spriteBatch.draw(bullet.getTexture(), bullet.getPosition().x, bullet.getPosition().y, bullet.getWidth(),
                    bullet.getHeight());
        }

        for (Projectile drop : water) {
            spriteBatch.draw(drop.getTexture(), drop.getPosition().x, drop.getPosition().y, drop.getWidth(),
                    drop.getHeight());
        }

        stopwatch.drawTime(spriteBatch, ui);
        ui.setColor(Color.WHITE);

        // Gives user 15 second warning as time limit approaches.
        if ((timeLimit - 15) < stopwatch.getTime() && stopwatch.getTime() < (timeLimit - 10)) {
            ui.draw(spriteBatch, "The firestation is being attacked \n You have 15 seconds before it's destroyed!",
                    50, 1020);
        }

        // Draws UI Text onto the screen
        ui.setColor(Color.DARK_GRAY);
        ui.draw(spriteBatch, "Truck 1 Health: " + Integer.toString(firetruck1.getCurrentHealth()), 70,
                Kroy.HEIGHT - 920);
        ui.draw(spriteBatch, "Truck 2 Health: " + Integer.toString(firetruck2.getCurrentHealth()), 546,
                Kroy.HEIGHT - 920);

        // Code added by Dalai Java - Shows health of fire trucks
        ui.draw(spriteBatch, "Truck 3 Health: " + Integer.toString(firetruck3.getCurrentHealth()),  1023,
                Kroy.HEIGHT - 920);
        ui.draw(spriteBatch, "Truck 4 Health: " + Integer.toString(firetruck4.getCurrentHealth()), 1499,
                Kroy.HEIGHT - 920);

        for (Firetruck truck: firetrucks) {
            if (truck.getCurrentWater() < 20) {
                spriteBatch.draw(new Texture("RefillWarning.png"), truck.getPosition().x - 20,
                        truck.getPosition().y + 40);
            }
        }

        // If end game reached, draws level fail or level won images to the screen
        if (levelLost) {
            spriteBatch.draw(new Texture("levelFail.png"), 0, 0);
            Kroy.INTRO.setPitch(Kroy.ID, 1f);
        }

        if (levelWon & !levelLost) {
            spriteBatch.draw(new Texture("LevelWon.png"), 0, 0);
            Kroy.INTRO.setPitch(Kroy.ID, 1f);
        }

        // Dalai Java - explosion
        for (Explosion explosion: explosions){
            explosion.render(spriteBatch);
        }
        spriteBatch.end();
    }

    /**
     * Used to dispose of all textures, music etc. when no longer required to avoid memory leaks
     */
    @Override
    public void dispose() {
        background.dispose();
        map.dispose();
        quitLevel.dispose();
        quitGame.dispose();
        waterShoot.dispose();

        for (Firetruck firetruck : firetrucks) {
            firetruck.dispose();
        }

        for (Alien alien : aliens) {
            alien.dispose();
        }

        for (Firetruck firetruck : destroyedFiretrucks) {
            firetruck.dispose();
        }

        for (Projectile bullet : bullets) {
            bullet.dispose();
        }


        for (Projectile drop : water) {
            drop.dispose();
        }


        for (Entity obstacles: obstacles) {
            obstacles.dispose();
        }

        fireStation.dispose();
        fortress.dispose();
    }

    /**
     * Used to call the correct method to move the trucks position depending on potential obstacle overlap and which
     * truck is currently selected.
     * @param truck the truck which is currently selected
     */
    /**
    public void truckMovement(Firetruck truck) {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            truck.setTexture(new Texture("truck.png"));
            boolean obstacleCollision = false;
            if (truck.getPosition().y >= 1043 - truck.getHeight()) {
                obstacleCollision = true;
            }
            for (Entity obstacle : obstacles) {
                if (truck.willCollide(obstacle, 3)) {
                    obstacleCollision = true;
                }
            }
            if (!obstacleCollision) {
                truck.move(3);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            truck.setTexture(new Texture("truckdown.png"));
            boolean obstacleCollision = false;
            if (truck.getPosition().y <= 212) {
                obstacleCollision = true;
            }
            for (Entity obstacle : obstacles) {
                if (truck.willCollide(obstacle, 4)) {
                    obstacleCollision = true;
                }
            }
            if (!obstacleCollision) {
                truck.move(4);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            truck.setTexture(new Texture("truckleft.png"));
            boolean obstacleCollision = false;
            if (truck.getPosition().x <= 33) {
                obstacleCollision = true;
            }
            for (Entity obstacle : obstacles) {
                if (truck.willCollide(obstacle, 1)) {
                    obstacleCollision = true;
                }
            }
            if (!obstacleCollision) {
                truck.move(1);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            truck.setTexture(new Texture("truckright.png"));
            boolean obstacleCollision = false;
            if (truck.getPosition().x >= 1888 - truck.getWidth()) {
                obstacleCollision = true;
            }
            for (Entity obstacle : obstacles) {
                if (truck.willCollide(obstacle, 2)) {
                    obstacleCollision = true;
                }
            }
            if (!obstacleCollision) {
                truck.move(2);
            }
        }
    }
     **/

    /**
     * Used to spawn the Aliens around the fortress by accessing the spawnRate and alienPositions stored within
     * the fortress object.
     */
    public void spawnAlien() {
        Random rand = new Random();
        if (fortress.getAlienPositions().size() > 0) {
            Vector2 coordinate = fortress.getAlienPositions().get(rand.nextInt(fortress.getAlienPositions().size()));

            // Allows for unique spec aliens on each level - Assessment 3
            if (this.level.equals("1")) {
                aliens.add(createAlien(coordinate, 32, 32, new Texture("alien.gif"), 30 + rand.nextInt(60),
                        250, null, 1, 5 + rand.nextInt(15), new Vector2[]{new Vector2(coordinate.x, coordinate.y),
                                new Vector2(coordinate.x, coordinate.y - 300), new Vector2(coordinate.x - 300, coordinate.y)}, 3f));
            }
            if (this.level.equals("2")) {
                aliens.add(createAlien(coordinate, 32, 32, new Texture("alien.gif"), 30 + rand.nextInt(60),
                        250, null, 1, 5 + rand.nextInt(15), new Vector2[]{new Vector2(coordinate.x, coordinate.y),
                                new Vector2(coordinate.x, coordinate.y - 300), new Vector2(coordinate.x - 300, coordinate.y)}, 2f));
            }
            if (this.level.equals("3")) {
                aliens.add(createAlien(coordinate, 32, 32, new Texture("alien.gif"), 30 + rand.nextInt(60),
                        300, null, 7, 3 + rand.nextInt(15), new Vector2[]{new Vector2(coordinate.x, coordinate.y),
                                new Vector2(coordinate.x, coordinate.y - 300), new Vector2(coordinate.x - 300, coordinate.y)}, 1f));
            }
            if (level.equals("4")) {
                aliens.add(createAlien(coordinate, 32, 32, new Texture("alien.gif"), 30 + rand.nextInt(60),
                        250, null, 1, 13 + rand.nextInt(15), new Vector2[]{new Vector2(coordinate.x, coordinate.y),
                                new Vector2(coordinate.x, coordinate.y - 300), new Vector2(coordinate.x - 300, coordinate.y)}, 0.75f));
            }
            if (level.equals("5")) {
                aliens.add(createAlien(coordinate, 32, 32, new Texture("alien.gif"), 30 + rand.nextInt(60),
                        250, null, 5, 19 + rand.nextInt(15), new Vector2[]{new Vector2(coordinate.x, coordinate.y),
                                new Vector2(coordinate.x, coordinate.y - 300), new Vector2(coordinate.x - 300, coordinate.y)}, 0.5f));
            }
            if (level.equals("6")) {
                aliens.add(createAlien(coordinate, 32, 32, new Texture("alien.gif"), 30 + rand.nextInt(60),
                        250, null, 3, 25 + rand.nextInt(15), new Vector2[]{new Vector2(coordinate.x, coordinate.y),
                                new Vector2(coordinate.x, coordinate.y - 300), new Vector2(coordinate.x - 300, coordinate.y)}, 0.2f));
            }

            fortress.getAlienPositions().remove(coordinate);
            //if (timeLimit-30 < stopwatch.getTime()){
            // for(Alien alien: aliens){
            // alien.moveAlongGrid(new Vector2(33 + 21 * 32, 212 + 0 * 32));
            //  }
            //}
        }
    }

    /**
     * Method that allows for easy creation of an Alien entity.
     * @param coordinate
     * @param width
     * @param height
     * @param texture
     * @param maxHealth
     * @param range
     * @param target
     * @param speed
     * @param dps
     * @param waypoints
     * @param attackCooldown
     * @return
     */
    private Alien createAlien(Vector2 coordinate, int width, int height, Texture texture, int maxHealth, int range,
                              Unit target, int speed, int dps, Vector2[] waypoints, float attackCooldown){
        return (new Alien(coordinate, width, height, texture, maxHealth,
                range, target, speed, dps, waypoints, attackCooldown));
    }
    public void setMinigameWon(Boolean won) {this.minigameWon = won;}
}
