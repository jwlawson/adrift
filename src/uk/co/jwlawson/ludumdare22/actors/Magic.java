package uk.co.jwlawson.ludumdare22.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Magic extends Actor {

	TextureRegion mTex;
	
	
	public Magic(String name, Texture tex) {
		super(name);
		mTex = new TextureRegion(tex, 48, 32, 16, 16);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		batch.draw(mTex, x+(width*(1-scaleX)/2), y, width*scaleX, height*scaleY);
	}

	@Override
	public Actor hit(float x, float y) {
		return x > 0 && x < width && y > 0 && y < height ? this : null;
	}
	
}
