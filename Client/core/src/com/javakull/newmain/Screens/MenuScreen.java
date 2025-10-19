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

import java.io.IOException;

public class MenuScreen extends ApplicationAdapter implements Screen {

    private String playerName;

    GameClient gameClient;
    private Stage stage;
    private boolean create = false;
    private Skin skin;
    private TextButton startButton;
    private TextButton quitButton;
    private SpriteBatch spriteBatch;
    private Sprite sprite;
    private TextField textField;
    private Sound sound;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public MenuScreen(GameClient game) {
        this.gameClient = game;
    }

    @Override
    public void create() {
        StretchViewport stretchViewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Set background
        spriteBatch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("background_final.png")));
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Setup stage and first screen.
        this.stage = new Stage(stretchViewport);

        Table table = new Table();

        createScreenSetup(table);

        // Dialog for event that player hasn't given name.
        Dialog dialog = new Dialog("Invalid username!", skin);
//        dialog.getTitleLabel().setColor(skin.getColor("red"));

        createStart(table, dialog);
        createQuit(table);
        this.create = true;
    }

    public void createScreenSetup(Table table) {
        table.setWidth(stage.getWidth());
        table.setHeight(stage.getHeight());

        // Skins
        this.skin = new Skin(Gdx.files.internal("start/startnupp.json"));

        // Setup input processor, so that stage can handle inputs.
        InputMultiplexer im = new InputMultiplexer(stage);
        Gdx.input.setInputProcessor(im);
    }
    public void createStart(Table table, Dialog dialog) {
        // Create start button with json defined style.
        startButton = new TextButton("Start", skin, "default");

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                    if (gameClient.connectToServer()) {
                        gameClient.createLobby();
                        dispose();
                    }
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
        table.add(startButton).width(100).height(70).padBottom(20);
    }
    public void createQuit(Table table) {
        table.row();
        // Create quit button with json defined style.
        quitButton = new TextButton("Quit", skin, "default");
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
        table.add(quitButton).width(100).height(70);
        stage.addActor(table);
    }

    public Stage getStage() {
        return stage;
    }

    public void showFull(Stage stage) {
        Dialog dialog = new Dialog("Server is full, please wait for a while!", skin);
        dialog.getTitleLabel().setColor(skin.getColor("red"));
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
