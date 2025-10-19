package com.javakull.newmain.Screens;

import NewClientConnection.NewClientConnection;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.javakull.newmain.GameInfo.ClientWorld;
import com.javakull.newmain.GameInfo.GameClient;
import org.w3c.dom.Text;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LobbyScreen extends ApplicationAdapter implements Screen {

    private String playerName;
    private TextField textField;
    private java.util.List<String> playerNamesInServer = new ArrayList<>();
    GameClient gameClient;
    private NewClientConnection newClientConnection;
    private Stage stage;
    private boolean create = false;
    private Skin startSkin = new Skin(Gdx.files.internal("start/startnupp.json"));
    private Skin textSkin = new Skin(Gdx.files.internal("text/textt.json"));
    private Skin labelSkin = new Skin(Gdx.files.internal("labels/label.json"));
    private TextButton startButton;
    private TextButton quitButton;
    private TextButton LobbyButton1;
    private boolean lobbyButtonExists = false;
    private TextButton spectateButton;
    private static Integer lobby1People = 0;
    private boolean gameStarted = false;
    private boolean spectateButtonExist = false;
    private SpriteBatch spriteBatch;
    private Sprite sprite;
    private Sound sound;
    private Dialog dialog = new Dialog("Invalid username!", labelSkin);
    private Table table = new Table();

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public LobbyScreen(GameClient game, NewClientConnection newClientConnection) {
        this.gameClient = game;
        this.newClientConnection = newClientConnection;
        newClientConnection.setLobbyScreen(this);
        newClientConnection.updateLobbyNumbers();
    }

    @Override
    public void create() {
        StretchViewport stretchViewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Set background
        spriteBatch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("lobbyScreen.png")));
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Setup stage and first screen.
        this.stage = new Stage(stretchViewport);
        stage.addActor(table);
        newClientConnection.sendPacketRequestLobbyPeople(true);
        createScreenSetup(table);

        // Dialog for event that player hasn't given name.
//        dialog.getTitleLabel().setColor(skin.getColor("red"));
        //createScreenButtons();
        this.create = true;
    }

    public void createScreenSetup(Table table) {
        table.setWidth(stage.getWidth());
        table.setHeight(stage.getHeight());

        // Skins

        // Setup input processor, so that stage can handle inputs.
        InputMultiplexer im = new InputMultiplexer(stage);
        Gdx.input.setInputProcessor(im);
    }
    public void createScreenButtons() {
        System.out.println("CREATE BUTTONS");
        table.clear();
        //createStart(table, dialog);
        if (!gameStarted) {
            createTextField(table);
            table.row();
            createLobby1(table, dialog);
            table.row();
            createQuit(table);
        } else {
            Label label = new Label("Game Has Already Started.", labelSkin);
            table.add(label);
            table.row();
            createSpectate(table);
            table.row();
            createQuit(table);
        }
    }
    public void createTextField(Table table) {
        textField = new TextField("Enter username", textSkin, "default");
        table.add(textField).width(220).height(50).pad(40);
    }
    public void createLobby1(Table table, Dialog dialog) {
        LobbyButton1 = new TextButton("Lobby 1:" + lobby1People, startSkin, "default");
        table.add(LobbyButton1).width(100).padBottom(20);
        LobbyButton1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playerName = textField.getText();
                if (playerName.equals("") || playerName.equals("Enter username") || playerName.length() <= 2 ||
                        playerNamesInServer.contains(playerName) || playerName.length() > 10 || playerName.trim().length() == 0) {
                    dialog.show(stage);
                    dialog.setPosition(stage.getWidth() / 2 - 115, stage.getHeight() / 2 + 115);
                    dialog.setSize(250, 50);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            dialog.hide();
                        }
                    }, 1);
                } else {
                    gameClient.setPlayerName(playerName);
                    newClientConnection.sendPacketConnectLobby(1, gameClient.getPlayerName());
                    gameClient.setScreen(new OneLobbyScreen(gameClient, newClientConnection));
                }
            }
        });
    }

    public void createStart(Table table, Dialog dialog) {
        // Create start button with json defined style.
        startButton = new TextButton("Start", startSkin, "default");

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                ClientWorld clientWorld = new ClientWorld();
//                GameScreen gameScreen = new GameScreen(clientWorld);
//                setPlayerName("Yeet");;
                gameClient.startGame();
                dispose();
//                try {
//                    gameClient.createClient(clientWorld, gameScreen);
//                    new NewClientConnection();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                gameClient.setScreen(gameScreen);

            }
        });
        startButton.addListener(new InputListener() {
            boolean playing = false;

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (!playing) {
                    Sound sound = Gdx.audio.newSound(Gdx.files.internal("buttonrollover.wav"));
                    sound.play(1F);
                    playing = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                playing = false;
            }

        });
        table.add(startButton).width(100).padBottom(20);
    }
    public void createQuit(Table table) {
        table.row();
        // Create quit button with json defined style.
        quitButton = new TextButton("Quit", startSkin, "default");
        quitButton.setWidth(300);
        quitButton.setHeight(80);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        quitButton.addListener(new InputListener() {
            boolean playing = false;

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (!playing) {
                    Sound sound = Gdx.audio.newSound(Gdx.files.internal("buttonrollover.wav"));
                    sound.play(1F);
                    playing = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                playing = false;
            }

        });
        table.add(quitButton).width(100);
    }
    public void createSpectate(Table table) {
        spectateButton = new TextButton("spectate", startSkin, "default");
        table.add(spectateButton).width(100).padBottom(20);
        spectateButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameClient.spectateGame();
                dispose();
            }
        });
    }
    public Stage getStage() {
        return stage;
    }

    public void setPlayerNamesInServer(List<String> playerNamesInServer) {
        this.playerNamesInServer = playerNamesInServer;
    }
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }
    public void setLobby1People(Integer lobby1) {
        this.lobby1People = lobby1;
    }

    public void showFull(Stage stage) {
        Dialog dialog = new Dialog("Server is full, please wait for a while!", startSkin);
        dialog.getTitleLabel().setColor(startSkin.getColor("red"));
        dialog.show(stage);
        dialog.setPosition(stage.getWidth() / 2 - 115, 240);
        dialog.setSize(250, 50);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                dialog.hide();
            }}, 1);
    }

    /**
     * Resize.
     *
     * @param width of the window
     * @param height of the window
     */
    @Override
    public void resize (int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }

    /**
     * This is here because of the implemented method, but it helps to display the window.
     */
    @Override
    public void show() {
    }

    /**
     * This method renders menu of the gameClient.
     *
     * @param view is not used but it is for view.
     */
    @Override
    public void render(float view) {
        if (!this.create) {
            create();
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (stage != null) {
            spriteBatch.begin();
            sprite.draw(spriteBatch);
            spriteBatch.end();
            stage.draw();
            stage.act();
        }
    }

    /**
     * Enable hiding window.
     */
    @Override
    public void hide() {

    }

}
