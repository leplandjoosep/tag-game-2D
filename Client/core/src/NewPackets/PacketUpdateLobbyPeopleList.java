package NewPackets;

import java.util.List;

public class PacketUpdateLobbyPeopleList extends Packet {
    private boolean forLobby;
    private List<String> connectedPlayers;

    public List<String> getConnectedPlayers() {
        return connectedPlayers;
    }

    public void setConnectedPlayers(List<String> connectedPlayers) {
        this.connectedPlayers = connectedPlayers;
    }

    public boolean isForLobby() {
        return forLobby;
    }

    public void setForLobby(boolean forLobby) {
        this.forLobby = forLobby;
    }
}
