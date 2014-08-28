/*
 * Adrift, a short maze game.
 * Copyright (C) 2011 John Lawson
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
 package uk.co.jwlawson.ludumdare22.screens;

import java.util.List;
import java.util.Random;

import uk.co.jwlawson.ludumdare22.LudumDare;
import uk.co.jwlawson.ludumdare22.actors.AttackPlant;
import uk.co.jwlawson.ludumdare22.actors.Blob;
import uk.co.jwlawson.ludumdare22.actors.Bullet;
import uk.co.jwlawson.ludumdare22.actors.Crystal;
import uk.co.jwlawson.ludumdare22.actors.Magic;
import uk.co.jwlawson.ludumdare22.actors.Mover;
import uk.co.jwlawson.ludumdare22.actors.Plant;
import uk.co.jwlawson.ludumdare22.actors.Turret;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Delay;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.MoveBy;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameScreen implements Screen {
	
	private static final String TAG = "GameScreen";
	private static final long PROFILE_DELTA = 10000;
	private static final int TILE_SIZE = 40;
	private static final int STAGE_WIDTH = 480;
	private static final int STAGE_HEIGHT = 320;
	private static final int WALL = 1;
	private static final int FLOOR = 2;
	private static final int PLANTFLOOR = 3;
	private static final float SIGHT = 120;
	private static final boolean DEBUG = true;
	
	private LudumDare mGame;
	private Stage mStage;
	private Blob mBlob;
	private Random mRand;
	private Label mLab;
	private Crystal mCrystal;
	
	private int mFrames;
	private long mLastProfile;
	
	private Texture texWall;
	private Group grBlock;
	private Group grWall;
	private Group grFloor;
	private Group grMagic;
	private Group grPlants;
	private Group grOverlay;
	private Group grTurret;
	private Group grBullet;
	
	private boolean plantCol;
	private boolean mGameOver = false;;
	private long mGameOverTime;
	
	private boolean mGameWin = false;;
	private long mGameWinTime;
	
	private Sound mGOFX;
	private Sound mHitFX;
	private boolean mMute;
	private float mVolume;
	

	public GameScreen(LudumDare game) {
		mGame = game;
		mStage = new Stage(STAGE_WIDTH, STAGE_HEIGHT, false);
		mRand = new Random();
		
		grBlock = new Group("Block");
		grWall = new Group("Walls");
		grFloor = new Group("Floor");
		grMagic = new Group("Magic");
		grPlants = new Group("Plants");
		grOverlay = new Group("Overlay");
		grTurret = new Group("Turrets");
		grBullet = new Group("Bullets");

		mStage.addActor(grFloor);
		grBlock.addActor(grWall);
		mStage.addActor(grMagic);
		grBlock.addActor(grPlants);
		mStage.addActor(grBullet);
		grBlock.addActor(grTurret);

		mStage.addActor(grBlock);
		
		load("maps/map1.png");
		
		mStage.addActor(grOverlay);
		
		mLab = new Label("something", new Skin(Gdx.files.internal("ui/uiskin.json")));
		mLab.x = 2*STAGE_WIDTH/3;
		mLab.y = STAGE_HEIGHT/2;
		mLab.setWrap(true);
		mLab.width = STAGE_WIDTH/3;
		mLab.action(FadeOut.$(0f));
		grOverlay.addActor(mLab);
		
		Texture hTex = new Texture(Gdx.files.internal("texture/heart.png"));
		Image h1 = new Image(hTex);
		h1.width = 25;
		h1.height = 25;
		h1.x = STAGE_WIDTH - 30;
		h1.y = STAGE_HEIGHT - 30;
		grOverlay.addActor(h1);
		
		Image h2 = new Image(hTex);
		h2.width = 25;
		h2.height = 25;
		h2.x = STAGE_WIDTH - 60;
		h2.y = STAGE_HEIGHT - 30;
		grOverlay.addActor(h2);
		
		Image h3 = new Image(hTex);
		h3.width = 25;
		h3.height = 25;
		h3.x = STAGE_WIDTH - 90;
		h3.y = STAGE_HEIGHT - 30;
		grOverlay.addActor(h3);
		
		Mover mover = new Mover();
		mStage.addActor(mover);
		
		mover.init();
				
		Vector2 coords = new Vector2(-mBlob.x, -mBlob.y);
		mStage.getRoot().toLocalCoordinates(coords);
		coords.add(STAGE_WIDTH/2, STAGE_HEIGHT/2);
		coords.sub(TILE_SIZE/2, TILE_SIZE/2);
		mStage.getRoot().action(MoveBy.$(coords.x, coords.y, 0f));
		grOverlay.action(MoveBy.$(-coords.x, -coords.y, 0f));
		
		mGOFX = Gdx.audio.newSound(Gdx.files.internal("sfx/gameover.wav"));
		mHitFX = Gdx.audio.newSound(Gdx.files.internal("sfx/hit.wav"));
		Preferences prefs = Gdx.app.getPreferences("adrift");
		mMute = prefs.getBoolean("Mute", false);
		mVolume = prefs.getFloat("Volume", 0.8f);
		if (DEBUG) if (DEBUG) Gdx.app.log(TAG, "Mute: "+ mMute+ " Volume: "+ mVolume);
		
		if (DEBUG) if (DEBUG) Gdx.app.log(TAG, "Game screen created");
	}
	
	public void fades(List<Actor> list) {
		for (int i = 0; i < list.size(); i++){
			Actor actor = list.get(i);
			if( "Overlay".equals(actor.name) || actor instanceof Label){
				continue;
			}
			if (actor instanceof Group && !(actor instanceof Plant) ){
				fades( ((Group)actor).getActors() );
			} else if (actor instanceof Bullet ){
				if ( Math.pow(actor.x - mBlob.x, 2) + Math.pow(actor.y - mBlob.y, 2) > 3*SIGHT*SIGHT ) {
					actor.action(FadeOut.$(0.7f));
				} else {
					actor.action(FadeIn.$(1));
				}
			} else if (actor.visible && ( Math.pow(actor.x - mBlob.x, 2) + Math.pow(actor.y - mBlob.y, 2) > SIGHT*SIGHT ) ){
				actor.action(FadeOut.$(1f));
			} else {
				actor.action(FadeIn.$(1));
			}
		}
	}

	@Override
	public void render(float delta) {
		
		if (mGameOver && System.currentTimeMillis() - mGameOverTime > 5000) {
			mGame.setScreen(mGame.mGameOver);
		}
		
		if (mGameWin && System.currentTimeMillis() - mGameWinTime > 10000) {
			mGame.setScreen(mGame.mMenuScreen);
		}
		
		Actor actor;
		Vector2 coords = new Vector2();
		for (int i = 0; i < grBullet.getActors().size(); i++) {
			actor = grBullet.getActors().get(i);
			coords.set(actor.x, actor.y);
			mBlob.toLocalCoordinates(coords);
			if ( mBlob.hit(coords.x + actor.width/2, coords.y + actor.height/2) != null) {
				if (DEBUG) Gdx.app.log(TAG, "Hit!");
				lifeLost();
				actor.markToRemove(true);
				break;
			}
		}
		
		coords.set(mBlob.x, mBlob.y);
		mCrystal.toLocalCoordinates(coords);
		if (mCrystal.hit(coords.x + mBlob.width/2, coords.y + mBlob.height/2) != null) {
			win();
		}
		
		fades(mStage.getActors());
		
		actor = null;
		actor = grMagic.hit(mBlob.x + mBlob.width/2, mBlob.y + mBlob.height/2);
		if (actor != null) {
			mLab.setText(actor.name);
			mLab.action(Sequence.$(FadeIn.$(0.5f), Delay.$(FadeOut.$(2f), 2f)));
		}
		
		actor = null;
		actor = grPlants.hit(mBlob.x+mBlob.width/2, mBlob.y - 3);
		actor = actor != null ? actor : grPlants.hit(mBlob.x + mBlob.width/2, mBlob.y+mBlob.height+ 3);
		actor = actor != null? actor : grPlants.hit(mBlob.x - 3, mBlob.y + mBlob.height / 2);
		actor = actor != null ? actor : grPlants.hit(mBlob.x + mBlob.width + 3, mBlob.y + mBlob.height/2);
		if (actor != null){
			plantCol = true;
			try { 
				((Plant)actor).die();
			} catch (ClassCastException e){
				e.printStackTrace();
			}
		} else plantCol = false;
		
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		mStage.act(delta);
		mStage.draw();
		
		// Profiling
		long dtime = System.currentTimeMillis();
		mFrames++;
		if (dtime - mLastProfile > PROFILE_DELTA) {
			if (DEBUG) Gdx.app.log(TAG, "FPS: "+(float) mFrames * 1000 / (dtime - mLastProfile));
			mFrames = 0;
			mLastProfile = dtime;
		}
		
	}

	private void lifeLost() {
		if (!mGameOver){
			int health = mBlob.hurt();
			grOverlay.getActors().get(health + 1).visible = false;
			mHitFX.play(mVolume);
			if (health == 0){
				gameOver();
			}
		}
	}

	private void gameOver() {
		mLab.setText("Oh no!");
		mLab.action(Sequence.$(FadeIn.$(0.5f), Delay.$(FadeOut.$(2f), 2f)));
		mGOFX.play(mVolume);
		mStage.unfocusAll();
		mBlob.action(ScaleTo.$(0f, 0f, 5f));
		mStage.getRoot().action(Delay.$(FadeOut.$(2.5f), 2.5f));
		mGameOver = true;
		mGameOverTime = System.currentTimeMillis();
	}
	
	private void win() {
		grPlants.clear();
		grTurret.clear();
		grBullet.clear();
		mStage.unfocusAll();
		if (DEBUG) Gdx.app.log(TAG, "Player wins");
		mLab.setText("I think this must be it. \n" +
					 "A dead end and a spinning crystal. \n" +
					 "What happened on this ship is beyond me, " +
					 "but I seem to be stuck here. \n" +
					 "All alone.");
		mLab.action(Sequence.$(FadeIn.$(0.5f), Delay.$(FadeOut.$(2.5f), 4f)));
		mStage.getRoot().action(Delay.$(FadeOut.$(2f), 2f));
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(mStage);
		mStage.setKeyboardFocus(mBlob);
		if (DEBUG) Gdx.app.log(TAG, "Showing");
		
		Preferences prefs = Gdx.app.getPreferences("adrift");
		mMute = prefs.getBoolean("Mute", false);
		mVolume = prefs.getFloat("Volume", 0.8f);
		
		for (int i = 0; i < grTurret.getActors().size(); i++) {
			((Turret) grTurret.getActors().get(i)).reloadPrefs(prefs);
		}
		for (int i = 0; i < grPlants.getActors().size(); i++){
			((Plant) grPlants.getActors().get(i)).reloadPrefs(prefs);
		}
		prefs.flush();
		
	}
	
	public void load(String file){

		texWall = new Texture(Gdx.files.internal("texture/spaceshipfloor.png"));
		Texture texTurret = new Texture(Gdx.files.internal("texture/turret.png")); 
		Texture texBullet = new Texture(Gdx.files.internal("texture/bullet.png"));
		Texture texCrys = new Texture(Gdx.files.internal("texture/crystal.png"));

		mBlob = new Blob();
		
		FileHandle sfile = Gdx.files.internal("string.txt");
		String string = sfile.readString();
		String[] strings = string.split("\n");
		
		Pixmap pix = new Pixmap(Gdx.files.internal(file));
		for (int i = 0; i < pix.getWidth(); i++){
			for (int j = 0; j < pix.getHeight(); j++){
				switch (pix.getPixel(i, j) & 0xFF000000) {
				// Gets pix in RGBA8888 format ie. 0xRRGGBBAA
					case 0xFF000000:
						//Either floor or wall
						addLand(pix.getPixel(i, j), i, j, pix.getHeight());
						break;
					case 0x33000000:
						//Magic button
						Magic mag = new Magic(strings[(pix.getPixel(i, j) & 0X0000FF00)/256], texWall);
						mag.x = i * TILE_SIZE;
						mag.y = (pix.getHeight() - j) * TILE_SIZE;
						mag.width = TILE_SIZE;
						mag.height = TILE_SIZE;
						grMagic.addActor(mag);
						break;
					case 0x55000000:
						// Spawn point
						addLand(0xFF000000, i, j, pix.getHeight());
						mBlob.x = i * TILE_SIZE;
						mBlob.y = (pix.getHeight() - j) * TILE_SIZE;
						mBlob.width = TILE_SIZE;
						mBlob.height = TILE_SIZE;
						break;
					case 0x77000000:
						//Turret
						addLand(0xFF000000, i, j, pix.getHeight());
						Turret tur = new Turret(texTurret, (pix.getPixel(i, j) & 0X0000FF00)/256, texBullet, mBlob);
						tur.x = i * TILE_SIZE;
						tur.y = (pix.getHeight() - j) * TILE_SIZE;
						tur.width = TILE_SIZE;
						tur.height = TILE_SIZE;
						grTurret.addActor(tur);
						tur.init();
						break;
					case 0x88000000:
						// AttackPlant
						addLand(0xFF000000, i, j, pix.getHeight());
						AttackPlant ap = new AttackPlant("texture/attackplant.png", mRand.nextFloat());
						ap.x = i * TILE_SIZE;
						ap.y = (pix.getHeight() - j) * TILE_SIZE;
						ap.width = TILE_SIZE;
						ap.height = TILE_SIZE;
						grPlants.addActor(ap);
						ap.init(mBlob, mRand.nextFloat(), texBullet);
						break;
					case 0x22000000:
						//Crsytal
						addLand(0xFF000000, i, j, pix.getHeight());
						mCrystal = new Crystal(texCrys);
						mCrystal.x = i * TILE_SIZE;
						mCrystal.y = (pix.getHeight() - j) * TILE_SIZE;
						mCrystal.width = TILE_SIZE;
						mCrystal.height = TILE_SIZE;
						break;
					default:
						break;
				}
			}
		}
		mStage.addActor(mCrystal);

		mStage.addActor(mBlob);
		mStage.setKeyboardFocus(mBlob);
		mBlob.init();
		mBlob.setGS(this);

	}
	
	private void addLand(int pix, int i, int j, float height) {
		Image img = new Image();
		int type = 0;
		switch(pix & 0xFF00FF00){
		case 0xFF000000:
			img.setRegion(new TextureRegion(texWall, 0,0,16,16));
			type = FLOOR;
			break;
		case 0xFF00A100:
			img.setRegion(new TextureRegion(texWall, 16,0,16,16));
			type = WALL;
			break;
		case 0xFF00A200:
			img.setRegion(new TextureRegion(texWall, 32,0,16,16));
			type = WALL;
			break;
		case 0xFF00A300:
			img.setRegion(new TextureRegion(texWall, 48,0,16,16));
			type = WALL;
			break;
		case 0xFF00B000:
			img.setRegion(new TextureRegion(texWall, 0,16,16,16));
			type = WALL;
			break;
		case 0xFF00B100:
			img.setRegion(new TextureRegion(texWall, 16,16,16,16));
			type = WALL;
			break;
		case 0xFF00B200:
			img.setRegion(new TextureRegion(texWall, 32,16,16,16));
			type = WALL;
			break;
		case 0xFF00B300:
			img.setRegion(new TextureRegion(texWall, 48,16,16,16));
			type = WALL;
			break;
		case 0xFF00C000:
			img.setRegion(new TextureRegion(texWall, 0,32,16,16));
			type = WALL;
			break;
		case 0xFF00C100:
			img.setRegion(new TextureRegion(texWall, 16,32,16,16));
			type = WALL;
			break;
		case 0xFF00C200:
			img.setRegion(new TextureRegion(texWall, 32,32,16,16));
			type = WALL;
			break;
		case 0xFF00C300:
			img.setRegion(new TextureRegion(texWall, 48,32,16,16));
			break;
		case 0xFF00D000:
			img.setRegion(new TextureRegion(texWall, 0,48,16,16));
			type = WALL;
			break;
		case 0xFF00D100:
			img.setRegion(new TextureRegion(texWall, 16,48,16,16));
			type = WALL;
			break;
		case 0xFF00D200:
			img.setRegion(new TextureRegion(texWall, 32,48,16,16));
			type = WALL;
			break;
		case 0xFF00D300:
			img.setRegion(new TextureRegion(texWall, 48,48,16,16));
			type = WALL;
			break;
		case 0xFF00FF00:
			img.setRegion(new TextureRegion(texWall, 0,0,16,16));
			type = PLANTFLOOR;
			break;
		}
		img.x = i * TILE_SIZE;
		img.y = (height - j) * TILE_SIZE;
		img.width = TILE_SIZE;
		img.height = TILE_SIZE;
		
		switch(type) {
		case WALL:
			grWall.addActor(img);
			break;
		case PLANTFLOOR:
			if (mRand.nextFloat() > 0.3f){
				Plant plant = new Plant("texture/plant.png", mRand.nextFloat());
				plant.x = img.x;
				plant.y = img.y;
				plant.width = TILE_SIZE;
				plant.height = TILE_SIZE;
				plant.init(mRand.nextFloat());
				grPlants.addActor(plant);
			}
		case FLOOR:
			grFloor.addActor(img);
			break;
		default:
				mStage.addActor(img);
		}
	}

	@Override
	public void dispose() {
		mStage.dispose();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resize(int width, int height) {
		if ( Gdx.app.getType() ==  ApplicationType.Android ) {
			mStage.setViewport(width, height, false);
		}
	}

	@Override
	public void resume() {
	}

	public boolean isPlantCol() {
		return plantCol;
	}
	
	public void pauseGame() {
		mGame.setScreen(mGame.mPauseScreen);
	}
}
