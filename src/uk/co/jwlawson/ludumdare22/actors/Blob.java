package uk.co.jwlawson.ludumdare22.actors;

import java.util.List;

import uk.co.jwlawson.ludumdare22.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Forever;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;

public class Blob extends Actor {
	
	private static final String TAG = "Blob";
	private static final float MOVE = 75;
	private static final boolean DEBUG = false;
	private static final float OFFSET  = 0.75f;
	private static final int HURTTIME = 500;
	
//	private Texture mTex;
	private TextureRegion texRight;
	private TextureRegion texLeft;
	private TextureRegion texUp;
	private TextureRegion texDown;
	private TextureRegion texDraw;
	
	private int mHealth = 3;
	private boolean mHurting = false;
	private long mHurtTime;
	
	private boolean mLeft;
	private boolean mRight;
	private boolean mUp;
	private boolean mDown;
	private Group mWall;
	private GameScreen mGS;
	
	public Blob() {
		Texture tex = new Texture(Gdx.files.internal("texture/blob.png"));
		texDown = new TextureRegion(tex, 0, 0, 16, 16);
		texUp = new TextureRegion(tex, 0, 16, 16, 16);
		texRight = new TextureRegion(tex, 16, 16, 16, 16);
		texLeft = new TextureRegion(tex, 16, 0, 16, 16);
		touchable = true;
		
		texDraw = texDown;
		mHurtTime = System.currentTimeMillis();
	}
	
	public void init() {
		List<Group> groups = this.getStage().getGroups();
		for (int i = 0; i < groups.size(); i++){
			if (groups.get(i).name == "Block"){
				mWall = groups.get(i);
			}
		}
		action(Forever.$(Sequence.$(ScaleTo.$(1.3f, 1.3f, 2), ScaleTo.$(1.1f, 1.1f, 2f))));
	}

	public void setGS(GameScreen gS) {
		mGS = gS;
	}

	public GameScreen getGS() {
		return mGS;
	}
	
	/**
	 * Subtracts health from blob
	 * @return Whether blob is now dead
	 */
	public int hurt() {
		if (!mHurting){
			mHurtTime = System.currentTimeMillis();
			mHurting = true;
			mHealth -= 1;
		}
		return (mHealth);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		
		if (mHurting) {
			batch.setColor(color.r, 0, 0, color.a* parentAlpha);
		} else {

			batch.setColor(color.r, color.g, color.b, color.a* parentAlpha);
		}
		batch.draw(texDraw, x+(width*(1-scaleX)/2), y, width*scaleX, height*scaleY);
		
	}

	@Override
	public Actor hit(float x, float y) {
		return x > width*(1-OFFSET)/2 && x < width*(1+OFFSET)/2 && y > height*(1-OFFSET)/2 && y < height*(1+OFFSET)/2 ? this : null;
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		
		if (mRight && (mWall.hit(x + MOVE*delta + width*(1+OFFSET)/2, y + height*(1-OFFSET)/2 )==null) 
				&& (mWall.hit(x + MOVE*delta + width*(1+OFFSET)/2, y + height*(1+OFFSET)/2 )==null) 
				&& (mWall.hit(x + MOVE*delta + width*(1+OFFSET)/2, y + height/2 )==null) ){ 
			x += MOVE*delta; 
		}
		if (mLeft && (mWall.hit(x - MOVE*delta + width*(1-OFFSET)/2, y + height*(1-OFFSET)/2 )==null) 
				&& (mWall.hit(x - MOVE*delta + width*(1-OFFSET)/2, y + height*(1+OFFSET)/2 )==null)
				&& (mWall.hit(x - MOVE*delta + width*(1-OFFSET)/2, y + height/2 )==null)){ 
			x -= MOVE*delta; 
		}
		if (mUp && (mWall.hit(x + width*(1-OFFSET)/2, y + MOVE*delta  + height*(1+OFFSET)/2)==null) 
				&& (mWall.hit(x + width*(1+OFFSET)/2, y + MOVE*delta  + height*(1+OFFSET)/2)==null) 
				&& (mWall.hit(x + width/2, y + MOVE*delta  + height*(1+OFFSET)/2)==null) ) {
			y += MOVE*delta; 
		}
		if (mDown && (mWall.hit(x + width*(1-OFFSET)/2, y - MOVE*delta + height*(1-OFFSET)/2)==null)
				 && (mWall.hit(x + width*(1+OFFSET)/2, y - MOVE*delta + height*(1-OFFSET)/2)==null)
				 && (mWall.hit(x + width/2, y - MOVE*delta + height*(1-OFFSET)/2)==null)){ 
			y -= MOVE*delta; 
		}
		
		if (mHurting && (System.currentTimeMillis() - mHurtTime > HURTTIME) ) mHurting = false;
	}
	
	@Override
	public boolean keyDown(int keycode){
		if(DEBUG) { Gdx.app.log(TAG, "Key down event"); }
		boolean handled = false;
		switch (keycode){
			case Input.Keys.D:
			case Input.Keys.RIGHT:
				mRight = true;
				handled = true;
				break;
			case Input.Keys.W:
			case Input.Keys.UP:
				mUp = true;
				handled = true;
				break;
			case Input.Keys.A:
			case Input.Keys.LEFT:
				mLeft = true;
				handled = true;
				break;
			case Input.Keys.S:
			case Input.Keys.DOWN:
				mDown = true;
				handled = true;
				break;
			case Input.Keys.ESCAPE:
			case Input.Keys.MENU:
			case Input.Keys.BUTTON_START:
				mGS.pauseGame();
				break;
		}	
		if (handled){
			changeTex();
		}
		return handled;
	}
	
	private void changeTex() {
		if (mRight) { 
			texDraw = texRight;
		} else if ( mLeft){
			texDraw = texLeft;			
		} else if (mUp) {
			texDraw = texUp;
		} else if (mDown){
			texDraw = texDown;
		}
	}
	
	@Override
	public boolean keyUp (int keycode) {
		boolean handled = false;
		switch (keycode){
		case Input.Keys.D:
		case Input.Keys.RIGHT:
			mRight = false;
			handled = true;
			break;
		case Input.Keys.W:
		case Input.Keys.UP:
			mUp = false;
			handled = true;
			break;
		case Input.Keys.A:
		case Input.Keys.LEFT:
			mLeft = false;
			handled = true;
			break;
		case Input.Keys.S:
		case Input.Keys.DOWN:
			mDown = false;
			handled = true;
			break;
	}	
		if(handled){
			changeTex();
		}
		return handled;
	}

	public int getHealth() {
		return mHealth;
	}

	public void setHealth(int health) {
		mHealth = health;
	}

}
