package newmain.packets;

import java.util.Map;

public class PacketSendScoreMap extends Packet {

    private Map<String, Float> map;

    public Map<String, Float> getMap() {
        return map;
    }

    public void setMap(Map<String, Float> map) {
        this.map = map;
    }
}
