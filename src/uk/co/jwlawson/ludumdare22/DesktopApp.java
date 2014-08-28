package uk.co.jwlawson.ludumdare22;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopApp extends ApplicationAdapter {

	public static void main(String[] args) {

		new LwjglApplication(new LudumDare(), "Game", 1024, 600, false);

	}
}
