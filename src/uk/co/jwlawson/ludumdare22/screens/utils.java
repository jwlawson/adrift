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
 package uk.co.jwlawson.ludumdare22.screens;

import com.badlogic.gdx.Gdx;

public class utils {
	
	public static float[] scale(float width, float height) {
		if (width > height && width / (float)Gdx.graphics.getWidth() <= height / (float)Gdx.graphics.getHeight()) {
			float toDeviceSpace = Gdx.graphics.getHeight() / height;
			float toViewportSpace = height / Gdx.graphics.getHeight();

			float deviceWidth = width * toDeviceSpace;
			width = width + (Gdx.graphics.getWidth() - deviceWidth) * toViewportSpace;
		} else {
			float toDeviceSpace = Gdx.graphics.getWidth() / width;
			float toViewportSpace = width / Gdx.graphics.getWidth();

			float deviceHeight = height * toDeviceSpace;
			height = height + (Gdx.graphics.getHeight() - deviceHeight) * toViewportSpace;
		}
		return new float[] {width, height};
	}
}
