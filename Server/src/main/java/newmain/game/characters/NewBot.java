package newmain.game.characters;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import newmain.game.entities.BotCounter;
import newmain.game.pathfinding.Algo.AStarPathFinding;
import newmain.game.pathfinding.Node;
import newmain.game.world.World;

import java.util.List;
import java.util.Random;

public class NewBot extends PlayerGameCharacter {

    public List<Float> moveToNewPosBot;
    private AStarPathFinding aStarPathFinding;
    private int playerGameCharacterId;
    private Node currentNode, nextNode;
    private float deltaX, deltaY;
    private direction xMovingDirection = direction.NULL,
            yMovingDirection = direction.NULL,
            movingDirection = direction.NULL;
    private World world;
    private enum direction {
        LEFT, RIGHT, UP, DOWN, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT, NULL
    }
    private static final float PLUS_MOVEMENT_COORDINATE = 2f;
    private static final float MINUS_MOVEMENT_COORDINATE = -2f;


    /**
     * Constructor for GameCharacter.
     *
     * @param movementSpeed movement speed of the character
     * @param boundingBox   encapsulates a 2D rectangle(bounding box) for the GameCharacter
     * @param xPos          x position of the character
     * @param yPos          y position of the character
     * @param width         width of the character
     * @param height        height of the character
     * @param world
     * @param tagged
     */
    public NewBot(String botName, float movementSpeed, Rectangle boundingBox, float xPos, float yPos, float width, float height, World world, boolean tagged) {
        super(botName, movementSpeed, boundingBox, xPos, yPos, width, height, world, tagged);
        this.world = world;
        this.aStarPathFinding = new AStarPathFinding(world, this, world.getGameCharacter(playerGameCharacterId));
    }
    public static NewBot createBot(float x, float y, String name, World world, int id, boolean tagged) {
        Rectangle playerGameCharacterRectangle = new Rectangle(x, y, 35f, 30f);
        NewBot newGameCharacter = new NewBot(name, 2f, playerGameCharacterRectangle, x, y, 40f, 40f, world, tagged);
        newGameCharacter.setPlayerGameCharacterId(id);
        return newGameCharacter;
    }

    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    public float getDeltaX() {
        return deltaX;
    }

    public float getDeltaY() {
        return deltaY;
    }

    public void findNextNode() {
        aStarPathFinding.calculatePath();
        if (aStarPathFinding.getSolutionGraphPath().getCount() == 1) {
            nextNode = currentNode;
        } else if (aStarPathFinding.getSolutionGraphPath().getCount() > 1) {
            nextNode = aStarPathFinding.getSolutionGraphPath().get(1);
        }
    }

    public void findNextXAndY() {
        if (nextNode != null && currentNode != null) {
            if (nextNode.equals(currentNode)) {
                switch (xMovingDirection) {
                    case LEFT:
                        deltaX = MINUS_MOVEMENT_COORDINATE;
                        break;
                    case RIGHT:
                        deltaX = PLUS_MOVEMENT_COORDINATE;
                        break;
                    default:
                        deltaX = 0f;
                        break;
                }
                switch (yMovingDirection) {
                    case DOWN:
                        deltaY = MINUS_MOVEMENT_COORDINATE;
                        break;
                    case UP:
                        deltaY = PLUS_MOVEMENT_COORDINATE;
                        break;
                    default:
                        deltaY = 0f;
                        break;
                }
                if (deltaX == PLUS_MOVEMENT_COORDINATE && deltaY == PLUS_MOVEMENT_COORDINATE) {
                    movingDirection = direction.UP_RIGHT;
                } else if (deltaX == PLUS_MOVEMENT_COORDINATE && deltaY == MINUS_MOVEMENT_COORDINATE) {
                    movingDirection = direction.DOWN_RIGHT;
                } else if (deltaX == MINUS_MOVEMENT_COORDINATE && deltaY == PLUS_MOVEMENT_COORDINATE) {
                    movingDirection = direction.UP_LEFT;
                } else if (deltaX == MINUS_MOVEMENT_COORDINATE && deltaY == MINUS_MOVEMENT_COORDINATE) {
                    movingDirection = direction.DOWN_LEFT;
                }

            }
        }
    }

    public List<Float> moveToNewPosBot() {
        float speed = (float) 2 / getWorld().getClients().values().size();
        Vector2 closest = getClosestPosition();
        float minusMovement = MINUS_MOVEMENT_COORDINATE; // (getWorld().getClients().size() - 1);
        float plusMovement = PLUS_MOVEMENT_COORDINATE; // (getWorld().getClients().size() - 1);
        if (getWorld().getClients().values().size() > 2) {
            minusMovement = -1f;
            plusMovement = 1f;
        }
        this.aStarPathFinding = new AStarPathFinding(world, this, world.getGameCharacter(this.playerGameCharacterId));
        findNextNode();
        System.out.println("player id to follow: " + this.playerGameCharacterId);
        System.out.println("next node " + nextNode);
        if (nextNode != null) {
            if (nextNode.getX() < currentNode.getX()) {  // Moves left.
                deltaX = minusMovement;
                xMovingDirection = direction.LEFT;
                movingDirection = direction.LEFT;
                setCharacterDirection("left");
            } else if (nextNode.getX() > currentNode.getX()) {  // Moves right.
                deltaX = plusMovement;
                xMovingDirection = direction.RIGHT;
                movingDirection = direction.RIGHT;
                setCharacterDirection("right");
            } else {
                deltaX = 0f;
                setCharacterDirection("left");
            }

            if (nextNode.getY() < currentNode.getY()) {  // Moves down.
                deltaY = minusMovement;
                yMovingDirection = direction.DOWN;
                movingDirection = direction.DOWN;
                setCharacterDirection("down");
            } else if (nextNode.getY() > currentNode.getY()) {  // Moves up.
                deltaY = plusMovement;
                yMovingDirection = direction.UP;
                movingDirection = direction.UP;
                setCharacterDirection("up");
            } else {
                deltaY = 0f;
                setCharacterDirection("left");
            }
        }
        setCharacterDirection("left");

//        if (closest.x < this.boundingBox.getX() && closest.y < this.boundingBox.getY()) {
//            setCharacterDirection("down-left");
//            System.out.println("down left");
//            return List.of(-speed, -speed);
//        } else if (closest.x > this.boundingBox.getX() && closest.y > this.boundingBox.getY()) {
//            setCharacterDirection("up-right");
//            System.out.println("up right");
//            return List.of(speed, speed);
//        } else if (closest.x < this.boundingBox.getX() && closest.y > this.boundingBox.getY()) {
//            setCharacterDirection("up-left");
//            System.out.println("up left");
//            return List.of(-speed, speed);
//        } else if (closest.x > this.boundingBox.getX() && closest.y < this.boundingBox.getY()) {
//            setCharacterDirection("down-right");
//            System.out.println("down right");
//            return List.of(speed, -speed);
//        } else if (closest.x == this.boundingBox.getX() && closest.y > this.boundingBox.getY()) {
//            setCharacterDirection("up");
//            System.out.println("up");
//            return List.of(0f, speed);
//        } else if (closest.x == this.boundingBox.getX() && closest.y < this.boundingBox.getY()) {
//            setCharacterDirection("down");
//            System.out.println("down");
//            return List.of(0f, -speed);
//        } else if (closest.x > this.boundingBox.getX() && closest.y == this.boundingBox.getY()) {
//            setCharacterDirection("right");
//            System.out.println("right");
//            return List.of(speed, 0f);
//        } else if (closest.x < this.boundingBox.getX() && closest.y == this.boundingBox.getY()) {
//            setCharacterDirection("left");
//            System.out.println("left");
//            return List.of(-speed, 0f);
//        }
//        setCharacterDirection("idle-right");
//        System.out.println("idle");
        return List.of(deltaX, deltaY);
    }

    private Vector2 getClosestPosition() {
        float closestX = 0;
        float closestY = 0;
        for (PlayerGameCharacter character : getWorld().getClients().values()) {
            // find the closest player coordinates (x,y)
            if (character != this && character != null) {
                if (closestX == 0 && closestY == 0) {
                    closestX = character.boundingBox.getX();
                    closestY = character.boundingBox.getY();
                    this.playerGameCharacterId = character.getPlayerGameCharacterId();
                } else if (Math.sqrt((closestX - boundingBox.getX()) * (closestX - boundingBox.getX()) + (closestY - boundingBox.getY()) * (closestY - boundingBox.getY())) >
                        Math.sqrt((character.boundingBox.getX() - boundingBox.getX()) * (character.boundingBox.getX() - boundingBox.getX()) + (character.boundingBox.getY() - boundingBox.getY()) * (character.boundingBox.getY() - boundingBox.getY()))) {
                    closestX = character.boundingBox.getX();
                    closestY = character.boundingBox.getY();
                    this.playerGameCharacterId = character.getPlayerGameCharacterId();
                }
            }
        }
        return new Vector2(closestX, closestY);
    }
}
