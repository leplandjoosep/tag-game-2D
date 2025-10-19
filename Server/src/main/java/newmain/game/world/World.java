package newmain.game.world;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import newmain.game.TMXLoaders.HijackedTmxLoader;
import newmain.game.TMXLoaders.MyServer;
import newmain.game.characters.NewBot;
import newmain.game.characters.PlayerGameCharacter;
import newmain.game.pathfinding.GraphGenerator;
import newmain.game.pathfinding.GraphImp;


import java.io.IOException;
import java.util.*;

public class World {

    private com.badlogic.gdx.physics.box2d.World gdxWorld;
    private Map<Integer, PlayerGameCharacter> clients = new HashMap<>();
    private boolean isNewGame = false;
    public static int taggedId = -1;

    private TiledMap tiledMap;
    private GraphImp graph;


    /**
     * World constructor.
     */
    public World() throws IOException {
        Headless.loadHeadless(this);
        this.gdxWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);
        this.gdxWorld.step(1/60f, 6, 2);
        this.tiledMap = new HijackedTmxLoader(new MyServer.MyFileHandleResolver())
                .load("/home/ubuntu/map/map3.tmx");
       // /home/ubuntu/Server/src/main/java/newmain/game/world/map3.tmx for server
        // /home/ubuntu/map/map3.tmx for local

        this.graph = GraphGenerator.generateGraph(tiledMap);
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public GraphImp getGraph() {
        return graph;
    }

    public com.badlogic.gdx.physics.box2d.World getGdxWorld() {
        return gdxWorld;
    }

    public PlayerGameCharacter getGameCharacter(int id){
        return clients.get(id);
    }

    public Map<Integer, PlayerGameCharacter> getClients(){
        return clients;
    }

    public void setNewGame(boolean newGame) {
        isNewGame = newGame;
    }

    public boolean isNewGame() {
        return isNewGame;
    }


    /**
     * Add a new PlayerGameCharacter to the clients map.
     *
     * @param id of the PlayerGameCharacter and connection whose the playerGameCharacter is
     * @param gameCharacter new PlayerGamCharacter
     */
    public void addGameCharacter(Integer id, PlayerGameCharacter gameCharacter){
        clients.put(id, gameCharacter);
    }
    
    public void addBotCharacter(Integer id, NewBot botCharacter) {
        clients.put(id, botCharacter);
    }


    /**
     * Remove a client from the clients map.
     *
     * @param id of the PlayerGameCharacter and connection
     */
    public void removeClient(int id){
        clients.remove(id);
    }

    /**
     * Get a list of clients ids.
     *
     * @return list of ids (List<Integer>)
     */
    public List<Integer> getClientsIds() {
        return new LinkedList<>(clients.keySet());
    }

    /**
     * Move the PlayerGameCharacter and update PlayerGameCharacter's direction.
     *
     * @param id of the PlayerGameCharacter
     * @param xPosChange how much the x coordinate has changed
     * @param yPosChange how much the y coordinate has changed
     */
    public void movePlayerGameCharacter(int id, float xPosChange, float yPosChange) throws InterruptedException {
        PlayerGameCharacter character = getGameCharacter(id);
        if (character != null) {
            character.moveToNewPos(xPosChange, yPosChange);
            if (xPosChange == 0 && yPosChange > 0) {
                character.setCharacterDirection("up");
            } else if (xPosChange < 0 && yPosChange == 0) {
                character.setCharacterDirection("left");
            } else if (xPosChange == 0 && yPosChange < 0) {
                character.setCharacterDirection("down");
            } else if (xPosChange > 0 && yPosChange == 0) {
                character.setCharacterDirection("right");
            } else if (xPosChange < 0 && yPosChange > 0) {
                character.setCharacterDirection("up-left");
            } else if (xPosChange > 0 && yPosChange > 0) {
                character.setCharacterDirection("up-right");
            } else if (xPosChange < 0 && yPosChange < 0) {
                character.setCharacterDirection("down-left");
            } else if (xPosChange > 0 && yPosChange < 0) {
                character.setCharacterDirection("down-right");
            }
        }
    }


    /**
     * Reset World instance variables.
     */
    public void restartWorld() {
        clients.clear();
        isNewGame = false;
    }

    public void setTaggedId(int id) {
        taggedId = id;
    }

    public int getTaggedId() {
        return taggedId;
    }
}
