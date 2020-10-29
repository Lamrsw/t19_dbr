package com.dbr.game;

public class Boat {

    Integer robustness;
    public float speed;
    Integer acceleration;
    Integer maneuverability;
    String colour;
    float penaltyTime;
    float xpos;
    float ypos;

    //Used for initialising class
    Boat(Integer robustness, Integer speed, Integer acceleration, Integer maneuverability, String colour, float penaltyTime){
        this.robustness = robustness;
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
    public void setx(Integer pos){
        xpos = pos;
    }

    public void sety(Integer pos){
        ypos = pos;
    }

    public float getx(){

        return xpos;
    }

    public float gety(){

        return ypos;
    }
}
