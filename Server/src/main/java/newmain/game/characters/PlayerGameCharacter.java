package newmain.game.characters;

import com.badlogic.gdx.math.Rectangle;
import newmain.game.world.World;



public class PlayerGameCharacter extends GameCharacter {

    private final String playerName;
    private int playerGameCharacterId;
    private World world;

    private static final int TIME_BETWEEN_COLLISIONS = 100;

    /**
     * Constructor for GameCharacter.
     *
     * @param movementSpeed movement speed of the character
     * @param boundingBox   encapsulates a 2D rectangle(bounding box) for the GameCharacter
     * @param xPos          x position of the character
     * @param yPos          y position of the character
     * @param width         width of the character
     * @param height        height of the character
     * @param world world
     */
    public PlayerGameCharacter(String playerName, float movementSpeed, Rectangle boundingBox, float xPos, float yPos,
                               float width, float height, World world, boolean tagged) {
        super(movementSpeed, boundingBox, xPos, yPos, width, height, world, tagged);
        this.playerName = playerName;
        this.movementSpeed = movementSpeed;
        this.boundingBox = boundingBox;
        this.xPos = xPos;
        this.yPos = yPos;
        this.height = height;
        this.width = width;
        this.world = world;
        this.tagged = tagged;
    }

    public String getName() {
        return playerName;
    }

    public void setPlayerGameCharacterId(int playerGameCharacterId) {
        this.playerGameCharacterId = playerGameCharacterId;
    }

    public int getPlayerGameCharacterId() {
        return playerGameCharacterId;
    }

    /**
     * PlayerGameCharacter static method for creating a new PlayerGameCharacter instance.
     *
     * @param x coordinate of the PlayerGameCharacter (float)
     * @param y coordinate of the PlayerGameCharacter (float)
     * @param name of the player (String)
     * @param world where the PlayerGameCharacter is (World)
     * @param id unique id (int)
     * @return new PlayerGameCharacter instance
     */
    public static PlayerGameCharacter createPlayerGameCharacter(float x, float y, String name, World world, int id, boolean tagged) {
        Rectangle playerGameCharacterRectangle = new Rectangle(x, y, 30f, 30f);
        PlayerGameCharacter newGameCharacter = new PlayerGameCharacter(name, 4f, playerGameCharacterRectangle, x, y, 30f, 30f, world, tagged);
        newGameCharacter.setPlayerGameCharacterId(id);
        return newGameCharacter;
    }
}
