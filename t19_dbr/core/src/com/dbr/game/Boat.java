package com.dbr.game;

import com.badlogic.gdx.math.Rectangle;

public class Boat extends Rectangle {

    Integer health;
    float speed;
    Integer acceleration;
    Integer maneuverability;
    String colour;
    float penaltyTime;
    float xpos;
    float ypos;
    Integer stamina;

    //Used for initialising class
    Boat(Integer health, float speed, Integer acceleration, Integer maneuverability, String colour, float penaltyTime, Integer stamina){
        this.health = health;
        this.speed = speed;
        this.acceleration = acceleration;
        this.maneuverability = maneuverability;
        this.colour = colour;
        this.penaltyTime = penaltyTime;

    }

    //Moves the boats x or y position by given amount
    public void movex(float amount){
        xpos += amount;
    }

    public void movey(float amount){
        ypos += amount;
    }

    //Allows x and y positions to be set directly
    public void setx(float pos){
        xpos = pos;
    }

    public void sety(float pos){
        ypos = pos;
    }

    //Returns the x and y values
    public float getx(){ return xpos; }

    public float gety(){ return ypos; }

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
