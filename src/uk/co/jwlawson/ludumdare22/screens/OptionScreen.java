package uk.co.jwlawson.ludumdare22.screens;

import uk.co.jwlawson.ludumdare22.LudumDare;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class OptionScreen implements Screen {
	
	private static final String TAG = "OptionScreen";
	private static final int STAGEWIDTH = 480;
	private static final int STAGEHEIGHT = 320;
	private static final boolean DEBUG = false;
	
	private Stage mStage;
	
	private LudumDare mGame; 
	
	private TextButton bOK;
	private TextButton bCancel;
	//private CheckBox chkFullscreen;
	private CheckBox chkMute;
	private Slider sldVolume;
	private Label labVol;
	
	private float mVolume;
	
	private Skin skin;
	private float[] screen;
	

	public OptionScreen(LudumDare game) {
		mGame = game;
		mStage = new Stage(STAGEWIDTH, STAGEHEIGHT, false);
		
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		
		screen = utils.scale(STAGEWIDTH, STAGEHEIGHT);
		
		Preferences prefs = Gdx.app.getPreferences("adrift");

		mVolume = prefs.getFloat("Volume", 0.8f);
		
/*		chkFullscreen = new CheckBox("FullScreen", skin);
		chkFullscreen.x = (screen[0]/2 - 180);
		chkFullscreen.y = screen[1]*6/8;
		chkFullscreen.setChecked(prefs.getBoolean("Fullscreen", false));
		mStage.addActor(chkFullscreen);*/
		
		chkMute = new CheckBox("Mute", skin);
		chkMute.x = (screen[0]/2 + 40);
		chkMute.y = screen[1]*4/8;
		chkMute.setChecked(prefs.getBoolean("Mute", false));
		mStage.addActor(chkMute);
		
		sldVolume = new Slider(0, 1, 0.01f, skin);
		sldVolume.x = (screen[0]/2 - 175);
		sldVolume.y = screen[1]*4/8;
		sldVolume.setValue(mVolume);
		sldVolume.setValueChangedListener(new SliderListener());
		mStage.addActor(sldVolume);
		
		labVol = new Label("Volume:", skin);
		labVol.x = (screen[0]/2 - 180);
		labVol.y = screen[1]*9/16;
		mStage.addActor(labVol);
		
		init();
	}
	
	private void init() {
		bOK = new TextButton("OK", skin);
		bOK.x = (screen[0]/2 - 100);
		bOK.y = screen[1]*1/8;
		mStage.addActor(bOK);
				
		bCancel = new TextButton("Cancel", skin);
		bCancel.x = (screen[0]/2 + 100);
		bCancel.y = screen[1]*1/8;
		mStage.addActor(bCancel);
	}

	@Override
	public void render(float delta) {
		
		if(Gdx.input.justTouched() && bOK.isPressed) {
			Gdx.app.log(TAG, "OK button pressed");
			
			Preferences prefs = Gdx.app.getPreferences("adrift");
			//prefs.putBoolean("Fullscreen", false);
			prefs.putBoolean("Mute", chkMute.isChecked());
			prefs.putFloat("Volume", mVolume);
			if (DEBUG) {
				Gdx.app.log(TAG, "Mute: " + chkMute.isChecked());
				Gdx.app.log(TAG, "Volume: "+sldVolume.getValue());
			}
			
			
			prefs.flush();
			
			mGame.setLastScreen();
			
		} else if (Gdx.input.justTouched() && bCancel.isPressed) {
		
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
	}

	@Override
	public void hide() {
		mStage.removeActor(bOK);
		mStage.removeActor(bCancel);
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
	
	private class SliderListener implements Slider.ValueChangedListener {

		@Override
		public void changed(Slider slider, float value) {
			Gdx.app.log(TAG, "Slider value changed to "+value);
			mVolume = value;
		}
		
	}
}
