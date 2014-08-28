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
 package uk.co.jwlawson.ludumdare22;

import uk.co.jwlawson.ludumdare22.screens.*;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class LudumDare extends Game {
	
	public Screen mMenuScreen;
	public Screen mGameScreen;
	public Screen mGameOver;
	public Screen mOptionScreen;
	public Screen mPauseScreen;
	public Screen mHelpScreen;
	
	private Screen mLastScreen;
	

	public LudumDare() {
	}

	@Override
	public void create() {
		Gdx.graphics.setVSync(true);
		mMenuScreen = new MenuScreen(this);
		mGameScreen = new GameScreen(this);
		mGameOver = new GameOver(this);
		mOptionScreen = new OptionScreen(this);
		mPauseScreen = new PauseScreen(this);
		mHelpScreen = new HelpScreen(this); 
		
		setScreen(mMenuScreen);
	}
	
	@Override
	public void setScreen(Screen screen) {
		mLastScreen = this.getScreen();
		super.setScreen(screen);
	}
	
	public void setLastScreen() {
		super.setScreen(mLastScreen);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		mMenuScreen.dispose();
		mGameScreen.dispose();
		mGameOver.dispose();
		mOptionScreen.dispose();
	}
	
	public void restartGame() {
		mGameScreen = new GameScreen(this);
		setScreen(mGameScreen);
	}
}
