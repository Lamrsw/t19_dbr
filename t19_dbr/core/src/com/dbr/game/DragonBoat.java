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

	//Texture variables
	private Texture boat;
	private Texture obstacleImageA;
	private Texture obstacleImageB;
	private Texture obstacleImageC;
	private Array<Texture> obstacleImage;
	private Texture barrierImage;

	//background images
	private Texture backImage1;
	private Texture backImage2;
	private int backImage1y;
	private int backImage2y;

	//Font variables
	private BitmapFont font;

	//Obstacle variables
	private Array<Obstacle> obstacles;
	private long lastDropTime;

	private boolean gameOver = false;

	//Screen variables
	Integer SCREEN_WIDTH;
	Integer SCREEN_HEIGHT;

	//Boats
	private Boat mainBoat;
	private Boat aiBoatOne;
	private Boat aiBoatTwo;
	private Boat aiBoatThree;
	private Boat aiBoatFour;


	//Used to control CPU boat movement
	private int frameCount;
	private float aiMoveDistance;

	//barrier variables
	private Array<Rectangle> barriers;
	
	@Override
	public void create () {
		//Setting Screen width and height
		SCREEN_WIDTH = 1280;
		SCREEN_HEIGHT = 720;

		frameCount = 0;
		aiMoveDistance = 1;

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

		//background images
		backImage1 = new Texture("water.png");
		backImage2 = new Texture("water.png");

		//barrier image
		barrierImage = new Texture("barrier.jpg");

		//Creating boat as rectangle
		mainBoat = new Boat(5,200, 10 ,10 ,"red", 10,10, 64, 128,768,512);
		mainBoat.x = (SCREEN_WIDTH/2)-32;
		mainBoat.y = 0;

		//Creating CPU boats
		aiBoatOne = new Boat(2, 200, 10, 10, "red", 10, 10, 64, 128,256,0);
		aiBoatOne.x = 96;
		aiBoatOne.y = 0;

		aiBoatTwo = new Boat(2, 200, 10, 10, "red", 10, 10, 64, 128,512,256);
		aiBoatTwo.x = 352;
		aiBoatTwo.y = 0;

		aiBoatThree = new Boat(2, 200, 10, 10, "red", 10, 10, 64, 128,1024,768);
		aiBoatThree.x = 864;
		aiBoatThree.y = 0;

		aiBoatFour = new Boat(2, 200, 10, 10, "red", 10, 10, 64, 128,1280,1024);
		aiBoatFour.x = 1120;
		aiBoatFour.y = 0;


		//Creates obstacle array and spawn the first obstacle
		obstacles = new Array<Obstacle>();
		spawnObstacle();

		barriers = new Array<Rectangle>();
		for(int i= 1;i<6;i++) {
			createBarrier(i);
		}

		backImage1y = 0;
		backImage2y = SCREEN_HEIGHT;
	}

	@Override
	public void render () {

		frameCount += 1;

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

			//Allows pressing of space to continue game
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

			//Drawing scrolling background
			batch.draw(backImage1,0,backImage1y);
			batch.draw(backImage2,0,backImage2y);
			backImage1y -= 1;
			backImage2y -= 1;
			if(backImage1y <= -SCREEN_HEIGHT){
				backImage1y = SCREEN_HEIGHT;
			}
			if(backImage2y <= -SCREEN_HEIGHT){
				backImage2y = SCREEN_HEIGHT;
			}

			//draws each barrier in barriers array
			for(Rectangle barrier: barriers){
				batch.draw(barrierImage,barrier.x,barrier.y);
			}

			//Rendering boats if their health isn't 0
			batch.draw(boat, mainBoat.x, mainBoat.y);


			if(aiBoatOne.getHealth() > 0) {
				batch.draw(boat, aiBoatOne.x, aiBoatOne.y);
			}
			else{
				aiBoatOne.x = -200;
				aiBoatOne.y = -200;
			}
				batch.draw(boat, aiBoatTwo.x, aiBoatTwo.y);
				batch.draw(boat, aiBoatThree.x, aiBoatThree.y);
				batch.draw(boat, aiBoatFour.x, aiBoatFour.y);

			//draws each obstacle currently stored in obstacles array
			for (Obstacle obstacle : obstacles) {
				batch.draw(obstacle.getTexture(), obstacle.x, obstacle.y);
			}

			batch.end();

			//decrease boat speed every set number of frames to simulate tiredness
			mainBoat.speedCheck(frameCount);
			aiBoatOne.speedCheck(frameCount);
			aiBoatTwo.speedCheck(frameCount);
			aiBoatThree.speedCheck(frameCount);
			aiBoatFour.speedCheck(frameCount);


			//Movement options for the boat
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
				mainBoat.setX(mainBoat.x + (-mainBoat.speed * Gdx.graphics.getDeltaTime()));
			}
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
				mainBoat.setX(mainBoat.x + (mainBoat.speed * Gdx.graphics.getDeltaTime()));
			}
			if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
				mainBoat.setY(mainBoat.y + (mainBoat.speed * Gdx.graphics.getDeltaTime()));
			}
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
				mainBoat.setY(mainBoat.y + (-mainBoat.speed * Gdx.graphics.getDeltaTime()));
			}

			//CPU boats random movement
			aiBoatOne.move(obstacles);
			aiBoatTwo.move(obstacles);
			aiBoatThree.move(obstacles);
			aiBoatFour.move(obstacles);


			//Spawns in obstacle after set amount of time
			if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
				spawnObstacle();
			}

			//Obstacle movement and collision checking
			for (Iterator<Obstacle> iter = obstacles.iterator(); iter.hasNext(); ) {
				Obstacle obstacle = iter.next();
				obstacle.y -= 200 * Gdx.graphics.getDeltaTime();
				if (obstacle.y + 64 < 0) iter.remove();
				collisionCheck(obstacle, iter);

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

	//Spawns obstacles in at the top of the screen with a random x value
	private void spawnObstacle(){
		Obstacle obstacle = new Obstacle(obstacleImage.get(random(0,2)));
		obstacle.x = random(0, SCREEN_WIDTH-64);
		obstacle.y = SCREEN_HEIGHT;
		obstacle.width = 64;
		obstacle.height = 64;
		obstacles.add(obstacle);
		lastDropTime = TimeUtils.nanoTime();
	}

	//Checks collisions between boats and obstacles
	private void collisionCheck(Obstacle obstacle, Iterator<Obstacle> iter){
        if (obstacle.overlaps(mainBoat)) {
			iter.remove();
			mainBoat.reduceHealth(1);
			if (mainBoat.getHealth() == 0) {
				gameOver = true;
			}
		}
        else if(obstacle.overlaps(aiBoatOne)){
        	iter.remove();
        	aiBoatOne.reduceHealth(1);
        	if(aiBoatOne.getHealth() == 0){
        		aiBoatOne.x = -200;
        		aiBoatOne.y = -200;
			}
		}
		else if(obstacle.overlaps(aiBoatTwo)){
			iter.remove();
			aiBoatTwo.reduceHealth(1);
			if(aiBoatTwo.getHealth() == 0){
				aiBoatTwo.x = -200;
				aiBoatTwo.y = -200;
			}
		}
		else if(obstacle.overlaps(aiBoatThree)){
			iter.remove();
			aiBoatThree.reduceHealth(1);
			if(aiBoatThree.getHealth() == 0){
				aiBoatThree.x = -200;
				aiBoatThree.y = -200;
			}
		}
		else if(obstacle.overlaps(aiBoatFour)){
			iter.remove();
			aiBoatFour.reduceHealth(1);
			if(aiBoatFour.getHealth() == 0){
				aiBoatFour.x = -200;
				aiBoatFour.y = -200;
			}
		}
    }

    private void createBarrier(int count){
		Rectangle barrier = new Rectangle();
		barrier.x = (SCREEN_WIDTH/5)*count;
		barrier.y = 0;
		barrier.width = 1;
		barrier.height = SCREEN_HEIGHT;
		barriers.add(barrier);
	}

}
