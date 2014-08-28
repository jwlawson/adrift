package uk.co.jwlawson.ludumdare22.actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlantPart extends Actor {
	
	private TextureRegion mTex;
	
	public PlantPart(TextureRegion tex) {
		mTex = tex;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		batch.draw(mTex, x, y, width, height);
	}

	@Override
	public Actor hit(float x, float y) {
		return null;
	}

}
