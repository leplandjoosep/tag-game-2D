package newmain.packets;

/**
 * Packet for connecting to the server.
 */
public class PacketConnect extends Packet {

    private boolean forSpectate = false;
    private String playerName;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName(){
        return playerName;
    }

    public boolean isForSpectate() {
        return forSpectate;
    }

    public void setForSpectate(boolean forSpectate) {
        this.forSpectate = forSpectate;
    }
}
