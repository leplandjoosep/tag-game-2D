package com.javakull.main;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.javakull.newmain.GameInfo.GameClient;


// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

	public static void createGame() {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("libgdxKull");
		config.setWindowedMode(1200, 800);
		GameClient gameClient = new GameClient();
		new Lwjgl3Application(gameClient, config);
	}

	public static void main (String[] arg) {
		createGame();
	}

}
