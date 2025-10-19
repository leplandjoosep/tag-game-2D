package com.javakull.newmain.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class PlayerGameCharacter extends GameCharacter {

    private String playerName;
    private int playerGameCharacterId;


    private TextureRegion characterTexture, characterTexture0, characterTexture1;
    private boolean textureBoolean = false;
    private String playerDirection = "idle_right";
    private float elapsedTime = 0f;
    private Array<TextureRegion> runningFramesRight = new Array<>();
    private Array<TextureRegion> runningFramesLeft = new Array<>();
    private Array<TextureRegion> idleFramesRight = new Array<>();
    private Array<TextureRegion> idleFramesLeft = new Array<>();
    private Array<TextureRegion> kullRunningFramesRight = new Array<>();
    private Array<TextureRegion> kullRunningFramesLeft = new Array<>();
    private Array<TextureRegion> kullIdleFramesRight = new Array<>();
    private Array<TextureRegion> kullIdleFramesLeft = new Array<>();
    private Array<TextureRegion> botRunningFramesRight = new Array<>();
    private Array<TextureRegion> botRunningFramesLeft = new Array<>();
    private Array<TextureRegion> botIdleFramesRight = new Array<>();
    private Array<TextureRegion> botIdleFramesLeft = new Array<>();
    Animation<TextureRegion> currentAnimation = new Animation<TextureRegion>(0.1f, idleFramesRight);

    private float score;

    /**
     * Constructor for PlayerGameCharacter.
     * @param playerName name of the player
     * @param movementSpeed movement speed of the player
     * @param boundingBox encapsulates a 2D rectangle(bounding box) for the PlayerGameCharacter
     * @param xPos x position of the PlayerGameCharacter
     * @param yPos y position of the PlayerGameCharacter
     * @param width width of the PlayerGameCharacter
     * @param height height of the PlayerGameCharacter
     */
    public PlayerGameCharacter(String playerName, float movementSpeed, Rectangle boundingBox, float xPos,
                               float yPos, float width, float height, boolean tagged) {
        super(movementSpeed, boundingBox, xPos, yPos, width, height, playerName, tagged);
        this.playerName = playerName;
        this.movementSpeed = movementSpeed;
        this.boundingBox = boundingBox;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.tagged = tagged;
        this.score = 0;
    }

    public void setScore(float score) {
        this.score += score;
    }

    public float getScore() {
        return score;
    }

    public String getName() {
        return this.playerName;
    }

    @Override
    public void setName(String name) {
        this.playerName = name;
    }

    public void setPlayerGameCharacterId(int playerGameCharacterId) {
        this.playerGameCharacterId = playerGameCharacterId;
    }

    public int getPlayerGameCharacterId() {
        return this.playerGameCharacterId;
    }

    public void setPlayerDirection(String playerDirection) {
        this.playerDirection = playerDirection;
    }

    public String getPlayerDirection() {
        return this.playerDirection;
    }

    public void setCharacterTexture(Array<TextureRegion> frames) {
        currentAnimation = new Animation<TextureRegion>(0.1f, frames);
    }

    public boolean getTagged() {
        return this.tagged;
    }

    public void setPlayerTagged(boolean tagged) {
        this.tagged = tagged;
    }


    /**
     * Static method for creating a new PlayerGameCharacter.
     * @param x x position of the PlayerGameCharacter
     * @param y y position of the PlayerGameCharacter
     * @param name name of the PlayerGameCharacter
     * @param id id of the PlayerGameCharacter
     * @return a new PlayerGameCharacter
     */
    public static PlayerGameCharacter createPlayerGameCharacter(float x, float y, String name, int id, boolean tagged) {
        Rectangle playerGameCharacterRectangle = new Rectangle(x, y, 35f, 30f);
        PlayerGameCharacter newGameCharacter = new PlayerGameCharacter(name, 4f, playerGameCharacterRectangle, x, y, 40f, 40f, tagged);
        newGameCharacter.setPlayerGameCharacterId(id);
        return newGameCharacter;
    }

    /**
     * Drawing the PlayerGameCharacter on the screen.
     * @param batch is used to draw 2D rectangles
     */
    public void draw(Batch batch) {
        if (!textureBoolean) {
            this.characterTexture = this.characterTexture0;
            this.textureBoolean = true;
            runningFramesRight.add(new TextureAtlas("run_right\\run_right.atlas").findRegion("run_right_1"));
            runningFramesRight.add(new TextureAtlas("run_right\\run_right.atlas").findRegion("run_right_2"));
            runningFramesRight.add(new TextureAtlas("run_right\\run_right.atlas").findRegion("run_right_3"));
            runningFramesRight.add(new TextureAtlas("run_right\\run_right.atlas").findRegion("run_right_4"));
            runningFramesRight.add(new TextureAtlas("run_right\\run_right.atlas").findRegion("run_right_5"));
            runningFramesRight.add(new TextureAtlas("run_right\\run_right.atlas").findRegion("run_right_6"));

            runningFramesLeft.add(new TextureAtlas("run_left\\run_left.atlas").findRegion("run_left_1"));
            runningFramesLeft.add(new TextureAtlas("run_left\\run_left.atlas").findRegion("run_left_2"));
            runningFramesLeft.add(new TextureAtlas("run_left\\run_left.atlas").findRegion("run_left_3"));
            runningFramesLeft.add(new TextureAtlas("run_left\\run_left.atlas").findRegion("run_left_4"));
            runningFramesLeft.add(new TextureAtlas("run_left\\run_left.atlas").findRegion("run_left_5"));
            runningFramesLeft.add(new TextureAtlas("run_left\\run_left.atlas").findRegion("run_left_6"));

            idleFramesLeft.add(new TextureAtlas("idle_left\\idle_left.atlas").findRegion("idle_left_1"));
            idleFramesLeft.add(new TextureAtlas("idle_left\\idle_left.atlas").findRegion("idle_left_2"));
            idleFramesLeft.add(new TextureAtlas("idle_left\\idle_left.atlas").findRegion("idle_left_3"));
            idleFramesLeft.add(new TextureAtlas("idle_left\\idle_left.atlas").findRegion("idle_left_4"));

            idleFramesRight.add(new TextureAtlas("idle_right\\idle_right.atlas").findRegion("idle_right_1"));
            idleFramesRight.add(new TextureAtlas("idle_right\\idle_right.atlas").findRegion("idle_right_2"));
            idleFramesRight.add(new TextureAtlas("idle_right\\idle_right.atlas").findRegion("idle_right_3"));
            idleFramesRight.add(new TextureAtlas("idle_right\\idle_right.atlas").findRegion("idle_right_4"));

            // tagged player frames
            TextureAtlas atlas1 = new TextureAtlas("kull_run_right\\kull_run_right.atlas");
            for (int i = 1; i <= 6; i++) {
                kullRunningFramesRight.add(atlas1.findRegion("kull_Run_" + i));
            }

            TextureAtlas atlas2 = new TextureAtlas("kull_run_left\\kull_run_left.atlas");
            for (int i = 1; i <= 6; i++) {
                kullRunningFramesLeft.add(atlas2.findRegion("kull_Run_left_" + i));
            }

            TextureAtlas atlas3 = new TextureAtlas("kull_idle_right\\kull_idle_right.atlas");
            for (int i = 1; i <= 4; i++) {
                kullIdleFramesRight.add(atlas3.findRegion("kull_Idle_" + i));
            }

            TextureAtlas atlas4 = new TextureAtlas("kull_idle_left\\kull_idle_left.atlas");
            for (int i = 1; i <= 4; i++) {
                kullIdleFramesLeft.add(atlas4.findRegion("kull_Idle_left_" + i));
            }

            // bot frames
            TextureAtlas atlas11 = new TextureAtlas("bot_idle_left\\bot_idle_left.atlas");
            for (int i = 1; i <= 4; i++) {
                botIdleFramesLeft.add(atlas11.findRegion("Dude_Monster_Idle_" + i));
            }

            TextureAtlas atlas12 = new TextureAtlas("bot_idle_right\\bot_idle_right.atlas");
            for (int i = 1; i <= 4; i++) {
                botIdleFramesRight.add(atlas12.findRegion("Dude_Monster_Idle_" + i));
            }

            TextureAtlas atlas13 = new TextureAtlas("bot_run_left\\bot_run_left.atlas");
            for (int i = 1; i <= 6; i++) {
                botRunningFramesLeft.add(atlas13.findRegion("Dude_Monster_Run_" + i));
            }

            TextureAtlas atlas14 = new TextureAtlas("bot_run_right\\bot_run_right.atlas");
            for (int i = 1; i <= 6; i++) {
                botRunningFramesRight.add(atlas14.findRegion("Dude_Monster_Run_" + i));
            }

            if (this instanceof NewBot) {
                currentAnimation = new Animation<TextureRegion>(0.1f, botIdleFramesRight);
            } else {
                currentAnimation = new Animation<TextureRegion>(0.1f, idleFramesRight);
            }
        }


        elapsedTime += Gdx.graphics.getDeltaTime();

        Animation<TextureRegion> runningRightAnimation = new Animation<>(0.1f, runningFramesRight);
        Animation<TextureRegion> runningLeftAnimation = new Animation<>(0.1f, runningFramesLeft);
        Animation<TextureRegion> idleLeftAnimation = new Animation<>(0.1f, idleFramesLeft);
        Animation<TextureRegion> idleRightAnimation = new Animation<>(0.1f, idleFramesRight);

        Animation<TextureRegion> kullRunningRightAnimation = new Animation<>(0.1f, kullRunningFramesRight);
        Animation<TextureRegion> kullRunningLeftAnimation = new Animation<>(0.1f, kullRunningFramesLeft);
        Animation<TextureRegion> kullIdleLeftAnimation = new Animation<>(0.1f, kullIdleFramesLeft);
        Animation<TextureRegion> kullIdleRightAnimation = new Animation<>(0.1f, kullIdleFramesRight);

        Animation<TextureRegion> botRunningRightAnimation = new Animation<>(0.1f, botRunningFramesRight);
        Animation<TextureRegion> botRunningLeftAnimation = new Animation<>(0.1f, botRunningFramesLeft);
        Animation<TextureRegion> botIdleLeftAnimation = new Animation<>(0.1f, botIdleFramesLeft);
        Animation<TextureRegion> botIdleRightAnimation = new Animation<>(0.1f, botIdleFramesRight);


        TextureRegion currentFrame = currentAnimation.getKeyFrame(elapsedTime, true);
        if (currentFrame == null) {
            currentFrame = new TextureAtlas("kull_idle_right\\kull_idle_right.atlas").findRegion("kull_Idle_1");
        }
        batch.draw(currentFrame, boundingBox.getX() - 1, boundingBox.getY(), 40f, 40f);
    }


    /**
     * Set PlayerGameCharacter's texture according to the movement direction.
     */
    public void setTextureForDirection() {
        if (getPlayerGameCharacterId() == Integer.MAX_VALUE) {
            switch (this.playerDirection) {
                case "up-right":
                case "right":
                case "down-right":
                    setCharacterTexture(botRunningFramesRight);
                    break;
                case "up-left":
                case "left":
                case "down-left":
                    setCharacterTexture(botRunningFramesLeft);
                    break;
                case "idle_right":
                    setCharacterTexture(botIdleFramesRight);
                    break;
                case "idle_left":
                    setCharacterTexture(botIdleFramesLeft);
                    break;
            }
        } else {
            if (this.tagged) {
                switch (this.playerDirection) {
                    case "up-right":
                    case "right":
                    case "down-right":
                        setCharacterTexture(kullRunningFramesRight);
                        break;
                    case "up-left":
                    case "left":
                    case "down-left":
                        setCharacterTexture(kullRunningFramesLeft);
                        break;
                    case "idle_right":
                        setCharacterTexture(kullIdleFramesRight);
                        break;
                    case "idle_left":
                        setCharacterTexture(kullIdleFramesLeft);
                        break;
                }
            } else {
                switch (this.playerDirection) {
                    case "up-right":
                    case "right":
                    case "down-right":
                        setCharacterTexture(runningFramesRight);
                        break;
                    case "up-left":
                    case "left":
                    case "down-left":
                        setCharacterTexture(runningFramesLeft);
                        break;
                    case "idle_right":
                        setCharacterTexture(idleFramesRight);
                        break;
                    case "idle_left":
                        setCharacterTexture(idleFramesLeft);
                        break;
                }
            }
        }
    }
}
