package com.dbr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

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
    int direction =1;

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
    /*
    //Moves the boats in random directions
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
     */

    public void move(Array<Obstacle> obstacles){
        if(this.x+64 + (this.speed * Gdx.graphics.getDeltaTime()) >= this.maxX && direction == 1){
            this.direction = -1;
        }
        else if(this.x -(this.speed * Gdx.graphics.getDeltaTime()) <= this.minX && direction == -1){
            this.direction = 1;
        }
        for (Iterator<Obstacle> iter = obstacles.iterator(); iter.hasNext(); ) {
            Obstacle obstacle = iter.next();
            if(obstacle.getY()-this.getY() < 200 && obstacle.getX()-this.getX()<64 && obstacle.getX() - this.getX() > -64 ) {

                this.setX(this.x + ((this.speed * Gdx.graphics.getDeltaTime()) * direction));
            }
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

    public void speedCheck(int framecount){
        if(this.speed >100 && framecount % 60 == 0){
            speed -=1;
        }
    }
}
