package com.javakull.newmain.Screens;

import NewClientConnection.NewClientConnection;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
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
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class EndScreen extends ApplicationAdapter implements Screen {
    private String playerName;

    GameClient gameClient;
    private Stage stage;
    private boolean create = false;
    private Skin startSkin;
    private Skin labelSkin;
    private SpriteBatch spriteBatch;
    private Sprite sprite;
    private  NewClientConnection clientConnection;
    private Sound sound;
    private Map<String, Float> scores;
    private Table table = new Table();
    private TextButton requestScores;
    private TextButton quitButton;
    private boolean gameEndedTimeOut;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public EndScreen(GameClient game, boolean gameEndedTimeOut) {
        this.gameClient = game;
        this.gameEndedTimeOut = gameEndedTimeOut;
    }
    public void setClientConnection(NewClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        clientConnection.setEndScreen(this);
    }
    public void setScores(Map<String, Float> scores) {
        if (scores != null) {
            this.scores = scores;
        }
    }

    @Override
    public void create() {
        StretchViewport stretchViewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Set background
        spriteBatch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("endScreen.png")));
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.startSkin = new Skin(Gdx.files.internal("start/startnupp.json"));
        this.labelSkin = new Skin(Gdx.files.internal("labels/label.json"));

        // Setup stage and first screen.
        this.stage = new Stage(stretchViewport);
        stage.addActor(table);

        createScreenSetup(table);
        createStartingScreenContent();

        this.create = true;
    }

    public void createScreenSetup(Table table) {
        table.setWidth(stage.getWidth());
        table.setHeight(stage.getHeight());

        // Setup input processor, so that stage can handle inputs.
        InputMultiplexer im = new InputMultiplexer(stage);
        Gdx.input.setInputProcessor(im);
    }
    public void createStartingScreenContent() {
        if (gameEndedTimeOut) {
            Label labelText = new Label("Time Over!", labelSkin);
            table.add(labelText);
            table.row();
        } else if (!gameEndedTimeOut) {
            Label labelText = new Label("Not Enough Players To Continue Playing!", labelSkin);
            table.add(labelText);
            table.row();
        }
        createRequestScoresButton();

    }
    public void createRequestScoresButton() {
        System.out.println("CREATING REQUEST SCORES");
        requestScores = new TextButton("View Scores", startSkin);
        requestScores.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clientConnection.askScore();
                if (scores != null && !scores.isEmpty()) {
                    displayScores();
                }
            }
        });
        requestScores.addListener(new InputListener() {
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
        table.add(requestScores);
    }
    public void displayScores() {
        table.clear();
        Object[] a = scores.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Float>) o2).getValue().compareTo(((Map.Entry<String, Float>) o1).getValue());
            }
        });
        for (Object e : a) {
            String nameAndScore = ((Map.Entry<String, Float>) e).getKey() + " : " + ((Map.Entry<String, Float>) e).getValue();
            Label scoreLabel = new Label(nameAndScore, labelSkin);
            table.add(scoreLabel);
            table.row();
        }
        table.row();
        createQuit(table);
    }
    public Stage getStage() {
        return stage;
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