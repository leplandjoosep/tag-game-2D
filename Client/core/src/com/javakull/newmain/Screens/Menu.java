package com.javakull.newmain.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.w3c.dom.Text;

public class Menu extends Actor {


    Texture texture = new Texture(Gdx.files.internal("uiBackground.jpg"));

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, 50 , 50);
    }


}