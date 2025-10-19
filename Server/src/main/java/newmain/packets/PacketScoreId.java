package newmain.packets;

public class PacketScoreId extends Packet{
    private int id;
    private float score;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
