package uk.co.jwlawson.ludumdare22.actors;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class Turret extends Actor {
	
	private static final float FIRE = 1800;
	private static final float OFFSET = 0.9f;
	private static final float HEARRANGE = 210*210;
	
	private TextureRegion mTex;
	private long mLastFire;
	private Group mBullet;
	private Group mWalls;
	private Texture bulTex;
	
	private Blob mBlob;
	private Vector2 coords;
	
	private Sound mFireFX;
	private boolean mMute; 
	private float mVolume;
	
	private int pos;
	
	public Turret(Texture tex, int pos, Texture bullTex, Blob blob) {
		this.pos = pos;
		mBlob = blob;
		mTex = new TextureRegion(tex, ((int)pos/16) * 16, (pos % 16) * 16, 16, 16);
		bulTex = bullTex;
		
		mFireFX = Gdx.audio.newSound(Gdx.files.internal("sfx/turretfire.wav"));
		Preferences prefs = Gdx.app.getPreferences("adrift");
		mMute = prefs.getBoolean("Mute", false);
		mVolume = prefs.getFloat("Volume", 0.8f);
		prefs.flush();
		
		coords = new Vector2();
	}
	
	public void init() {
		List<Group> groups = this.getStage().getGroups();
		for (int i = 0; i < groups.size(); i++){
			if (groups.get(i).name == "Bullets"){
				mBullet = groups.get(i);
			} else if (groups.get(i).name == "Block"){
				mWalls = groups.get(i);
			}
		}
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(color.r, color.g, color.b, color.a*parentAlpha);
		batch.draw(mTex, x, y, width, height);

	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		long time = System.currentTimeMillis();
		if (time - mLastFire > FIRE) {
			Bullet bul = new Bullet(bulTex, pos, x+ width/2, y + height/2, mWalls);
			bul.height = 10f;
			bul.width = 10f;
			bul.color.r = 0f;
			mBullet.addActor(bul);
			coords.set(mBlob.x, mBlob.y);
			toLocalCoordinates(coords);
			if (!mMute && coords.len2() < HEARRANGE) {
				mFireFX.play(mVolume);
			}
			mLastFire = time;
		}
	}

	@Override
	public Actor hit(float x, float y) {
		return x > width*(1-OFFSET)/2 && x < width*(1+OFFSET)/2 && y > height*(1-OFFSET)/2 && y < height*(1+OFFSET)/2 ? this : null;
	}
	
	public void reloadPrefs(Preferences prefs) {
		mMute = prefs.getBoolean("Mute", false);
		mVolume = prefs.getFloat("Volume", 0.8f);
		prefs.flush();
	}
}
