package com.dbr.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Boat extends Rectangle {

/**
 * Represents the main boat class which then
 * gets inherited by CPUBoat and PlayerBoat.
 * Contains the functions speed check and function 
 * reset.
 */

    Integer health, maneuverability, stamina, maxX, minX, startX, startY, startHealth;
    float speed, acceleration, penaltyTime, moveDistancey, startAcceleration, startSpeed;
    String colour;
    int direction =1;

    //Used for initialising class
    Boat(Integer health, float speed, float acceleration, Integer maneuverability, float penaltyTime,Integer width,Integer height, Integer maxX, Integer minX, Integer x, Integer y){
        
        /**
         * Creates a new instance of the class Boat
         * @param int health This is the health of the boat
         * @param int maneuverability This is the maneuverability of the boat
         * @param int width This is the width of the boat
         * @param int height This is the height of the boad
         * @param int maxX This is the maximum X
         * @param int minX This is the minimum X
         * @param int maxY This is the maximum Y
         * @param int minY This is the minimum Y
         * @param float speed This is the speed of the boat
         * @param float acceleration This is the acceleration of the boat
         * @param float penaltyTime This is how much time has been added as a penalty
         * @return None
         */
        
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

        /**
         * Class which allows to check collision between the boat
         * and an object.
         * 
         * @param Obstacle obstacle The current obstacle
         * @param Iterator<Obstacle> iter An iterator which goes through the obstacles
         * @return Iterator<Obstacle> iter An iteator which goes through the obstacles
         */

        return iter;

    }

    //Health functions
    public void reduceHealth(Integer amount){ health -= amount; }

    public Integer getHealth(){ return health;}

    //Resets variables that can change to their original
    public void reset(){

        /**
         * resets the boats health, x, y, speed and acceleration
         * to 0 at the beginning of every leg.
         * 
         * @param None
         * @return None
         * 
         */

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
        
        /**
         * Decreases the speed and the acceleration
         * as time passes to mimic the idea of
         * rowers gettinf tired.
         * 
         * @param framecount This is the number of frames counted
         * @return None
         * 
         */
        
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
