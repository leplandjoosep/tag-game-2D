package newmain.packets;

public class PacketUpdateBotInformation extends Packet {
    private String playerName;
    private int id;
    private float x;
    private float y;
    private String direction;
    private boolean tagged;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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

    public void setDirection(String direction) {
        this.direction = direction;
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

    public String getDirection() {
        return this.direction;
    }

    public boolean getTagged() {
        return this.tagged;
    }
}
