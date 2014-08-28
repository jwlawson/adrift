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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.Forever;
import com.badlogic.gdx.scenes.scene2d.actions.MoveBy;

public class Bullet extends Actor {
	
	private static final float SPEED = 100f;
	private static final float OFFSET = 1f;
	
	private TextureRegion mTex;
	private Group mWalls;
	
	public Bullet (Texture tex, int dir, float x, float y, Group walls) {
		int dx = ((int)dir/16);
		int dy = (dir % 16);
		mTex = new TextureRegion(tex, dx * 16, dy * 16, 16, 16);
		this.x = x; this.y = y;
		mWalls = walls;
		color.a=0f;
		dx -= 1; dy -= 1; dy *= -1;
		action(Forever.$(MoveBy.$(dx*SPEED, dy*SPEED, 1f)));
	}
	
	public void act(float delta) {
		super.act(delta);
		Actor actor = mWalls.hit(x+width/2 , y + height/2);
		if ( actor != null && !(actor instanceof Turret) && !(actor instanceof AttackPlant)) {
			action(FadeOut.$(0.2f));
			this.markToRemove(true);
		}
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(color.r, color.g, color.b, color.a*parentAlpha);
		batch.draw(mTex, x, y, width, height);
	}

	@Override
	public Actor hit(float x, float y) {
		return x > width*(1-OFFSET)/2 && x < width*(1+OFFSET)/2 && y > height*(1-OFFSET)/2 && y < height*(1+OFFSET)/2 ? this : null;
	}

}
