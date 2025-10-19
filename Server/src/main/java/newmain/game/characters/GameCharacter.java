package newmain.game.characters;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import newmain.game.server.ServerConnection;
import newmain.game.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameCharacter {

    // Character properties
    protected float movementSpeed;
    protected boolean tagged;

    // Position and dimensions
    protected float xPos, yPos;
    protected float width, height;
    protected Rectangle boundingBox;

    private String characterDirection;

    private World world;
    private String name;

    private static int spawnPoint = 0;


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
                         World world, boolean tagged) {
        this.movementSpeed = movementSpeed;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.boundingBox = boundingBox;
        this.world = world;
        this.tagged = tagged;
        defineCharacter();
    }

    public void defineCharacter() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(boundingBox.getX(), boundingBox.getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        Body body = world.getGdxWorld().createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(boundingBox.getWidth() / 2);

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
    }

    public float getMovementSpeed() {
        return this.movementSpeed;
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

    public void setCharacterDirection(String characterDirection) {
        this.characterDirection = characterDirection;
    }

    public String getCharacterDirection() {
        return this.characterDirection;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }

    /**
     * Moves the GameCharacter to a new position.
     * @param newXPos new x position of the character
     * @param newYPos new y position of the character
     */
    public void moveToNewPos(float newXPos, float newYPos) throws InterruptedException {
        this.boundingBox.set(boundingBox.getX() + newXPos, boundingBox.getY() + newYPos, boundingBox.getWidth(), boundingBox.getHeight());
        if (collidesWithPlayer()) {
            this.boundingBox.set(boundingBox.getX() - newXPos, boundingBox.getY() - newYPos, boundingBox.getWidth(), boundingBox.getHeight());
        }
    }

    public boolean collidesWithPlayer() throws InterruptedException {
        ArrayList<PlayerGameCharacter> players = new ArrayList<>(world.getClients().values());
        HashMap<Integer, PlayerGameCharacter> clients = new HashMap<>(world.getClients());
        int playerId = -1;
        int thisId = -1;
        float thisXPos = 0;
        float thisYPos = 0;
        float playerXPos = 0;
        float playerYPos = 0;
        int botHitId = 0;
        for (PlayerGameCharacter player : players) {
            for (Map.Entry<Integer, PlayerGameCharacter> entry : clients.entrySet()) {
                if (entry.getValue() != null) {
                    if (entry.getValue().equals(player)) {
                        playerId = entry.getKey();
                        playerXPos = entry.getValue().getBoundingBox().getX();
                        playerYPos = entry.getValue().getBoundingBox().getY();
                    }
                    if (entry.getValue().equals(this)) {
                        thisId = entry.getKey();
                        thisXPos = entry.getValue().getBoundingBox().getX();
                        thisYPos = entry.getValue().getBoundingBox().getY();
                    }
                }
            }
            if (player != null && player.getBoundingBox().overlaps(boundingBox) && player != this) {
                if (player instanceof NewBot || this instanceof NewBot) {
                    if (playerId == Integer.MAX_VALUE) {
                        ServerConnection.botHitPlayerDecreaseScore(thisId, -0.05f);
                    } else {
                        ServerConnection.botHitPlayerDecreaseScore(playerId, -0.05f);
                    }
                    System.out.println("remove points--------------------");
                    return true;
                }
                if (this.tagged || player.getTagged()){
                    for (Map.Entry<Integer, PlayerGameCharacter> entry : clients.entrySet()) {
                        if (entry.getValue() != null) {
                            if (entry.getValue().equals(player)) {
                                playerId = entry.getKey();
                                playerXPos = entry.getValue().getBoundingBox().getX();
                                playerYPos = entry.getValue().getBoundingBox().getY();
                            }
                            if (entry.getValue().equals(this)) {
                                thisId = entry.getKey();
                                thisXPos = entry.getValue().getBoundingBox().getX();
                                thisYPos = entry.getValue().getBoundingBox().getY();
                            }
                        }
                    }
                    if (this.tagged && !player.getTagged()) {

                        if (spawnPoint == 0) {
                            player.moveToNewPos(300 - playerXPos, 300 - playerYPos);
                            spawnPoint++;
                        } else if (spawnPoint == 1) {
                            player.moveToNewPos(1500 - playerXPos, 300 - playerYPos);
                            spawnPoint++;
                        } else if (spawnPoint == 2) {
                            player.moveToNewPos(300 - playerXPos, 650 - playerYPos);
                            spawnPoint++;
                        } else {
                            player.moveToNewPos(1500 - playerXPos, 650 - playerYPos);
                            spawnPoint = 0;
                        }

                        world.setTaggedId(playerId);

                        ServerConnection.sendUpdatedGameCharacterDelay(thisId, this.characterDirection, false, "Yeet");

                        ServerConnection.sendUpdatedGameCharacterDelay(playerId, player.getCharacterDirection(), true, "KULL");

                    } else if (player.getTagged() && !this.tagged) {

                        if (spawnPoint == 0) {
                            this.moveToNewPos(300 - thisXPos, 300 - thisYPos);
                            spawnPoint++;
                        } else if (spawnPoint == 1) {
                            this.moveToNewPos(1500 - thisXPos, 300 - thisYPos);
                            spawnPoint++;
                        } else if (spawnPoint == 2) {
                            this.moveToNewPos(300 - thisXPos, 650 - thisYPos);
                            spawnPoint++;
                        } else {
                            this.moveToNewPos(1500 - thisXPos, 650 - thisYPos);
                            spawnPoint = 0;
                        }

//                        this.moveToNewPos(300 - thisXPos, 300 - thisYPos);
                        world.setTaggedId(thisId);

                        ServerConnection.sendUpdatedGameCharacterDelay(playerId, player.getCharacterDirection(), false, "Yeet");

                        ServerConnection.sendUpdatedGameCharacterDelay(thisId, this.characterDirection, true, "KULL");
                    }
                }
                return true;
            }
        }
        return false;
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
        return Objects.hash(movementSpeed, tagged, xPos, yPos, width, height);
    }

    public void setName(String name) {
        this.name = name;
    }
}

