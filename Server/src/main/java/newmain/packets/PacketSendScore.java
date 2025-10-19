package newmain.packets;

public class PacketSendScore extends Packet {
    private String name;
    private float score;

    public void setScore(float score) {
        this.score = score;
    }

    public float getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
