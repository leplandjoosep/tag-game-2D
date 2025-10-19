package newmain.game.server;

import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import newmain.game.characters.GameCharacter;
import newmain.game.characters.NewBot;
import newmain.game.characters.PlayerGameCharacter;
import newmain.game.world.Headless;
import newmain.game.world.Lobbies;
import newmain.game.world.World;
import newmain.packets.*;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerConnection {

    static com.esotericsoftware.kryonet.Server server;
    static final int udpPort = 8090, tcpPort = 8080;
    private static World serverWorld;
    private Lobbies lobbies;
    private ServerUpdateThread serverUpdateThread;

    private float playerGameCharacterX = 900f;
    private float playerGameCharacterY = 500f;
    private int playerCount = 0;
    private static final float INCREASE_X_COORDINATE = 50f;
    private float botX = 935f;
    private float botY = 80f;
    private float kullX = 950f;
    private float kullY = 1100f;


    /**
     * Server connection.
     */
    public ServerConnection()  {
        try {
            server = new Server(49152, 49152);
            server.start();
            server.bind(tcpPort, udpPort);

            // Starts the game (create a new World instance for the game).
            this.serverWorld = new World();
            this.lobbies = new Lobbies();
            Headless.loadHeadless(serverWorld);

        } catch (IOException exception) {
            JOptionPane.showMessageDialog(null, "Can not start the Server.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Register all packets that are sent over the network.
        server.getKryo().register(Packet.class);
        server.getKryo().register(PacketConnect.class);
        server.getKryo().register(PacketAddCharacter.class);
        server.getKryo().register(GameCharacter.class);
        server.getKryo().register(PacketUpdateCharacterInformation.class);
        server.getKryo().register(PacketCreator.class);
        server.getKryo().register(ArrayList.class);
        server.getKryo().register(Rectangle.class);
        server.getKryo().register(HashMap.class);
        server.getKryo().register(PacketClientDisconnect.class);
        server.getKryo().register(PacketServerIsFull.class);
        server.getKryo().register(PacketAddBot.class);
        server.getKryo().register(PacketUpdateBotInformation.class);
        server.getKryo().register(PacketUpdateLobbyPeopleNumber.class);
        server.getKryo().register(PacketConnectToLobby.class);
        server.getKryo().register(PacketUpdateLobbyPeopleList.class);
        server.getKryo().register(PacketStartMatch.class);
        server.getKryo().register(PacketStartSpectating.class);
        server.getKryo().register(PacketDisconnectFromLobby.class);
        server.getKryo().register(PacketResetLobby.class);
        server.getKryo().register(PacketScoreId.class);
        server.getKryo().register(PacketSendScore.class);
        server.getKryo().register(PacketSendScoreMap.class);

        // Add listener to handle receiving objects.
        server.addListener(new Listener() {

            // Receive packets from clients.
            public void received(Connection connection, Object object){
                if (object instanceof PacketConnect && serverWorld.getClients().size() < 8) {
                    PacketConnect packetConnect = (PacketConnect) object;
                    if (!packetConnect.isForSpectate()) {
                        playerCount += 1;
                        if (playerCount == 1) {
                            PlayerGameCharacter newPlayerGameCharacter = PlayerGameCharacter
                                    .createPlayerGameCharacter(kullX, kullY,
                                            packetConnect.getPlayerName(), serverWorld, connection.getID(), true);
                            serverWorld.setTaggedId(connection.getID());
                            System.out.println("first is kull 111111111111111111111111111111111111");
                            addCharacterToClientsGame(connection, newPlayerGameCharacter, false);
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
//                            NewBot bot = NewBot
//                                    .createBot(botX, botY, "BOT", serverWorld, Integer.MAX_VALUE, false);
//                            addBotToClientsGame(Integer.MAX_VALUE, bot);
//                            System.out.println("Bot created!!!!!!!!!");

                        } else {
                            boolean kull_in_game = false;
                            boolean bot_in_game = false;
                            PlayerGameCharacter newPlayerGameCharacter;
                            for (PlayerGameCharacter character : serverWorld.getClients().values()) {
                                if (character.getTagged()) {
                                    kull_in_game = true;
                                    break;
                                }
                                if (character.getPlayerGameCharacterId() == Integer.MAX_VALUE) {
                                    bot_in_game = true;
                                }
                            }
                            if (!bot_in_game) {
                                NewBot bot = NewBot
                                        .createBot(botX, botY, "bot1", serverWorld, Integer.MAX_VALUE, false);
                                addBotToClientsGame(Integer.MAX_VALUE, bot);
                            }
                            if (kull_in_game) {
                                // Creates new PlayerGameCharacter instance for the connection.
                                newPlayerGameCharacter = PlayerGameCharacter
                                        .createPlayerGameCharacter(playerGameCharacterX, playerGameCharacterY,
                                                packetConnect.getPlayerName(), serverWorld, connection.getID(), false);
                            } else {
                                newPlayerGameCharacter = PlayerGameCharacter
                                        .createPlayerGameCharacter(kullX + 50f, kullY,
                                                packetConnect.getPlayerName(), serverWorld, connection.getID(), true);
                                System.out.println("second is kull 2222222222222222222222222222222222222222222");
                            }

                            // Add new PlayerGameCharacter instance to all connections.
                            addCharacterToClientsGame(connection, newPlayerGameCharacter, false);
                        }
                        if (playerCount <= 4) {
                            playerGameCharacterX += INCREASE_X_COORDINATE;
                        } else {
                            playerGameCharacterX = 700f;
                            playerGameCharacterY = 40f;
                            playerCount = 0;
                        }
                    } else {
                        addCharacterToClientsGame(connection, null, true);
                    }

                } else if (object instanceof PacketConnect && serverWorld.getClients().size() == 5) {
                    // If server is full then send a packet notifying that player cant join game.
                    server.sendToTCP(connection.getID(), new PacketServerIsFull());

                } else if (object instanceof PacketSendScore) {
                    // put scores to map
                    PacketSendScore packet = (PacketSendScore) object;
                    lobbies.putScores(packet.getName(), packet.getScore());
                } else if (object instanceof PacketSendScoreMap) {
                    // put scores to map
                    PacketSendScoreMap packet = (PacketSendScoreMap) object;
                    sendScoresMap();
                } else if (object instanceof PacketUpdateCharacterInformation) {
                    System.out.println(serverWorld.getClients());
                    PacketUpdateCharacterInformation packet = (PacketUpdateCharacterInformation) object;
                    // Update PlayerGameCharacter's coordinates and direction.
                    // Send PlayerGameCharacter's new coordinate and direction to all connections.
                    try {
                        if (serverWorld.getTaggedId() == packet.getId() && !packet.getTagged()) {
                            sendUpdatedGameCharacter(connection.getID(), packet.getX(), packet.getY(), packet.getDirection(),
                                    true, packet.getPlayerName());
                        } else {
                            sendUpdatedGameCharacter(connection.getID(), packet.getX(), packet.getY(), packet.getDirection(),
                                    packet.getTagged(), packet.getPlayerName());
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (object instanceof PacketConnectToLobby) {
                    PacketConnectToLobby packet = (PacketConnectToLobby) object;
                    addPlayerToLobby(packet.getPlayerName());
                    sendUpdatedLobbies();
                }
                else if (object instanceof PacketUpdateLobbyPeopleNumber) {
                    sendUpdatedLobbies();
                }
                else if (object instanceof PacketUpdateLobbyPeopleList) {
                    updateLobbyPeopleList();
                }

                else if (object instanceof PacketUpdateBotInformation) {

                    PacketUpdateBotInformation packet = (PacketUpdateBotInformation) object;
                    // Update PlayerGameCharacter's coordinates and direction.
                    // Send PlayerGameCharacter's new coordinate and direction to all connections.
                    try {
                        sendUpdatedBot(packet.getId(), packet.getX(), packet.getY(), packet.getDirection(),
                                packet.getTagged(), packet.getPlayerName());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if (object instanceof PacketDisconnectFromLobby) {
                    PacketDisconnectFromLobby packet = (PacketDisconnectFromLobby) object;
                    System.out.println(packet.getPlayerName());
                    removePlayerFromLobby(packet.getPlayerName());
                }
                else if (object instanceof PacketStartMatch) {
                    lobbies.clearScores();
                    lobbies.clearConnectedPlayers();
                    PacketStartMatch packet = new PacketStartMatch();
                    lobbies.setGameStarted(true);
                    // lobby game full. start spectate
                    server.sendToAllTCP(packet);
                }
                else if (object instanceof PacketClientDisconnect) {
                    disconnected(connection);
                }
            }

            // Client disconnects from the Server.
            public void disconnected (Connection c) {
                PacketClientDisconnect packetClientDisconnect = PacketCreator.createPacketClientDisconnect(c.getID());
                // Remove client from the game.
                serverWorld.removeClient(c.getID());
                // Send to other connections that client has disconnected from the game.
                server.sendToAllExceptTCP(c.getID(), packetClientDisconnect);
                c.close();
                if (c.getID() == serverWorld.getTaggedId()) {
                    for (Integer i : serverWorld.getClients().keySet()) {
                        if (i < Integer.MAX_VALUE) {
                            serverWorld.setTaggedId(i);
                            botHitPlayerDecreaseScore(i, 40);
                            break;
                        }
                    }
                }
                if (serverWorld.getClients().size() == 1 || (serverWorld.getClients().size() == 2 && serverWorld.getClients().containsKey(Integer.MAX_VALUE))) {
                    PacketClientDisconnect packetClientDisconnectBot = PacketCreator.createPacketClientDisconnect(Integer.MAX_VALUE);
                    serverWorld.removeClient(Integer.MAX_VALUE);
                    server.sendToAllExceptTCP(Integer.MAX_VALUE, packetClientDisconnectBot);
                    lobbies.setGameStarted(false);
                    lobbies.clearConnectedPlayers();
                    PacketResetLobby packet = new PacketResetLobby();
                    server.sendToAllTCP(packet);
                }
            }
        });

        System.out.println("Server is on!");
    }

    /**
     * Adds a new PlayerGameCharacter instance to all connections.
     * @param newCharacterConnection The connection of the new PlayerGameCharacter.
     * @param newPlayerGameCharacter The new PlayerGameCharacter instance.
     */
    public void addCharacterToClientsGame(Connection newCharacterConnection, PlayerGameCharacter newPlayerGameCharacter, boolean forSpectate) {
        // existing PlayerGameCharacter instance to new connection
        List<PlayerGameCharacter> clientsValues = new ArrayList<>(serverWorld.getClients().values());
        for (int i = 0; i < clientsValues.size(); i++) {
            PlayerGameCharacter character = clientsValues.get(i);
            // new packet for sending player info
            if (character != null) {
                PacketAddCharacter addCharacter = PacketCreator.createPacketAddCharacter(character.getName(),
                        character.getPlayerGameCharacterId(), character.getBoundingBox().getX(),
                        character.getBoundingBox().getY(), character.getTagged());
                // send packet to new connection
                server.sendToTCP(newCharacterConnection.getID(), addCharacter);
            }
        }

        // add new PlayerGameCharacter instance to server world
        serverWorld.addGameCharacter(newCharacterConnection.getID(), newPlayerGameCharacter);

        // add new player to all connections
        // create a packet to send new player info
        if (!forSpectate) {
            PacketAddCharacter addCharacter = PacketCreator.createPacketAddCharacter(newPlayerGameCharacter.getName(),
                    newCharacterConnection.getID(), newPlayerGameCharacter.getBoundingBox().getX(),
                    newPlayerGameCharacter.getBoundingBox().getY(), newPlayerGameCharacter.getTagged());
            server.sendToAllTCP(addCharacter); // send packet to all connections
        }
    }

    public void sendScoresMap() {
        PacketSendScoreMap packet = PacketCreator.createPacketSendScoreMap(lobbies.getScores());
        server.sendToAllTCP(packet);
    }
    public void addPlayerToLobby(String playerName) {
        lobbies.addOnePlayerToLobby();
        lobbies.addPlayer(playerName);
    }
    public void updateLobbyPeopleList() {
        PacketUpdateLobbyPeopleList packet = new PacketUpdateLobbyPeopleList();
        packet.setConnectedPlayers(lobbies.getConnectedPlayers());
        server.sendToAllTCP(packet);
    }
    public void sendUpdatedLobbies() {
        PacketUpdateLobbyPeopleNumber packet = new PacketUpdateLobbyPeopleNumber();
        packet.setLobby1peopleNumber(lobbies.getLobby1ConnectedNumber());
        packet.setGameStarted(lobbies.isGameStarted());
        server.sendToAllTCP(packet);
    }
    public void removePlayerFromLobby(String playerName) {
        lobbies.removePlayerFromConnectedPlayers(playerName);
        sendUpdatedLobbies();
        PacketDisconnectFromLobby packet = new PacketDisconnectFromLobby();
        packet.setPlayerName(playerName);
        packet.setConnectedPlayers(lobbies.getConnectedPlayers());
        server.sendToAllTCP(packet);
    }
    /**
     * Adds a new PlayerGameCharacter instance to all connections.
     * @param id of the bot
     * @param newBot The new PlayerGameCharacter instance.
     */
    public void addBotToClientsGame(Integer id, NewBot newBot) {
        // existing BotGameCharacter instance to new connection
        List<PlayerGameCharacter> clientsValues = new ArrayList<>(serverWorld.getClients().values());
        for (int i = 0; i < clientsValues.size(); i++) {
            PlayerGameCharacter character = clientsValues.get(i);
            // new packet for sending player info
            PacketAddBot addCharacter = PacketCreator.createPacketAddBot(character.getName(),
                    character.getPlayerGameCharacterId(), (float)character.getBoundingBox().getX(),
                    (float)character.getBoundingBox().getY(), character.getTagged());
            // send packet to new connection
            server.sendToTCP(id, addCharacter);
        }

        // add new PlayerGameCharacter instance to server world
        serverWorld.addBotCharacter(id, newBot);
        // serverWorld.addBotCharacter()

        // add new player to all connections
        // create a packet to send new player info
        PacketAddBot addBot = PacketCreator.createPacketAddBot(newBot.getName(),
                id, (float)newBot.getBoundingBox().getX(),
                (float)newBot.getBoundingBox().getY(), newBot.getTagged());
        server.sendToAllTCP(addBot); // send packet to all connections
    }


    /**
     * Sends updated PlayerGameCharacter's coordinates and direction to all connections.
     * @param id The id of the PlayerGameCharacter.
     * @param x The x coordinate of the PlayerGameCharacter.
     * @param y The y coordinate of the PlayerGameCharacter.
     * @param direction The direction of the PlayerGameCharacter.
     */
    public static void sendUpdatedGameCharacter(int id, float x, float y, String direction, boolean tagged, String name) throws InterruptedException {
        serverWorld.movePlayerGameCharacter(id, x, y); // move player game character
        PlayerGameCharacter character = serverWorld.getGameCharacter(id);
        if (character != null) {
            character.setTagged(tagged);
            character.setName(name);
            // send updated player info
            PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(character.getName(),
                    id, character.getBoundingBox().getX(), character.getBoundingBox().getY(), direction, character.getTagged());
            server.sendToAllTCP(packet); // send packet to all connections
        }
    }

    public static void sendUpdatedGameCharacterDelay(int id, String direction, boolean tagged, String name) throws InterruptedException {
        PlayerGameCharacter character = serverWorld.getGameCharacter(id);
        character.setTagged(tagged);
        character.setName(name);
        // send updated player info
        PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(character.getName(),
                id, character.getBoundingBox().getX(), character.getBoundingBox().getY(), direction, character.getTagged());
        server.sendToAllTCP(packet); // send packet to all connections
        Thread.sleep(25);
    }

    public static void sendUpdatedBot(int id, float x, float y, String direction, boolean tagged, String name) throws InterruptedException {
        if (serverWorld.getClients().containsKey(id)) {
            NewBot character = (NewBot) serverWorld.getGameCharacter(id);
            List<Float> posXAndY = character.moveToNewPosBot();
            serverWorld.movePlayerGameCharacter(id, posXAndY.get(0), posXAndY.get(1)); // move player game character
            character.setTagged(tagged);
            character.setName(name);
            // send updated player info
            PacketUpdateBotInformation packet = PacketCreator.createPacketUpdateBotInformation(character.getName(),
                    id, character.getBoundingBox().getX(), character.getBoundingBox().getY(), character.getCharacterDirection(), character.getTagged());
            server.sendToAllTCP(packet); // send packet to all connections
        }
    }

    public static void botHitPlayerDecreaseScore(int id, float score) {
        PacketScoreId packet = PacketCreator.createPacketScoreId(id, score);
        server.sendToAllTCP(packet); // send packet to all connections
    }


    public void restartServer() {
        playerGameCharacterX = 280f;
    }

    public World getServerWorld() {
        return serverWorld;
    }

    public static void main(String[] args) {
        // runs main application
        new ServerConnection();
    }
}
