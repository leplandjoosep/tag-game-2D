package NewClientConnection;


import NewPackets.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import com.javakull.newmain.Characters.GameCharacter;
import com.javakull.newmain.Characters.NewBot;
import com.javakull.newmain.GameInfo.ClientWorld;
import com.javakull.newmain.GameInfo.GameClient;
import com.javakull.newmain.Screens.*;
import com.javakull.newmain.Characters.PlayerGameCharacter;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NewClientConnection {

    private GameScreen gameScreen;
    private ClientWorld clientWorld;
    private GameClient gameClient;
    private LobbyScreen lobbyScreen;
    private OneLobbyScreen oneLobbyScreen;
    private EndScreen endScreen;
    private Client client;
    private String playerName;
    private boolean connected = false;


    public NewClientConnection() {

        // local 127.0.0.1
        // 193.40.156.195
        String ip = "193.40.156.195";

        // must be same on server
        int udpPort = 8090, tcpPort = 8080;

        client = new Client(49152, 49152);
        client.start();

        // Register all packets that are sent over the network.
        client.getKryo().register(Packet.class);
        client.getKryo().register(PacketConnect.class);
        client.getKryo().register(PacketAddCharacter.class);
        client.getKryo().register(GameCharacter.class);
        client.getKryo().register(PacketUpdateCharacterInformation.class);
        client.getKryo().register(PacketCreator.class);
        client.getKryo().register(ArrayList.class);
        client.getKryo().register(Rectangle.class);
        client.getKryo().register(HashMap.class);
        client.getKryo().register(PacketClientDisconnect.class);
        client.getKryo().register(PacketServerIsFull.class);
        client.getKryo().register(PacketAddBot.class);
        client.getKryo().register(PacketUpdateBotInformation.class);
        client.getKryo().register(PacketUpdateLobbyPeopleNumber.class);
        client.getKryo().register(PacketConnectToLobby.class);
        client.getKryo().register(PacketUpdateLobbyPeopleList.class);
        client.getKryo().register(PacketStartMatch.class);
        client.getKryo().register(PacketStartSpectating.class);
        client.getKryo().register(PacketDisconnectFromLobby.class);
        client.getKryo().register(PacketResetLobby.class);
        client.getKryo().register(PacketScoreId.class);
        client.getKryo().register(PacketSendScore.class);
        client.getKryo().register(PacketSendScoreMap.class);


        // add a listener to receive objects
        client.addListener(new Listener.ThreadedListener(new Listener()) {

            // receive packets from server
            public void received(Connection connection, Object object) {
                if (object instanceof Packet && clientWorld != null) {
                    if (object instanceof PacketAddCharacter) {
                        PacketAddCharacter packetAddCharacter = (PacketAddCharacter) object;

                        // Create new PlayerGameCharacter
                        PlayerGameCharacter newGameCharacter = PlayerGameCharacter.createPlayerGameCharacter(packetAddCharacter.getX(),
                                packetAddCharacter.getY(), packetAddCharacter.getPlayerName(), packetAddCharacter.getId(), packetAddCharacter.getTagged());

                        // add new character to world
                        clientWorld.addGameCharacter(packetAddCharacter.getId(), newGameCharacter);
                        if (packetAddCharacter.getId() == connection.getID()) {
                            // if new character is the player
                            clientWorld.setMyPlayerGameCharacter(newGameCharacter);
                        }
                    } else if (object instanceof PacketAddBot) {
                        PacketAddBot packetAddBot = (PacketAddBot) object;

                        // Create new PlayerGameCharacter
                        NewBot newGameCharacter = NewBot.createBot(packetAddBot.getX(),
                                packetAddBot.getY(), packetAddBot.getBotName(), packetAddBot.getId(), packetAddBot.getTagged());

                        // add new character to world
                        clientWorld.addGameCharacter(packetAddBot.getId(), newGameCharacter);
                        if (packetAddBot.getId() == Integer.MAX_VALUE) {
                            // if new character is the player
                            clientWorld.setMyBot(newGameCharacter);

                            sendBotInformation(clientWorld.getNewBot().getBoundingBox().getX(),
                                    clientWorld.getNewBot().getBoundingBox().getY(), clientWorld.getNewBot().getPlayerDirection(),
                                    clientWorld.getNewBot().getTagged());
                        }

                    } else if (object instanceof PacketUpdateBotInformation) {
                        PacketUpdateBotInformation packetUpdateBotInformation = (PacketUpdateBotInformation) object;
                        if (clientWorld.getWorldGameCharactersMap().containsKey(packetUpdateBotInformation.getId())) {
                            // update character values
                            clientWorld.movePlayerGameCharacter(packetUpdateBotInformation.getId(),
                                    packetUpdateBotInformation.getX(), packetUpdateBotInformation.getY(),
                                    packetUpdateBotInformation.getDirection(), packetUpdateBotInformation.getTagged());
                            clientWorld.getGameCharacter(packetUpdateBotInformation.getId()).setTagged(packetUpdateBotInformation.getTagged());
                            clientWorld.getGameCharacter(packetUpdateBotInformation.getId()).setName(packetUpdateBotInformation.getPlayerName());
                        }
                    } else if (object instanceof PacketUpdateCharacterInformation) {
                        PacketUpdateCharacterInformation packetUpdateCharacterInformation = (PacketUpdateCharacterInformation) object;
                        if (clientWorld.getWorldGameCharactersMap().containsKey(packetUpdateCharacterInformation.getId())) {
                            // update character values
                            boolean tagged = clientWorld.getGameCharacter(packetUpdateCharacterInformation.getId()).getTagged();

                            // score system
                            if (tagged && !packetUpdateCharacterInformation.getTagged()) {
                                clientWorld.getGameCharacter(packetUpdateCharacterInformation.getId()).setScore(0);
                            }

                            clientWorld.movePlayerGameCharacter(packetUpdateCharacterInformation.getId(),
                                    packetUpdateCharacterInformation.getX(), packetUpdateCharacterInformation.getY(),
                                    packetUpdateCharacterInformation.getDirection(), packetUpdateCharacterInformation.getTagged());


                        }
                    } else if (object instanceof PacketServerIsFull) {
                        gameClient.showFull();
                    } else if (object instanceof PacketClientDisconnect) {
                        PacketClientDisconnect packetClientDisconnect = (PacketClientDisconnect) object;
                        System.out.println("Player ID: " + packetClientDisconnect.getId() + " is disconnected");
                        clientWorld.removeCharacter(packetClientDisconnect.getId());
                        if (clientWorld.getWorldGameCharactersMap().size() == 1 || (clientWorld.getWorldGameCharactersMap().size() == 2 && clientWorld.getWorldGameCharactersMap().containsKey(Integer.MAX_VALUE)) ||
                                (clientWorld.getWorldGameCharactersMap().size() == 3 && clientWorld.getWorldGameCharactersMap().containsKey(Integer.MAX_VALUE) && clientWorld.getWorldGameCharactersMap().containsValue(null))) {
                            if (gameScreen != null) {
                                //JOptionPane.showMessageDialog(null, "Not enough players.");
                                gameClient.createEndScreen(false);
                            }
                        }
                    } else if (object instanceof PacketScoreId) {
                        PacketScoreId packet = (PacketScoreId) object;
                        PlayerGameCharacter player = clientWorld.getWorldGameCharactersMap().get(packet.getId());
                        //PlayerGameCharacter player = clientWorld.getMyPlayerGameCharacter();
                        player.setScore(packet.getScore());
                    } else if (object instanceof PacketSendScoreMap) {
                        PacketSendScoreMap packet = (PacketSendScoreMap) object;
                        Map<String, Float> scores = packet.getMap();
                        endScreen.setScores(scores);
                    }
                } else if (object instanceof PacketUpdateLobbyPeopleNumber) {
                    PacketUpdateLobbyPeopleNumber packet = (PacketUpdateLobbyPeopleNumber) object;
                    lobbyScreen.setLobby1People(packet.getLobby1peopleNumber());
                    lobbyScreen.setGameStarted(packet.isGameStarted());
                    lobbyScreen.createScreenButtons();
                } else if (object instanceof PacketUpdateLobbyPeopleList) {
                    PacketUpdateLobbyPeopleList packetUpdateLobbyPeopleList = (PacketUpdateLobbyPeopleList) object;
                    if (lobbyScreen != null) {
                        lobbyScreen.setPlayerNamesInServer(packetUpdateLobbyPeopleList.getConnectedPlayers());
                    }
                    if (oneLobbyScreen != null) {
                        oneLobbyScreen.refreshLobby1(packetUpdateLobbyPeopleList.getConnectedPlayers());
                        System.out.println("creating buttons...");
                    }
                } else if (object instanceof PacketDisconnectFromLobby) {
                    if (oneLobbyScreen != null) {
                        PacketDisconnectFromLobby packetDisconnectFromLobby = (PacketDisconnectFromLobby) object;
                        oneLobbyScreen.refreshLobby1(packetDisconnectFromLobby.getConnectedPlayers());
                    }
                }
                else if (object instanceof PacketStartMatch) {
                    if (oneLobbyScreen != null) {
                        oneLobbyScreen.startGamePls();
                    } if (lobbyScreen != null) {
                        System.out.println("SET TRUE");
                        lobbyScreen.setGameStarted(true);
                        lobbyScreen.createScreenButtons();
                    }
                } else if (object instanceof PacketStartSpectating) {
                    PacketStartSpectating packet = (PacketStartSpectating) object;
                    for (Integer playerID : packet.getWorldGameCharactersMap().keySet()) {
                        clientWorld.addGameCharacter(playerID, packet.getWorldGameCharactersMap().get(playerID));
                    }
                } else if (object instanceof PacketResetLobby) {
                    if (lobbyScreen != null) {
                        lobbyScreen.setGameStarted(false);
                        lobbyScreen.setLobby1People(0);
                        lobbyScreen.createScreenButtons();
                    }
                }
            }
        });

        try {
            // connected to server - wait 5000ms before failing
            client.connect(5000, ip, tcpPort, udpPort);
            connected = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not connect to server");
            return;
        }
    }
    public void updateLobbyNumbers() {
        PacketUpdateLobbyPeopleNumber packet = new PacketUpdateLobbyPeopleNumber();
        client.sendTCP(packet);
    }

    /**
     * Send packets to server.
     * Sent when client wants to connect to the server.
     * @param playerName name of the player
     */
    public void sendPacketConnect(String playerName, boolean forSpectate) {
        PacketConnect packetConnect = PacketCreator.createPacketConnect(playerName);
        packetConnect.setForSpectate(forSpectate);
        client.sendTCP(packetConnect);
    }

    public void sendPacketDisconnect(int id) {
        PacketClientDisconnect packet = PacketCreator.createPacketClientDisconnect(id);
        client.sendTCP(packet);
    }
    public void sendPacketConnectLobby(Integer id, String playerName) {
        PacketConnectToLobby packet = new PacketConnectToLobby();
        packet.setPlayerName(playerName);
        client.sendTCP(packet);
    }
    public void sendPacketDisconnectFromLobby(String playerName) {
        PacketDisconnectFromLobby packet = new PacketDisconnectFromLobby();
        packet.setPlayerName(playerName);
        client.sendTCP(packet);
    }
    public void sendPacketRequestLobbyPeople(boolean forLobby) {
        PacketUpdateLobbyPeopleList packet = new PacketUpdateLobbyPeopleList();
        client.sendTCP(packet);
    }
    /**
     * Send player information to server.
     * @param x x coordinate of the player
     * @param y y coordinate of the player
     * @param direction direction of the player
     * @param tagged if true then player is tagged
     */
    public void sendPlayerInformation(float x, float y, String direction, boolean tagged) {
        PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(playerName,
                client.getID(), x, y, direction, tagged);
        client.sendUDP(packet);
    }
    
    public void sendBotInformation(float x, float y, String direction, boolean tagged) {
    PacketUpdateBotInformation packetBot = PacketCreator.createPacketUpdateBotInformation("bot1",
            Integer.MAX_VALUE, x, y, direction, tagged);
    client.sendUDP(packetBot);
    }
    public void requestStartGame() {
        PacketStartMatch packet = new PacketStartMatch();
        client.sendTCP(packet);
    }
    public void sendScore(String name, float score) {
        PacketSendScore packet = new PacketSendScore();
        packet.setName(name);
        packet.setScore(score);
        client.sendTCP(packet);
    }

    public void askScore() {
        PacketSendScoreMap packet = new PacketSendScoreMap();
        client.sendTCP(packet);
    }
    public void setEndScreen(EndScreen endScreen) {
        this.endScreen = endScreen;
    }

    public void setOneLobbyScreen(OneLobbyScreen oneLobbyScreen) {
        this.oneLobbyScreen = oneLobbyScreen;
    }
    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void setClientWorld(ClientWorld clientWorld) {
        this.clientWorld = clientWorld;
    }

    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
    }
    public void setLobbyScreen(LobbyScreen lobbyScreen) {
        this.lobbyScreen = lobbyScreen;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public GameScreen getGameScreen() {
        return this.gameScreen;
    }

    public ClientWorld getClientWorld() {
        return this.clientWorld;
    }

    public boolean isConnected() {
        return connected;
    }

    public static void main(String[] args) {
        new NewClientConnection(); // runs main application
    }
}
