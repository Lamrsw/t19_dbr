package com.dbr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;

public class PlayerBoat extends Boat{

    private Integer SCREEN_HEIGHT;

    PlayerBoat(Integer health, float speed, Integer acceleration, Integer maneuverability, float penaltyTime, Integer width, Integer height, Integer maxX, Integer minX, Integer x, Integer y, Integer SCREEN_HEIGHT) {
        super(health, speed, acceleration, maneuverability, penaltyTime,width,height, maxX, minX, x, y);
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;
    };

    @Override
    public void move(Array<Obstacle> obstacles, int frames){
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
