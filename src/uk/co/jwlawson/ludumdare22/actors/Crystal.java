/*
 * Adrift, a short maze game.
 * Copyright (C) 2011 John Lawson
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
 package uk.co.jwlawson.ludumdare22.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Crystal extends Actor {
	
	@SuppressWarnings("unused")
	private static final String TAG = "Crystal";
	private static final float OFFSET = 1f;
	private static final int ANITIME = 320;
	
	private TextureRegion mTex;

	private long mAniTime;
	private boolean scrolled;
	
	private int[] left = new int[] {0,0,16,16};
	private int[] right = new int[] {16,0,16,16};
	private int[] scroll = right;
	
	public Crystal(Texture tex) {
		mTex = new TextureRegion(tex, 0, 0, 16, 16);
		scrolled = false;
		mAniTime = System.currentTimeMillis();
		color.set(1, 0.6f, 0.4f, 1);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(color.r, color.g, color.b, color.a*parentAlpha);
		batch.draw(mTex, x, y, width, height);
		long time = System.currentTimeMillis();
		if (time - mAniTime > ANITIME) {
			mTex.setRegion(scroll[0], scroll[1], scroll[2], scroll[3]);
			mAniTime = time;
			if (scrolled) scroll = right;
			else scroll = left;
			scrolled = !scrolled;
		}
	}

	@Override
	public Actor hit(float x, float y) {
		return x > width*(1-OFFSET)/2 && x < width*(1+OFFSET)/2 && y > height*(1-OFFSET)/2 && y < height*(1+OFFSET)/2 ? this : null;
	}

}
