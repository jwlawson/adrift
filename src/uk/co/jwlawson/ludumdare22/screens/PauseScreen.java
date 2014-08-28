package uk.co.jwlawson.ludumdare22.screens;

import uk.co.jwlawson.ludumdare22.LudumDare;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class PauseScreen implements Screen {
	
	private static final String TAG = "PauseScreen";
	private static final int STAGEWIDTH = 480;
	private static final int STAGEHEIGHT = 320;
	private static final boolean DEBUG = true;

	private LudumDare mGame;
	private Stage mStage;
	private Image iTitle;
	private TextButton bResume;
	private TextButton bRestart;
	private TextButton bOptions;
	private TextButton bHelp; 
	private TextButton bQuit;
	
	private float[] screen;
	private Skin skin;
	
	public PauseScreen(LudumDare game) {
		mGame = game;
		mStage = new Stage(STAGEWIDTH, STAGEHEIGHT, false);
		
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		
		screen = utils.scale(STAGEWIDTH, STAGEHEIGHT);
		
		init();
	}
	
	private void init() {
		
		iTitle = new Image(new Texture(Gdx.files.internal("texture/title.png")));
		iTitle.width = 64*4;
		iTitle.height = 16*4;
		iTitle.x = (screen[0] - iTitle.width)/2;
		iTitle.y = screen[1]*6/8;
		mStage.addActor(iTitle);
		
		bResume = new TextButton("Resume", skin);
		bResume.x = (screen[0] - bResume.width)/2;
		bResume.y = screen[1]*5/8;
		mStage.addActor(bResume);
		
		bRestart = new TextButton("Restart", skin);
		bRestart.x = (screen[0] - bRestart.width)/2;
		bRestart.y = screen[1]*4/8;
		mStage.addActor(bRestart);
		
		bOptions = new TextButton("Options", skin);
		bOptions.x = (screen[0] - bOptions.width)/2;
		bOptions.y = screen[1]*3/8;
		mStage.addActor(bOptions);
		
		bHelp = new TextButton("Help", skin);
		bHelp.x = (screen[0] - bHelp.width)/2;
		bHelp.y = screen[1]*2/8;
		mStage.addActor(bHelp);
		
		bQuit = new TextButton("Quit", skin);
		bQuit.x = (screen[0] - bQuit.width)/2;
		bQuit.y = screen[1]*1/8;
		mStage.addActor(bQuit);
		
	}

	@Override
	public void render(float delta) {
		
		if (Gdx.input.justTouched() ){			
			if (bResume.isPressed) {
				mGame.setScreen(mGame.mGameScreen);
				if (DEBUG) Gdx.app.log(TAG, "Resuming game");
			} else if (bRestart.isPressed) {
				mGame.restartGame();
				mGame.setScreen(mGame.mGameScreen);
				if (DEBUG) Gdx.app.log(TAG, "Restarting game");
			} else if (bOptions.isPressed) {
				mGame.setScreen(mGame.mOptionScreen);
				if (DEBUG) Gdx.app.log(TAG, "Loading option menu");
			} else if (bHelp.isPressed) {
				mGame.setScreen(mGame.mHelpScreen);
				if (DEBUG) Gdx.app.log(TAG, "Loading help menu");
			} else if (bQuit.isPressed) {
				Gdx.app.exit();
				if (DEBUG) Gdx.app.log(TAG, "Exiting game");
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
	}

	@Override
	public void hide() {
		mStage.clear();
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
