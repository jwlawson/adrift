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
