package NewPackets;

import java.util.Map;

/**
 * Class that creates packets to keep code readable in ClientConnection and ServerConnection.
 */
public class PacketCreator {

    /**
     * Create PacketUpdateCharacterInformation.
     * @param name Name of the character
     * @param id ID of the character
     * @param x X coordinate of the character
     * @param y Y coordinate of the character
     * @param direction Direction of the character
     * @param tagged If the character is tagged
     * @return PacketUpdateCharacterInformation
     */
    public static PacketUpdateCharacterInformation createPacketUpdateCharacterInformation(String name, int id, float x,
                                                                                          float y, String direction,
                                                                                          boolean tagged) {
        PacketUpdateCharacterInformation packetUpdateCharacterInformation = new PacketUpdateCharacterInformation();
        packetUpdateCharacterInformation.setPlayerName(name);
        packetUpdateCharacterInformation.setId(id);
        packetUpdateCharacterInformation.setX(x);
        packetUpdateCharacterInformation.setY(y);
        packetUpdateCharacterInformation.setDirection(direction);
        packetUpdateCharacterInformation.setTagged(tagged);
        return packetUpdateCharacterInformation;
    }
    public static PacketUpdateBotInformation createPacketUpdateBotInformation(String name, int id, float x,
                                                                            float y, String direction,
                                                                            boolean tagged) {
        PacketUpdateBotInformation packetUpdateBotInformation = new PacketUpdateBotInformation();
        packetUpdateBotInformation.setPlayerName(name);
        packetUpdateBotInformation.setId(id);
        packetUpdateBotInformation.setX(x);
        packetUpdateBotInformation.setY(y);
        packetUpdateBotInformation.setDirection(direction);
        packetUpdateBotInformation.setTagged(tagged);
        return packetUpdateBotInformation;
    }


    /**
     * Create PacketAddCharacter.
     * @param name Name of the character
     * @param id ID of the character
     * @param x X coordinate of the character
     * @param y Y coordinate of the character
     * @return PacketAddCharacter
     */
    public static PacketAddCharacter createPacketAddCharacter(String name, int id, float x, float y) {
        PacketAddCharacter packetAddCharacter = new PacketAddCharacter();
        packetAddCharacter.setPlayerName(name);
        packetAddCharacter.setId(id);
        packetAddCharacter.setX(x);
        packetAddCharacter.setY(y);
        return packetAddCharacter;
    }
    public static PacketAddBot createPacketAddBot(String name, int id, float x, float y, boolean tagged) {
        PacketAddBot packetAddCharacter = new PacketAddBot();
        packetAddCharacter.setPlayerName(name);
        packetAddCharacter.setId(id);
        packetAddCharacter.setX(x);
        packetAddCharacter.setY(y);
        packetAddCharacter.setTagged(tagged);
        return packetAddCharacter;
    }


    /**
     * Create PacketConnect
     * @param name Name of the player
     * @return PacketConnect
     */
    public static PacketConnect createPacketConnect(String name) {
        PacketConnect packetConnect = new PacketConnect();
        packetConnect.setPlayerName(name);
        return packetConnect;
    }

    /**
     * Create PacketDisconnect
     * @param id ID of the player
     * @return PacketDisconnect
     */
    public static PacketClientDisconnect createPacketClientDisconnect(int id) {
        PacketClientDisconnect packetClientDisconnect = new PacketClientDisconnect();
        packetClientDisconnect.setId(id);
        return packetClientDisconnect;
    }
    public static PacketSendScore createSendScore(String name, int score) {
        PacketSendScore packetSendScore = new PacketSendScore();
        packetSendScore.setScore(score);
        packetSendScore.setName(name);
        return packetSendScore;
    }

    public static PacketSendScoreMap createPacketSendScoreMap(Map map) {
        PacketSendScoreMap packetSendScoreMap = new PacketSendScoreMap();
        packetSendScoreMap.setMap(map);
        return packetSendScoreMap;
    }
}
