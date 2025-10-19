package com.javakull.newmain.Screens;

import NewClientConnection.NewClientConnection;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.javakull.World.Map;
import com.javakull.newmain.Characters.PlayerGameCharacter;
import com.javakull.newmain.GameInfo.ClientWorld;
import com.javakull.newmain.GameInfo.GameClient;
import com.javakull.newmain.GameInfo.ScoreCalculation;
import com.javakull.newmain.scenes.Hud;

import java.util.ArrayList;
import java.util.List;


public class GameScreen implements Screen, InputProcessor {

    // screen
    private OrthographicCamera camera;
    private StretchViewport stretchViewport;
    boolean buttonHasBeenPressed;
    private Integer counter = 0;
    private BitmapFont font = new BitmapFont(Gdx.files.internal("my_font.fnt"));
    private BitmapFont nameFont;

    // Graphics and Texture
    private SpriteBatch batch;
    private Texture img;
    private com.javakull.World.Map map;

    private Rectangle compareRectangle;

    private MapObjects objects;
    private TextureAtlas textureAtlas;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    // world parameters
    private final float WORLD_WIDTH = 1920;
    private final float WORLD_HEIGHT = 1280;

    // client connection
    private NewClientConnection clientConnection;
    private ClientWorld clientWorld;
    private GameClient gameClient;
    private MenuScreen menuScreen;

    private String lastDirection = "right";
    private Hud hud;
    private boolean onGround = false;
    private boolean canJump = true;
    private Integer jumpCycle = 0;
    private float lastY = 0;
    private boolean reachedMax;
    private boolean forSpectate = false;
    private boolean cameraForSpectate = false;
    private boolean firstTime = true;
    private ScoreCalculation scoreCalculation;

    /**
     * Constructor for GameScreen.
     * @param clientWorld ClientWorld
     */
    public GameScreen(ClientWorld clientWorld) {

        this.clientWorld = clientWorld;

        this.map = new Map();
        objects = map.getObjects();

        compareRectangle = new Rectangle();

        batch = new SpriteBatch();
        // camera and screen
        float aspectRatio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        camera = new OrthographicCamera(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f);
        buttonHasBeenPressed = false;
        if (clientWorld.getMyPlayerGameCharacter() != null) {
            camera.position.set(clientWorld.getMyPlayerGameCharacter().getBoundingBox().getX(),
                    clientWorld.getMyPlayerGameCharacter().getBoundingBox().getY(), 0);
        } else {
            camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        }
        batch.setProjectionMatrix(camera.combined);
        // texture and background
        tiledMap = new TmxMapLoader().load("map3.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

    }

    public void setGameClient(GameClient client) {
        this.gameClient = client;
    }

    public void registerClientConnection(NewClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {
        if (clientWorld.getNewBot() != null) {
            clientConnection.sendBotInformation(clientWorld.getNewBot().getBoundingBox().getX(),
                    clientWorld.getNewBot().getBoundingBox().getY(), clientWorld.getNewBot().getPlayerDirection(),
                    clientWorld.getNewBot().getTagged());
            System.out.println("send bot information");
        }
        if (clientWorld.getMyPlayerGameCharacter() != null && !forSpectate) {
            if (firstTime) {
                scoreCalculation = new ScoreCalculation(clientWorld.getMyPlayerGameCharacter());
                firstTime = false;
            }
            clientConnection.sendPlayerInformation(clientWorld.getMyPlayerGameCharacter().getMovementSpeed() - 4f,
                    clientWorld.getMyPlayerGameCharacter().getMovementSpeed() - 4f, lastDirection,
                    clientWorld.getMyPlayerGameCharacter().getTagged());

            float xCamera = clientWorld.getMyPlayerGameCharacter().getBoundingBox().getX();
            float yCamera = clientWorld.getMyPlayerGameCharacter().getBoundingBox().getY();
            if ((clientWorld.getMyPlayerGameCharacter().getBoundingBox().getX() <= 481)) {
                xCamera = 481f;
            }
            if ((clientWorld.getMyPlayerGameCharacter().getBoundingBox().getY() <= 320)) {
                yCamera = 320f;
            }
            if ((clientWorld.getMyPlayerGameCharacter().getBoundingBox().getX() >= 1440)) {
                xCamera = 1440f;
            }
            if ((clientWorld.getMyPlayerGameCharacter().getBoundingBox().getY() >= 958)) {
                yCamera = 958f;
            }
            camera.position.set(xCamera, yCamera, 0);


            camera.update();

            tiledMapRenderer.setView(camera);
            tiledMapRenderer.render();

            //batch.disableBlending();

            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            detectInput();

            // draw characters
            drawPlayerGameCharacters();
            batch.end();

            // HUD render and draw
            scoreCalculation.scoreCheck();
            hud = new Hud(batch, clientWorld, gameClient, clientConnection);
            batch.setProjectionMatrix(hud.stage.getCamera().combined);
            hud.stage.draw();
            hud.update(Gdx.graphics.getDeltaTime());

            removeBeingStuck();


        } else if (forSpectate){
            if (!cameraForSpectate) {
                camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
                camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
                cameraForSpectate = true;
            }


            camera.update();

            tiledMapRenderer.setView(camera);
            tiledMapRenderer.render();

            //batch.disableBlending();

            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            detectInput();

            // draw characters
            drawPlayerGameCharacters();
            batch.end();

            // HUD render and draw
        }
    }
    public void removeBeingStuck() {
        boolean tagged = clientWorld.getMyPlayerGameCharacter().getTagged();
        if (!detectCollision(0, 0)) {
            for (int n = 1; n < 50; n++) {
                System.out.println("NUMBER:");
                System.out.println(n);
                if (detectCollision(n, 0)) {
                    clientConnection.sendPlayerInformation(n, 0, "right", tagged);
                    break;
                }
                else if (detectCollision(0, n)) {
                    clientConnection.sendPlayerInformation(0, n, lastDirection, tagged);
                    break;
                } else if (detectCollision(-n, 0)) {
                    clientConnection.sendPlayerInformation(-n, 0, "left", tagged);
                    break;
                } else if (detectCollision(0, -n)) {
                    clientConnection.sendPlayerInformation(0, -n, lastDirection, tagged);
                    break;
                }
            }
        }
    }
    private boolean detectCollision(int xDiff, int yDiff) {
        for (MapObject rectangleObject : objects) {
            RectangleMapObject rectangleMapObject = (RectangleMapObject) rectangleObject;
            Rectangle rectangle = rectangleMapObject.getRectangle();
            compareRectangle.set(clientWorld.getMyPlayerGameCharacter().getBoundingBox().getX() + xDiff,
                    clientWorld.getMyPlayerGameCharacter().getBoundingBox().getY() + yDiff,
                    clientWorld.getMyPlayerGameCharacter().getBoundingBox().getWidth(),
                    clientWorld.getMyPlayerGameCharacter().getBoundingBox().getHeight());
            if (compareRectangle.overlaps(rectangle)) {
                return false;
            }
        }
        return true;
    }


    private void detectInput() {
        if (clientWorld.getMyPlayerGameCharacter() != null) {
            if (!forSpectate) {
                Gdx.app.addLifecycleListener(new LifecycleListener() {
                    @Override
                    public void pause() {

                    }

                    @Override
                    public void resume() {

                    }

                    @Override
                    public void dispose() {
                        clientConnection.sendScore(clientWorld.getMyPlayerGameCharacter().getName(),
                                clientWorld.getMyPlayerGameCharacter().getScore());
                        clientConnection.sendPacketDisconnect(clientWorld.getMyPlayerGameCharacter().getPlayerGameCharacterId());
                        clientWorld.removeCharacter(clientWorld.getMyPlayerGameCharacter().getPlayerGameCharacterId());
                        Gdx.app.exit();
                    }
                });
            }
            if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && !forSpectate) {
                clientConnection.sendScore(clientWorld.getMyPlayerGameCharacter().getName(),
                        clientWorld.getMyPlayerGameCharacter().getScore());
                clientConnection.sendPacketDisconnect(clientWorld.getMyPlayerGameCharacter().getPlayerGameCharacterId());
                clientWorld.removeCharacter(clientWorld.getMyPlayerGameCharacter().getPlayerGameCharacterId());
                Gdx.app.exit();
            } else {
//                if (clientWorld.getNewBot() != null) {
//                    clientConnection.sendBotInformation(clientWorld.getNewBot().getBoundingBox().getX(),
//                            clientWorld.getNewBot().getBoundingBox().getY(), clientWorld.getNewBot().getPlayerDirection(),
//                            clientWorld.getNewBot().getTagged());
//                }

                if (clientWorld.getMyPlayerGameCharacter() != null) {
                    float movementSpeed = clientWorld.getMyPlayerGameCharacter().getMovementSpeed();
                    //float movementSpeed = 2;
                    boolean tagged = clientWorld.getMyPlayerGameCharacter().getTagged();
                    if (onGround && detectCollision(6, -4)) {
                        clientConnection.sendPlayerInformation(4f, -4f, "right", tagged);
                    }
                    if(onGround && detectCollision(-6, -4)) {
                        clientConnection.sendPlayerInformation(-4f, -4f, "left", tagged);
                    }
                    if (!onGround) {
                        if (!canJump || !Gdx.input.isKeyPressed(Input.Keys.W)) {
                            clientConnection.sendPlayerInformation(0, -movementSpeed, "down-" + lastDirection, tagged);
                            jumpCycle = 50;
                        }
                        if (!detectCollision(0, -4)) {
                            clientConnection.sendPlayerInformation(0, movementSpeed, "down-" + lastDirection, tagged);
                            onGround = true;
                            canJump = true;
                            jumpCycle = 0;
                        }

                    }

                    if (canJump && (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D))) {
                        moveJumpRight(movementSpeed, tagged);
                    } else if (!onGround && (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D))) {
                        moveDownRight(movementSpeed, tagged);
                    } else if (canJump && (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A))) {
                        moveJumpLeft(movementSpeed, tagged);
                    } else if (!onGround && Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)) {
                        moveDownLeft(movementSpeed, tagged);
                    } else if (canJump && Gdx.input.isKeyPressed(Input.Keys.W)) {
                        moveJump(movementSpeed, tagged);
                    } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                        moveLeft(movementSpeed, tagged);
                    } else if (!onGround && Gdx.input.isKeyPressed(Input.Keys.S)) {
                        moveDown(movementSpeed, tagged);
                    } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                        moveRight(movementSpeed, tagged);
                    }
                    if (detectCollision(0, -1)) {
                        onGround = false;
                    }
                    if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
                        if (!lastDirection.contains("idle_")) {
                            lastDirection = "idle_" + lastDirection;
                        }
                        clientConnection.sendPlayerInformation(0, 0, lastDirection, tagged);;
                    }
                }
            }
        }
    }
    public void moveJumpRight(float movementSpeed, boolean tagged) {
        lastDirection = "right";
        if (detectCollision(4, 0)) {
            clientConnection.sendPlayerInformation(movementSpeed, 0, "up-right", tagged);
        }
        if (detectCollision(0, 4)) {
            clientConnection.sendPlayerInformation(0, movementSpeed, "up-right", tagged);
        }
        jumpCycle++;
        if (jumpCycle >= 50) {
            canJump = false;
            jumpCycle = 0;
        }
    }
    public void moveDownRight(float movementSpeed, boolean tagged) {
        lastDirection = "right";
        if (detectCollision(4, 0)) {
            clientConnection.sendPlayerInformation(movementSpeed, 0, "down-right", tagged);
        }
        if (detectCollision(0, -8)) {
            clientConnection.sendPlayerInformation(0, -movementSpeed, "down-right", tagged);
        }
    }
    public void moveJumpLeft(float movementSpeed, boolean tagged) {
        lastDirection = "left";
        if (detectCollision(-4, 0)) {
            clientConnection.sendPlayerInformation(-movementSpeed, 0, "up-left", tagged);
        }
        if (detectCollision(0, 4)) {
            clientConnection.sendPlayerInformation(0, movementSpeed, "up-left", tagged);
        }
        jumpCycle++;
        if (jumpCycle >= 50) {
            jumpCycle = 0;
            canJump = false;
        }
    }
    public void moveDownLeft(float movementSpeed, boolean tagged) {
        lastDirection = "left";
        if (detectCollision(0, -8)) {
            clientConnection.sendPlayerInformation(0, -movementSpeed, "down-left", tagged);
        }
        if (detectCollision(-4, 0)) {
            clientConnection.sendPlayerInformation(-movementSpeed, 0, "down-left", tagged);
        }
    }
    public void moveJump(float movementSpeed, boolean tagged) {
        if (detectCollision(0, 4)) {
            clientConnection.sendPlayerInformation(0, movementSpeed, "up-" + lastDirection, tagged);
        }
        jumpCycle++;
        if (jumpCycle >= 50) {
            jumpCycle = 0;
            canJump = false;
        }
    }
    public void moveRight(float movementSpeed, boolean tagged) {
        lastDirection = "right";
        if (detectCollision(4, 0)) {
            clientConnection.sendPlayerInformation(movementSpeed, 0, "right", tagged);
        }
    }
    public void moveLeft(float movementSpeed, boolean tagged) {
        lastDirection = "left";
        if (detectCollision(-4, 0)) {
            clientConnection.sendPlayerInformation(-movementSpeed, 0, "left", tagged);
        }
    }
    public void moveDown(float movementSpeed, boolean tagged) {
        if (detectCollision(0, -8)) {
            clientConnection.sendPlayerInformation(0, -movementSpeed, "down-" + lastDirection, tagged);
        }
    }
    public void drawPlayerGameCharacters() {
        List<PlayerGameCharacter> characterValues = new ArrayList<>(clientWorld.getWorldGameCharactersMap().values());

        for (PlayerGameCharacter character : characterValues) {
            character.setTextureForDirection();
            character.draw(batch);
            if (character.getName() != null) {
                if (character.getName().length() < 5) {
                    font.draw(batch, character.getName(), character.getBoundingBox().getX() + character.getName().length() / 2 + 2, character.getBoundingBox().getY() + character.getBoundingBox().height + 16);
                } else {
                    font.draw(batch, character.getName(), character.getBoundingBox().getX() - character.getName().length() - character.getName().length() / 2, character.getBoundingBox().getY() + character.getBoundingBox().height + 16);
                }
            }
        }

    }

    public void setForSpectate(boolean forSpectate) {
        this.forSpectate = forSpectate;
    }

    @Override
    public void resize(int width, int height) {
        batch.setProjectionMatrix(camera.combined);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
