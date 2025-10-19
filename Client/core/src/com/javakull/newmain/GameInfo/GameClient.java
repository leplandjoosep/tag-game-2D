package com.javakull.newmain.GameInfo;

import NewClientConnection.NewClientConnection;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.javakull.newmain.Screens.EndScreen;
import com.javakull.newmain.Screens.GameScreen;
import com.javakull.newmain.Screens.LobbyScreen;
import com.javakull.newmain.Screens.MenuScreen;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class GameClient extends Game {

    private GameScreen gameScreen;
    private NewClientConnection clientConnection;
    private NewClientConnection lobby1Connection;
    private NewClientConnection lobby2Connection;
    private ClientWorld clientWorld;
    private String playerName;
    private MenuScreen menuScreen;
    private LobbyScreen lobbyScreen;
    private EndScreen endScreen;
    private HashMap<Integer, NewClientConnection> lobbyConnections = new HashMap<>();

    /**
     * Method creates a new Client who connects to the Server with its ClientWorld and GameScreen.
     */
    public void createClient(ClientWorld clientWorld, GameScreen gameScreen) throws IOException {
        clientConnection.setGameScreen(gameScreen);
        clientConnection.setClientWorld(clientWorld);
        clientConnection.setGameClient(this);
        clientConnection.sendPacketConnect(playerName, false);
        System.out.println(playerName);
        gameScreen.registerClientConnection(clientConnection);
        clientWorld.registerClient(clientConnection);
        gameScreen.setGameClient(this);
    }

    public void createClientForSpectate(ClientWorld clientWorld, GameScreen gameScreen) throws IOException {
        clientConnection.setGameScreen(gameScreen);
        clientConnection.setClientWorld(clientWorld);
        clientConnection.setGameClient(this);
        clientConnection.sendPacketConnect(playerName, true);
        System.out.println(playerName);
        gameScreen.registerClientConnection(clientConnection);
        clientWorld.registerClient(clientConnection);
        gameScreen.setGameClient(this);
        gameScreen.setForSpectate(true);
    }

    public boolean connectToServer() {
        clientConnection = new NewClientConnection();
        return clientConnection.isConnected();
    }

    public void createLobby() {
        this.lobbyScreen = new LobbyScreen(this, clientConnection);
        setScreen(lobbyScreen);
    }

    public void createEndScreen(boolean gameEndedTimeOut) {
        if (clientWorld.getMyPlayerGameCharacter() != null) {
            clientConnection.sendScore(clientWorld.getMyPlayerGameCharacter().getName(),
                    clientWorld.getMyPlayerGameCharacter().getScore());
        }
        this.endScreen = new EndScreen(this, gameEndedTimeOut);
        endScreen.setClientConnection(clientConnection);
        setScreen(endScreen);
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Creates menu screen.
     */
    @Override
    public void create() {
        this.menuScreen = new MenuScreen(this);
        setScreen(menuScreen);
    }
    public void showFull() {
//        this.menuScreen.showFull(menuScreen.getStage());
    }

    /**
     * Starts the game (tries to create a new client).
     */
    public void startGame() {
        clientWorld = new ClientWorld();
        gameScreen = new GameScreen(clientWorld);
        setScreen(gameScreen);
        try {
            createClient(clientWorld, gameScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gdx.input.setInputProcessor(gameScreen);
    }
    public void spectateGame() {
        clientWorld = new ClientWorld();
        gameScreen = new GameScreen(clientWorld);
        setScreen(gameScreen);
        try {
            createClientForSpectate(clientWorld, gameScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Gdx.input.setInputProcessor(gameScreen);
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
