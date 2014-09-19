package cz.roller.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import cz.roller.game.Roller;
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=1280;
		new LwjglApplication(new Roller(), config);
		System.out.println(System.currentTimeMillis()+" MAIN");
	}
}
