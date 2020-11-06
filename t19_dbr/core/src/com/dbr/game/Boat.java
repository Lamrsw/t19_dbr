package com.dbr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import static com.badlogic.gdx.math.MathUtils.random;

public class Boat extends Rectangle {
    Integer health;
    float speed;
    Integer acceleration;
    Integer maneuverability;
    String colour;
    float penaltyTime;
    Integer stamina;
    Integer maxX;
    Integer minX;
    float moveDistancex;
    float moveDistancey;

    //Used for initialising class
    Boat(Integer health, float speed, Integer acceleration, Integer maneuverability, String colour, float penaltyTime, Integer stamina,Integer width,Integer height, Integer maxX, Integer minX){
        this.width = width;
        this.height = height;
        this.health = health;
        this.speed = speed;
        this.acceleration = acceleration;
        this.maneuverability = maneuverability;
        this.colour = colour;
        this.penaltyTime = penaltyTime;
        this.stamina = stamina;
        this.maxX = maxX;
        this.minX = minX;


    }

    //Moves the boats x or y position by given amount
    public void move(int frames){
        //Changes boats direction every 10 frames
        if (frames%10 == 0) {
            moveDistancex = random(-20,20);
            moveDistancey = random(-10,10);
        }
        //checks if the boat is trying to move out of its lane, picks a new distance if it is, otherwise it moves;
        if(this.x + (this.speed * Gdx.graphics.getDeltaTime() * (moveDistancex/10)) > this.maxX-this.width || this.x + (this.speed * Gdx.graphics.getDeltaTime() * (moveDistancex/10)) < this.minX) {
            moveDistancex = random(-20, 20);
        }
        else{
            this.setX(this.x +(this.speed * Gdx.graphics.getDeltaTime() * (moveDistancex/10)));
        }
        if(this.y + (this.speed * Gdx.graphics.getDeltaTime() * (moveDistancey/10)) > 1152 || this.y + (this.speed * Gdx.graphics.getDeltaTime() * (moveDistancey/10)) < 0){
            moveDistancey = random(-10,10);
        }
        else {
            this.setY(this.y + (this.speed * Gdx.graphics.getDeltaTime() * (moveDistancey / 10)));
        }

    }

    //Health functions
    public void reduceHealth(Integer amount){ health -= amount; }

    public Integer getHealth(){ return health;}

    public void setHealth(Integer amount){ health = amount;}

    //Stamina functions
    public void reduceStamina(Integer amount){ stamina -= amount;}

    public Integer getStamina(){ return stamina; }

    public void setStamina(Integer amount){stamina = amount;}

    //Acceleration functions
    public Integer getAcceleration(){ return acceleration;}

    public void setAcceleration(Integer amount){acceleration = amount;}

    //Colour functions
    public String getColour(){return colour;}
}
