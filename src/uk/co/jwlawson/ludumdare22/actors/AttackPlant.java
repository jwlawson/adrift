package uk.co.jwlawson.ludumdare22.actors;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

public class AttackPlant extends Plant {
	
	@SuppressWarnings("unused")
	private static final String TAG = "AttackPlant";
	private static final long FIRE = 2300;
	private static final int RANGE = 200*200;
	
	private long mLastFire;
	private Group mWalls;
	private Texture bulTex;
	private Group mBullet;
	private Blob mBlob;
	private Vector2 coords;
	
	private Sound mFireFX;
	private boolean mMute;
	private float mVolume;
	
	public AttackPlant(String texfile, float rand) {
		super(texfile, rand);
		coords = new Vector2();
		
		Preferences prefs = Gdx.app.getPreferences("adrift");
		mMute = prefs.getBoolean("Mute", false);
		mVolume = prefs.getFloat("Volume", 0.8f);
		mFireFX = Gdx.audio.newSound(Gdx.files.internal("sfx/plantfire.wav"));
		prefs.flush();
	}
	
	/**
	 * 
	 * @param pos direction of fire "xy"
	 * @param rand
	 */
	public void init(Blob blob, float rand, Texture bulTex) {
		super.init(rand);
		this.bulTex = bulTex;
		mBlob = blob;
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
	public void act(float delta) {
		super.act(delta);
		long time = System.currentTimeMillis();
		if (time - mLastFire > FIRE) {
			coords.set(mBlob.x, mBlob.y);
			toLocalCoordinates(coords);
			if (coords.len2() < RANGE ) {
				float ang = coords.angle();
				
				Bullet bul = new Bullet(bulTex, findTarget(ang), x+ width/2, y + height/2, mWalls);
				bul.height = 10f;
				bul.width = 10f;
				bul.color.set(1, 0.5f, 0.5f, 1);
				mBullet.addActor(bul);
				
				if (!mMute) mFireFX.play(mVolume);
			}
			mLastFire = time;
		}
	}
	
	public void reloadPrefs(Preferences prefs) {
		mMute = prefs.getBoolean("Mute", false);
		mVolume = prefs.getFloat("Volume", 0.8f);
		prefs.flush();
	}
	
	private int findTarget(float angle){
		if ( angle < 22.5) return 0x21;
		if ( angle < 67.5) return 0x20;
		if (angle < 112.5) return 0x10;
		if (angle < 157.5) return 0x00;
		if (angle < 202.5) return 0x01;
		if (angle < 247.5) return 0x02;
		if (angle < 292.5) return 0x12;
		if (angle < 337.5) return 0x22;
		return 0x21;
	}
}
