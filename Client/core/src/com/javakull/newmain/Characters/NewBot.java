package com.javakull.newmain.Characters;

import com.badlogic.gdx.math.Rectangle;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class NewBot extends PlayerGameCharacter {

    /**
     * Constructor for GameCharacter.
     *
     * @param movementSpeed movement speed of the character
     * @param boundingBox   encapsulates a 2D rectangle(bounding box) for the GameCharacter
     * @param xPos          x position of the character
     * @param yPos          y position of the character
     * @param width         width of the character
     * @param height        height of the character
     * @param tagged
     */
    public NewBot(String botName, float movementSpeed, Rectangle boundingBox, float xPos, float yPos, float width, float height, boolean tagged) {
        super(botName, movementSpeed, boundingBox, xPos, yPos, width, height, tagged);
        this.movementSpeed = movementSpeed;
        this.boundingBox = boundingBox;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.tagged = tagged;
    }

    public static NewBot createBot(float x, float y, String name, int id, boolean tagged) {
        Rectangle playerGameCharacterRectangle = new Rectangle(x, y, 35f, 30f);
        NewBot newGameCharacter = new NewBot(name, 2f, playerGameCharacterRectangle, x, y, 40f, 40f, tagged);
        newGameCharacter.setPlayerGameCharacterId(id);
        return newGameCharacter;
    }

    public List<Float> moveToNewPosBot() {
        int count = 0;
        int randomNum;
        if (count == 0) {
            Random rand = new Random();
            randomNum = rand.nextInt(4) + 1;
        } else {
            randomNum = 1;
        }
        if (randomNum == 1 && count != 10) { // Goes right
            count++;
            if (count == 10) {
                count = 0;
            }
            return Arrays.asList(2f, 0f);
        } else if (randomNum == 2 && count != 10) { // Goes left
            count++;
            if (count == 10) {
                count = 0;
            }
            return Arrays.asList(-2f, 0f);
        } else if (randomNum == 3 && count != 8) { // Goes up
            count++;
            if (count == 8) {
                count = 0;
            }
            return Arrays.asList(0f, 2f);
        } else if (randomNum == 4 && count != 8) { // Goes down
            count++;
            if (count == 8) {
                count = 0;
            }
            return Arrays.asList(0f, -2f);
        }
        return Arrays.asList(0f, 0f);
    }

    public void moveToNewPosBot(float newXPos, float newYPos) {
        this.boundingBox.set(newXPos, newYPos, boundingBox.getWidth(), boundingBox.getHeight());
    }
}
