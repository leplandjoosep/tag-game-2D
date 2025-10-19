package newmain.game.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobbies {
    private boolean gameStarted = false;
    private Integer lobbyID = 1;
    private Integer lobby1ConnectedNumber = 0;
    private List<String> connectedPlayers = new ArrayList<>();
    private static Map<String, Float> scores = new HashMap<>();

    public Map<String, Float> getScores() {
        return scores;
    }

    public void putScores(String name, float score) {
        scores.put(name, score);
    }

    public void addOnePlayerToLobby() {
        lobby1ConnectedNumber++;
    }

    public Integer getLobby1ConnectedNumber() {
        return connectedPlayers.size();
    }
    public void addPlayer(String playerName) {
        connectedPlayers.add(playerName);
    }

    public List<String> getConnectedPlayers() {
        return connectedPlayers;
    }

    public Integer getLobbyID() {
        return lobbyID;
    }
    public void clearConnectedPlayers() {
        connectedPlayers.clear();
    }
    public void updateLobbyID() {
        lobbyID++;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }
    public void removePlayerFromConnectedPlayers(String playerName) {
        connectedPlayers.remove(playerName);
    }
    public void clearScores() {
        scores.clear();
    }
}
