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

import uk.co.jwlawson.ludumdare22.LudumDare;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GameOver implements Screen {
	
	private static final String TAG = "GameOverScreen";
	private static final int STAGEHEIGHT = 320;
	private static final int STAGEWIDTH = 480;
	
	private LudumDare mGame;
	private Stage mStage;
	
	private TextButton butRestart;
	private TextButton butMenu;
	private TextButton butQuit;
	

	public GameOver(LudumDare game) {
		mGame = game;
				
		mStage = new Stage(STAGEWIDTH, STAGEHEIGHT, false);
		init();
	}
	
	public void init() {

		Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		
		float[] screen = utils.scale(STAGEWIDTH, STAGEHEIGHT);
		Gdx.app.log(TAG, ""+ screen[0] + " "+ screen[1]);
		
		Label lab = new Label("Game Over", skin);
		lab.x = (screen[0] - lab.width)/2;
		lab.y = screen[1]*4/5;
		mStage.addActor(lab);
		
		butRestart = new TextButton("Restart", skin);
		butRestart.x = (screen[0] - butRestart.width)/2;
		butRestart.y = (screen[1]*3/5);
		mStage.addActor(butRestart);
		
		butMenu = new TextButton("Main Menu", skin);
		butMenu.x = (screen[0] - butMenu.width)/2;
		butMenu.y = (screen[1]*2/5);
		mStage.addActor(butMenu);
		
		butQuit = new TextButton("Quit", skin);
		butQuit.x = (screen[0] - butQuit.width)/2;
		butQuit.y = (screen[1]*1/5);
		mStage.addActor(butQuit);
	}

	@Override
	public void render(float delta) {
		
		if(Gdx.input.justTouched()) {
			
			if ( butRestart.isPressed) {
				
				Gdx.app.log(TAG, "Restart button pressed");
				mGame.restartGame();
				//mGame.setScreen(mGame.mGameScreen);
				
			} else if ( butMenu.isPressed){
				
				mGame.setScreen(mGame.mMenuScreen);
				Gdx.app.log(TAG, "Menu buton pressed");
				
			} else if ( butQuit.isPressed) {
				
				Gdx.app.exit();
				Gdx.app.log(TAG, "Quit button pressed");
			}
		}
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		mStage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		init();
		Gdx.input.setInputProcessor(mStage);
		Gdx.app.log(TAG, "Showing");
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		mStage.dispose();
	}

}
