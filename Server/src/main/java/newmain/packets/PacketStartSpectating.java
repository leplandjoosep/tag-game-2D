package newmain.packets;

import newmain.game.characters.PlayerGameCharacter;

import java.util.Map;

public class PacketStartSpectating extends Packet {
    private Map<Integer, PlayerGameCharacter> worldGameCharactersMap;

    public Map<Integer, PlayerGameCharacter> getWorldGameCharactersMap() {
        return worldGameCharactersMap;
    }

    public void setWorldGameCharactersMap(Map<Integer, PlayerGameCharacter> worldGameCharactersMap) {
        this.worldGameCharactersMap = worldGameCharactersMap;
    }
}
