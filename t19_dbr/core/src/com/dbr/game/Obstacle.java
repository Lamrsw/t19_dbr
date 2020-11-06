package com.dbr.game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle extends Rectangle{
    Texture texture;

    Obstacle(Texture texture){
        this.texture = texture;
    }

    public Texture getTexture(){ return texture;}
}
