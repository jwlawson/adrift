package uk.co.jwlawson.ludumdare22.actors;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveBy;

public class Mover extends Actor {
	
	private final static String TAG = "Mover";
	private final static long TIME_DELTA = 16;
	private final static boolean DEBUG = false;
	
	private float mLastX;
	private float mLastY;
	
	private long mLastTime;
	
	private Group grOverlay;

	public Mover() {
		super();
		this.touchable = true;
		this.visible = true;
	}

	public Mover(String name) {
		super(name);
		this.touchable = true;
		this.visible = true;
	}
	
	public void init() {
		List<Group> groups = getStage().getGroups();
		for (int i = 0; i < groups.size(); i++){
			if (groups.get(i).name == "Overlay"){
				grOverlay = groups.get(i);
				break;
			}
		}
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer){
	if(DEBUG) {	Gdx.app.log(TAG, "Touched at "+ x +" "+ y);}
		mLastX = x;
		mLastY = y;
		mLastTime = System.currentTimeMillis();
		return true;
	}
	
	@Override
	public void touchDragged(float x, float y, int pointer){
		long time = System.currentTimeMillis();
		if (time - mLastTime > TIME_DELTA){
			if(DEBUG) {	Gdx.app.log(TAG, "Dragged Delta "+(x)+" x "+(y)); }
			getStage().getRoot().action(MoveBy.$(x - mLastX, y - mLastY, 0f));
			grOverlay.action(MoveBy.$(mLastX - x, mLastY - y, 0f));
			mLastX = x;
			mLastY = y;
			mLastTime = time;
		}
	}
	
	@Override
	public void touchUp(float x, float y, int pointer){
		if(DEBUG) {	Gdx.app.log(TAG, "Pointer up"); }
		mLastX = 0;
		mLastY = 0;
	}
	

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// Do not draw
	}

	@Override
	public Actor hit(float x, float y) {
		return this;
	}

}
