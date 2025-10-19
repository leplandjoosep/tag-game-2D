package newmain.game.server;

import newmain.game.world.World;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

import static java.lang.Thread.sleep;

public class ServerUpdateThread implements Runnable {

    private ServerConnection serverConnection;
    private World serverWorld;

    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public void setServerWorld(World serverWorld) {
        this.serverWorld = serverWorld;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (serverWorld.getClients().size() == 2) {
                    serverWorld.setNewGame(true);
                } else if (serverWorld.getClients().isEmpty() && serverWorld.isNewGame()) {
                    System.out.println("Thread restart");
                    serverConnection.restartServer();
                    serverWorld.restartWorld();
                }
                sleep(5);
            } catch (InterruptedException e) {
                System.out.println("Exception: " + Arrays.toString(e.getStackTrace()));
                System.out.println("Cause: " + e.getCause().toString());
            }
        }
    }
}
