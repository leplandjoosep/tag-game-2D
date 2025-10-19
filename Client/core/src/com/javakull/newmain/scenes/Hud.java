package com.javakull.newmain.scenes;

import NewClientConnection.NewClientConnection;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.javakull.newmain.Characters.PlayerGameCharacter;
import com.javakull.newmain.GameInfo.ClientWorld;
import com.javakull.newmain.GameInfo.GameClient;

public class Hud {

    private static final int V_WIDTH = 1920;
    private static final int V_HEIGHT = 1280;
    public Stage stage;
    private Viewport viewport;
//    private static Integer worldTimer = 300;
    private static Integer worldTimer;
    private static float timeCount = 0;
    private float score;
    private int showScore;
    private ClientWorld clientWorld;
    private PlayerGameCharacter character;

    Label countdownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label taggedLabel;
    Label scoreNameLabel;
    Label taggedNameLabel;
    private GameClient gameClient;
    private NewClientConnection connection;

    public Hud(SpriteBatch spriteBatch, ClientWorld clientWorld, GameClient gameClient, NewClientConnection connection) {
        this.clientWorld = clientWorld;
        this.gameClient = gameClient;
        this.connection = connection;
        character = clientWorld.getMyPlayerGameCharacter();
        if (character != null) {
            score = character.getScore();

            Integer worldTimer = ClientWorld.getWorldTime();
            Hud.worldTimer = worldTimer;



            viewport = new StretchViewport(V_WIDTH, V_HEIGHT, new OrthographicCamera());
            stage = new Stage(viewport, spriteBatch);

            Table table = new Table();
            table.top();
            table.setFillParent(true);

            BitmapFont font = new BitmapFont();
            font.getData().setScale(2); // Set the font scale to 2x
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

            String tagged = "False";
            if (character.getTagged()) {
                tagged = "True";
            }
            showScore = Math.round(score);

            countdownLabel = new Label(String.format("%03d", worldTimer), labelStyle);
            scoreLabel = new Label(String.format("%03d", showScore), labelStyle);
            timeLabel = new Label("TIME", labelStyle);
            taggedLabel = new Label(tagged, labelStyle);
            scoreNameLabel = new Label("SCORE", labelStyle);
            taggedNameLabel = new Label("TAGGED", labelStyle);

            table.add(taggedNameLabel).expandX().padTop(10);
            table.add(scoreNameLabel).expandX().padTop(10);
            table.add(timeLabel).expandX().padTop(10);
            table.row();
            table.add(taggedLabel).expandX();
            table.add(scoreLabel).expandX();
            table.add(countdownLabel).expandX();

            stage.addActor(table);
        }
    }

    public void update(float deltaTime) {
        if (worldTimer == 0) {
            connection.sendScore(character.getName(), character.getScore());
            gameClient.createEndScreen(true);
        }
        timeCount += deltaTime;
        if (timeCount >= 1) {
            if (!character.getTagged()) {
                character.setScore(1);
            }
            clientWorld.setWorldTimeOneLess();
            worldTimer = ClientWorld.getWorldTime();
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }
}
