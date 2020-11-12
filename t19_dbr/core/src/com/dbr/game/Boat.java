package com.dbr.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Boat extends Rectangle {
    Integer health;
    float speed;
    float acceleration;
    Integer maneuverability;
    String colour;
    float penaltyTime;
    Integer stamina;
    Integer maxX;
    Integer minX;
    Integer startX;
    Integer startY;
    float moveDistancey;
    int direction =1;
    float startAcceleration;
    float startSpeed;
    Integer startHealth;

    //Used for initialising class
    Boat(Integer health, float speed, float acceleration, Integer maneuverability, float penaltyTime,Integer width,Integer height, Integer maxX, Integer minX, Integer x, Integer y){
        this.width = width;
        this.height = height;
        this.health = health;
        this.speed = speed;
        this.acceleration = acceleration;
        this.maneuverability = maneuverability;
        this.penaltyTime = penaltyTime;
        this.maxX = maxX;
        this.minX = minX;
        this.startX = x;
        this.startY = y;
        this.x = x;
        this.y = y;
        this.startSpeed = speed;
        this.startAcceleration = acceleration;
        this.startHealth = health;
    }

    //Move function which will overriden by the children
    protected void move(Array<Obstacle> obstacles, int frameCount){};

    //FinishCheck function checks if boat overlaps with finish line
    protected int finishCheck(Obstacle finish){
        return 0;
    };

    protected Iterator<Obstacle> collisionCheck(Obstacle obstacle, Iterator<Obstacle> iter){

        return iter;

    }

    //Health functions
    public void reduceHealth(Integer amount){ health -= amount; }

    public Integer getHealth(){ return health;}

    //Resets variables that can change to their original
    public void reset(){
        this.x = this.startX;
        this.y = this.startY;
        this.health =  startHealth;
        this.speed = startSpeed;
        this.acceleration = startAcceleration;
    }

    public void setHealth(Integer amount){ health = amount;}

    //Acceleration functions
    public float getAcceleration(){ return acceleration;}

    public void setAcceleration(Integer amount){acceleration = amount;}

    //Colour functions
    public String getColour(){return colour;}

    public void speedCheck(int framecount){
        //Decreases boats speed every 60 frames
        if(speed >100 && framecount % 60 == 0){
            speed -=1;
        }
        //Decreases boats acceleration every 240 frames
        if(acceleration > 0.5 && framecount %240 == 0){
            acceleration -= 0.1;
        }
    }
}
