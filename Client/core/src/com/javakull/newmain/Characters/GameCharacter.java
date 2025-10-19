package com.javakull.newmain.Characters;

import com.badlogic.gdx.math.Rectangle;

import java.util.Objects;

public class GameCharacter {

    // Character properties
    protected float movementSpeed;
    protected boolean tagged;

    // Position and dimensions
    protected float xPos, yPos;
    protected float width, height;
    protected Rectangle boundingBox;
    private String name;


    /**
     * Constructor for GameCharacter.
     * @param movementSpeed movement speed of the character
     * @param boundingBox encapsulates a 2D rectangle(bounding box) for the GameCharacter
     * @param xPos x position of the character
     * @param yPos y position of the character
     * @param width width of the character
     * @param height height of the character
     */
    public GameCharacter(float movementSpeed, Rectangle boundingBox, float xPos, float yPos, float width, float height,
                         String name, boolean tagged) {
        this.movementSpeed = movementSpeed;
        this.boundingBox = boundingBox;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.name = name;
        this.tagged = tagged;
    }

    /**
     * Constructor for GameCharacter without parameters.
     */
    public GameCharacter() { }

    public float getMovementSpeed() {
        return this.movementSpeed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    public boolean getTagged() {
        return this.tagged;
    }

    public Rectangle getBoundingBox() {
        return this.boundingBox;
    }

    /**
     * Moves the GameCharacter to a new position.
     * @param newXPos new x position of the character
     * @param newYPos new y position of the character
     */
    public void moveToNewPos(float newXPos, float newYPos) {
        this.boundingBox.set(newXPos, newYPos, boundingBox.getWidth(), boundingBox.getHeight());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameCharacter that = (GameCharacter) o;
        return Float.compare(that.movementSpeed, movementSpeed) == 0 && tagged == that.tagged
                && Float.compare(that.xPos, xPos) == 0 && Float.compare(that.yPos, yPos) == 0
                && Float.compare(that.width, width) == 0 && Float.compare(that.height, height) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movementSpeed, tagged, boundingBox.getX(), boundingBox.getY(),
                boundingBox.getWidth(), boundingBox.getHeight());
    }
}
