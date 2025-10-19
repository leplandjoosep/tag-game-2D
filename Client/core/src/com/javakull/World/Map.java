package com.javakull.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.List;
import java.util.ArrayList;

public class Map {
    private TiledMap map;
    private List<Rectangle> obstacles;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private MapObjects mapObjects;
    public Map() {
        // Load the map from the Tiled file
        map = new TmxMapLoader().load("map3.tmx");

        // Create a renderer to render the map
        renderer = new OrthogonalTiledMapRenderer(map);

        // Create a camera to view the map
        mapObjects = map.getLayers().get(3).getObjects();
    }


    public MapObjects getObjects() {
        return mapObjects;
    }
}