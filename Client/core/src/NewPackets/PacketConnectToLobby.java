package NewPackets;

public class PacketConnectToLobby extends Packet {
    private Integer lobbyId = 1;
    private String playerName;

    public Integer getLobbyId() {
        return lobbyId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
