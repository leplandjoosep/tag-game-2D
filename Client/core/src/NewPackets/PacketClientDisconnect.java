package NewPackets;

/**
 * Packet for disconnecting from the server.
 */
public class PacketClientDisconnect extends Packet {

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
