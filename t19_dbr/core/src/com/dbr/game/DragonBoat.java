package com.dbr.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class DragonBoat extends ApplicationAdapter {
	SpriteBatch batch;
	Texture boat;
	Texture obstacleImage;
	Boat mainBoat;

	private Array<Rectangle> obstacles;
	private long lastDropTime;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		boat = new Texture("boat.jpg");

		obstacleImage = new Texture("obstacle.jpg");

		//Creating boat as rectangle
		mainBoat = new Boat(10,200, 10 ,10 ,"red", 10,10);
		mainBoat.width = 64;
		mainBoat.height= 128;


		//Creates obstacle array and spawn the first obstacle
		obstacles = new Array<Rectangle>();
		spawnObstacle();
	}

	@Override
	public void render () {


		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(boat, mainBoat.getx(), mainBoat.gety());

		for(Rectangle obstacle: obstacles){
			batch.draw(obstacleImage, obstacle.x, obstacle.y);
		}
		batch.end();



		//Movement options for the boat
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
			mainBoat.movex(-mainBoat.speed * Gdx.graphics.getDeltaTime());
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)|| Gdx.input.isKeyPressed(Input.Keys.D)) {
			mainBoat.movex(mainBoat.speed * Gdx.graphics.getDeltaTime());
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)){
			mainBoat.movey(mainBoat.speed * Gdx.graphics.getDeltaTime());
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)){
			mainBoat.movey(-mainBoat.speed * Gdx.graphics.getDeltaTime());
		}
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000){
			spawnObstacle();
		}

		for (Iterator<Rectangle> iter = obstacles.iterator(); iter.hasNext(); ) {
			Rectangle obstacle = iter.next();
			obstacle.y -= 200 * Gdx.graphics.getDeltaTime();
			if(obstacle.y + 64 < 0) iter.remove();
			if(obstacle.overlaps(mainBoat)){
				mainBoat.reduceHealth(1);
				iter.remove();
			}
		}

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		boat.dispose();
		obstacleImage.dispose();
	}

	private void spawnObstacle(){
		Rectangle obstacle = new Rectangle();
		obstacle.x = MathUtils.random(0, 800-64);
		obstacle.y = 480;
		obstacle.width = 64;
		obstacle.height = 64;
		obstacles.add(obstacle);
		lastDropTime = TimeUtils.nanoTime();
	}
}
