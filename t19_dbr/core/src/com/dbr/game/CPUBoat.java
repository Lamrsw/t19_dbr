package com.dbr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import java.util.Iterator;
import static com.badlogic.gdx.math.MathUtils.random;

public class CPUBoat extends Boat{

    CPUBoat(Integer health, float speed, Integer acceleration, Integer maneuverability, float penaltyTime,Integer width,Integer height, Integer maxX, Integer minX, Integer x, Integer y){
        super(health, speed, acceleration, maneuverability, penaltyTime,width,height, maxX, minX, x, y);
    };

     public void move(Array<Obstacle> obstacles, int frames){

        //Boats should only move if they have health left
        if(getHealth() != 0) {

            //Controls boats y movement which is random and can change every 10 frames
            if (frames % 20 == 0) {
                moveDistancey = random(-10, 10);
            }
            if (y + (speed * acceleration * Gdx.graphics.getDeltaTime() * (moveDistancey / 10)) > 896) {
                moveDistancey = random(-10, 0);
            } else if (y + (speed * acceleration * Gdx.graphics.getDeltaTime() * (moveDistancey / 10)) < 0) {
                moveDistancey = random(0, 10);
            } else {
                this.setY(y + (speed * acceleration * Gdx.graphics.getDeltaTime() * (moveDistancey / 10)));
            }

            //Controls boats x movement, tries to avoid obstacles without leaving its lane

            if (x + 64 + (speed* acceleration * Gdx.graphics.getDeltaTime()) >= maxX && direction == 1) {
                direction = -1;

            } else if (x - (speed * acceleration * Gdx.graphics.getDeltaTime()) <= minX && direction == -1) {
                direction = 1;
            }

            for (Iterator<Obstacle> iter = obstacles.iterator(); iter.hasNext(); ) {
                Obstacle obstacle = iter.next();
                if (obstacle.getY() - getY() < 200 && obstacle.getX() - getX() < 64 && obstacle.getX() - getX() > -64) {

                    setX(x + ((speed* acceleration * Gdx.graphics.getDeltaTime()) * direction));
                }
            }
        }
    }
    
    @Override
    public int finishCheck(Obstacle finishLine){

        if (this.overlaps(finishLine)){
            this.y = -200;
            this.x = -200;
            return 1;
        }
        else{
            return 0;
        }
    };

    @Override
    public Iterator<Obstacle> collisionCheck(Obstacle obstacle, Iterator<Obstacle> iter){
        if (obstacle.overlaps(this)){
            iter.remove();
            this.reduceHealth(1);
        }
        if (this.getHealth() == 0){
            this.x = -200;
            this.y = -200;
        }
        return iter;

    }

}
