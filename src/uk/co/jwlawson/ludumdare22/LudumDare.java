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
