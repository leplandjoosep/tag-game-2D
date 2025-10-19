package NewPackets;

public class PacketAddCharacter extends Packet {

    private String playerName;
    private int id;
    private float x;
    private float y;
    private boolean tagged;

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public int getId() {
        return this.id;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public boolean getTagged() {
        return this.tagged;
    }
}
