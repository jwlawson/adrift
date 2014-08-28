package uk.co.jwlawson.ludumdare22.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Delay;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.Forever;
import com.badlogic.gdx.scenes.scene2d.actions.MoveBy;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;

public class Plant extends Group {
	
	private static final float XOFFSET = 0.5f;
	
	private PlantPart pTop;
	private PlantPart pBase;
	private boolean dead;
	private long deadtime;
	
	public Plant(String texfile, float rand){
		Texture tex = new Texture(Gdx.files.internal(texfile));
		int r = rand==1 ? 3 : (int)(rand*4);
		int r1 = r % 2;
		int r2 = (int)(r/2) % 2;
		r1 *= 16; r2 *= 16;

		TextureRegion texT= new TextureRegion(tex, 0 + r1, 0 + r2, 16, 9);
		TextureRegion texB= new TextureRegion(tex, 0 + r1, 9 + r2, 16, 7);
		
		pTop = new PlantPart(texT);
		pBase = new PlantPart(texB);
		
		addActor(pTop);
		addActor(pBase);
	}

	public void init( float rand) {
		pTop.height = height*9/16;
		pTop.width = width;
		pTop.y += height*7/16;
		pBase. height = height*7/16;
		pBase.width = width;
		
		pTop.action(Delay.$(Forever.$(Sequence.$(MoveBy.$(0f, -2f, 1.5f), MoveBy.$(0f, 2f, 1.5f))), rand));
	}
	
	public void act(float delta){
		super.act(delta);
		if (dead && System.currentTimeMillis() - deadtime > 1000){
			markToRemove(true);
		}
	}
	
	public void die() {
		if (!dead){
			action(Sequence.$(FadeOut.$(0.3f), FadeIn.$(0.3f), FadeOut.$(0.2f),FadeIn.$(0.1f), FadeOut.$(0.1f)));
			dead = true;
			deadtime = System.currentTimeMillis();
		}
	}
	
	@Override
	public void action(Action action){
		if (!dead){
			super.action(action);
		}
	}
	
	@Override
	public Actor hit(float x, float y) {
		return x > width*(1-XOFFSET)/2 && x < width*(1+XOFFSET)/2 && y > 0 && y < height ? this : null;
	}

	public void reloadPrefs(Preferences prefs) {
	}
}
