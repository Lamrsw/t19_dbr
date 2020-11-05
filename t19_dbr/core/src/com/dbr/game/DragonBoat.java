package com.dbr.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

import static com.badlogic.gdx.math.MathUtils.random;

public class DragonBoat extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture boat;
	private Texture obstacleImageA;
	private Texture obstacleImageB;
	private Texture obstacleImageC;
	private Array<Texture> obstacleImage;
	private BitmapFont font;

	private Array<Obstacle> obstacles;
	private long lastDropTime;

	private boolean gameOver = false;
	Integer SCREEN_WIDTH;
	Integer SCREEN_HEIGHT;

	//Boats
	private Boat mainBoat;
	private Boat aiBoatOne;


	
	@Override
	public void create () {
		//Setting Screen width and height
		SCREEN_WIDTH = 1280;
		SCREEN_HEIGHT = 720;

		//Creating font
		font = new BitmapFont();
		font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		batch = new SpriteBatch();

		//Boat images
		boat = new Texture("boat.png");

		//Obstacle Images
		obstacleImageA = new Texture("obstacleA.jpg");
		obstacleImageB = new Texture("obstacleB.jpg");
		obstacleImageC = new Texture("obstacleC.jpg");
		obstacleImage = new Array<Texture>();
		obstacleImage.add(obstacleImageA);
		obstacleImage.add(obstacleImageB);
		obstacleImage.add(obstacleImageC);

		//Creating boat as rectangle
		mainBoat = new Boat(5,200, 10 ,10 ,"red", 10,10, 64, 128);
		mainBoat.x = SCREEN_WIDTH/2;
		mainBoat.y = 0;

		//Creating CPU boats
		aiBoatOne = new Boat(2,200,10,10,"red",10,10, 64 , 128);
		aiBoatOne.x = 300;
		aiBoatOne.y = 300;


		//Creates obstacle array and spawn the first obstacle
		obstacles = new Array<Obstacle>();
		spawnObstacle();
	}

	@Override
	public void render () {

		//Gameover screen rendering - may move to separate file later
		if(gameOver == true){

			//Sets game over background to black
			Gdx.gl.glClearColor(0,0,0,1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			//Draws game over text on screen
			batch.begin();
			font.draw(batch,"Game Over!",(SCREEN_WIDTH/2) ,SCREEN_HEIGHT/2);
			font.draw(batch,"Press space to retry.",SCREEN_WIDTH/2,SCREEN_HEIGHT/2-20);
			batch.end();

			//Allows pressing of space to continute game
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
				create();
				gameOver = false;
			}

		}

		//Rendering for main part of game
		else {
			Gdx.gl.glClearColor(0, 0, 1, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			batch.begin();
			batch.draw(boat, mainBoat.x, mainBoat.y);
			batch.draw(boat,aiBoatOne.x,aiBoatOne.y);

			for (Obstacle obstacle : obstacles) {
				batch.draw(obstacle.getTexture(), obstacle.x, obstacle.y);
			}
			batch.end();


			//Movement options for the boat
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
				mainBoat.movex(-mainBoat.speed * Gdx.graphics.getDeltaTime());
			}
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
				mainBoat.movex(mainBoat.speed * Gdx.graphics.getDeltaTime());
			}
			if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
				mainBoat.movey(mainBoat.speed * Gdx.graphics.getDeltaTime());
			}
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
				mainBoat.movey(-mainBoat.speed * Gdx.graphics.getDeltaTime());
			}

			aiBoatOne.movex(aiBoatOne.speed * Gdx.graphics.getDeltaTime() * random(-1,1));
			aiBoatOne.movey(aiBoatOne.speed * Gdx.graphics.getDeltaTime()*random(-1,1));

			//Spawns in obstacle after set amount of time
			if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
				spawnObstacle();
			}

			//Obstacle movement and collision checking
			for (Iterator<Obstacle> iter = obstacles.iterator(); iter.hasNext(); ) {
				Rectangle obstacle = iter.next();
				obstacle.y -= 200 * Gdx.graphics.getDeltaTime();
				if (obstacle.y + 64 < 0) iter.remove();
				if (obstacle.overlaps(mainBoat)) {
					iter.remove();
					mainBoat.reduceHealth(1);
					if (mainBoat.getHealth() == 0) {
						gameOver = true;
					}

				}

			}
		}

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		boat.dispose();
		obstacleImageA.dispose();
		obstacleImageB.dispose();
		obstacleImageC.dispose();
	}

	private void spawnObstacle(){
		Obstacle obstacle = new Obstacle(obstacleImage.get(random(0,2)));
		obstacle.x = random(0, SCREEN_WIDTH-64);
		obstacle.y = SCREEN_HEIGHT;
		obstacle.width = 64;
		obstacle.height = 64;
		obstacles.add(obstacle);
		lastDropTime = TimeUtils.nanoTime();
	}

}
