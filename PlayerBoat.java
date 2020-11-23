package com.dbr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;

public class PlayerBoat extends Boat{

    private Integer SCREEN_HEIGHT;

    PlayerBoat(Integer health, float speed, Integer acceleration, Integer maneuverability, float penaltyTime, Integer width, Integer height, Integer maxX, Integer minX, Integer x, Integer y, Integer SCREEN_HEIGHT) {
		/**
		 * /**
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
         */
		
		super(health, speed, acceleration, maneuverability, penaltyTime,width,height, maxX, minX, x, y);
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;
    };

    @Override
    public void move(Array<Obstacle> obstacles, int frames){
	/**
	 * This function allows the player to move when the
	 * correct key is pressed
	 * @param Array<Obstacle> obstacles This is the array of obstacles that can hit the boat
	 * @param int frames This is the number of frames
	 * 
	 */
	
		//Movement options for the players boat
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
			this.setX(this.x + (-this.speed * this.acceleration * Gdx.graphics.getDeltaTime()));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
			this.setX(this.x + (this.speed * this.acceleration * Gdx.graphics.getDeltaTime()));
		}
		if ((Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) && this.y <= this.SCREEN_HEIGHT - this.height) {
			this.setY(this.y + (this.speed * this.acceleration * Gdx.graphics.getDeltaTime()));
		}
		if ((Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) && this.y >= 0) {
			this.setY(this.y + (-this.speed * this.acceleration * Gdx.graphics.getDeltaTime()));
		}

    }
    
}
