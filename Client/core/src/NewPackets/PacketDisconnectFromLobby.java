package NewPackets;

import java.util.List;

public class PacketDisconnectFromLobby extends Packet {
    private String playerName;
    private List<String> connectedPlayers;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public List<String> getConnectedPlayers() {
        return connectedPlayers;
    }

    public void setConnectedPlayers(List<String> connectedPlayers) {
        this.connectedPlayers = connectedPlayers;
    }
}
