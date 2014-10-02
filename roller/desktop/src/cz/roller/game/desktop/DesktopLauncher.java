package cz.roller.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import cz.roller.Roller;
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.addIcon("icon.png", Files.FileType.Internal);
		config.addIcon("icon16.png", Files.FileType.Internal);
		config.addIcon("icon128.png", Files.FileType.Internal);
		config.width=1280;
		config.resizable=false;
		new LwjglApplication(new Roller(), config);
	}
}
