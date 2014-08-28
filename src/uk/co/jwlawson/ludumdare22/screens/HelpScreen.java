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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class HelpScreen implements Screen {
	
	private static final String TAG = "HelpScreen";
	private static final int STAGEWIDTH = 480;
	private static final int STAGEHEIGHT = 320;
	private static final boolean DEBUG = true;
	
	
	private LudumDare mGame;
	private Stage mStage;
	private Skin skin;
	private float[] screen;
	
	private TextButton bOK;
	private Label lab;
	private Image iTitle;

	public HelpScreen(LudumDare game) {
		mGame = game;
		mStage = new Stage(STAGEWIDTH, STAGEHEIGHT, false);
		
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		
		screen = utils.scale(STAGEWIDTH, STAGEHEIGHT);
		
		iTitle = new Image(new Texture(Gdx.files.internal("texture/title.png")));
		iTitle.width = 64*4;
		iTitle.height = 16*4;
		iTitle.x = (screen[0] - iTitle.width)/2;
		iTitle.y = screen[1]*6/8;
		mStage.addActor(iTitle);
		
		lab = new Label(skin);

		
		lab.setText("Arrow keys or WASD control movement. \n" +
					"Mouse moves the camera. \n" +
					"Bullets hurt.");
		
		lab.setWrap(true);
		lab.width = screen[0] - 100;
		lab.x = 50;
		lab.y = screen[1] - (lab.height + iTitle.height + screen[1]*2/8);
		mStage.addActor(lab);
		
		init();
	}
	
	private void init() {
		bOK = new TextButton("Done", skin);
		bOK.x = (screen[0] - bOK.width)/2;
		bOK.y = screen[1]*1/16;
		mStage.addActor(bOK);
	}

	@Override
	public void render(float delta) {
		
		if (Gdx.input.justTouched() && bOK.isPressed){
			if (DEBUG) Gdx.app.log(TAG, "Done with help");
			mGame.setLastScreen();
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
		if (DEBUG) Gdx.app.log(TAG, "Showing");
	}

	@Override
	public void hide() {
		mStage.removeActor(bOK);
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
