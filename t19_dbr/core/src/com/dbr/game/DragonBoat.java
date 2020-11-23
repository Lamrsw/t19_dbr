package com.dbr.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Arrays;
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
	private Texture finishImage;
	private Texture healthImage;

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

	//Game over for when health reaches 0
	private boolean gameOver = false;

	//Setting Screen width and height
	int SCREEN_WIDTH = 1280;
	int SCREEN_HEIGHT = 720;

	//Creating players boat
	private Boat mainBoat = new PlayerBoat(10,200, 1 , 10, 10, 64, 128,768,512, (SCREEN_WIDTH/2)-32, 0, SCREEN_HEIGHT);
	
	//Creating CPU boats
	private Boat aiBoatOne = new CPUBoat(5, 100, 2,10, 10, 64, 128,256,0, 96, 0);
	private Boat aiBoatTwo = new CPUBoat(2, 300, 1, 10, 10, 64, 128,512,256, 352, 0);
	private Boat aiBoatThree = new CPUBoat(3, 250, 1,  10, 10, 64, 128,1024,768, 864, 0);
	private Boat aiBoatFour = new CPUBoat(8, 150, 1,  10, 10, 64, 128,1280,1024, 1120, 0);

	//Used to control CPU boat movement
	private int frameCount;

	//barrier variables
	private Array<Rectangle> barriers;

	//Finish line variables
    boolean finishDrawn;
    Obstacle finishLine;
    int position;
    int finished = -1;

    //difficulty modifier
	int difficulty = 0;

	//Leg number
	int leg = 1;

	//Used for boat selection screen
	boolean selectBoat = false;
	private Stage selectStage;

	//Used for timing leg length
	long startTime = 0;
	long endTime;

	//Used to keep track of if the tutorial has been seen
	boolean tutorial = false;

	//Used for penalty for leaving lane
	int penalty;

	//Demo mode
	boolean demo = false;

	@Override
	public void create () {
		penalty = 0;

		//Used to keep track of leg time
		startTime = System.currentTimeMillis();

        //Used to keep track of total number of frames
		frameCount = 0;

		//Creating font

		font = new BitmapFont();
		font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		batch = new SpriteBatch();

		//Boat images
		boat = new Texture("boat.png");

		//Reset boats
		mainBoat.reset();
		aiBoatOne.reset();
		aiBoatTwo.reset();
		aiBoatThree.reset();
		aiBoatFour.reset();

		//Reset Position
		position = 0;

		//Obstacle Images
		obstacleImageA = new Texture("obstacleA.png");
		obstacleImageB = new Texture("obstacleB.png");
		obstacleImageC = new Texture("obstacleC.png");
		obstacleImage = new Array<Texture>();
		obstacleImage.add(obstacleImageA);
		obstacleImage.add(obstacleImageB);
		obstacleImage.add(obstacleImageC);

		//background images
		backImage1 = new Texture("water.png");
		backImage2 = new Texture("water.png");

		//barrier image
		barrierImage = new Texture("barrier.jpg");

		//Creates obstacle array and spawn the first obstacle
		obstacles = new Array<Obstacle>();
		spawnObstacle();

		barriers = new Array<Rectangle>();
		for(int i= 1;i<6;i++) {
			createBarrier(i);
		}

		//Sets start position of background images
		backImage1y = 0;
		backImage2y = SCREEN_HEIGHT;

		//Finish line variables
        finishDrawn = false;
        finishImage = new Texture("finishLine.png");
        finishLine = new Obstacle(finishImage);
        finishLine.width = SCREEN_WIDTH;
        finishLine.x = 0;
        finishLine.y = SCREEN_HEIGHT;
		position = 1;

		//Health bar variables
		healthImage = new Texture("health.png");

		//Boat select screen
		selectStage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(selectStage);
		//Creating select buttons
		createButtons();
	}

	@Override
	public void render () {

		if(tutorial == false){
			tutorialScreen();
		}

		else if(selectBoat == false){
			selectBoatScreen();
		}

		//Game over screen
		else if(gameOver){
			gameOverScreen();
		}

		//Final screen for finishing race
		else if(finished != -1 && leg == 4){
			endScreen();
		}

		//Screen for in between legs
		else if(finished != -1){
			midScreen();
		}

		//Rendering for main part of game
		else {
			frameCount += 1;

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

			//Rendering boats
			batch.draw(boat, mainBoat.x, mainBoat.y);
			batch.draw(boat, aiBoatOne.x, aiBoatOne.y);
			batch.draw(boat, aiBoatTwo.x, aiBoatTwo.y);
			batch.draw(boat, aiBoatThree.x, aiBoatThree.y);
			batch.draw(boat, aiBoatFour.x, aiBoatFour.y);

			//draws each obstacle currently stored in obstacles array
			for (Obstacle obstacle : obstacles) {
				batch.draw(obstacle.getTexture(), obstacle.x, obstacle.y);
			}

			//Drawing finish line after set amount of time;
            if(frameCount > (960)*(1+(difficulty/2))){
                batch.draw(finishImage,finishLine.x,finishLine.y);
                finishDrawn = true;
            }

            //Draws health bar
			for(int i=0;i<mainBoat.getHealth();i++){
				drawHealth(i);
			}

			batch.end();

			//decrease boat speed every set number of frames to simulate tiredness
			mainBoat.speedCheck(frameCount);
			aiBoatOne.speedCheck(frameCount);
			aiBoatTwo.speedCheck(frameCount);
			aiBoatThree.speedCheck(frameCount);
			aiBoatFour.speedCheck(frameCount);

			//Players boat movement
			mainBoat.move(obstacles,frameCount);

			//CPU boats movement
			for (Boat boat1 : Arrays.asList(aiBoatOne, aiBoatTwo, aiBoatThree, aiBoatFour)) {
				boat1.move(obstacles,frameCount);
			}

			//Checking if players boat has left its lane
			if(mainBoat.getX() > mainBoat.maxX || mainBoat.getX() < mainBoat.minX){
				penalty += 1;
			}

			//Spawns in obstacle after set amount of time
			if (TimeUtils.nanoTime() - lastDropTime > 1000000000-(250000000*difficulty)) {
				spawnObstacle();
			}

			//Obstacle movement and collision checking
			for (Iterator<Obstacle> iter = obstacles.iterator(); iter.hasNext(); ) {
				Obstacle obstacle = iter.next();
				obstacle.y -= 200 * Gdx.graphics.getDeltaTime();
				if (obstacle.y + 64 < 0) iter.remove();
				collisionCheck(obstacle, iter);

			}

			//Moves finish line down if it has been drawn
            if(finishDrawn){
                finishLine.y -=200*Gdx.graphics.getDeltaTime();
                finishCheck();
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
		finishImage.dispose();
		font.dispose();
		backImage1.dispose();
		backImage2.dispose();
		healthImage.dispose();
		selectStage.dispose();
	}


	//Collision checks

	//Checks collisions between boats and obstacles and removes boats from screen if their health is 0
	private void collisionCheck(Obstacle obstacle, Iterator<Obstacle> iter){
        if (obstacle.overlaps(mainBoat)) {
			iter.remove();
			mainBoat.reduceHealth(1);
			if (mainBoat.getHealth() == 0) {
				gameOver = true;
			}
		}
		aiBoatOne.collisionCheck(obstacle, iter);
		aiBoatTwo.collisionCheck(obstacle, iter);
		aiBoatThree.collisionCheck(obstacle, iter);
		aiBoatFour.collisionCheck(obstacle, iter);
    }

    //Checks for the boats crossing the finish line
    private void finishCheck(){

		if(mainBoat.overlaps(finishLine) && finished == -1){
			endTime = System.currentTimeMillis();
			finished = position;
			difficulty += 1;
		}
		position += aiBoatOne.finishCheck(finishLine);
		position += aiBoatTwo.finishCheck(finishLine);
		position += aiBoatThree.finishCheck(finishLine);
		position += aiBoatFour.finishCheck(finishLine);
	}


	//Entity Creation

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

	//Boat select button creation
	private void createButtons(){
		//skin used from https://github.com/czyzby/gdx-skins
		Skin buttonSkin = new Skin(Gdx.files.internal("skin/level-plane-ui.json"));

		//Top left button
		Button buttonA = new TextButton("Select",buttonSkin);
		buttonA.setPosition(SCREEN_WIDTH/4,SCREEN_HEIGHT/4+SCREEN_HEIGHT/2);
		buttonA.addListener(new InputListener(){
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				mainBoat.health = 10;
				mainBoat.speed = 200;
				mainBoat.acceleration = 1;
				selectBoat = true;
			}
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});

		//Top right button
		Button buttonB = new TextButton("Select",buttonSkin);
		buttonB.setPosition((SCREEN_WIDTH/4+SCREEN_WIDTH/2)-buttonB.getWidth(),SCREEN_HEIGHT/4+SCREEN_HEIGHT/2);
		buttonB.addListener(new InputListener(){
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				mainBoat.health = 5;
				mainBoat.speed = 250;
				mainBoat.acceleration = 1;
				selectBoat = true;
			}
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});

		//Bottom left button
		Button buttonC = new TextButton("Select",buttonSkin);
		buttonC.setPosition(SCREEN_WIDTH/4,SCREEN_HEIGHT/4);
		buttonC.addListener(new InputListener(){
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				mainBoat.health = 3;
				mainBoat.speed = 200;
				mainBoat.acceleration = 2;
				selectBoat = true;
			}
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});

		//Bottom right button
		Button buttonD = new TextButton("Select",buttonSkin);
		buttonD.setPosition((SCREEN_WIDTH/4+SCREEN_WIDTH/2)-buttonD.getWidth(),SCREEN_HEIGHT/4);
		buttonD.addListener(new InputListener(){
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				mainBoat.health = 7;
				mainBoat.speed = 300;
				mainBoat.acceleration = 1;
				selectBoat = true;
			}
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});

		//Demo mode button
		Button buttonDemo = new TextButton("Demo", buttonSkin);
		buttonDemo.setPosition(SCREEN_WIDTH/2-(buttonD.getWidth()/2),0);
		buttonDemo.addListener(new InputListener(){
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				demo = true;
				mainBoat = new CPUBoat(10,200, 1 , 10, 10, 64, 128,768,512, (SCREEN_WIDTH/2)-32, 0);
				selectBoat = true;
			}
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});

		selectStage.addActor(buttonA);
		selectStage.addActor(buttonB);
		selectStage.addActor(buttonC);
		selectStage.addActor(buttonD);
		selectStage.addActor(buttonDemo);
	}

	//Draws the barriers between lanes at regular intervals
    private void createBarrier(int count){
		Rectangle barrier = new Rectangle();
		barrier.x = (SCREEN_WIDTH/5)*count;
		barrier.y = 0;
		barrier.width = 1;
		barrier.height = SCREEN_HEIGHT;
		barriers.add(barrier);
	}

	//Draws the health bar
	private void drawHealth(int num){
		batch.draw(healthImage,(num*16)+num,SCREEN_HEIGHT-32);
	}


	///Screens

	//Draws game over screen
	private void gameOverScreen(){
		//Sets game over background to black
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Draws game over text on screen
		batch.begin();
		font.draw(batch,"Game Over!",(SCREEN_WIDTH/2) ,SCREEN_HEIGHT/2);
		font.draw(batch,"Press space to retry.",SCREEN_WIDTH/2,SCREEN_HEIGHT/2-20);
		batch.end();


		//Continuing if in demo mode
		if(demo){
			startTime = System.currentTimeMillis();
			finished = -1;
			leg += 1;
			create();
		}

		//Allows pressing of space to continue game
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			gameOver = false;
			create();
		}
	}

	//Screen for end of first three legs
	private void midScreen(){
		//Sets game over background to black
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Draws game over text on screen
		long end = (endTime-startTime);
		float time = Math.round(end/1000F);
		float totalTime = time+(penalty/30);
		batch.begin();
		font.draw(batch,"You finished leg " + leg + " in position: " + finished ,(SCREEN_WIDTH/2)-97 ,SCREEN_HEIGHT/2);
		font.draw(batch,"Your time was: " +time+" seconds with a penalty of: "+penalty/30+" seconds",SCREEN_WIDTH/2-182,SCREEN_HEIGHT/2+60);
		font.draw(batch,"For a total time of: "+totalTime+" seconds",SCREEN_WIDTH/2-102,SCREEN_HEIGHT/2+30);
		if(leg == 3) {
			font.draw(batch, "Press space to continue to the final.", SCREEN_WIDTH / 2-113, SCREEN_HEIGHT / 2 - 30);
		}
		else{
			font.draw(batch, "Press space to continue.", (SCREEN_WIDTH / 2)-77, SCREEN_HEIGHT / 2 - 30);
		}
		batch.end();

		//Continuing if in demo mode
		if(demo){
				startTime = System.currentTimeMillis();
				finished = -1;
				leg += 1;
				create();
		}

		//Allows pressing of space to continue game
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			startTime = System.currentTimeMillis();
			finished = -1;
			leg += 1;
			create();
		}

	}

	//Screen for end of third leg
	private void endScreen(){
		//Sets game over background to black
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Draws game over text on screen
		batch.begin();
		font.draw(batch,"Congratulations you finished the race in position: " + finished ,(SCREEN_WIDTH/2)-161 ,SCREEN_HEIGHT/2);
		batch.end();
	}

	private void tutorialScreen(){
		batch.begin();
		font.draw(batch,"Use W A S D to move",(SCREEN_WIDTH/2)-74,SCREEN_HEIGHT/2+100);
		font.draw(batch,"Avoid obstacles in the river",(SCREEN_WIDTH/2)-82,SCREEN_HEIGHT/2+70);
		font.draw(batch,"Leaving your lane will cause a penalty to be added to your time",(SCREEN_WIDTH/2)-200,SCREEN_HEIGHT/2+40);
		font.draw(batch,"Press space to select your boat and begin",(SCREEN_WIDTH/2)-135,SCREEN_HEIGHT/2+10);
		batch.end();

		//Allows pressing of space to continue game
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			tutorial = true;
		}
	}

	private void selectBoatScreen(){
		//Sets game over background to black
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		selectStage.act();
		selectStage.draw();
		batch.begin();
		font.draw(batch,"Robustness: 10\nSpeed: 200\nAcceleration: 1",SCREEN_WIDTH/4,SCREEN_HEIGHT/4+SCREEN_HEIGHT/2+90);
		font.draw(batch,"Robustness: 5\nSpeed: 250\nAcceleration: 1",SCREEN_WIDTH/4+SCREEN_WIDTH/2-85,SCREEN_HEIGHT/4+SCREEN_HEIGHT/2+90);
		font.draw(batch,"Robustness: 3\nSpeed: 200\nAcceleration: 2",SCREEN_WIDTH/4,SCREEN_HEIGHT/4+90);
		font.draw(batch,"Robustness: 7\nSpeed: 300\nAcceleration: 1",SCREEN_WIDTH/4+SCREEN_WIDTH/2-85,SCREEN_HEIGHT/4+90);
		font.draw(batch,"Select a boat to use",SCREEN_WIDTH/2-75,SCREEN_HEIGHT/2+50);
		batch.end();

	}

}
