package NewPackets;

public class PacketUpdateLobbyPeopleNumber extends Packet {
    private boolean gameStarted = false;
    private Integer lobby1peopleNumber;

    public void setLobby1peopleNumber(Integer lobby1peopleNumber) {
        this.lobby1peopleNumber = lobby1peopleNumber;
    }

    public Integer getLobby1peopleNumber() {
        return lobby1peopleNumber;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }
}
