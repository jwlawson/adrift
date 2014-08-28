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

public class MenuScreen implements Screen {
	
	private static final String TAG = "MenuScreen";
	private static final int STAGE_WIDTH = 480;
	private static final int STAGE_HEIGHT = 320;
	
	private LudumDare mGame;
	private Stage mStage;
	private TextButton bPlay;
	private TextButton bOption;
	private TextButton bHelp;
	private TextButton bQuit;
	private Image iTitle;
	private Label lLab;
	
	public MenuScreen(LudumDare game){
		mGame = game;
		mStage = new Stage(STAGE_WIDTH, STAGE_HEIGHT, false);
		Gdx.app.log(TAG, "Menu Screen created");
				
		Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"), Gdx.files.internal("ui/uiskin.png"));
		
		float[] screen = utils.scale(STAGE_WIDTH, STAGE_HEIGHT);
		
		iTitle = new Image(new Texture(Gdx.files.internal("texture/title.png")));
		iTitle.width = 64*4;
		iTitle.height = 16*4;
		iTitle.x = (screen[0] - iTitle.width)/2;
		iTitle.y = screen[1]*5/8;
		mStage.addActor(iTitle);
		
		bPlay = new TextButton("Play", skin);
		bPlay.x = (screen[0] - bPlay.width)/2;
		bPlay.y = screen[1]*4/8;
		mStage.addActor(bPlay);
		
		bOption = new TextButton("Options", skin);
		bOption.x = (screen[0] - bOption.width)/2;
		bOption.y = screen[1]*3/8;
		mStage.addActor(bOption);
		
		bHelp = new TextButton("Help", skin);
		bHelp.x = (screen[0] - bHelp.width)/2;
		bHelp.y = screen[1]*2/8;
		mStage.addActor(bHelp);
		
		bQuit = new TextButton("Quit", skin);
		bQuit.x = (screen[0] - bQuit.width)/2;
		bQuit.y = screen[1]*1/8;
		mStage.addActor(bQuit);
		
		lLab = new Label("Made by John Lawson for Ludum Dare 22. " +
						 "A game made in 48 hours.", skin);
		lLab.x = (screen[0] - lLab.width)/2;
		lLab.y = 0;
		mStage.addActor(lLab);
	}
	
	@Override
	public void show() {
		// Load resources
		Gdx.input.setInputProcessor(mStage);
	}

	@Override
	public void hide() {
		// Called when game changed away from this screen
	}

	@Override
	public void render(float delta) {
		// Update and render
		if (Gdx.input.justTouched()) {
			
			if(bPlay.isPressed){
				
				Gdx.app.log(TAG, "Play button selected");
				mGame.setScreen(mGame.mGameScreen);
				
			} else if (bOption.isPressed) {
				
				Gdx.app.log(TAG, "Option button selected");
				mGame.setScreen(mGame.mOptionScreen);
				
			}else if (bHelp.isPressed) {
				
				Gdx.app.log(TAG, "Help button selected");
				mGame.setScreen(mGame.mHelpScreen);
				
			} else if (bQuit.isPressed) {
				Gdx.app.log(TAG,  "Quit button pressed");
				Gdx.app.exit();
			}
		}
		
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		mStage.draw();
	}
	
	@Override
	public void dispose() {
		// Never automatically called

	}

	@Override
	public void pause() {

	}


	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void resume() {

	}

}
