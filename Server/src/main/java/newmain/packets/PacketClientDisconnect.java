package newmain.packets;

/**
 * Packet for disconnection.
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
