package com.dbr.game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * The main obstacle class which creates obstacles
 * for the boats to avoid them
 */

public class Obstacle extends Rectangle{
    Texture texture;

    Obstacle(Texture texture){
        this.texture = texture;
    }

    //Allows each obstacle to have a different texture
    public Texture getTexture(){ return texture;}
}
