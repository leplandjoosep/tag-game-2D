package com.javakull.newmain.GameInfo;

import NewClientConnection.NewClientConnection;
import com.badlogic.gdx.math.Rectangle;
import com.javakull.newmain.Characters.NewBot;
import com.javakull.newmain.Characters.PlayerGameCharacter;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

public class ClientWorld {

    private NewClientConnection clientConnection;
    private PlayerGameCharacter myPlayerGameCharacter;
    private NewBot newBot;
    private final HashMap<Integer, PlayerGameCharacter> worldGameCharactersMap = new HashMap<>();
    private int score = 0;
    private static Integer worldTime = 300;

    /**
     * Add the instance of ClientConnection to this class.
     * @param clientConnection The instance of ClientConnection.
     */
    public void registerClient(NewClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public static Integer getWorldTime() {
        return worldTime;
    }

    public void setWorldTimeOneLess() {
        worldTime -= 1;
    }

    public void setScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return this.score;
    }

    public void setMyPlayerGameCharacter(PlayerGameCharacter myPlayerGameCharacter) {
        this.myPlayerGameCharacter = myPlayerGameCharacter;
    }

    public void removeCharacter(int id) {
        worldGameCharactersMap.remove(id);
    }

    public PlayerGameCharacter getMyPlayerGameCharacter() {
        return this.myPlayerGameCharacter;
    }

    /**
     * Map of clients and their PlayerGameCharacters.
     * Key: id, value: PlayerGameCharacter
     */
    public HashMap<Integer, PlayerGameCharacter> getWorldGameCharactersMap() {
        //System.out.println(worldGameCharactersMap);
        return worldGameCharactersMap;
    }

    /**
     * Add a new PlayerGameCharacter to the characters map.
     * @param id id of the PlayerGameCharacter
     * @param newCharacter new PlayerGameCharacter to add
     */
    public void addGameCharacter(Integer id, PlayerGameCharacter newCharacter) {
        worldGameCharactersMap.put(id, newCharacter);
    }

    public PlayerGameCharacter getGameCharacter(Integer id) {
        return worldGameCharactersMap.get(id);
    }

    /**
     * Moves the PlayerGameCharacter to the new position.
     * @param id of the moving character - id is key in worldGameCharactersMap
     * @param xChange x change of the moving character
     * @param yChange y change of the moving character
     * @param direction direction of the moving character
     * @param tagged true if the moving character is tagged
     */
    public void movePlayerGameCharacter(Integer id, float xChange, float yChange, String direction, boolean tagged) {
        getGameCharacter(id).moveToNewPos(xChange, yChange);
        getGameCharacter(id).setPlayerDirection(direction);
        getGameCharacter(id).setTextureForDirection();
        getGameCharacter(id).setPlayerTagged(tagged);
    }

    public void setMyBot(NewBot newGameCharacter) {
        this.newBot = newGameCharacter;
    }

    public NewBot getNewBot() {
        return newBot;
    }

}
