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
import com.javakull.newmain.GameInfo.GameClient;
import java.util.concurrent.TimeUnit;
import java.util.List;

public class OneLobbyScreen extends ApplicationAdapter implements Screen {

    private String playerName;
    GameClient gameClient;
    private NewClientConnection newClientConnection;
    private Stage stage;
    private boolean create = false;
    private Skin startSkin = new Skin(Gdx.files.internal("start/startnupp.json"));
    private Skin textSkin = new Skin(Gdx.files.internal("text/textt.json"));
    private Skin labelSkin = new Skin(Gdx.files.internal("labels/label.json"));
    private boolean startGame = false;
    private boolean startButtonExtists = false;
    private TextButton startButton;
    private TextButton quitButton;
    private TextButton LobbyButton1;
    private TextButton LobbyText1;
    private static Integer lobby1People = 0;
    private List<String> connectedPlayers = null;
    private SpriteBatch spriteBatch;
    private Sprite sprite;
    private TextField textField;
    private Sound sound;
    private Table table = new Table();

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public OneLobbyScreen(GameClient game, NewClientConnection newClientConnection) {
        this.gameClient = game;
        this.newClientConnection = newClientConnection;
        newClientConnection.setOneLobbyScreen(this);
        newClientConnection.sendPacketRequestLobbyPeople(false);

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
        createScreenSetup(table);

        // Dialog for event that player hasn't given name.
        Dialog dialog = new Dialog("Invalid username!", labelSkin);
//        dialog.getTitleLabel().setColor(skin.getColor("red"));
        //createScreenButtons();
        this.create = true;

        Gdx.app.addLifecycleListener(new LifecycleListener() {
            @Override
            public void pause() {

            }

            @Override
            public void resume() {

            }

            @Override
            public void dispose() {
                newClientConnection.sendPacketDisconnectFromLobby(gameClient.getPlayerName());
                Gdx.app.exit();
            }
        });

    }

    public void createScreenSetup(Table table) {
        table.setWidth(stage.getWidth());
        table.setHeight(stage.getHeight());

        // Setup input processor, so that stage can handle inputs.
        InputMultiplexer im = new InputMultiplexer(stage);
        Gdx.input.setInputProcessor(im);
    }
    public void createScreenButtons() {
        table.clear();
        if (connectedPlayers.size() >= 2) {
            createStart();
        }
        else {
            Label textNotEnoughPlayers = new Label(" Not Enough Players To Start Game ", labelSkin);
            table.add(textNotEnoughPlayers);
        }
        table.row();
        createPlayerButtons(table);
        table.row();
        createQuit(table);
    }

    public void createPlayerButtons(Table table) {
        Table connectedPlayersButtons = new Table();
        if (connectedPlayers != null) {
            for (String player : connectedPlayers) {
                Label playerName = new Label(player, labelSkin);
                connectedPlayersButtons.add(playerName).padBottom(20);
            }
        }
        table.add(connectedPlayersButtons);
    }
    public void refreshLobby1(java.util.List<String> connectedPlayers) {
        this.connectedPlayers = connectedPlayers;
        createScreenButtons();
    }

    public void createStart() {
        if (connectedPlayers != null && connectedPlayers.get(0).equals(gameClient.getPlayerName())) {
            // Create start button with json defined style.
            startButton = new TextButton("Start", startSkin, "default");

            startButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
//                ClientWorld clientWorld = new ClientWorld();
//                GameScreen gameScreen = new GameScreen(clientWorld);
//                setPlayerName("Yeet");
                    newClientConnection.requestStartGame();
                    //startGamePls();
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
            startButtonExtists = true;
        } else {
            Label label = new Label("First Participant Can Start The Game!", labelSkin);
            table.add(label);
        }
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
                newClientConnection.sendPacketDisconnectFromLobby(gameClient.getPlayerName());
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
    public Stage getStage() {
        return stage;
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
    public void startGamePls() {
        System.out.println("START GAME");
        System.out.println(gameClient);
        startGame = true;
        //this.gameClient.startGame();
        //dispose();
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
        if (startGame) {
            gameClient.startGame();
        }
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
